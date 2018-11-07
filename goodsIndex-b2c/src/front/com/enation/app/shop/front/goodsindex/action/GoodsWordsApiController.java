/**
 * 
 */
package com.enation.app.shop.front.goodsindex.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.shop.core.goodsindex.model.GoodsWords;
import com.enation.app.shop.core.goodsindex.service.IGoodsIndexManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 商品分词 api action
 * @author kingapex
 *2015-4-19
 */
@Controller
@RequestMapping("/api/shop/goods-words")
public class GoodsWordsApiController  {
	
	@Autowired
	private IGoodsIndexManager goodsIndexManager;
	

	@ResponseBody
	@RequestMapping("/list-words")
	public JsonResult listWords(String keyword){
		try{
			List<GoodsWords> wordsList = this.goodsIndexManager.getGoodsWords(keyword);
			return JsonResultUtil.getObjectJson(wordsList);
		}catch(Exception e){
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("error");
		}
	}
}
