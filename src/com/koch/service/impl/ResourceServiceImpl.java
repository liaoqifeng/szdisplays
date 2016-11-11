package com.koch.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.ResourceDao;
import com.koch.service.ResourceService;


@Service
public class ResourceServiceImpl extends BaseServiceImpl<com.koch.entity.Resource> implements ResourceService{
	@Resource
	private ResourceDao resourceDao;
	@Resource
	public void setBaseDao(ResourceDao resourceDao) {
		super.setBaseDao(resourceDao);
	}
	public Map<com.koch.entity.Resource,List<com.koch.entity.Resource>> orderBy() {
		List<com.koch.entity.Resource> items = getAll();
		List<com.koch.entity.Resource> array = new ArrayList<com.koch.entity.Resource>();
		Map<com.koch.entity.Resource,List<com.koch.entity.Resource>> map = new LinkedHashMap<com.koch.entity.Resource, List<com.koch.entity.Resource>>();
		if(items != null){
			for(com.koch.entity.Resource item : items){
				if(item.getParentId()==0)
					array.add(item);
			}
			if(map != null){
				for (com.koch.entity.Resource resource : array) {
					List<com.koch.entity.Resource> resources = new ArrayList<com.koch.entity.Resource>();
					for(com.koch.entity.Resource item : items){
						if(resource.getId().equals(item.getParentId())){
							resources.add(item);
						}
					}
					map.put(resource, resources);
				}
			}
		}
		return map;
	}
	
	
}
