package com.enation.app.shop.front.tag.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.goods.service.IGoodsManager;
import com.enation.app.shop.core.member.model.MemberAddress;
import com.enation.app.shop.core.member.service.IMemberAddressManager;
import com.enation.app.shop.core.order.model.support.CartItem;
import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.app.shop.core.order.plugin.cart.CartPluginBundle;
import com.enation.app.shop.core.order.service.ICartManager;
import com.enation.app.shop.core.order.service.IDeliveryFeeManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 订单价格tag
 * @author kingapex
 *2013-7-26下午1:27:28
 */
@Component
@Scope("prototype")
public class OrderPriceTag extends BaseFreeMarkerTag {
	
	@Autowired
	private ICartManager cartManager;
	
	@Autowired
	private IMemberAddressManager memberAddressManager ;
	
	@Autowired
	private CartPluginBundle cartPluginBundle;

	@Autowired
	private IGoodsManager goodsManager;

	@Autowired
	private IDeliveryFeeManager deliveryFeeManager;
	
	/**
	 * 订单价格标签
	 * @param address_id:收货地址id，int型
	 * @param shipping_id:配送方式id，int型
	 * @return 订单价格,OrderPrice型
	 * {@link OrderPrice}
	 */
	@Override
	public Object exec(Map args) throws TemplateModelException {
		HttpServletRequest request =ThreadContextHolder.getHttpRequest();
		String sessionid  = request.getSession().getId();
		
		Integer addressid =(Integer)args.get("address_id");
		Integer shipping_id =(Integer)args.get("shipping_id"); 
		String regionid =null;
		
		//如果传递了地址，已经选完配送方式了
		if(addressid!=null){
			MemberAddress address =memberAddressManager.getAddress(addressid);
			regionid= ""+address.getRegion_id();
		}
		List<CartItem> cartList  = cartManager.listGoods(sessionid);
		//计算订单价格
		OrderPrice orderprice  =this.cartManager.countPrice(cartList, shipping_id,regionid);
		
		//激发价格计算事件
		orderprice  = this.cartPluginBundle.coutPrice(orderprice);
		
		return orderprice;
	}
	
	//key:store_id店铺id,value:price店铺金额
	public Map getStorePrice(List<CartItem> cartList){
		//获取购物车内选择结算的商品
		List<CartItem> checkList = new ArrayList<CartItem>();
		for (CartItem cartItem : cartList) {
			if(cartItem.getIs_check() == 1){
				checkList.add(cartItem);
			}
		}
		Map map = new HashMap();
		//遍历商品
		for (CartItem cartItem : checkList) {
			Map g = goodsManager.get(cartItem.getGoods_id());
			Integer store_id = (Integer)g.get("store_id");//获取商品所属店铺id
			Double price = cartItem.getNum()*cartItem.getMktprice();//计算商品价格
			if(map.containsKey(store_id)){
				price += (Double)map.get(store_id);
			}
			map.put(store_id, price);
		}
		return map;
	}
	//计算配送费总和
	public Double getDeliveryFee(Map map){
		Double df = 0d;
		Iterator entries = map.entrySet().iterator(); 
		while (entries.hasNext()) { 
		  Map.Entry entry = (Map.Entry) entries.next(); 
		  Double value = (Double)entry.getValue(); 
		  df += deliveryFeeManager.getPriceByFull(value);
		}
		return df;
	}
}
