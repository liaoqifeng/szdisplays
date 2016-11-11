package com.koch.dao;

import com.koch.entity.DeliverInfo;

public interface DeliverInfoDao extends BaseDao<DeliverInfo>{
	public String getLastDeliverNumber();
}
