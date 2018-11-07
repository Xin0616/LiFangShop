package com.enation.app.b2b2c.front.tag.goods;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.goods.service.IGroupbuyingManager;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取店铺全部的有效并上架的商品分页列表Tag
 * @author DMRain
 * @date 2015年12月30日
 * @version v1.0
 * @since v1.0
 */
@Component
public class GroupbuyingPageTag extends BaseFreeMarkerTag{
	@Autowired
	private IGroupbuyingManager groupbuyingManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		
		int pageNo = this.getPage();
		int pageSize = this.getPageSize();
		
		
		
		Page page = this.groupbuyingManager.goodsList(pageNo, pageSize);
		
		return page;
	}
	
}
