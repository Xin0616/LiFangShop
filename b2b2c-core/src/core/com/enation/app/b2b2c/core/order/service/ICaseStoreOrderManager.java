package com.enation.app.b2b2c.core.order.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.order.model.StoreOrder;
import com.enation.app.shop.core.order.model.Order;
import com.enation.framework.database.Page;

/**
 * 店铺订单管理类
 * @author LiFenLong
 *@version v2.0 modify by kingapex-2015-08-21
 *v2.0 修改了以下内容：
 *增加创建订单的接口，因为要将单店和多店创建订单分开
 */
public interface ICaseStoreOrderManager {
	

	/**
	 * 查询所有商家的订单列表<br>
	 * 只查询子订单
	 * @author LiFenLong 
	 * @param map 过滤条件<br>
	 * <li>stype:搜索类型(integer,0为基本搜索)</li>
	 * <li>keyword:关键字(String)</li>
	 * <li>order_state:订单状态特殊查询(String型，可以是如下的值：
	 * wait_ship:待发货
	 * wait_pay:待付款
	 * wait_rog:待收货
	 * )</li>
	 * <li>start_time:(开始时间,String型，如2015-10-10 )</li>
	 * <li>end_time(结束时间,String型，如2015-10-10 )</li>
	 * <li>status:订单状态(int型，对应status字段，{@link OrderStatus})</li>
	 * <li>paystatus:付款状态(int型，对应pay_status字段，{@link OrderStatus})</li>
	 * <li>shipstatus发货状态(int型，对应ship_status字段，{@link OrderStatus})</li>
	 * <li>sn:订单编号(String)</li>
	 * <li>ship_name:收货人(String 对应ship_name字段)</li>
	 * <li>shipping_type:配送方式(Integer，对应shipping_id字段)</li>
	 * <li>payment_id:支付方式(Integer 对应payment_id字段)</li>
	 * <li>depotid:仓库id(Integer 对应depotid字段)</li>
	 * <li>store_name:店铺名称(String 会联合es_store表查询)</li> 
	 * <li>store_id:店铺id(Integer 对应store_id字段)</li> 
	 * @param page
	 * @param pageSize
	 * @param sortField 排序字段
	 * @param sortType 排序方式
	 * @return 订单的分页列表
	 */
	public Page listOrder(Map map,int page,int pageSize,String sortField,String sortType);
	/**
	 * 确认操作
	 * @param lv
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(Integer[] order_id);
	
 
	
}
