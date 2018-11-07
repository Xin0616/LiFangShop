package com.enation.app.shop.core.member.model;

/**
 * 流水实体
 * @author 沈忱 2017.5.26
 */
public class Wealth {
	private Integer id;
	private Integer member_id;
	private Double consume;
	private Long create_time;
	private Integer consume_type;//1:充值 2:消费
	
	public Double getConsume() {
		return consume;
	}
	public void setConsume(Double consume) {
		this.consume = consume;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	public Integer getConsume_type() {
		return consume_type;
	}
	public void setConsume_type(Integer consume_type) {
		this.consume_type = consume_type;
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
}
