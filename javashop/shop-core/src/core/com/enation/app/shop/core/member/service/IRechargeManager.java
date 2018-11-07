package com.enation.app.shop.core.member.service;

import java.util.List;

import com.enation.app.shop.core.member.model.Recharge;

/**
 * 充值记录接口
 * @author 沈忱 2017.5.26
 */
public interface IRechargeManager {
	
	/**
	 * 根据id获取记录
	 * @param id
	 * @return搜索记录
	 */
	public Recharge get(Integer id);

	/**
	 * 保存记录
	 * @param apartment
	 */
	public Integer save(Recharge recharge);
	
	/**
	 * 根据会员id搜索记录
	 * @param member_id 会员id
	 * @return
	 */
	public List<Recharge> getByMemberId(Integer member_id);
	
	public void payOrder(Integer order_id);
}
