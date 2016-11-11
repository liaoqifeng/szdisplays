package com.koch.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.koch.dao.DeliverInfoDao;
import com.koch.dao.ReturnsDao;
import com.koch.entity.DeliverInfo;
import com.koch.entity.Returns;

@Repository("returnsDao")
public class ReturnsDaoImpl extends BaseDaoImpl<Returns> implements ReturnsDao{
	
	public String getLastReturnsNumber(){
		String hql = "select a.number from Returns as a where a.id=(select max(b.id) from Returns as b)";
		List<String> list = entityManager.createQuery(hql).getResultList();
		if(list != null && list.size() > 0)
			return list.get(0);
		return "";
	}
	
}
