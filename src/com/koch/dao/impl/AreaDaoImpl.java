package com.koch.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.koch.dao.AreaDao;
import com.koch.entity.Area;

@Repository("areaDao")
public class AreaDaoImpl extends BaseDaoImpl<Area> implements AreaDao{
	public List<Area> findRoots(Integer count) {
		String str = "select area from Area area where area.parent is null order by area.orderList asc";
		TypedQuery<Area> query = this.entityManager.createQuery(str, Area.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count.intValue());
		}
		return query.getResultList();
	}
}
