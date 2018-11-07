/**
 * 
 */
package com.enation.app.shop.core.goodsindex.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.LabelAndValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.core.goods.model.Brand;
import com.enation.app.shop.core.goods.plugin.search.SearchSelector;
import com.enation.app.shop.core.goods.service.IBrandManager;
import com.enation.app.shop.core.goods.utils.BrandUrlUtils;
import com.enation.app.shop.core.goodsindex.service.ISearchSelectorCreator;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

/**
 * 品牌选择器生成
 * @author kingapex
 *2015-4-23
 */
@Service("brandSelectorCreator")
public class BrandSelectorCreator implements ISearchSelectorCreator {
	
	@Autowired
	private IBrandManager brandManager;
	
	
	/* (non-Javadoc)
	 * @see com.enation.app.shop.component.goodsindex.service.ISearchSelectorCreator#createAndPut(java.util.Map, java.util.List)
	 */
	@Override
	public void createAndPut(Map<String, Object> map, List<FacetResult> results) {
			map.put("brand", new ArrayList());
			List<Brand> brandList  = brandManager.list();
			
			 for (FacetResult tmp : results) {  
			    	String dim =tmp.dim;//维度
			    	
			    	 
			    	if(dim.equals("brand_id")){
			    		if(tmp.labelValues.length>1){
			    			List<SearchSelector> brandDim  = createBrandSelector( tmp.labelValues,brandList);
			    			map.put("brand", brandDim);
			    		}
			    		break;
			    	}
			 }
			 
			 List<SearchSelector> selectedCat = BrandUrlUtils.createSelectedBrand(brandList);
			 map.put("selected_brand", selectedCat); //已经选择的品牌


	}
	
	
	
	
	/**
	 * 生成品牌选择器<br>
	 * lucene中索引的是brandid，需要取出catname，并生成正确的url
	 * @param lvs
	 * @return
	 */
	private List<SearchSelector> createBrandSelector(LabelAndValue[] lvs,List<Brand> brandList){
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String servlet_path = request.getServletPath();
		List<SearchSelector> selectorList  = new ArrayList();
		
		for (int i = 0; i < lvs.length; i++) {
			
    	
			LabelAndValue labelAndValue = lvs[i];
			int brandid= StringUtil.toInt( labelAndValue.label ,0);
			String brandname ="";
			Brand findbrand  = BrandUrlUtils.findBrand(brandList,brandid); 
			if(findbrand!=null){
				brandname = findbrand.getName();
			}
			if(StringUtil.isEmpty(brandname)){
	 
				continue;
			}
			SearchSelector selector = new SearchSelector();
			selector.setName(brandname);
			String url =servlet_path +"?"+ BrandUrlUtils.createBrandUrl(""+brandid);
			selector.setUrl(url);
			selector.setValue(findbrand.getLogo());
			selectorList.add(selector);
		}
		
		return selectorList;
	}
	
	
}
