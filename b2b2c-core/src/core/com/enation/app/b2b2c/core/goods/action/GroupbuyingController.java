package com.enation.app.b2b2c.core.goods.action;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.goods.model.Groupbuying;
import com.enation.app.b2b2c.core.goods.service.IGroupbuyingManager;
import com.enation.app.shop.core.goods.model.Goods;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

@Controller
@RequestMapping("/b2b2c/admin/group-buying")
public class GroupbuyingController extends GridController {
	@Autowired
	private IGroupbuyingManager groupbuyingManager;
	
	@RequestMapping(value="/list")
	public ModelAndView list() {
		String market_enable=ThreadContextHolder.getHttpRequest().getParameter("market_enable");
		ModelAndView view= getGridModelAndView();
		view.addObject("optype","no");
		view.addObject("market_enable",market_enable);
		view.setViewName("/b2b2c/admin/groupbuying/groupbuying_list");
		return view;
	}
	@RequestMapping(value="/edit")
	public ModelAndView edit(Integer id, Integer type) {
		ModelAndView view = this.getGridModelAndView();
		
		if(id != null){
			Groupbuying apart = this.groupbuyingManager.get(id);
		
			view.addObject("apart",apart);
		}
		view.setViewName("/b2b2c/admin/groupbuying/groupbuying_edit");
		return view;
	}
	@ResponseBody
	@RequestMapping(value="/save")
	public JsonResult save(Integer id,String name,String buying_number, Double earnest,Double buying_money,String fictitious_number,String store_name,String sn,String end_date,String start_date) throws ParseException {
		Long nowTime = DateUtil.getDateline();
		AdminUser au = UserConext.getCurrentAdminUser();
		Groupbuying newGroupbuying = groupbuyingManager.get(id);

		if(newGroupbuying != null){
			newGroupbuying.setName(name);
			newGroupbuying.setBuying_number(buying_number);
			newGroupbuying.setEarnest(earnest);
			newGroupbuying.setBuying_money(buying_money);
			newGroupbuying.setFictitious_number(fictitious_number);
			newGroupbuying.setStore_name(store_name);
			newGroupbuying.setSn(sn);
			newGroupbuying.setNumber(fictitious_number);

			Timestamp timeEnd = Timestamp.valueOf(end_date);
			Timestamp timeStart = Timestamp.valueOf(start_date);
			int start=(int)(timeStart.getTime()/1000);
			int end=(int)(timeEnd.getTime()/1000);
			newGroupbuying.setEnd_date(end);
			newGroupbuying.setStart_date(start);
			newGroupbuying.setStatus("0");
			groupbuyingManager.save(newGroupbuying);	
		}else{
			
			newGroupbuying =new Groupbuying();	
			newGroupbuying.setName(name);
			newGroupbuying.setBuying_number(buying_number);
			newGroupbuying.setEarnest(earnest);
			newGroupbuying.setBuying_money(buying_money);
			newGroupbuying.setFictitious_number(fictitious_number);
			newGroupbuying.setStore_name(store_name);
			newGroupbuying.setSn(sn);
			newGroupbuying.setStatus("0");
			newGroupbuying.setNumber(fictitious_number);
			Timestamp timeEnd = Timestamp.valueOf(end_date);
			Timestamp timeStart = Timestamp.valueOf(start_date);
			
			int start=(int)(timeStart.getTime()/1000);
			int end=(int)(timeEnd.getTime()/1000);
			newGroupbuying.setEnd_date(end);
			newGroupbuying.setStart_date(start);
			groupbuyingManager.save(newGroupbuying);
			
		}
		
	
	
		
		return JsonResultUtil.getSuccessJson("保存成功");
	}
	@ResponseBody
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "type", required = false) Integer type, @RequestParam(value = "store_name", required = false) String store_name, @RequestParam(value = "sn", required = false) String sn) {
		Map map = new HashMap();
		map.put("name", name);
		map.put("type", type);
		map.put("store_name", store_name);
		map.put("sn", sn);

		webpage = groupbuyingManager.searchApartments(map, getPage(), getPageSize(), this.getSort(), this.getOrder());
		return JsonResultUtil.getGridJson(webpage);
	}
	@ResponseBody
	@RequestMapping(value="/delete")
	public JsonResult delete(Integer[] id) {
		try {
			groupbuyingManager.delete(id);
			return JsonResultUtil.getSuccessJson("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("操作失败");	
		}
	}
	@ResponseBody
	@RequestMapping(value="/queryCustomer")
	public List<Map> queryCustomer(String queryStr, HttpServletRequest request)
			throws UnsupportedEncodingException {
		if (StringUtil.isEmpty(queryStr)) {
			return new ArrayList<Map>();
		}

		return groupbuyingManager.queryResp(queryStr);
	}
	@ResponseBody
	@RequestMapping(value="/batteryCombox")
	public List<Map> stationCombox(HttpServletRequest request) {

		List<Map> list = groupbuyingManager.getBatteryList();
		if (list == null) {
			return null;
		}
		return list;
	}

	@ResponseBody
	@RequestMapping(value="/querySn")
	public JsonResult queryPhone(String sn, HttpServletRequest request) {
		JsonResult result = new JsonResult();
		request.setAttribute("edit", "1");
		sn = (String) request.getParameter("sn");
		Goods goods = groupbuyingManager.querySn(sn);
	
		result.setObj(goods);
		result.setSuccess(true);
		return result;
}
	@RequestMapping(value="/status")
	public ModelAndView status(Integer id, Integer type) {
		ModelAndView view = this.getGridModelAndView();
		
		if(id != null){
			Groupbuying apart = this.groupbuyingManager.get(id);
			view.addObject("apart",apart);
		}
		view.setViewName("/b2b2c/admin/groupbuying/groupbuying_status");
		return view;
	}
	//审核
	@ResponseBody
	@RequestMapping(value="/saves")
	public JsonResult saves(Integer id,String name,String buying_number,Double earnest,Double buying_money,String fictitious_number,String store_name,String sn,String end_date,String start_date) throws ParseException {
		Long nowTime = DateUtil.getDateline();
		AdminUser au = UserConext.getCurrentAdminUser();
		Groupbuying newGroupbuying = groupbuyingManager.get(id);

		
			newGroupbuying.setName(name);
			newGroupbuying.setBuying_number(buying_number);
			newGroupbuying.setEarnest(earnest);
			newGroupbuying.setBuying_money(buying_money);
			newGroupbuying.setFictitious_number(fictitious_number);
			newGroupbuying.setStore_name(store_name);
			newGroupbuying.setSn(sn);
			Timestamp timeEnd = Timestamp.valueOf(end_date);
			Timestamp timeStart = Timestamp.valueOf(start_date);
			int start=(int)(timeStart.getTime()/1000);
			int end=(int)(timeEnd.getTime()/1000);
			newGroupbuying.setEnd_date(end);
			newGroupbuying.setStart_date(start);
			newGroupbuying.setStatus("1");
			groupbuyingManager.save(newGroupbuying);	
		return JsonResultUtil.getSuccessJson("审核通过");
	}
	//驳回
	@ResponseBody
	@RequestMapping(value="/nosaves")
	public JsonResult nosaves(Integer id,String name,String buying_number,Double earnest,Double buying_money,String fictitious_number,String store_name,String sn,String end_date,String start_date) throws ParseException {
		Long nowTime = DateUtil.getDateline();
		AdminUser au = UserConext.getCurrentAdminUser();
		Groupbuying newGroupbuying = groupbuyingManager.get(id);

		
			newGroupbuying.setName(name);
			newGroupbuying.setBuying_number(buying_number);
			newGroupbuying.setEarnest(earnest);
			newGroupbuying.setBuying_money(buying_money);
			newGroupbuying.setFictitious_number(fictitious_number);
			newGroupbuying.setNumber(fictitious_number);
			newGroupbuying.setStore_name(store_name);
			newGroupbuying.setSn(sn);
			Timestamp timeEnd = Timestamp.valueOf(end_date);
			Timestamp timeStart = Timestamp.valueOf(start_date);
			int start=(int)(timeStart.getTime()/1000);
			int end=(int)(timeEnd.getTime()/1000);
			newGroupbuying.setEnd_date(end);
			newGroupbuying.setStart_date(start);
			newGroupbuying.setStatus("2");
			groupbuyingManager.save(newGroupbuying);	
	
			

		
	
	
		
		return JsonResultUtil.getSuccessJson("审核通过");
	}
}