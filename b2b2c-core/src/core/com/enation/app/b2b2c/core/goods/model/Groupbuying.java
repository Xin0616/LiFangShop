package com.enation.app.b2b2c.core.goods.model;

import java.util.Date;

import com.enation.app.shop.core.goods.model.Goods;

public class Groupbuying implements java.io.Serializable {
	private Integer id; //团购id
	private String name; // 商品名称
	private Integer goods_id; // 商品id
	private String sn; // 商品编号
	private String buying_number; // 起团数量
	private Double earnest; // 预付定金
	private Double buying_money; // 团购金额
	private Integer start_date; // 团购时间开始
	private Integer end_date;//团购时间结束
	private String store_name;//店铺名称

	private String number;//购买数量
	private String fictitious_number; //虚拟已团数量
	private String status; //审核  
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getBuying_number() {
		return buying_number;
	}
	public void setBuying_number(String buying_number) {
		this.buying_number = buying_number;
	}
	
	
	public Double getEarnest() {
		return earnest;
	}
	public void setEarnest(Double earnest) {
		this.earnest = earnest;
	}
	public Double getBuying_money() {
		return buying_money;
	}
	public void setBuying_money(Double buying_money) {
		this.buying_money = buying_money;
	}
	public String getFictitious_number() {
		return fictitious_number;
	}
	public void setFictitious_number(String fictitious_number) {
		this.fictitious_number = fictitious_number;
	}
	public Integer getStart_date() {
		return start_date;
	}
	public void setStart_date(Integer start_date) {
		this.start_date = start_date;
	}
	public Integer getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Integer end_date) {
		this.end_date = end_date;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
}
