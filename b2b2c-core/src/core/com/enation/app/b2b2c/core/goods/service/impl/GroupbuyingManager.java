package com.enation.app.b2b2c.core.goods.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.goods.model.Groupbuying;
import com.enation.app.b2b2c.core.goods.service.IGroupbuyingManager;
import com.enation.app.shop.core.goods.model.Goods;
import com.enation.eop.sdk.utils.StaticResourcesUtil;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;
@Service("groupbuyingManager")
public class GroupbuyingManager implements IGroupbuyingManager   {
	
	@Autowired
	private IDaoSupport  daoSupport;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(Groupbuying groupbuying) {
		if(groupbuying.getId()==null){
			this.daoSupport.insert("es_groupbuying", groupbuying);
		}else{
			this.daoSupport.update("es_groupbuying", groupbuying, "id=" + groupbuying.getId());
		}
	}
	public Groupbuying get(Integer id) {
        String sql = "select * from es_groupbuying where id=" + id;
        return daoSupport.queryForObject(sql, Groupbuying.class);
	}
	@Override
	public Page searchApartments(Map map, Integer page, Integer pageSize, String other, String order) {
		String name = (String) map.get("name");
		Integer type = (Integer) map.get("type");
		String sn = (String) map.get("sn");
		String store_name = (String) map.get("store_name");
		String sql = "";
		
			sql = "select a.* from es_groupbuying a where 1=1";
		
		if(!StringUtil.isEmpty(name)) {
			sql += " and a.name like '%" + name + "%'";
		}
		if(!StringUtil.isEmpty(store_name)) {
			sql += " and a.store_name like '%" + store_name + "%'";
		}
		if(!StringUtil.isEmpty(sn)) {
			sql += " and a.sn like '%" + sn + "%'";
		}
		if(other != null && order != null){
			sql+=" order by "+other+" "+order;
		}
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer[] id) {
		if (id != null ){
			String ids = StringUtil.arrayToString(id, ",");
			String sql1 = "delete from es_groupbuying where id in (" + ids + ")";
			this.daoSupport.execute(sql1);
			
			
		}
	}
	
	public List<Map> queryResp(String queryStr) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("select m.sn,m.name name,m.store_name stroe_name  from es_goods m  ");

		if (!StringUtil.isEmpty(queryStr)) {
			sql.append(" where m.sn like ?");
		}
		List<Map> ret = null;

		ret = this.daoSupport.queryForList(sql.toString(),
				new Object[] { queryStr + '%' });

		if (ret == null) {
			ret = new ArrayList<Map>();
		}
		return ret;
	}
	
	public List<Map> getBatteryList() {
		String sql = "select m.sn,m.name name,m.store_name from es_goods m ";
		List<Map> list = this.daoSupport.queryForList(sql);

		return list;
	}
	public Goods querySn(String sn) {
		// TODO Auto-generated method stub
		   String sql = "select * from es_goods where sn=" + sn;
	        return daoSupport.queryForObject(sql, Goods.class);
	}
	@Override
	public Page goodsList(Integer pageNo, Integer pageSize) {
		
		
		String sql = "SELECT g.*,a.id,a.name,g.intro,a.store_name,a.sn,a.number,a.buying_money,a.buying_number,a.earnest,a.fictitious_number,a.end_date FROM es_groupbuying a INNER JOIN es_goods g ON a.sn=g.sn WHERE a.status=1 and g.market_enable=1 AND a.end_date>unix_timestamp()";
		
	
		
		sql += " order by a.end_date desc";
		
		Page goodsPage = this.daoSupport.queryForPage(sql, pageNo, pageSize);
		return goodsPage;
	}
	@Override
	public Map getId(Integer id) {
		//判断此商品是否存�?add_by DMRain 2016-8-30
		String sql = "";
	
		
		Map groupbuying = new HashMap();
		
		//如果此商品不存在  add_by DMRain 2016-8-30
		
			sql = "SELECT g.*,a.id,a.end_date,a.start_date,a.buying_number,a.earnest,a.buying_money,a.fictitious_number,a.number, b.name as brand_name from es_goods g left join es_brand b on g.brand_id=b.brand_id  left join es_groupbuying  a on a.sn=g.sn where g.market_enable=1 and a.status=1 and id=? ";

			groupbuying = this.daoSupport.queryForMap(sql,id);

			/**
			 * ====================== 对商品图片的处理 ======================
			 */
	 
			String small = (String) groupbuying.get("small");
			if (small != null) {
				small = StaticResourcesUtil.convertToUrl(small);
				groupbuying.put("small", small);
			}
			String big = (String) groupbuying.get("big");
			if (big != null) {
				big = StaticResourcesUtil.convertToUrl(big);
				groupbuying.put("big", big);
			
		}
 
		return groupbuying;
	}
}
