package com.koch.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.bean.OrderListComparator;
import com.koch.dao.PaymentwayDao;
import com.koch.service.PaymentwayService;
import com.koch.entity.Paymentway;

@Service
public class PaymentwayServiceImpl extends BaseServiceImpl<Paymentway> implements PaymentwayService{
	@Resource
	private PaymentwayDao paymentwayDao;
	@Resource
	public void setBaseDao(PaymentwayDao paymentwayDao) {
		super.setBaseDao(paymentwayDao);
	}
	@Override
	public List<Paymentway> getAll() {
		List<Paymentway> list = super.getAll();
		if(list != null && list.size()>0){
			Paymentway [] paymentways = list.toArray(new Paymentway[list.size()]);
			Arrays.sort(paymentways,new OrderListComparator());
			return Arrays.asList(paymentways);
		}
		return null;
	}
	
	
}
