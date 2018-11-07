package com.enation.app.shop.core.member.model;

/**
 * 搜索记录实体
 * @author 沈忱 2017.5.26
 */
public class SearchRecord {
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getSearch_time() {
		return search_time;
	}
	public void setSearch_time(Long search_time) {
		this.search_time = search_time;
	}
	private Integer id;
	private Integer member_id;
	private String content;
	private Long search_time;
}
