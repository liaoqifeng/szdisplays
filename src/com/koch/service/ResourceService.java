package com.koch.service;

import java.util.List;
import java.util.Map;

import com.koch.entity.Resource;
import org.springframework.stereotype.Service;


public interface ResourceService extends BaseService<Resource>{
	public Map<com.koch.entity.Resource,List<Resource>> orderBy();
}
