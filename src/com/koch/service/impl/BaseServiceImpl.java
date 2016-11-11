package com.koch.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaQuery;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.dao.BaseDao;
import com.koch.service.BaseService;

public class BaseServiceImpl<T> implements BaseService<T>{
	private BaseDao<T> baseDao;

	public BaseDao<T> getBaseDao() {
		return baseDao;
	}
	public void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}

	@Transactional(readOnly=true)
	public Pager<T> findByPage(Pager<T> pager) {
		return baseDao.findByPager(pager);
	}
	
	@Transactional(readOnly=true)
	public Pager<T> findByPager(CriteriaQuery<T> criteriaQuery, Pager<T> pager){
		return baseDao.findByPager(criteriaQuery, pager);
	}

	@Transactional(readOnly=true)
	public List<T> getAll() {
		return baseDao.getAll();
	}

	@Transactional(readOnly=true)
	public List<T> getAll(OrderBy order){
		return baseDao.getAll(order);
	}
	
	@Transactional(readOnly=true)
	public List<T> getList(String propertyName, Object propetyValue) {
		return baseDao.getList(propertyName, propetyValue);
	}
	
	@Transactional(readOnly=true)
	public List<T> getList(String propertyName, Object value, OrderBy order) {
		return baseDao.getList(propertyName, value, order);
	}
	
	public List<T> find(Map parameters) {
		return null;
	}
	
	@Transactional(readOnly = true)
	public List<T> findAll() {
		return findList(null, null, null, null);
	}
	
	@Transactional(readOnly = true)
	public List<T> findList(Integer count, List<Filter> filters, List<OrderBy> orders) {
		return findList(null, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<T> findList(Integer first, Integer count, List<Filter> filters, List<OrderBy> orders) {
		return this.baseDao.findList(first, count, filters, orders);
	}

	@Transactional(readOnly=true)
	public T get(Integer id) {
		return baseDao.get(id);
	}
	
	@Transactional(readOnly=true)
	public List<T> findList(Integer [] ids){
		if(ids == null || ids.length <= 0)
			return new ArrayList<T>();
		return baseDao.findList(ids);
	}

	public T get(String propertyName, Object propetyValue) {
		return baseDao.get(propertyName, propetyValue);
	}
	
	public Long getTotalCount(){
		return baseDao.getTotalCount();
	}

	@Transactional
	public T update(T t) {
		return baseDao.update(t);
	}
	
	@Transactional
	public Integer save(T t) {
		return baseDao.save(t);
	}
	
	@Transactional
	public void delete(Integer id){
		baseDao.delete(id);
	}
	
	@Transactional
	public void delete(String id) {
		baseDao.delete(id);
	}

	@Transactional
	public void delete(String[] ids) {
		baseDao.delete(ids);
	}
	
	@Transactional
	public void delete(Integer[] ids) {
		if (ids != null) {
			for (Integer id : ids) {
				delete(this.baseDao.get(id));
			}
		}
	}
	
	@Transactional
	public void delete(String propertyName, Object value){
		baseDao.delete(propertyName, value);
	}
	
	@Transactional
	public void delete(T t){
		baseDao.delete(t);
	}
	
}
