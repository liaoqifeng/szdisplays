package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.ReceiverDao;
import com.koch.entity.Receiver;
import com.koch.service.ReceiverService;

@Service
public class ReceiverServiceImpl extends BaseServiceImpl<Receiver> implements ReceiverService{
	@Resource
	private ReceiverDao receiverDao;
	@Resource
	public void setBaseDao(ReceiverDao receiverDao) {
		super.setBaseDao(receiverDao);
	}
	
	
}
