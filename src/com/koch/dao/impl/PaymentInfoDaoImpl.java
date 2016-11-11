package com.koch.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.koch.dao.PaymentInfoDao;
import com.koch.entity.PaymentInfo;

@Repository("paymentInfoDao")
public class PaymentInfoDaoImpl extends BaseDaoImpl<PaymentInfo> implements PaymentInfoDao{
	public String getLastPaymentNumber(){
		String hql = "select a.number from PaymentInfo as a where a.id=(select max(b.id) from PaymentInfo as b)";
		List<String> list = entityManager.createQuery(hql).getResultList();
		if(list != null && list.size() > 0)
			return list.get(0);
		return "";
	}
}
