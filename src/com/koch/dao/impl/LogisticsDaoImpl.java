package com.koch.dao.impl;

import org.springframework.stereotype.Repository;

import com.koch.dao.LogisticsDao;
import com.koch.entity.Logistics;

@Repository("logisticsDao")
public class LogisticsDaoImpl extends BaseDaoImpl<Logistics> implements LogisticsDao{
	
}
