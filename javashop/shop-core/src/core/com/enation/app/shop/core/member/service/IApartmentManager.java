package com.enation.app.shop.core.member.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.core.member.model.Apartment;
import com.enation.framework.database.Page;

/**
 * 小区/商圈接口
 * @author 沈忱 2017.5.22
 */
public interface IApartmentManager {
	
	/**
	 * 根据id获取小区/商圈
	 * @param id
	 * @return小区/商圈
	 */
	public Apartment get(Integer id);
	
	/**
	 * 根据name获取小区/商圈
	 * @param name
	 * @return 小区/商圈
	 */
	public Apartment getByName(String name);

	/**
	 * 保存小区/商圈
	 * @param apartment
	 */
	public void save(Apartment apartment);
	
	/**
	 * 保存商圈,保存属于该商圈的小区
	 * @param apartment
	 */
	public void save(Apartment apartment, String ids);

	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Integer[] ids, Integer type);

	/**
	 * 搜索列表
	 * @param memberMap 搜索条件
	 * @param page 页号
	 * @param pageSize 每页个数
	 * @param other 排序字段
	 * @param order 升/降序
	 * @return 列表
	 */
	public Page searchApartments(Map map, Integer page, Integer pageSize, String other, String order);

	/**
	 * 搜索小区
	 * @param type -1:不可选择的,0:未选择的,1:已选择的
	 * @param id 商圈id
	 * @return 小区列表
	 */
	public List<Apartment> searchSons(Integer type, Integer id);
	
	public List<Apartment> getAllByType(Integer type);
}
