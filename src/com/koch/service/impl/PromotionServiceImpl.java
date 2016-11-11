package com.koch.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.dao.PromotionDao;
import com.koch.entity.Promotion;
import com.koch.service.PromotionService;

@Service
public class PromotionServiceImpl extends BaseServiceImpl<Promotion> implements PromotionService{
	@Resource
	public PromotionDao promotionDao;
	
	@Resource
	public void setBaseDao(PromotionDao promotionDao) {
		super.setBaseDao(promotionDao);
	}
	
	@Transactional(readOnly = true)
	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<OrderBy> orders) {
		return this.promotionDao.findList(hasBegun, hasEnded, count, filters, orders);
	}
	  
	@Transactional(readOnly = true)
	@Cacheable(cacheName="promotion")
	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<OrderBy> orders, String cacheRegion) {
		return this.promotionDao.findList(hasBegun, hasEnded, count, filters, orders);
	}
	  
	@Transactional
	@TriggersRemove(cacheName = { "promotion" }, removeAll = true)
	public Integer save(Promotion promotion) {
		return super.save(promotion);
	}
	  
	@Transactional
	@TriggersRemove(cacheName = { "promotion" }, removeAll = true)
	public Promotion update(Promotion promotion) {
		return super.update(promotion);
	}
	  
	@Transactional
	@TriggersRemove(cacheName = { "promotion" }, removeAll = true)
	public void delete(Integer id) {
		super.delete(id);
	}
	  
	@Transactional
	@TriggersRemove(cacheName = { "promotion" }, removeAll = true)
	public void delete(Promotion promotion) {
		super.delete(promotion);
	}
}
