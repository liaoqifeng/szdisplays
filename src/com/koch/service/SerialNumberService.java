package com.koch.service;

import com.koch.entity.SerialNumber;
import com.koch.entity.SerialNumber.Type;


public interface SerialNumberService extends BaseService<SerialNumber>{
	public String generate(Type type);
}
