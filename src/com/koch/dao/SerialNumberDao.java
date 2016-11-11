package com.koch.dao;

import com.koch.entity.SerialNumber;
import com.koch.entity.SerialNumber.Type;

public interface SerialNumberDao extends BaseDao<SerialNumber>{
	public String generate(Type type);
}
