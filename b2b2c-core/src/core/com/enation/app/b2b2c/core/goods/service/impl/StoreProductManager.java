package com.enation.app.b2b2c.core.goods.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.goods.service.IStoreProductManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberLv;
import com.enation.app.shop.core.goods.model.Cat;
import com.enation.app.shop.core.goods.model.GoodsLvPrice;
import com.enation.app.shop.core.goods.model.Product;
import com.enation.app.shop.core.goods.model.SpecValue;
import com.enation.app.shop.core.goods.model.Specification;
import com.enation.app.shop.core.goods.service.IGoodsCatManager;
import com.enation.app.shop.core.goods.service.IProductManager;
import com.enation.app.shop.core.member.service.IMemberLvManager;
import com.enation.app.shop.core.member.service.IMemberPriceManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.utils.StaticResourcesUtil;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.database.StringMapper;
import com.enation.framework.util.StringUtil;

/**
 * 
 * (货品管理) 
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2017年3月23日 下午7:37:07
 */
@Service("StoreProductManager")
public class StoreProductManager implements IStoreProductManager {
	
	@Autowired
	private IMemberPriceManager memberPriceManager;
	@Autowired
	private IMemberLvManager memberLvManager;
	@Autowired
	private IGoodsCatManager goodsCatManager;
	@Autowired
	private IDaoSupport daoSupport;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.core.goods.service.IProductManager#warningGoodsList(java.lang.Integer)
	 */
	@Override
	public List<Product> warningGoodsList(Integer goodsId,Integer store_id) {
		String sql = "select * from es_store where store_id=?";
		Map queryForMap = daoSupport.queryForMap(sql, store_id);
		Integer goods_warning_count = Integer.valueOf(queryForMap.get("goods_warning_count").toString());
		sql ="select * from es_product where goods_id=? and enable_store<=? order by product_id asc";
		List<Product> prolist = daoSupport.queryForList(sql,Product.class, goodsId,goods_warning_count);
		
		sql="select sv.*,gs.product_id product_id from  es_goods_spec   gs inner join  es_spec_values   sv on gs.spec_value_id = sv.spec_value_id where  gs.goods_id=? order by gs.id desc" ;
		 
		//sql="select * from "+ "es_goods_spec")+" gs,"+"es_spec_values")+" sv where gs.spec_value_id = sv.spec_value_id and  gs.goods_id=? ";
		//System.out.println(sql);
		List<Map> gsList  = this.daoSupport.queryForList(sql, goodsId);
		
		
		List<GoodsLvPrice> memPriceList = new ArrayList<GoodsLvPrice>();
		
		Member member = UserConext.getCurrentMember();
		double discount =1; //默认是原价,防止无会员级别时出错
		if(member!=null){
			memPriceList  = this.memberPriceManager.listPriceByGid(goodsId);
			MemberLv lv =this.memberLvManager.get(member.getLv_id());
			if(lv!=null)
			discount = lv.getDiscount()/100.00;
			////System.out.println(discount);
		}
		
		for(Product pro:prolist){
			
			if(member!=null){
				Double price  = pro.getPrice();
				if(memPriceList!=null && memPriceList.size()>0)
				price = this.getMemberPrice(pro.getProduct_id(), member.getLv_id(), price, memPriceList, discount);
				pro.setPrice(price);
			}
			int size = gsList.size()-1;
			for(int i=size;i>=0;i--){
				Map gs = gsList.get(i);
				Integer productid;
				productid = ((Integer)gs.get("product_id")).intValue();
				
				//是这个货品的规格
				//则压入到这个货品的规格中
				//用到了spec_value_id
				if(  pro.getProduct_id().intValue()  ==   productid  ){ 
					SpecValue spec = new SpecValue();
					spec.setSpec_value_id( (Integer)gs.get("spec_value_id")  );
					spec.setSpec_id( (Integer)gs.get("spec_id"));
					String spec_img  = (String)gs.get("spec_image");
					
					//将本地中径替换为静态资源服务器地址
					if( spec_img!=null ){
						spec_img  =StaticResourcesUtil.convertToUrl(spec_img); 
					}
					spec.setSpec_image(spec_img);
					spec.setSpec_value((String)gs.get("spec_value"));
					spec.setSpec_type(Integer.parseInt(gs.get("spec_type").toString()));
					pro.addSpec(spec);
				}
				
			}
		}
		return prolist;
	}
	/**
	 * 获取某货品的会员价格
	 * @param price 销售价
	 * @param memPriceList 会员价列表
	 * @param discount 此会员级别的折扣
	 * @return
	 */
	private Double getMemberPrice(int productid,int lvid,Double price,List<GoodsLvPrice> memPriceList,double discount){
		Double memPrice  = price * discount; //默认是此会员级别的折扣价
		
		//然后由具体会员价格中寻找，看是否指定了具体的会员价格
		for( GoodsLvPrice  lvPrice  :memPriceList ){
			if(lvPrice.getProductid() == productid && lvPrice.getLvid() == lvid){ //找到此货品,此会员级别的价格
				memPrice = lvPrice.getPrice();
			}
		}
		return memPrice;
	}
}
