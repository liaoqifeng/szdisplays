package com.koch.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.koch.dao.DeliverInfoDao;
import com.koch.entity.DeliverInfo;

@Repository("deliverInfoDao")
public class DeliverInfoDaoImpl extends BaseDaoImpl<DeliverInfo> implements DeliverInfoDao{
	public String getLastDeliverNumber(){
		String hql = "select a.number from DeliverInfo as a where a.id=(select max(b.id) from DeliverInfo as b)";
		List<String> list = entityManager.createQuery(hql).getResultList();
		if(list != null && list.size() > 0)
			return list.get(0);
		return "";
	}
}
