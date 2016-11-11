package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.FreightTempletItemDao;
import com.koch.entity.FreightTempletItem;
import com.koch.service.FreightTempletItemService;

@Service
public class FreightTempletItemServiceImpl extends BaseServiceImpl<FreightTempletItem> implements FreightTempletItemService{
	@Resource
	private FreightTempletItemDao freightTempletItemDao;
	@Resource
	public void setBaseDao(FreightTempletItemDao freightTempletItemDao) {
		super.setBaseDao(freightTempletItemDao);
	}
}
