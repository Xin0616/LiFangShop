package com.enation.app.b2b2c.core.order.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.member.model.StoreMember;
import com.enation.app.b2b2c.core.member.service.IStoreMemberManager;
import com.enation.app.b2b2c.core.order.service.ICaseStoreOrderManager;
import com.enation.app.shop.component.payment.plugin.paypal.api.payments.Order;
import com.enation.app.shop.core.order.plugin.order.OrderPluginBundle;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 多店铺订单管理类<br>
 * 负责多店铺订单的创建、查询
 * 
 * @author kingapex
 * @version 2.0: 对价格逻辑进行改造 2015年8月21日下午1:49:27
 * 
 * @author xulipeng 2016年03月03日 改造springMVC
 * @version 1.1 Kanon 2016年07月06日 修改会员订单列表查询方法
 */

@SuppressWarnings("rawtypes")
@Service("caseStoreOrderManager")
public class CaseStoreOrderManager implements ICaseStoreOrderManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private OrderPluginBundle orderPluginBundle;// 订单插件桩
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.b2b2c.core.service.order.IStoreOrderManager#listOrder(
	 * java.util.Map, int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Page listOrder(Map map, int page, int pageSize, String other, String order) {

		String sql = createTempSql(map, other, order);
		Page webPage = this.daoSupport.queryForPage(sql, page, pageSize);

		//orderPluginBundle.filterOrderPage(webPage);// 对订单查询结果进行过滤

		return webPage;
	}



	/**
	 * 生成查询sql
	 * 
	 * @param map
	 * @param sortField
	 * @param sortType
	 * @return
	 */
	private String createTempSql(Map map, String sortField, String sortType) {
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT o.*, m1.member_id , m1.`name` ,m1.nickname ,m2.nickname courier , m1.balance_frozen  "); 
		sql.append("FROM es_order o  "); 
		sql.append("INNER JOIN es_member m1 ON m1.member_id = o.member_id "); 
		sql.append("INNER JOIN es_member m2 ON m2.member_id = o.courier_id ");
		sql.append("WHERE o.`status` = 2 AND  o.parent_id is NOT NULL ");

		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time + " 00:00:00");
			sql.append(" and o.create_time>" + stime);
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = com.enation.framework.util.DateUtil.getDateline(end_time + " 23:59:59" , "yyyy-MM-dd HH:mm:ss");
			sql.append(" and o.create_time<" + etime);
		}
		sql.append(" ORDER BY " + sortField + " " + sortType);

		return sql.toString();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(Integer[] order_id){
		Integer userId=UserConext.getCurrentAdminUser().getUserid();
		String userName =UserConext.getCurrentAdminUser().getRealname();
		for (Integer orderId : order_id) {
			Map order =  this.daoSupport.queryForMap("select o.* , m1.balance_frozen FROM es_order o INNER JOIN es_member m1 ON m1.member_id = o.member_id WHERE o.order_id = ? ", orderId);
			if(order!=null){
				double  need_pay_money=(Double) order.get("need_pay_money");
				double  balance_frozen=(Double) order.get("balance_frozen");
				double newbalance =balance_frozen - need_pay_money;
				String memberId = order.get("member_id").toString();
				if(balance_frozen>=need_pay_money){
					this.daoSupport.execute("update es_member set balance_frozen = "+newbalance+"   WHERE member_id = "+memberId+" and balance_frozen = "+balance_frozen);
					this.daoSupport.execute("update es_order set status =5  WHERE order_Id =  "+ orderId );
					
					this.daoSupport.execute("insert into es_case_record(order_id,member_id,need_pay_money,op_id,op_time,op_name)values(?,?,?,?,?,?)", orderId,order.get("member_id"),need_pay_money,userId,DateUtil.getDateline(),userName);
				}else{
					throw new RuntimeException("选择订单中冻结账户金额小于需要付款金额，请从新选择");
				}
				
			}else{
				 throw new RuntimeException("选择订单中有不存在的");
			}
		}
	
	}

}
