package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.SpecAttributeDao;
import com.koch.entity.SpecAttribute;
import com.koch.service.SpecAttributeService;

@Service
public class SpecAttributeServiceImpl extends BaseServiceImpl<SpecAttribute> implements SpecAttributeService{
	@Resource
	private SpecAttributeDao specAttributeDao;
	@Resource
	public void setBaseDao(SpecAttributeDao specAttributeDao) {
		super.setBaseDao(specAttributeDao);
	}
}
