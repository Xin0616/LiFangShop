/**
 * 
 */
package com.enation.app.shop.core.goodsindex.service.impl;

import java.util.List;
import java.util.Map;


import org.apache.lucene.facet.FacetResult;
import org.springframework.stereotype.Service;

import com.enation.app.shop.core.goods.utils.SortUrlUtils;
import com.enation.app.shop.core.goodsindex.service.ISearchSelectorCreator;

/**
 * 排序选择器生成
 * @author kingapex
 *2015-4-23
 */
@Service("sortSelectorCreator")
public class SortSelectorCreator implements ISearchSelectorCreator {
	
	/* (non-Javadoc)
	 * @see com.enation.app.shop.component.goodsindex.service.ISearchSelectorCreator#createAndPut(java.util.Map, java.util.List)
	 */
	@Override
	public void createAndPut(Map<String, Object> map, List<FacetResult> results) {
		SortUrlUtils.createAndPut(map);
	}
	
	
	
	 
	
	
	
}
