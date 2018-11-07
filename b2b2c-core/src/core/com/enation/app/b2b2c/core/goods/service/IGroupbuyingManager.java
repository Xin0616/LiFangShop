package com.enation.app.b2b2c.core.goods.service;

import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.core.goods.model.Groupbuying;
import com.enation.app.shop.core.goods.model.Goods;
import com.enation.framework.database.Page;

public interface IGroupbuyingManager {
	public void save(Groupbuying agroupbuying);

	Groupbuying get(Integer id);
	
	public Page searchApartments(Map map, Integer page, Integer pageSize, String other, String order);
	public void delete(Integer[] ids);
	public List<Map> queryResp(String queryStr);
	public Goods querySn(String sn);
	public List<Map> getBatteryList();
	public Page goodsList(Integer pageNo, Integer pageSize);
	public Map getId(Integer id);
	

}
