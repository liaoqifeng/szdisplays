package com.koch.dao;

import com.koch.entity.PaymentInfo;

public interface PaymentInfoDao extends BaseDao<PaymentInfo>{
	public String getLastPaymentNumber();
}
