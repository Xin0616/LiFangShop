package com.enation.app.shop.core.order.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.core.order.model.DeliveryFee;
import com.enation.app.shop.core.order.service.IDeliveryFeeManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * @author 沈忱 2017-6-1
 */
@Controller 
@RequestMapping("/shop/admin/delivery-fee")
public class DeliveryFeeController {
	
	@Autowired
	private IDeliveryFeeManager deliveryFeeManager;

	@ResponseBody  
	@RequestMapping(value="/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView edit(){
		List<DeliveryFee> dfs = deliveryFeeManager.getAll();
		ModelAndView view = new ModelAndView();
		view.addObject("dfList", dfs);
		view.setViewName("/shop/admin/deliveryfee/edit");
		
		return view;
	}
	
	
	@ResponseBody  
	@RequestMapping(value="/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult save(@RequestBody List<DeliveryFee> dfs){
		try {
			this.deliveryFeeManager.save(dfs);
			return JsonResultUtil.getSuccessJson("保存成功");
		} catch (RuntimeException e) {
			return JsonResultUtil.getErrorJson("保存失败");
		}
	}
	
}
