package com.enation.app.b2b2c.front.tag.order.cart;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.order.service.cart.IStoreCartManager;
import com.enation.app.shop.core.member.model.MemberAddress;
import com.enation.app.shop.core.member.service.IMemberAddressManager;
import com.enation.app.shop.core.order.model.support.CartItem;
import com.enation.app.shop.core.order.model.support.OrderPrice;
import com.enation.app.shop.core.order.plugin.cart.CartPluginBundle;
import com.enation.app.shop.core.order.service.ICartManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 所选商品订单价格tag
 * @author Kanon
 *2016-5-17下午1:27:28
 */
@Component
@Scope("prototype")
public class StoreOrderSelectPriceTag extends BaseFreeMarkerTag{
	@Autowired
	private ICartManager cartManager;
	
	@Autowired
	private IMemberAddressManager memberAddressManager ;
	
	@Autowired
	private CartPluginBundle cartPluginBundle;
	
	@Autowired
	private IStoreCartManager storeCartManager;
	
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
		List<CartItem> cartList  = cartManager.selectListGoods(sessionid);
		//计算订单价格
		OrderPrice orderprice  =this.cartManager.countPrice(cartList, shipping_id,regionid);
		
		storeCartManager.countSelectPrice("no");
		
		//激发价格计算事件
		orderprice  = this.cartPluginBundle.coutPrice(orderprice);
		
		orderprice.setNeedPayMoney(orderprice.getNeedPayMoney() - orderprice.getDeliveryFeePrice());
		orderprice.setOrderPrice(orderprice.getOrderPrice() - orderprice.getDeliveryFeePrice());
		
		return orderprice;
	}
}
