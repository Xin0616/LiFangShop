package com.enation.app.b2b2c.core.store.service.bill.impl;
 

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.order.model.StoreOrder;
import com.enation.app.b2b2c.core.store.model.Store;
import com.enation.app.b2b2c.core.store.model.bill.Bill;
import com.enation.app.b2b2c.core.store.model.bill.BillDetail;
import com.enation.app.b2b2c.core.store.model.bill.BillStatusEnum;
import com.enation.app.b2b2c.core.store.service.IStoreManager;
import com.enation.app.b2b2c.core.store.service.bill.IBillManager;
import com.enation.app.b2b2c.core.store.service.bill.ISalesVolumeManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺结算管理类
 * @version 1.1 Kanon 修改类结构
 *
 */
@Service("salesVolumeManager")
public class SalesVolumeManager implements ISalesVolumeManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.bill.IBillManager#bill_list(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page sales_list(Map map ,Integer pageNo, Integer pageSize) {
		Integer catid =(Integer) (map.get("commodity"));
		Integer business =(Integer)(map.get("business"));
		String end_time =(String)(map.get("end_time"));
		String start_time =(String)(map.get("start_time"));
		String cat_path = "";
		if(catid==null || catid.intValue()==0){
			cat_path= "0";
		}else{
			cat_path = this.daoSupport.queryForString("select gc.cat_path from es_goods_cat gc where gc.cat_id="+catid);
		}
		String sql = " SELECT s.store_name store_name , g.`name` ,oi.num , g.price ,g.unit ,o.create_time";
		sql += " FROM es_order_items oi";
		sql += " INNER JOIN es_order o ON o.order_id =oi.order_id";
		sql += " INNER JOIN es_goods g ON g.goods_id = oi.goods_id";
		sql += " INNER JOIN es_store s ON s.store_id = o.store_id";
		if(catid==null || catid.intValue()==0  ){
		
		}else{
			sql += " where g.cat_id in (select gc.cat_id from es_goods_cat gc where gc.cat_path like '"+cat_path+"%')";		}
	
		
		
		if(business!=null) {
			if(business!=0){
			sql += " and s.store_id like '%" + business + "%'";
		}
		}
		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time + " 00:00:00");
			sql += " and o.create_time>" + stime;
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = com.enation.framework.util.DateUtil.getDateline(end_time + " 23:59:59" , "yyyy-MM-dd HH:mm:ss");
			sql += " and o.create_time<" + etime;
		}
		
		return this.daoSupport.queryForPage(sql, pageNo, pageSize);
	}

	
	
}
