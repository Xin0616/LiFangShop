package com.enation.app.shop.core.order.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.core.order.model.DeliveryFee;
import com.enation.app.shop.core.order.service.IDeliveryFeeManager;
import com.enation.framework.database.IDaoSupport;

 
/**
 * 配送费管理
 * @author 沈忱 2017-6-1
 */
@Service("deliveryFeeManager")
public class DeliveryFeeManager implements IDeliveryFeeManager{
	
	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public DeliveryFee get(Integer id) {
        String sql = "select * from es_delivery_fee where id=" + id;
        return daoSupport.queryForObject(sql, DeliveryFee.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(List<DeliveryFee> deliveryFees) {
		String sql = "delete from es_delivery_fee";
		daoSupport.execute(sql);
		for (DeliveryFee deliveryFee : deliveryFees) {
			daoSupport.insertGetId("es_delivery_fee", deliveryFee);
		}
	}

	@Override
	public Double getPriceByFull(Double money) {
		String sql = "select * from es_delivery_fee where full <= " + money + "order by full desc";
		List<DeliveryFee> dfs = daoSupport.queryForList(sql, DeliveryFee.class);
		if(dfs.size() > 0){
			DeliveryFee df = dfs.get(0);
			return df.getPrice() - df.getMinus();
		}else{
			String sql2 = "select * from es_delivery_fee";
			List<DeliveryFee> dfs2 = daoSupport.queryForList(sql2, DeliveryFee.class);
			if(dfs2.size() > 0){
				DeliveryFee df = dfs2.get(0);
				return df.getPrice() - df.getMinus();
			}else{
				return 0d;
			}
		}
	}

	@Override
	public List<DeliveryFee> getAll() {
		String sql = "select * from es_delivery_fee";
		return daoSupport.queryForList(sql, DeliveryFee.class);
	}
	
}
