package com.enation.app.b2b2c.core.statistics.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.goods.service.IGroupbuyingManager;
import com.enation.app.b2b2c.core.statistics.service.IB2b2cCourierManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;
@Service("b2b2cCourierManager")
public class B2b2cCourierManager implements IB2b2cCourierManager {
	@Autowired
	private IDaoSupport  daoSupport;
	@Override
	public Page searchApartments(Map map, Integer page, Integer pageSize, String other, String order) {
		String start_date = (String) map.get("start_date");
		String sql =" FROM es_member m";
		sql +=" LEFT JOIN es_order o ON o.courier_id = m.member_id AND  LENGTH(o.sn)=12 ";
		 if (start_date != null && !StringUtil.isEmpty(start_date)) {
			 	long stime = com.enation.framework.util.DateUtil.getDateline(start_date + " 00:00:00 ");
				long etime = com.enation.framework.util.DateUtil.getDateline(start_date +" 23:59:59", "yyyy-MM-dd HH:mm:ss");
				sql +=  " and o.create_time >" + stime;
				sql +=  " and o.create_time <" + etime;
			}	
		sql +=" LEFT JOIN ( SELECT  o1.order_id noOrder ,o1.need_pay_money goodsAmount FROM  es_member m1 LEFT  JOIN es_order o1  ON m1.member_id = o1.courier_id  WHERE  m1.is_ordinary =0 AND o1.pay_status =0 AND LENGTH(o1.sn)=12";
		 if (start_date != null && !StringUtil.isEmpty(start_date)) {
			 	long stime = com.enation.framework.util.DateUtil.getDateline(start_date + " 00:00:00 ");
				long etime = com.enation.framework.util.DateUtil.getDateline(start_date +" 23:59:59", "yyyy-MM-dd HH:mm:ss");
				sql +=  " and o1.create_time >" + stime;
				sql +=  " and o1.create_time <" + etime;
			}	
		sql +=" ) b ON b.noOrder  =o.order_id";
		sql +=" WHERE m.is_ordinary=0 ";
		sql +=" GROUP BY m.`name`";
			
	
		String selsql ="SELECT  m.`name` name ,COUNT(o.courier_id) number ,COUNT( b.`noOrder`) no_order ,SUM(b.`goodsAmount`) goods_amounts ,SUM(o.goods_amount)zonger ,SUM(b.`goodsAmount`) goods_amount " +sql;
		String countsql = "select count(*) from ( "	+selsql +" )coun " ;
		
		Page webpage = this.daoSupport.queryForPage(selsql, countsql,page, pageSize);
		return webpage;
	}
	@Override
	public Page courierList(Integer pageNo, Integer pageSize) {
		
		
		String sql = "SELECT a.`name` , a.danl, a.`ddan` ,COUNT( b.`未完成订单`) 未完成订单数 ,SUM(b.`未完成订单金额`) 未完成订单总额";
		 sql += "FROM  (SELECT  m1.`name` name ,COUNT(oi.item_id) danl , SUM(oi.price*oi.num) dand ,o.paymoney ,o.order_id ";
		 sql += "FROM  es_member m1";
		 sql += "LEFT  JOIN es_order o  ON m1.member_id = o.courier_id ";
		 sql += "INNER JOIN es_order_items oi ON oi.order_id = o.order_id";
		 sql += "WHERE  m1.is_ordinary =0 )a , ( SELECT o.order_id 未完成订单 ,o.goods_amount 未完成订单金额";
		 sql += "FROM  es_member m1";
		 sql += "LEFT  JOIN es_order o  ON m1.member_id = o.courier_id ";
		 sql += "WHERE  m1.is_ordinary =0 AND o.pay_status =0 AND LENGTH(o.sn)=12) b";
	
		
	
		
		Page goodsPage = this.daoSupport.queryForPage(sql, pageNo, pageSize);
		return goodsPage;
	}
}
