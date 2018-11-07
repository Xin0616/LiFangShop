package com.enation.app.shop.core.order.service;

import java.util.List;

import com.enation.app.shop.core.order.model.DeliveryFee;
/**
 * 配送费管理
 * @author 沈忱 2017-6-1
 */
public interface IDeliveryFeeManager {
	
	/**
	 * 根据id获取
	 * @param id
	 * @return
	 */
	public DeliveryFee get(Integer id);
	
	/**
	 * 保存
	 * @param deliveryFees
	 */
	public void save(List<DeliveryFee> deliveryFees);

	/**
	 * 根据金额计算配送费
	 * @param full
	 * @return
	 */
	public Double getPriceByFull(Double money);
	
	/**
	 * 获取所有
	 * @return
	 */
	public List<DeliveryFee> getAll();
}
