package com.enation.app.shop.core.member.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.core.member.model.Apartment;
import com.enation.app.shop.core.member.model.SearchRecord;
import com.enation.app.shop.core.member.service.ISearchRecordManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 搜索记录管理
 * @author 沈忱 2017.5.26
 */

@Service("searchRecordManager")
public class SearchRecordManager  implements
		ISearchRecordManager {
	
	@Autowired
	private IDaoSupport  daoSupport;

	@Override
	public SearchRecord get(Integer id) {
        String sql = "select * from es_search_record where id=" + id;
        return daoSupport.queryForObject(sql, SearchRecord.class);
	}

	@Override
	public SearchRecord getByContentAndMemberId(String content, Integer member_id) {
        String sql = "select * from es_search_record where member_id=" + member_id + " and content='" + content + "'";
        return daoSupport.queryForObject(sql, SearchRecord.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(SearchRecord searchRecord) {
		SearchRecord sr = getByContentAndMemberId(searchRecord.getContent(), searchRecord.getMember_id());
		if(sr == null){
			this.daoSupport.insert("es_search_record", searchRecord);
		}
	}

	@Override
	public List<SearchRecord> getByMemberId(Integer member_id) {
		String sql = "select * from es_search_record where member_id = " + member_id + " order by search_time desc";
		return this.daoSupport.queryForList(sql, SearchRecord.class);
	}

	@Override
	public void deleteRecord(Integer member_id) {
		String sql = "delete from es_search_record where member_id = " + member_id;
		this.daoSupport.execute(sql);
	}
}
