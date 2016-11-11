package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koch.dao.PropertyDao;
import com.koch.entity.Property;
import com.koch.service.PropertyService;
import com.koch.util.JsonUtil;

@Service
public class PropertyServiceImpl extends BaseServiceImpl<Property> implements PropertyService{
	@Resource
	public void setBaseDao(PropertyDao propertyDao) {
		super.setBaseDao(propertyDao);
	}
	@Transactional
	public Integer save(Property t) {
		if(t != null && t.getOptions() != null){
			String json = JsonUtil.toJson(t.getOptions()).toString();
			t.setValue(json);
		}
		return super.save(t);
	}
	@Transactional
	public Property update(Property t) {
		if(t != null && t.getOptions() != null){
			String json = JsonUtil.toJson(t.getOptions()).toString();
			t.setValue(json);
		}
		return super.update(t);
	}
	@Transactional
	public void delete(Integer[] ids) {
		super.delete(ids);
	}
	
	
}
