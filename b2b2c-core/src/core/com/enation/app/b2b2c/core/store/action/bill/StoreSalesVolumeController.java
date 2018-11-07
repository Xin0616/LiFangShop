package com.enation.app.b2b2c.core.store.action.bill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.b2b2c.core.store.service.bill.ISalesVolumeManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 商家销量统计
 * @author fenlongli
 *
 */

@Controller
@RequestMapping("/b2b2c/admin/store-sales-valume")
public class StoreSalesVolumeController extends GridController{
	
	@Autowired
	private ISalesVolumeManager salesVolumeManager;
	
	@Autowired
	private IStoreManager storeManager;
	
	/**
	 * 结算单列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public ModelAndView  list(){
		ModelAndView view=getGridModelAndView();
		view.setViewName("/b2b2c/admin/salesVolume/list");
		List<Store> storeList=this.storeManager.listAll();
		view.addObject("storeList", storeList);
		return view;
	}
	
	/**
	 * 获取结算列表JSON
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(@RequestParam(value = "commodity", required = false) Integer commodity, @RequestParam(value = "business", required = false) Integer business ,@RequestParam(value = "start_time", required = false) String start_time, @RequestParam(value = "end_time", required = false) String end_time){
		Map map = new HashMap();
		map.put("commodity", commodity);
		map.put("business", business);
		map.put("end_time", end_time);
		map.put("start_time", start_time);
		webpage = salesVolumeManager.sales_list( map ,this.getPage(), this.getPageSize());
		return JsonResultUtil.getGridJson(webpage);
	}
	
	
	
	
}
