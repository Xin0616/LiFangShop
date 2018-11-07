package com.enation.app.shop.front.tag.member;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.member.service.ISearchRecordManager;
import com.enation.app.shop.core.member.service.IWealthManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 账户明细列表挂件
 * @author 沈忱
 */
@Component
@Scope("prototype")
public class WealthListTag extends BaseFreeMarkerTag {

	@Autowired
	private IWealthManager wealthManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Member member = UserConext.getCurrentMember();
		
		return wealthManager.getByMemberId(member.getMember_id());
	}

}
