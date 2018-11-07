package com.enation.app.b2b2c.core.goods.service;

import java.util.List;

import com.enation.app.shop.core.goods.model.Product;
import com.enation.app.shop.core.goods.model.Specification;
import com.enation.framework.database.Page;

/**
 * 
 * (货品管理) 
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2017年3月23日 下午7:36:13
 */
public interface IStoreProductManager {

	
	/**
	 * 查询某个商品的预警货品
	 * 
	 * @param goods_id 商品id
	 * @param store_id 店铺id
	 * @return
	 */
	
	public List<Product> warningGoodsList(Integer goods_id,Integer store_id);

}
