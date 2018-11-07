package com.enation.app.shop.core.member.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.core.member.model.Apartment;
import com.enation.app.shop.core.member.service.IApartmentManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 小区/商圈管理
 * @author 沈忱 2017.5.22
 */

@Service("apartmentManager")
public class ApartmentManager  implements
		IApartmentManager {
	
	@Autowired
	private IDaoSupport  daoSupport;

	@Override
	public Apartment get(Integer id) {
        String sql = "select * from es_apartment where id=" + id;
        return daoSupport.queryForObject(sql, Apartment.class);
	}

	@Override
	public Apartment getByName(String name) {
        String sql = "select * from es_apartment where name='" + name + "'";
        return daoSupport.queryForObject(sql, Apartment.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(Apartment apartment) {
		if(apartment.getId() == null){
			this.daoSupport.insert("es_apartment", apartment);
		}else{
			this.daoSupport.update("es_apartment", apartment, "id=" + apartment.getId());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(Apartment apartment, String ids) {
		if(apartment.getId() == null){
			Integer id  = this.daoSupport.insertGetId("es_apartment", apartment);
			apartment.setId(id);
		}else{
			this.daoSupport.update("es_apartment", apartment, "id=" + apartment.getId());
		}
		String sql1 = "update es_apartment set parent_id = null where parent_id = " + apartment.getId();
		this.daoSupport.execute(sql1);
		String sql2 = "update es_apartment set parent_id = " + apartment.getId() + " where id in (" + ids + ")";
		this.daoSupport.execute(sql2);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer[] id, Integer type) {
		if (id != null ){
			String ids = StringUtil.arrayToString(id, ",");
			String sql1 = "delete from es_apartment where id in (" + ids + ")";
			this.daoSupport.execute(sql1);
			
			if(type == 1){				
				String sql2 = "update es_apartment set parent_id = null where parent_id in (" + ids + ")";
				this.daoSupport.execute(sql2);
			}
		}
	}

	@Override
	public Page searchApartments(Map map, Integer page, Integer pageSize, String other, String order) {
		String name = (String) map.get("name");
		Integer type = (Integer) map.get("type");
		String sql = "";
		if(type == 1){
			sql = "select a.* from es_apartment a where a.type=1";
		}else{
			sql = "select a.*, b.name as parent_name from (select * from es_apartment where type=0) a"
					+ " left join (select * from es_apartment where type=1) b on a.parent_id = b.id";
		}
		if(!StringUtil.isEmpty(name)) {
			sql += " and a.name like '%" + name + "%'";
		}
		if(other != null && order != null){
			sql+=" order by "+other+" "+order;
		}
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}

	@Override
	public List<Apartment> searchSons(Integer type, Integer id) {
		//所有小区
		String sql = "select * from es_apartment where type = 0";
		if(type == -1){
			//已有归属商圈的小区
			sql += " and parent_id is not null";
			if(id != null){
				//排除归属于自己的小区
				sql += " and parent_id != " + id;
			}
		}else if(type == 0){
			//未选择的小区
			sql += " and (parent_id is null)";
		}else{
			//已选择的小区
			sql += " and parent_id = " + id;
		}
		return this.daoSupport.queryForList(sql, Apartment.class);
	}

	@Override
	public List<Apartment> getAllByType(Integer type) {
		String sql = "select * from es_apartment where type = " + type;
		return this.daoSupport.queryForList(sql, Apartment.class);
	}
}
