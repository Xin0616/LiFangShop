package com.enation.app.shop.core.member.model;

/**
 * 充值记录实体
 * @author 沈忱 2017.5.26
 */
public class Recharge {
	private Integer id;
	private Integer member_id;
	private Double apply_num;
	private Long apply_date;
	private Integer status;//0:支付失败 1:支付成功
	private Integer pay_type;//1:支付宝 2:微信
	
	public Double getApply_num() {
		return apply_num;
	}
	public void setApply_num(Double apply_num) {
		this.apply_num = apply_num;
	}
	public Long getApply_date() {
		return apply_date;
	}
	public void setApply_date(Long apply_date) {
		this.apply_date = apply_date;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public Integer getPay_type() {
		return pay_type;
	}
	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
	}
}
