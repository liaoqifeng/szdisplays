package com.koch.dao.impl;
import org.springframework.stereotype.Repository;

import com.koch.dao.OrderLogDao;
import com.koch.entity.OrderLog;

@Repository("orderLogDao")
public class OrderLogDaoImpl extends BaseDaoImpl<OrderLog> implements OrderLogDao{
	
}
