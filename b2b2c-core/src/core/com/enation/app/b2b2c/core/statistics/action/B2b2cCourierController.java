package com.enation.app.b2b2c.core.statistics.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.goods.service.IGroupbuyingManager;
import com.enation.app.b2b2c.core.statistics.service.IB2b2cCourierManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;
@Controller
@RequestMapping("/b2b2c/admin/courier")
public class B2b2cCourierController extends GridController{
	
	@Autowired
	private IB2b2cCourierManager b2b2cCourierManager;
	@RequestMapping(value="/list")
	public ModelAndView list() {
		String market_enable=ThreadContextHolder.getHttpRequest().getParameter("market_enable");
		ModelAndView view= getGridModelAndView();
		view.addObject("optype","no");
		view.addObject("market_enable",market_enable);
		view.setViewName("/b2b2c/admin/statistics/sales/courier");
		return view;
	}
	
	@ResponseBody
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson( @RequestParam(value = "type", required = false) Integer type, @RequestParam(value = "end_date", required = false) String end_date, @RequestParam(value = "start_date", required = false) String start_date) {
		Map map = new HashMap();
		map.put("start_date", start_date);
		
	
		webpage = b2b2cCourierManager.searchApartments(map, getPage(), getPageSize(), this.getSort(), this.getOrder());
		return JsonResultUtil.getGridJson(webpage);
	}
}
