package com.enation.app.b2b2c.core.statistics.service;

import java.util.List;
import java.util.Map;

import com.enation.framework.database.Page;

public interface IB2b2cProductSalesManager {

	
	/**
	 * 销售收入统计总览表
	 * @author LSJ
	 * @param year 年
	 * @param month 月
	 * @param parames
	 * @param store_id 店铺ID
	 * @return 收款金额
	 * 2016年12月6日下午15:16
	 * bushan
	 */
	Map getReceivables(String product, String start_time , String end_time);

	

	/**
	 * 销售收入统计json数据
	 * @author LSJ
	 * @param year 年
	 * @param month 月
	 * @param page 当前页数
	 * @param pageSize 分页大小
	 * @param map
	 * @param store_id 店铺ID
	 * @return 销售收入统计json数据分页
	 * 2016年12月6日下午15:16
	 * bushan
	 */
	Page getSalesIncome(String product, String start_time,String end_time, int page, int pageSize);

	public List<Map> getBatteryList();

}
