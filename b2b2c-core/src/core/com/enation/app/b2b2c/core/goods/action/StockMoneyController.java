package com.enation.app.b2b2c.core.goods.action;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.enation.framework.action.GridController;
import com.enation.framework.context.webcontext.ThreadContextHolder;

@Controller
@RequestMapping("/b2b2c/admin/stock_money")
public class StockMoneyController extends GridController {
	
	
	/**
	 * 商品列表
	 * @param brand_id 品牌Id,Integer
	 * @param catid 商品分类Id,Integer
	 * @param name 商品名称,String
	 * @param sn 商品编号,String 
	 * @param tagids 商品标签Id,Integer[]
	 * @return 商品列表页
	 */
	@RequestMapping(value="/list")
	public ModelAndView list() {
		String market_enable=ThreadContextHolder.getHttpRequest().getParameter("market_enable");
		ModelAndView view= getGridModelAndView();
		view.addObject("optype","no");
		view.addObject("market_enable",market_enable);
		view.setViewName("/b2b2c/admin/stockMoney/stockMoney_list");
		return view;
	}
	
	


}
