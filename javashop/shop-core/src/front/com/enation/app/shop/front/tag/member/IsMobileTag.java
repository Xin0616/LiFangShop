package com.enation.app.shop.front.tag.member;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class IsMobileTag extends BaseFreeMarkerTag {
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String phone_type = "";
		
		//判断请求是否为空
		if(request==null){
			return "";
		}
		String user_agent = request.getHeader("user-agent");
		
		//判断user-agent是否为空
		if(StringUtil.isEmpty(user_agent)){
			 return "";
		}
		 
		String userAgent = user_agent.toLowerCase();

		if(userAgent.contains("android" )){
			phone_type = "android";
		}
		if(userAgent.contains("iphone") || userAgent.contains("iPod") || userAgent.contains("iPad")){
			phone_type = "ios";
		}
		return phone_type;
	}

}
