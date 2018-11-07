package com.enation.app.shop.core.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.core.member.model.Recharge;
import com.enation.app.shop.core.member.model.Wealth;
import com.enation.app.shop.core.member.service.IRechargeManager;
import com.enation.app.shop.core.member.service.IWealthManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.service.IOrderManager;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;

/**
 * 充值记录管理
 * @author 沈忱 2017.5.26
 */

@Service("rechargeManager")
public class RechargeManager  implements
		IRechargeManager {
	
	@Autowired
	private IDaoSupport  daoSupport;
	@Autowired
	private IMemberManager memberManager;
	@Autowired
	private IWealthManager wealthManager;
	@Autowired
	private IOrderManager orderManager;

	@Override
	public Recharge get(Integer id) {
        String sql = "select * from es_recharge where id=" + id;
        return daoSupport.queryForObject(sql, Recharge.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer save(Recharge recharge) {
		if(recharge.getId() == null){
			return this.daoSupport.insertGetId("es_recharge", recharge);
		}else{
			this.daoSupport.update("es_recharge", recharge, "id=" + recharge.getId());
			return recharge.getId();
		}
	}

	@Override
	public List<Recharge> getByMemberId(Integer member_id) {
		String sql = "select * from es_recharge where member_id = " + member_id + " order by apply_date desc";
		return this.daoSupport.queryForList(sql, Recharge.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void payOrder(Integer order_id) {
		Member member = UserConext.getCurrentMember();
		Order order = this.orderManager.get(order_id);

		Double money = member.getBalance() - order.getNeed_pay_money();
		member.setBalance(money);
		this.memberManager.edit(member);
		
		order.setStatus(OrderStatus.ORDER_COMPLETE);
		order.setPay_status(OrderStatus.PAY_YES);
		this.orderManager.edit(order);
		
		AdminUser adminUser = UserConext.getCurrentAdminUser();
		String opuser = "系统";
		if(adminUser!=null){
			opuser  = adminUser.getUsername()+"["+adminUser.getRealname()+"]";
		}
		String sql="update es_payment_logs set status=2,pay_date=?,admin_user=? where order_id=?";
		this.daoSupport.execute(sql,DateUtil.getDateline(),opuser,order.getOrder_id());

		Wealth w = new Wealth();
		w.setConsume(order.getNeed_pay_money());
		w.setConsume_type(2);
		w.setCreate_time(DateUtil.getDateline());
		w.setMember_id(order.getMember_id());
		this.wealthManager.save(w);
	}
}
