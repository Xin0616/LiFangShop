package com.enation.app.shop.front.tag.member;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.member.model.MemberComment;
import com.enation.app.shop.core.member.service.IApartmentManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 小区/商圈列表挂件
 * @author 沈忱
 */
@Component
@Scope("prototype")
public class ApartmentListTag extends BaseFreeMarkerTag {

	@Autowired
	private IApartmentManager apartmentManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Integer type =(Integer) params.get("type");
		if(type == 0){
			return apartmentManager.searchSons(-1, null);
		}else{			
			return apartmentManager.getAllByType(type);
		}
	}

}
