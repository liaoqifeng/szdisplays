package com.koch.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.bean.OrderBy;
import com.koch.bean.OrderBy.OrderType;
import com.koch.dao.LogisticsDao;
import com.koch.entity.Logistics;
import com.koch.service.LogisticsService;

@Service
public class LogisticsServiceImpl extends BaseServiceImpl<Logistics> implements LogisticsService{
	@Resource
	private LogisticsDao logisticsDao;
	@Resource
	public void setBaseDao(LogisticsDao logisticsDao) {
		super.setBaseDao(logisticsDao);
	}
	@Override
	public List<Logistics> getAll() {
		List<Logistics> list = logisticsDao.getAll(new OrderBy("orderList", OrderType.asc));
		return list;
	}
	
	
}
