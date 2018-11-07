package com.enation.app.b2b2c.core.store.model;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 店铺
 * @author LiFenLong
 *
 */
public class Store implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6138297514527391003L;
	private Integer store_id;	//店铺Id
	private String store_name;	//店铺名称
	private Integer  store_provinceid;	//省
	private Integer  store_cityid;	//市
	private Integer  store_regionid;	//区
	private Integer  store_townid;     //城镇
	

	private String  store_province;	//省
	private String  store_city;	//市
	private String  store_region;	//区
	private String  store_town;    //城镇
	

	private String  attr;	//详细地址
	private String zip;		//邮编
	private String  tel;	//联系方式
	private Integer store_level;//店铺等级
	private Integer  member_id;	//会员Id
	private String  member_name;	//会员名称
	private String  id_number;	//身份证号
	private String  id_img;		//身份证照片
	private String  license_img;//执照照片
	private Integer  disabled;		//店铺状态
	private Long  create_time;	//创建时间
	private Long  end_time;		//关闭时间
	private String  store_logo;	//店铺logo
	private String store_banner;//店铺横幅
	private String  description;//店铺简介
	private Integer  store_recommend;//是否推荐
	private Integer  themes_id;	//店铺主题ID
	private String themes_path;	//店铺主题
	private Integer store_credit;	//店铺信用
	private double  praise_rate;	//店铺好评率
	private double  store_desccredit;	//描述相符度
	private double  store_servicecredit;	//服务态度分数
	private double store_deliverycredit;	//发货速度分数
	private Integer  store_collect;	//店铺收藏数量
	private Integer store_auth;	//店铺认证
	private Integer name_auth;	//店主认证
	private Integer goods_num; //店铺商品数量
	private String qq;		//店铺客服QQ
	
	private Double 	commission;	//店铺佣金比例
	private String 	bank_account_name;		//银行开户名   
	private String 	bank_account_number;		//公司银行账号
	private String  	bank_name;				//开户银行支行名称
	private String  	bank_code;				//支行联行号
	private Integer 	bank_provinceid;		//开户银行所在省Id
	private Integer 	bank_cityid;			//开户银行所在市Id
	private Integer 	bank_regionid;			//开户银行所在区Id
	private Integer     bank_townid;            //开户银行所在城镇Id
	
	private String  	bank_province;			//开户银行所在省
	private String  	bank_city;				//开户银行所在市
	private String  	bank_region;		    //开户银行所在区
	private String      bank_town;              //开户银行所在城镇 
	
	
	//2015-8-6 author:TALON 新增字段 小区、坐标
	private Integer community_id;				//小区Id
	private String community;					//小区
	private Double longitude;					//经度
	private Double latitude;						//纬度
	
	private Integer apartment_id;
	
	//2016-12-11 author:zjp 新增字段
	@Min(value=1,message="值必须大于0")
	private Integer goods_warning_count;          //货品预警数
	
	@PrimaryKeyField
	public Integer getStore_id() {
		return store_id;
	}
	
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public Integer getStore_provinceid() {
		return store_provinceid;
	}
	public void setStore_provinceid(Integer store_provinceid) {
		this.store_provinceid = store_provinceid;
	}
	public Integer getStore_cityid() {
		return store_cityid;
	}
	public void setStore_cityid(Integer store_cityid) {
		this.store_cityid = store_cityid;
	}
	public Integer getStore_regionid() {
		return store_regionid;
	}
	public void setStore_regionid(Integer store_regionid) { 
		this.store_regionid = store_regionid;
	}
	
	public Integer getBank_townid() {
		return bank_townid;
	}

	public void setBank_townid(Integer bank_townid) {
		this.bank_townid = bank_townid;
	}

	
	public Integer getStore_townid() {
		return store_townid;
	}

	public void setStore_townid(Integer store_townid) {
		this.store_townid = store_townid;
	}
	
	public String getStore_province() {
		return store_province;
	}
	public void setStore_province(String store_province) {
		this.store_province = store_province;
	}
	public String getStore_city() {
		return store_city;
	}
	public void setStore_city(String store_city) {
		this.store_city = store_city;
	}
	
	public String getBank_town() {
		return bank_town;
	}

	public void setBank_town(String bank_town) {
		this.bank_town = bank_town;
	}
	
	public String getStore_town() {
		return store_town;
	}

	public void setStore_town(String store_town) {
		this.store_town = store_town;
	}
  
	public String getStore_region() {
		return store_region;
	}
	public void setStore_region(String store_region) {
		this.store_region = store_region;
	}
	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Integer getStore_level() {
		return store_level;
	}
	public void setStore_level(Integer store_level) {
		this.store_level = store_level;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public String getId_number() {
		return id_number;
	}
	public void setId_number(String id_number) {
		this.id_number = id_number;
	}
	public String getId_img() {
		return id_img;
	}
	public void setId_img(String id_img) {
		this.id_img = id_img;
	}
	public String getLicense_img() {
		return license_img;
	}
	public void setLicense_img(String license_img) {
		this.license_img = license_img;
	}
	public Integer getDisabled() {
		return disabled;
	}
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	public Long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}
	public String getStore_logo() {
		return store_logo;
	}
	public void setStore_logo(String store_logo) {
		this.store_logo = store_logo;
	}
	public String getStore_banner() {
		return store_banner;
	}
	public void setStore_banner(String store_banner) {
		this.store_banner = store_banner;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStore_recommend() {
		return store_recommend;
	}
	public void setStore_recommend(Integer store_recommend) {
		this.store_recommend = store_recommend;
	}
	public Integer getStore_credit() {
		return store_credit;
	}
	public void setStore_credit(Integer store_credit) {
		this.store_credit = store_credit;
	}
	public double getPraise_rate() {
		return praise_rate;
	}
	public void setPraise_rate(double praise_rate) {
		this.praise_rate = praise_rate;
	}
	public double getStore_desccredit() {
		return store_desccredit;
	}
	public void setStore_desccredit(double store_desccredit) {
		this.store_desccredit = store_desccredit;
	}
	public double getStore_servicecredit() {
		return store_servicecredit;
	}
	public void setStore_servicecredit(double store_servicecredit) {
		this.store_servicecredit = store_servicecredit;
	}
	public double getStore_deliverycredit() {
		return store_deliverycredit;
	}
	public void setStore_deliverycredit(double store_deliverycredit) {
		this.store_deliverycredit = store_deliverycredit;
	}
	public Integer getStore_collect() {
		return store_collect;
	}
	public void setStore_collect(Integer store_collect) {
		this.store_collect = store_collect;
	}
	public Integer getStore_auth() {
		return store_auth;
	}
	public void setStore_auth(Integer store_auth) {
		this.store_auth = store_auth;
	}
	public Integer getName_auth() {
		return name_auth;
	}
	public void setName_auth(Integer name_auth) {
		this.name_auth = name_auth;
	}
	public Integer getGoods_num() {
		return goods_num;
	}
	public void setGoods_num(Integer goods_num) {
		this.goods_num = goods_num;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public Double getCommission() {
		return commission;
	}
	public void setCommission(Double commission) {
		this.commission = commission;
	}
	public String getBank_account_name() {
		return bank_account_name;
	}
	public void setBank_account_name(String bank_account_name) {
		this.bank_account_name = bank_account_name;
	}
	public String getBank_account_number() {
		return bank_account_number;
	}
	public void setBank_account_number(String bank_account_number) {
		this.bank_account_number = bank_account_number;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getBank_code() {
		return bank_code;
	}
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	public Integer getBank_provinceid() {
		return bank_provinceid;
	}
	public void setBank_provinceid(Integer bank_provinceid) {
		this.bank_provinceid = bank_provinceid;
	}
	public Integer getBank_cityid() {
		return bank_cityid;
	}
	public void setBank_cityid(Integer bank_cityid) {
		this.bank_cityid = bank_cityid;
	}
	public Integer getBank_regionid() {
		return bank_regionid;
	}
	public void setBank_regionid(Integer bank_regionid) {
		this.bank_regionid = bank_regionid;
	}
	public String getBank_province() {
		return bank_province;
	}
	public void setBank_province(String bank_province) {
		this.bank_province = bank_province;
	}
	public String getBank_city() {
		return bank_city;
	}
	public void setBank_city(String bank_city) {
		this.bank_city = bank_city;
	}
	public String getBank_region() {
		return bank_region;
	}
	public void setBank_region(String bank_region) {
		this.bank_region = bank_region;
	}
	public Integer getCommunity_id() {
		return community_id;
	}
	public void setCommunity_id(Integer community_id) {
		this.community_id = community_id;
	}
	public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getThemes_id() {
		return themes_id;
	}

	public void setThemes_id(Integer themes_id) {
		this.themes_id = themes_id;
	}

	public String getThemes_path() {
		return themes_path;
	}

	public void setThemes_path(String themes_path) {
		this.themes_path = themes_path;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getGoods_warning_count() {
		return goods_warning_count;
	}

	public void setGoods_warning_count(Integer goods_warning_count) {
		this.goods_warning_count = goods_warning_count;
	}

	public Integer getApartment_id() {
		return apartment_id;
	}

	public void setApartment_id(Integer apartment_id) {
		this.apartment_id = apartment_id;
	}
 
	
}
