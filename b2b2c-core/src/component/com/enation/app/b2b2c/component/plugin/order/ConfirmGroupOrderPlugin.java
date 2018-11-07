package com.enation.app.b2b2c.component.plugin.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.order.plugin.order.IConfirmReceiptEvent;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.framework.plugin.AutoRegisterPlugin;

@Component
public class ConfirmGroupOrderPlugin extends AutoRegisterPlugin implements IConfirmReceiptEvent {

	@Autowired
	private IOrderManager orderManager;
	
	@Override
	public void confirm(Integer orderid, double price) {
		// TODO Auto-generated method stub
		orderManager.updateGroupOrderMoney(orderid, price);
	}

}
