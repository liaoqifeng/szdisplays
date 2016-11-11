package com.koch.dao.impl;

import org.springframework.stereotype.Repository;

import com.koch.dao.OrderItemDao;
import com.koch.entity.OrderItem;

@Repository("orderItemDao")
public class OrderItemDaoImpl extends BaseDaoImpl<OrderItem> implements OrderItemDao{
	
}
