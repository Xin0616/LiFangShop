/**
 * 
 */
package com.enation.app.shop.core.pagecreator.service.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.shop.core.pagecreator.service.IPageCreateManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * @author kingapex
 *2015-3-27
 */
@Controller
@RequestMapping("/shop/admin/page-create")
public class PageCreateController  {
	
	@Autowired
	private IPageCreateManager pageCreateManager;
	
	 
	/**
	 * 转向生成页面
	 */
	@RequestMapping(value="/input")
	public String execute(){
		return "/shop/admin/pagecreator/create";
	}
	
	
	
	
	/**
	 * 生成
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/create")
	public JsonResult create(String[] choose_pages){
		
		try {
			
			boolean result = pageCreateManager.startCreate( choose_pages);
			if(result){
				return JsonResultUtil.getSuccessJson("生成成功");
			}else{
				return JsonResultUtil.getErrorJson("有静态页生成任务正在进行中，需等待本次任务完成后才能再次生成。");
			}
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson("生成失败"+e.getMessage());
		}
		
	}
	
}
