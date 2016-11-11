package com.koch.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Service;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;


public interface BaseService<T> {
	public Pager<T> findByPage(Pager<T> pager);
	
	public Pager<T> findByPager(CriteriaQuery<T> criteriaQuery, Pager<T> pager);
	
	public List<T> getAll();
	
	public List<T> getAll(OrderBy order);
	
	public List<T> find(Map parameters);
	
	public List<T> getList(String propertyName,Object propetyValue);
	
	public List<T> getList(String propertyName, Object value, OrderBy order);
	
	public T get(Integer id);
	
	public List<T> findList(Integer [] ids);
	
	public List<T> findAll();
	
	public List<T> findList(Integer count, List<Filter> filters, List<OrderBy> orders);
	
	public List<T> findList(Integer first, Integer count, List<Filter> filters, List<OrderBy> orders);
	
	public T get(String propertyName,Object propetyValue);
	
	public Long getTotalCount();
	
	public T update(T t);
	
	public Integer save(T t);
	
	public void delete(Integer id);
	
	public void delete(String id);
	
	public void delete(String [] ids);
	
	public void delete(Integer [] ids);
	
	public void delete(String propertyName, Object value);
	
	public void delete(T t);
}
