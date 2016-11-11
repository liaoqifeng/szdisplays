package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.ReturnsDao;
import com.koch.entity.Returns;
import com.koch.service.ReturnsService;

@Service
public class ReturnsServiceImpl extends BaseServiceImpl<Returns> implements ReturnsService{
	@Resource
	private ReturnsDao returnsDao;
	@Resource
	public void setBaseDao(ReturnsDao returnsDao) {
		super.setBaseDao(returnsDao);
	}
	
	
}
