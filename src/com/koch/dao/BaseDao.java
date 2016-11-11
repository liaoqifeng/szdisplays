package com.koch.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaQuery;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;

public interface BaseDao<T>{
	/**
	 * 根据ID获取实体对象.
	 * 
	 * @param id
	 *            记录ID
	 * @return 实体对象
	 */

	public T get(Integer id);
	
	/**
	 * 根据ID数组获取实体对象集合.
	 * 
	 * @param ids
	 *            ID对象数组
	 * 
	 * @return 实体对象集合
	 */
	public List<T> findList(Integer [] ids);
	
	/**
	 * 根据条件获取区间内的对象集合
	 * @param first
	 * @param count
	 * @param filters
	 * @param orders
	 * @return
	 */
	public List<T> findList(Integer first, Integer count, List<Filter> filters, List<OrderBy> orders);
	
	/**
	 * 根据属性名和属性值获取实体对象.
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @return 实体对象
	 */
	public T get(String propertyName, Object value);
	
	/**
	 * 根据属性名和属性值获取实体对象集合.
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @return 实体对象集合
	 */
	public List<T> getList(String propertyName, Object value);

	/**
	 * 根据属性名、属性值和排序方式获取实体对象集合.
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @param order
	 *            排序方式
	 * @return 实体对象集合
	 */
	public List<T> getList(String propertyName, Object value, OrderBy order);
	/**
	 * 获取所有实体对象集合.
	 * 
	 * @return 实体对象集合
	 */
	public List<T> getAll();
	
	/**
	 * 获取所有实体对象集合.
	 * 
	 * @param order
	 *            排序方式
	 * @return 实体对象集合
	 */
	public List<T> getAll(OrderBy order);
	
	/**
	 * 获取所有实体对象总数.
	 * 
	 * @return 实体对象总数
	 */
	public Long getTotalCount();

	/**
	 * 根据属性名、修改前后属性值判断在数据库中是否唯一(若新修改的值与原来值相等则直接返回true).
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param oldValue
	 *            修改前的属性值
	 * @param oldValue
	 *            修改后的属性值
	 * @return boolean
	 */
	public boolean isUnique(String propertyName, Object oldValue, Object newValue);
	
	/**
	 * 根据属性名判断数据是否已存在.
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            值
	 * @return boolean
	 */
	public boolean isExist(String propertyName, Object value);

	/**
	 * 保存实体对象.
	 * 
	 * @param entity
	 *            对象
	 * @return ID
	 */
	public Integer save(T entity);
	
	/**
	 * 更新实体对象.
	 * 
	 * @param entity
	 *            对象
	 */
	public T update(T entity);
	
	/**
	 * 删除实体对象.
	 * 
	 * @param entity
	 *            对象
	 * @return
	 */
	public void delete(T entity);

	/**
	 * 根据ID删除实体对象.
	 * 
	 * @param id
	 *            记录ID
	 */
	public void delete(Integer id);
	
	/**
	 * 根据ID删除实体对象.
	 * 
	 * @param id
	 *            记录ID
	 */
	public void delete(String id);
	
	/**
	 * 根据属性名称删除实体对象.
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 * 			  属性值           
	 */
	public void delete(String propertyName, Object value);

	/**
	 * 根据ID数组删除实体对象.
	 * 
	 * @param ids
	 *            ID数组
	 */
	public void delete(String[] ids);
	
	/**
	 * 根据ID数组删除实体对象.
	 * 
	 * @param ids
	 *            ID数组
	 */
	public void delete(Integer [] ids);

	/**
	 * 判断实体是否处于托管状态
	 */
	public boolean isManaged(T t);
	
	/**
	 * 获取实体ID
	 */
	public Serializable getIdentifier(T entity);
	
	/**
	 * 刷新session.
	 * 
	 */
	
	public void flush();
	/**
	 * 刷新实体
	 * 
	 */
	
	public void refresh(T t);
	
	/**
	 * 清除Session.
	 * 
	 */
	public void clear();
	
	/**
	 * 分离某一对象.
	 * 
	 * @param object
	 *            需要分离的对象
	 */
	public void evict(Object object);

	/**
	 * 锁定某一对象.
	 * 
	 * @param entity
	 *            需要清除的对象
	 * @param lockModeType
	 *            锁类型 
	 */
	public void lock(T entity, LockModeType lockModeType);
	
	/**
	 * 查询集合条数
	 * @param filters
	 * @return
	 */
	public long count(Filter[] filters);
	
	/**
	 * 根据Pager对象进行查询(提供分页、查找、排序功能).
	 * 
	 * @param pager
	 *            Pager对象
	 * @return Pager对象
	 */
	public Pager<T> findByPager(Pager<T> pager);
	
	/**
	 * 根据Pager对象进行查询(提供分页、查找、排序功能).
	 * @param criteriaQuery
	 * 					CriteriaQuery<T>对象
	 * @param pager
	 *            Pager对象
	 * @return Pager对象
	 */
	public Pager<T> findByPager(CriteriaQuery<T> criteriaQuery, Pager<T> pager);
}
