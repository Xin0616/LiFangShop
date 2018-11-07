/**
 * 
 */
package com.enation.app.shop.core.goodsindex.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.core.goods.service.IGoodsSearchManager;
import com.enation.app.shop.core.goodsindex.service.IGoodsIndexManager;
import com.enation.framework.database.Page;

/**
 * 基于lucene全文检索的搜索器
 * @author kingapex
 *2015-4-20
 */
@Service("goodsLuceneSearch")
public class GoodsLuceneSearch implements IGoodsSearchManager {
	
	@Autowired
	private IGoodsIndexManager goodsIndexManager;
	
	/* (non-Javadoc)
	 * @see com.enation.app.shop.core.service.IGoodsSearchManager#getSelector(com.enation.app.shop.core.model.Cat)
	 */
	@Override
	public Map<String,Object> getSelector() {
		return   this.goodsIndexManager.createSelector();
	}
 
	/* (non-Javadoc)
	 * @see com.enation.app.shop.core.service.IGoodsSearchManager#search(int, int)
	 */
	@Override
	public Page search(int pageNo,int pageSize) {
		  return this.goodsIndexManager.search(pageNo, pageSize);
	}

}
