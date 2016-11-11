package com.koch.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.bean.OrderListComparator;
import com.koch.dao.DeliverwayDao;
import com.koch.service.DeliverwayService;
import com.koch.entity.Deliverway;

@Service
public class DeliverwayServiceImpl extends BaseServiceImpl<Deliverway> implements DeliverwayService{
	@Resource
	private DeliverwayDao deliverwayDao;
	@Resource
	public void setBaseDao(DeliverwayDao deliverwayDao) {
		super.setBaseDao(deliverwayDao);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Deliverway> getAll() {
		List<Deliverway> list = super.getAll();
		if(list != null && list.size()>0){
			Deliverway [] deliverways = list.toArray(new Deliverway[list.size()]);
			Arrays.sort(deliverways, new OrderListComparator());
			return Arrays.asList(deliverways);
		}
		return null;
	}
	
	
}
