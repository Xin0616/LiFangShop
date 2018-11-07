package com.enation.app.shop.core.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.core.member.model.Wealth;
import com.enation.app.shop.core.member.service.IWealthManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 充值记录管理
 * @author 沈忱 2017.5.26
 */

@Service("wealthManager")
public class WealthManager  implements
		IWealthManager {
	
	@Autowired
	private IDaoSupport  daoSupport;

	@Override
	public Wealth get(Integer id) {
        String sql = "select * from es_wealth where id=" + id;
        return daoSupport.queryForObject(sql, Wealth.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer save(Wealth wealth) {
		if(wealth.getId() == null){
			return this.daoSupport.insertGetId("es_wealth", wealth);
		}else{
			this.daoSupport.update("es_wealth", wealth, "id=" + wealth.getId());
			return wealth.getId();
		}
	}

	@Override
	public List<Wealth> getByMemberId(Integer member_id) {
		String sql = "select * from es_wealth where member_id = " + member_id + " order by create_time desc";
		return this.daoSupport.queryForList(sql, Wealth.class);
	}
}
