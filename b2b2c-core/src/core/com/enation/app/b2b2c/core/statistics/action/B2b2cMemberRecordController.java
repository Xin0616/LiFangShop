package com.enation.app.b2b2c.core.statistics.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cMemberRecordManager;
import com.enation.app.b2b2c.core.statistics.service.IB2b2cMemberStatisticsManager;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.shop.core.statistics.service.IMemberStatisticsManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.JsonResultUtil;

@Controller
@Scope("prototype")
@RequestMapping("/b2b2c/admin/memberRecord")
public class B2b2cMemberRecordController extends GridController {
	protected Logger logger=Logger.getLogger(getClass());
	
	@Autowired
	private IB2b2cMemberRecordManager b2b2cMemberRecordManager;

	
	@ResponseBody
	@RequestMapping(value="/memberStatistics_list-json")
	public GridJsonResult listJson(@RequestParam(value = "start_time", required = false) String start_time, @RequestParam(value = "end_time", required = false) String end_time,@RequestParam(value = "paystatus", required = false) String paystatus) {
		Map map = new HashMap();
		map.put("end_time", end_time);
		map.put("start_time", start_time);
		map.put("paystatus", paystatus);
		webpage = b2b2cMemberRecordManager.searchApartments(map, getPage(), getPageSize(), this.getSort(), this.getOrder());
		return JsonResultUtil.getGridJson(webpage);
	}
	
	@RequestMapping(value="/rechargeRecord_list")
	public ModelAndView rechargeRecord_list() {
		String market_enable=ThreadContextHolder.getHttpRequest().getParameter("market_enable");
		ModelAndView view= getGridModelAndView();
		view.addObject("optype","no");
		view.addObject("market_enable",market_enable);
		view.setViewName("/b2b2c/admin/statistics/member/memberRecord_list");
		return view;
	}
}
