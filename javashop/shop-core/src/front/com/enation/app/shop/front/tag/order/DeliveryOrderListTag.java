package com.enation.app.shop.front.tag.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.member.model.MemberOrderItem;
import com.enation.app.shop.core.member.service.IMemberOrderItemManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.OrderItem;
import com.enation.app.shop.core.order.service.IMemberOrderManager;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


@Component
@Scope("prototype")
public class DeliveryOrderListTag extends BaseFreeMarkerTag{
	@Autowired
	private IMemberOrderManager memberOrderManager;
	@Autowired
	private IOrderManager orderManager;
	@Autowired
	private IMemberOrderItemManager memberOrderItemManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Map result = new HashMap();
		Member member = UserConext.getCurrentMember();
		if(member==null){
			throw new TemplateModelException("未登录不能使用此标签[MemberOrderListTag]");
		}
		String page = request.getParameter("page");
		int pageNo = (page == null || page.equals("")) ? 1 : Integer.parseInt(page);
		int pageSize = 10;
		Integer status = (Integer)params.get("status");
		Integer apartment_id = (Integer)params.get("apartment_id");
		
		Page ordersPage = orderManager.listByStatusAndApartment(status, apartment_id, pageNo, pageSize);
		Long totalCount = ordersPage.getTotalCount();
		List ordersList = (List) ordersPage.getResult();
		ordersList = ordersList == null ? new ArrayList() : ordersList;
		result.put("totalCount", totalCount);
		result.put("pageSize", pageSize);
		result.put("page", page);
		result.put("ordersList", ordersList);
		result.put("s", status);
		
		return result;
	}

	

}
