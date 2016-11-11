package com.koch.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.koch.bean.OrderBy;
import com.koch.bean.OrderBy.OrderType;
import com.koch.dao.AreaDao;
import com.koch.entity.Area;
import com.koch.service.AreaService;

@Service
public class AreaServiceImpl extends BaseServiceImpl<Area> implements AreaService{
	@Resource
	private AreaDao areaDao;
	@Resource
	public void setBaseDao(AreaDao areaDao) {
		super.setBaseDao(areaDao);
	}
	@Cacheable(cacheName="area")
	public List<Area> findAll(){
		return areaDao.getAll(new OrderBy("orderList", OrderType.asc));
	}

	@Transactional
	@TriggersRemove(cacheName="area",removeAll=true)
	public Integer save(Area t) {
		return super.save(t);
	}
	
	@Transactional
	@TriggersRemove(cacheName="area",removeAll=true)
	public Area update(Area t) {
		return super.update(t);
	}
	
	@Transactional
	@TriggersRemove(cacheName="area",removeAll=true)
	public void delete(String id) {
		super.delete(id);
	}
	
	public List<Area> getList(Integer parentId) {
		return getList(findAll(),parentId);
	}
	
	public List<Area> findRoots(){
		return findRoots(null);
	}
	
	public List<Area> findRoots(Integer count){
		return this.areaDao.findRoots(count);
	}
	
	public List<Area> getList(List<Area> list, Integer parentId) {
		List<Area> areas = new ArrayList<Area>();
		if(list != null && list.size()>0){
			for(Area area : list){
				if(parentId == null && area.getParent() == null){
					areas.add(area);
				}else if(parentId != null && area.getParent() != null && parentId.equals(area.getParent().getId())){
					areas.add(area);
				}
			}
		}
		return areas;
	}
	
}
