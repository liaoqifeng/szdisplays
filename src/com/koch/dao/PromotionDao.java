package com.koch.dao;

import java.util.List;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.entity.Promotion;

public interface PromotionDao extends BaseDao<Promotion>{
	
	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<OrderBy> orders);
	
}
