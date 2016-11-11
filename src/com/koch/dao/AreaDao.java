package com.koch.dao;

import java.util.List;

import com.koch.entity.Area;

public interface AreaDao extends BaseDao<Area>{
	public List<Area> findRoots(Integer count);
}
