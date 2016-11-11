package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.OrderItemDao;
import com.koch.entity.OrderItem;
import com.koch.service.OrderItemService;

@Service
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem> implements OrderItemService{
	@Resource
	private OrderItemDao orderItemDao;
	@Resource
	public void setBaseDao(OrderItemDao orderItemDao) {
		super.setBaseDao(orderItemDao);
	}
}
