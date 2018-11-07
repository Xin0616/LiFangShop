package com.enation.app.shop.core.member.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.core.member.model.Apartment;
import com.enation.app.shop.core.member.model.SearchRecord;
import com.enation.framework.database.Page;

/**
 * 小区/商圈接口
 * @author 沈忱 2017.5.26
 */
public interface ISearchRecordManager {
	
	/**
	 * 根据id获取搜索记录
	 * @param id
	 * @return搜索记录
	 */
	public SearchRecord get(Integer id);
	
	/**
	 * 根据搜索记录和会员查询
	 * @param content
	 * @param member_id
	 * @return
	 */
	public SearchRecord getByContentAndMemberId(String content, Integer member_id);

	/**
	 * 保存搜索记录
	 * @param apartment
	 */
	public void save(SearchRecord searchRecord);
	
	/**
	 * 根据会员id获取搜索记录
	 * @param member_id 会员id
	 * @return
	 */
	public List<SearchRecord> getByMemberId(Integer member_id);

	/**
	 * 根据会员id删除搜索记录
	 * @param member_id
	 */
	public void deleteRecord(Integer member_id);
}
