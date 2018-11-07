package com.enation.app.shop.front.tag.member;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.member.service.ISearchRecordManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 搜索记录列表挂件
 * @author 沈忱
 */
@Component
@Scope("prototype")
public class SearchRecordListTag extends BaseFreeMarkerTag {

	@Autowired
	private ISearchRecordManager searchRecordManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Member member = UserConext.getCurrentMember();
		
		return searchRecordManager.getByMemberId(member.getMember_id());
	}

}
