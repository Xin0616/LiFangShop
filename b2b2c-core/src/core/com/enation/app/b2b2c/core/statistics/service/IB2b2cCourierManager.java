package com.enation.app.b2b2c.core.statistics.service;

import java.util.Map;

import com.enation.framework.database.Page;

public interface IB2b2cCourierManager {
	public Page searchApartments(Map map, Integer page, Integer pageSize, String other, String order);
	public Page courierList(Integer pageNo, Integer pageSize);


}
