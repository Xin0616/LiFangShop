package com.enation.app.b2b2c.core.store.service.bill;

import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.core.store.model.bill.BillDetail;
import com.enation.framework.database.Page;
/**
 * 商品销量
 * @author fenlongli
 *
 */
public interface ISalesVolumeManager {
	/**
	 * 获取结算单分页列表
	 * @param pageNO 页码
	 * @param pageSize 每页显示数量
	 * @return
	 */
	public Page sales_list(Map map ,Integer pageNo,Integer pageSize);

	
}
