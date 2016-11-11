package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.PaymentInfoDao;
import com.koch.entity.PaymentInfo;
import com.koch.service.PaymentInfoService;

@Service
public class PaymentInfoServiceImpl extends BaseServiceImpl<PaymentInfo> implements PaymentInfoService{
	@Resource
	private PaymentInfoDao paymentInfoDao;
	@Resource
	public void setBaseDao(PaymentInfoDao paymentInfoDao) {
		super.setBaseDao(paymentInfoDao);
	}
	
	public String getLastPaymentNumber(){
		return this.paymentInfoDao.getLastPaymentNumber();
	}
}
