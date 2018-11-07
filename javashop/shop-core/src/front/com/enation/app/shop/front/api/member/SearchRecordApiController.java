package com.enation.app.shop.front.api.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.member.model.SearchRecord;
import com.enation.app.shop.core.member.service.ISearchRecordManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;
/**
 * 会员API
 * @author Sylow
 * @version 2.1,2016-07-20
 */
@Controller
@RequestMapping("/api/shop/searchRecord")
@Scope("prototype")
public class SearchRecordApiController  {
	@Autowired
	private ISearchRecordManager searchRecordManager;
	
	@ResponseBody
	@RequestMapping(value="/add",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult add(String content){
		Member member = UserConext.getCurrentMember();
		if (!StringUtil.isEmpty(content)) {
			SearchRecord sr = new SearchRecord();
			sr.setContent(content);
			sr.setMember_id(member.getMember_id());
			sr.setSearch_time(DateUtil.getDateline());
			searchRecordManager.save(sr);
		}
		return JsonResultUtil.getSuccessJson("操作成功");
	}

	@ResponseBody
	@RequestMapping(value="/delete-record",produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult deleteRecord(String content){
		Member member = UserConext.getCurrentMember();
		searchRecordManager.deleteRecord(member.getMember_id());
		return JsonResultUtil.getSuccessJson("操作成功");
	}
}
