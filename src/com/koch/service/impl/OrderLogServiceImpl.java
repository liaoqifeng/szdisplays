package com.koch.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.OrderLogDao;
import com.koch.entity.OrderLog;
import com.koch.service.OrderLogService;

@Service
public class OrderLogServiceImpl extends BaseServiceImpl<OrderLog> implements OrderLogService{
	@Resource
	private OrderLogDao orderLogDao;
	@Resource
	public void setBaseDao(OrderLogDao orderLogDao) {
		super.setBaseDao(orderLogDao);
	}
}
