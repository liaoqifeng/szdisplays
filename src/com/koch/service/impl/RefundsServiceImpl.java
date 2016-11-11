package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.RefundsDao;
import com.koch.entity.Refunds;
import com.koch.service.RefundsService;

@Service
public class RefundsServiceImpl extends BaseServiceImpl<Refunds> implements RefundsService{
	@Resource
	private RefundsDao refundsDao;
	@Resource
	public void setBaseDao(RefundsDao refundsDao) {
		super.setBaseDao(refundsDao);
	}
	
}
