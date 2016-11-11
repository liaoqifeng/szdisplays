package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.FreightTempletAttributeDao;
import com.koch.dao.FreightTempletItemDao;
import com.koch.entity.FreightTempletAttribute;
import com.koch.entity.FreightTempletItem;
import com.koch.service.FreightTempletAttributeService;
import com.koch.service.FreightTempletItemService;

@Service
public class FreightTempletAttributeServiceImpl extends BaseServiceImpl<FreightTempletAttribute> implements FreightTempletAttributeService{
	@Resource
	private FreightTempletAttributeDao freightTempletAttributeDao;
	@Resource
	public void setBaseDao(FreightTempletAttributeDao freightTempletAttributeDao) {
		super.setBaseDao(freightTempletAttributeDao);
	}
}
