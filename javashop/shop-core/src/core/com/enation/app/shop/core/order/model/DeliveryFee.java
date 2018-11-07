package com.enation.app.shop.core.order.model;

/**
 * 配送费
 * @author 沈忱 2017-6-1
 */
public class DeliveryFee {

	private Integer id;

	private Double price;
	
	private Double full;

	private Double minus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getFull() {
		return full;
	}

	public void setFull(Double full) {
		this.full = full;
	}

	public Double getMinus() {
		return minus;
	}

	public void setMinus(Double minus) {
		this.minus = minus;
	}
	
}