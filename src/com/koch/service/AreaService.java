package com.koch.service;

import java.util.List;

import com.koch.entity.Area;

public interface AreaService extends BaseService<Area>{
	public List<Area> findAll();
	public List<Area> getList(Integer parentId);
	public List<Area> getList(List<Area> list, Integer parentId);
	public List<Area> findRoots();
	public List<Area> findRoots(Integer count);
}
