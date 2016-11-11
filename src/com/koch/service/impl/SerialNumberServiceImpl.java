package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koch.dao.SerialNumberDao;
import com.koch.entity.SerialNumber;
import com.koch.entity.SerialNumber.Type;
import com.koch.service.SerialNumberService;

@Service
public class SerialNumberServiceImpl extends BaseServiceImpl<SerialNumber> implements SerialNumberService{
	@Resource
	public SerialNumberDao serialNumberDao;
	
	@Resource
	public void setBaseDao(SerialNumberDao serialNumberDao) {
		super.setBaseDao(serialNumberDao);
	}
	
	@Transactional
	public String generate(Type type){
		return serialNumberDao.generate(type);
	}
}
