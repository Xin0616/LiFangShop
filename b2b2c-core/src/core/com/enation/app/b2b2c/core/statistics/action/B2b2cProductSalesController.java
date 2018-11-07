package com.enation.app.b2b2c.core.statistics.action;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cProductSalesManager;
import com.enation.app.b2b2c.core.statistics.service.IB2b2cSalesStatisticsManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.app.shop.core.statistics.service.ISalesStatisticsManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.JsonResultUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Controller
@Scope("prototype")
@RequestMapping("/b2b2c/admin/productSales")
public class B2b2cProductSalesController  extends GridController{

	@Autowired
	private IB2b2cProductSalesManager b2b2cProductSalesManager;
	
	@Autowired
	private IStoreManager storeManager;
	
	/**
	 * 单品销售页面
	 * @return
	 */
	@RequestMapping(value="/sale-income")
	public ModelAndView saleIncome(String product, String start_time,String end_time){
		ModelAndView view = new ModelAndView();  
		
		Map map =  this.b2b2cProductSalesManager.getReceivables(null,  null,null);
		String danshu="";
		String shuliang ="";
		String jine ="";
		if(map!=null){
			danshu=map.get("danshu").toString();
			shuliang=map.get("shuliang").toString();
			jine=map.get("jine").toString();
		}
		view.addObject("danshu", danshu);
		view.addObject("shuliang", shuliang);
		view.addObject("jine", jine);
		
		view.addObject("pageSize", this.getPageSize());
		view.setViewName("/b2b2c/admin/statistics/product/product_list");
		return view;
	}

	
	/**
	 *   列表
	 *
	 */
	@ResponseBody
	@RequestMapping(value="/sale-income-json")
	public GridJsonResult saleIncomeJson(String product, String start_time,String end_time){
		
		Page list = this.b2b2cProductSalesManager.getSalesIncome(product, start_time,end_time, this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(list);
	}
	
	/**
	 * 数据  金钱

	 */
	@ResponseBody
	@RequestMapping(value="/sale-income-totle-json")
	public Object saleIncomeTotleJson(String product, String start_time,String end_time){
		ModelAndView view = new ModelAndView();
		Map map =  this.b2b2cProductSalesManager.getReceivables(product,  start_time,end_time);
		
		return JsonMessageUtil.getObjectJson(map);
	
	}
	
	@ResponseBody
	@RequestMapping(value="/batteryCombox")
	public List<Map> stationCombox(HttpServletRequest request) {

		List<Map> list = b2b2cProductSalesManager.getBatteryList();
		if (list == null) {
			return null;
		}
		return list;
	}
}
