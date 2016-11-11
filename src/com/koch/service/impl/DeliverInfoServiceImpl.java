package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.DeliverInfoDao;
import com.koch.entity.DeliverInfo;
import com.koch.service.DeliverInfoService;

@Service
public class DeliverInfoServiceImpl extends BaseServiceImpl<DeliverInfo> implements DeliverInfoService{
	@Resource
	private DeliverInfoDao deliverInfoDao;
	@Resource
	public void setBaseDao(DeliverInfoDao deliverInfoDao) {
		super.setBaseDao(deliverInfoDao);
	}
	
	public String getLastDeliverNumber(){
		return this.deliverInfoDao.getLastDeliverNumber();
	}
}
