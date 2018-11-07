package com.enation.app.b2b2c.core.statistics.service;

import java.util.Map;

import com.enation.framework.database.Page;

public interface IB2b2cMemberRecordManager {

	public Page searchApartments(Map map, Integer page, Integer pageSize, String other, String order);

}
