package com.enation.app.shop.core.member.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.shop.core.member.model.Apartment;
import com.enation.app.shop.core.member.service.IApartmentManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

/**
 * 小区/商圈管理
 * @author 沈忱 2017.5.22
 */

@Controller 
@Scope("prototype")
@RequestMapping("/shop/admin/apartment")
public class ApartmentController extends GridController {
	@Autowired
	private IApartmentManager apartmentManager;
	@Autowired
	private IAdminUserManager adminUserManager;

	/**
	 * 小区/商圈列表
	 * @param type 状态,0:小区,1:商圈,Integer
	 * @return 小区/商圈列表页面
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Integer type) {
		ModelAndView view =this.getGridModelAndView();
		if (type==1) {
			view.setViewName("/shop/admin/apartment/list1");
		} else {
			view.setViewName("/shop/admin/apartment/list0");
		}
		return view;
	}
	
	/**
	 * 小区/商圈编辑
	 * @param name 名称,如果是修改,则有值,String
	 * @return 小区/商圈编辑页面
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(Integer id, Integer type) {
		ModelAndView view = this.getGridModelAndView();
		view.addObject("type", type);
		
		if(id != null){
			Apartment apart = this.apartmentManager.get(id);
			view.addObject("apart",apart);
		}
		
		if(type == 1){
			//不可选择的
			List<Apartment> dischecks = this.apartmentManager.searchSons(-1, id);
			//可选择的
			List<Apartment> canchecks = this.apartmentManager.searchSons(0, id);
			//已经选择的
			List<Apartment> alchecks = this.apartmentManager.searchSons(1, id);
			view.addObject("dischecks", dischecks);
			view.addObject("canchecks", canchecks);
			view.addObject("alchecks", alchecks);
		}
		view.setViewName("/shop/admin/apartment/edit");
		return view;
	}
	
	/**
	 * 小区/商圈列表json
	 * @param pageNo 分页页数,Integer
	 * @param pageSize  每页分页的数量,Integer
	 * @param type 状态,0:小区,1:商圈,Integer
	 * @return  小区/商圈列表json
	 */
	@ResponseBody
	@RequestMapping(value="/list-json")
	public GridJsonResult listJson(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "type", required = false) Integer type) {
		Map map = new HashMap();
		map.put("name", name);
		map.put("type", type);
		webpage = apartmentManager.searchApartments(map, getPage(), getPageSize(), this.getSort(), this.getOrder());
		return JsonResultUtil.getGridJson(webpage);
	}
	
	/**
	 * 新增
	 * @param name 名称,string
	 * @param type 状态,0:小区,1:商圈,Integer
	 * @return json
	 * result 1.操作成功.0.操作失败
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public JsonResult save(Integer id, String name, String ids, Integer type) {
		Long nowTime = DateUtil.getDateline();
		AdminUser au = UserConext.getCurrentAdminUser();
		
		if (StringUtil.isEmpty(name)) {
			return JsonResultUtil.getErrorJson("名称不能为空！");
		}
		
		Apartment newApartment = apartmentManager.get(id);
		Apartment apartment = apartmentManager.getByName(name);

		if(newApartment != null){
			if (apartment != null && newApartment.getId() != apartment.getId()) {
				return JsonResultUtil.getErrorJson("名称已存在！");
			}
		}else{
			if (apartment != null) {
				return JsonResultUtil.getErrorJson("名称已存在！");
			}
			newApartment = new Apartment();
			newApartment.setCreate_time(nowTime);
			newApartment.setCreate_id(au.getUserid());
			newApartment.setCreate_name(au.getRealname());
		}

		newApartment.setName(name);
		newApartment.setType(type);
		newApartment.setUpdate_time(nowTime);
		newApartment.setUpdate_id(au.getUserid());
		newApartment.setUpdate_name(au.getRealname());
		if(type == 0){
			apartmentManager.save(newApartment);
		}else{
			apartmentManager.save(newApartment, ids);
		}
		return JsonResultUtil.getSuccessJson("保存成功");
	}
	/**
	 * 删除
	 * @param ids,String
	 * @return json
	 * result 1.操作成功.0.操作失败
	 */
	@ResponseBody
	@RequestMapping(value="/delete")
	public JsonResult delete(Integer[] id, Integer type) {
		try {
			apartmentManager.delete(id, type);
			return JsonResultUtil.getSuccessJson("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("操作失败");	
		}
	}
}
