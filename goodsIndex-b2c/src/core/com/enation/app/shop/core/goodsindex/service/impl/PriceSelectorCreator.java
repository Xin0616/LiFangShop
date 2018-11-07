/**
 * 
 */
package com.enation.app.shop.core.goodsindex.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.lucene.facet.FacetResult;
import org.springframework.stereotype.Service;

import com.enation.app.shop.core.goods.utils.PriceUrlUtils;
import com.enation.app.shop.core.goodsindex.service.ISearchSelectorCreator;


/**
 * 价格搜索器生成
 * @author kingapex
 *2015-4-27
 */

@Service("priceSelectorCreator")
public class PriceSelectorCreator implements ISearchSelectorCreator {

	

	
	/* (non-Javadoc)
	 * @see com.enation.app.shop.component.goodsindex.service.ISearchSelectorCreator#createAndPut(java.util.Map, java.util.List)
	 */
	@Override
	public void createAndPut(Map<String, Object> map, List<FacetResult> results) {
		PriceUrlUtils.createAndPut(map);
		
	}
 
	
	
}
