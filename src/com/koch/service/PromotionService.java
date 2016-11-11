package com.koch.service;

import java.util.List;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.entity.Promotion;


public interface PromotionService extends BaseService<Promotion>{
	
	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<OrderBy> orders);
	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<OrderBy> orders, String cacheRegion);
}
