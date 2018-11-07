package com.enation.app.b2b2c.core.statistics.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cProductSalesManager;
import com.enation.app.b2b2c.core.statistics.service.IB2b2cSalesStatisticsManager;
import com.enation.app.shop.core.order.model.Order;
import com.enation.app.shop.core.order.model.OrderLog;
import com.enation.app.shop.core.order.plugin.order.OrderPluginBundle;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.app.shop.core.statistics.model.DayAmount;
import com.enation.app.shop.core.statistics.model.MonthAmount;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

@Service
public class B2b2cProductSalesManager implements IB2b2cProductSalesManager{
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private OrderPluginBundle orderPluginBundle;

	
	/**
	 * 列表
	
	 */
	@Override
	public Page getSalesIncome(String product, String start_time,String end_time, int page, int pageSize
		) {
		String sql="SELECT g.`name` commodity_name ,g.price price,COUNT(o.member_id) sale_count,COUNT(DISTINCT SUBSTRING(o.sn FROM 1 FOR 12) ) order_count";
		sql +=",SUM(oi.num) number ,SUM(oi.price*oi.num) money ,FROM_UNIXTIME(o.create_time , '%Y-%m-%d') create_time ";
		sql +=" FROM es_order_items oi";
		sql +=" INNER JOIN es_order o ON o.order_id = oi.order_id";
		sql +=" INNER JOIN es_goods g ON g.goods_id = oi.goods_id";
		sql +=" where 1=1 ";
		
		if(!StringUtil.isEmpty(product)) {
			sql += " and g.`name` = '" + product + "'";
		}
		
		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time + " 00:00:00");
			sql += " and o.create_time>" + stime;
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = com.enation.framework.util.DateUtil.getDateline(end_time + " 23:59:59" , "yyyy-MM-dd HH:mm:ss");
			sql += " and o.create_time<" + etime;
		}
		sql +=" GROUP BY oi.goods_id,FROM_UNIXTIME(o.create_time , '%Y-%m-%d')";
		List list = this.daoSupport.queryForListPage(sql, page, pageSize );

		Page salesPage= new Page(0, daoSupport.queryForList(sql).size(), pageSize, list);

		return salesPage;
	}


	/**
	 * 金钱
	
	 */
	@Override
	public Map getReceivables(String product,String start_time, String end_time) {

		String sql="SELECT g.`name` commodity_name ,g.price price,COUNT(o.member_id) sale_count,COUNT(DISTINCT SUBSTRING(o.sn FROM 1 FOR 12) ) order_count";
		sql +=",SUM(oi.num) number ,SUM(oi.price*oi.num) money ,FROM_UNIXTIME(o.create_time , '%Y-%m-%d') create_time ";
		sql +=" FROM es_order_items oi";
		sql +=" INNER JOIN es_order o ON o.order_id = oi.order_id";
		sql +=" INNER JOIN es_goods g ON g.goods_id = oi.goods_id";
		sql +=" where 1=1 ";
		
		if(!StringUtil.isEmpty(product)) {
			sql += " and g.`name` = '" + product + "'";
		}
		
		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time + " 00:00:00");
			sql += " and o.create_time>" + stime;
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = com.enation.framework.util.DateUtil.getDateline(end_time + " 23:59:59" , "yyyy-MM-dd HH:mm:ss");
			sql += " and o.create_time<" + etime;
		}
		sql +=" GROUP BY oi.goods_id,FROM_UNIXTIME(o.create_time , '%Y-%m-%d')";
		
		String newsql =" select sum(sale_count) danshu,sum(number) shuliang,sum(money) jine from( " +sql +" ) newtable ";
		Map map = this.daoSupport.queryForMap(newsql);
		
		return map;
	}


	@Override
	public List<Map> getBatteryList() {
		// TODO Auto-generated method stub
		String sql = "select m.name name from es_goods m ";
		List<Map> list = this.daoSupport.queryForList(sql);

		return list;
	}

	


	

}
