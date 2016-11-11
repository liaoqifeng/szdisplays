package com.koch.service;

import com.koch.entity.PaymentInfo;

public interface PaymentInfoService extends BaseService<PaymentInfo>{
	public String getLastPaymentNumber();
}
