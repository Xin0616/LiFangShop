package com.enation.app.b2b2c.core.statistics.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.b2b2c.core.statistics.service.IB2b2cMemberRecordManager;
import com.enation.app.b2b2c.core.statistics.service.IB2b2cMemberStatisticsManager;
import com.enation.app.shop.core.order.service.OrderStatus;
import com.enation.app.shop.core.statistics.service.IMemberStatisticsManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

@Service
public class B2b2cMemberRecordManager implements IB2b2cMemberRecordManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public Page searchApartments(Map map, Integer page, Integer pageSize, String other, String order) {
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");
		String paystatus = (String) map.get("paystatus");
	
		String sql = " SELECT  r.*,m.nickname FROM es_recharge r INNER JOIN es_member m ON m.member_id = r.member_id where 1=1 and status=1  ";
		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time + " 00:00:00");
			sql += " and r.apply_date>" + stime;
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = com.enation.framework.util.DateUtil.getDateline(end_time + " 23:59:59" , "yyyy-MM-dd HH:mm:ss");
			sql += " and r.apply_date<" + etime;
		} 
		if (paystatus != null && !StringUtil.isEmpty(paystatus)){
			sql += " and r.pay_type=" + paystatus ;
		}
		sql += " order by  r.apply_date "; 
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}

}
