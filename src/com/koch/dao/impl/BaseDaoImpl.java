package com.koch.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.bean.Pager.OrderType;
import com.koch.dao.BaseDao;

@Repository
public class BaseDaoImpl<T> implements BaseDao<T> {
	private static volatile long GeneratedAlias = 0L;
	private Class<T> entityClass;
	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		this.entityClass = null;
		Class c = getClass();
		Type type = c.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
			this.entityClass = (Class<T>) parameterizedType[0];
		}
	}

	public T get(Integer id) {
		if(id == null)
			return null;
		return (T) entityManager.find(entityClass, id);
	}

	public List<T> findList(Integer [] ids) {
		Assert.notEmpty(ids, "ids must not be empty");
		String where = "";
		for (int i = 0; i < ids.length; i++) {
			if (i == 0)
				where += ids[i];
			else
				where += "," + ids[i];
		}
		String hql = "from " + entityClass.getName() + " as model where model.id in("+where+")";
		List<T> list = entityManager.createQuery(hql).getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	public T get(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		String hql = "from " + entityClass.getName() + " as model where model." + propertyName + " = ?";
		T t = null;
		try {
			t = (T) entityManager.createQuery(hql).setParameter(1, value).getSingleResult();
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		}
		return t;
	}

	public List<T> getList(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		return getList(propertyName,value,null);
	}

	@SuppressWarnings("unchecked")
	public List<T> getList(String propertyName, Object value, OrderBy order) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		String hql = "from " + entityClass.getName() + " as model where model."+ propertyName + " = ?";
		if (order != null && StringUtils.isNotEmpty(order.getOrderBy())) {
			hql += " order by model." + order.getOrderBy();
			if (order.getOrderType() == com.koch.bean.OrderBy.OrderType.asc) {
				hql += " asc";
			} else {
				hql += " desc";
			}
		}
		List<T> list = null;
		try {
			list = entityManager.createQuery(hql).setParameter(1, value).getResultList();
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll(OrderBy order) {
		String o = "";
		if (order.getOrderType() == com.koch.bean.OrderBy.OrderType.asc)
			o = "asc";
		else
			o = "desc";
		String hql = "from " + entityClass.getName() + " order by " + order.getOrderBy() + " " + o;
		return entityManager.createQuery(hql).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		String hql = "from " + entityClass.getName();
		return entityManager.createQuery(hql).getResultList();
	}

	public Long getTotalCount() {
		String hql = "select count(*) from " + entityClass.getName();
		return (Long) entityManager.createQuery(hql).getSingleResult();
	}

	public boolean isUnique(String propertyName, Object oldValue,Object newValue) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(newValue, "newValue is required");
		if (newValue == oldValue || newValue.equals(oldValue)) {
			return true;
		}
		if (newValue instanceof String) {
			if (oldValue != null && StringUtils.equalsIgnoreCase((String) oldValue,(String) newValue)) {
				return true;
			}
		}
		T object = get(propertyName, newValue);
		return (object == null);
	}

	public boolean isExist(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		List list = getList(propertyName, value);
		return (list != null && list.size() > 0);
	}

	public Integer save(T entity) {
		Assert.notNull(entity, "entity is required");
		entityManager.persist(entity);
		return (Integer) getIdentifier(entity);
	}

	public Serializable getIdentifier(T entity) {
		Assert.notNull(entity);
		return (Serializable) this.entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
	}

	public T update(T entity) {
		Assert.notNull(entity, "entity is required");
		return entityManager.merge(entity);
	}

	public void delete(T entity) {
		Assert.notNull(entity, "entity is required");
		entityManager.remove(entity);
	}

	public void delete(String id) {
		delete(Integer.valueOf(id));
	}
	public void delete(Integer id) {
		Assert.notNull(id, "id is required");
		T entity = entityManager.getReference(entityClass, id);
		delete(entity);
	}
	public void delete(String[] ids) {
		Assert.notEmpty(ids, "ids must not be empty");
		for (String id : ids) {
			delete(Integer.valueOf(id));
		}
	}

	public void delete(String propertyName, Object value) {
		Assert.notNull(propertyName, "propertyName is required");
		Assert.notNull(value, "value is required");
		String hql = "delete from " + entityClass.getName() + " where "+ propertyName + " =?";
		entityManager.createQuery(hql).setParameter(1, value).executeUpdate();
	}

	public void delete(Integer[] ids) {
		Assert.notEmpty(ids, "ids must not be empty");
		for (int i = 0; i < ids.length; i++) {
			T entity = entityManager.getReference(entityClass, ids[i]);
			delete(entity);
		}
	}
	
	public boolean isManaged(T t){
		return entityManager.contains(t);
	}
	
	public void flush() {
		this.entityManager.flush();
	}
	
	public void refresh(T t){
		this.entityManager.refresh(t);
	}
	
	public void clear() {
		this.entityManager.clear();
	}
	
	public void evict(Object object) {
		Assert.notNull(object, "object is required");
		this.entityManager.detach(object);
	}
	
	public void lock(T entity, LockModeType lockModeType){
	    if ((entity != null) && (lockModeType != null))
	      this.entityManager.lock(entity, lockModeType);
	 }

	private Root<T> getRoot(CriteriaQuery<T> criteriaQuery) {
		if (criteriaQuery != null)
			return getRoot(criteriaQuery, criteriaQuery.getResultType());
		return null;
	}
	@SuppressWarnings("unchecked")
	private Root<T> getRoot(CriteriaQuery<?> criteriaQuery, Class<T> paramClass) {
		if ((criteriaQuery != null) && (criteriaQuery.getRoots() != null)
				&& (paramClass != null)) {
			Iterator localIterator = criteriaQuery.getRoots().iterator();
			while (localIterator.hasNext()) {
				Root localRoot = (Root) localIterator.next();
				if (paramClass.equals(localRoot.getJavaType()))
					return (Root) localRoot.as(paramClass);
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	private void setParameter(CriteriaQuery<T> criteriaQuery,List<Filter> paramList) {
		if ((criteriaQuery == null) || (paramList == null)|| (paramList.isEmpty()))
			return;
		Root root = getRoot(criteriaQuery);
		if (root == null)
			return;
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		Predicate predicate = criteriaQuery.getRestriction() != null ? criteriaQuery.getRestriction(): criteriaBuilder.conjunction();
		Iterator iterator = paramList.iterator();
		while (iterator.hasNext()) {
			Filter filter = (Filter) iterator.next();
			if ((filter != null) && (!StringUtils.isEmpty(filter.getProperty()))){
				if ((filter.getOperator() == Filter.Operator.eq) && (filter.getValue() != null)) {
					if ((filter.getIgnoreCase() != null) && (filter.getIgnoreCase().booleanValue()) && ((filter.getValue() instanceof String)))
						predicate = criteriaBuilder.and(predicate,criteriaBuilder.equal(criteriaBuilder.lower(root.get(filter.getProperty())),((String) filter.getValue()).toLowerCase()));
					else
						predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(filter.getProperty()), filter.getValue()));
				} else if ((filter.getOperator() == Filter.Operator.ne) && (filter.getValue() != null)) {
					if ((filter.getIgnoreCase() != null)&& (filter.getIgnoreCase().booleanValue())&& ((filter.getValue() instanceof String)))
						predicate = criteriaBuilder.and(predicate,criteriaBuilder.notEqual(criteriaBuilder.lower(root.get(filter.getProperty())),((String) filter.getValue()).toLowerCase()));
					else
						predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.get(filter.getProperty()), filter.getValue()));
				} else if ((filter.getOperator() == Filter.Operator.gt) && (filter.getValue() != null))
					predicate = criteriaBuilder.and(predicate,criteriaBuilder.gt(root.get(filter.getProperty()), (Number) filter.getValue()));
				else if ((filter.getOperator() == Filter.Operator.lt) && (filter.getValue() != null))
					predicate = criteriaBuilder.and(predicate,criteriaBuilder.lt(root.get(filter.getProperty()), (Number) filter.getValue()));
				else if ((filter.getOperator() == Filter.Operator.ge) && (filter.getValue() != null))
					predicate = criteriaBuilder.and(predicate,criteriaBuilder.ge(root.get(filter.getProperty()), (Number) filter.getValue()));
				else if ((filter.getOperator() == Filter.Operator.le) && (filter.getValue() != null))
					predicate = criteriaBuilder.and(predicate,criteriaBuilder.le(root.get(filter.getProperty()), (Number) filter.getValue()));
				else if ((filter.getOperator() == Filter.Operator.like) && (filter.getValue() != null) && ((filter.getValue() instanceof String)))
					predicate = criteriaBuilder.and(predicate,criteriaBuilder.like(root.get(filter.getProperty()), "%"+(String) filter.getValue()+"%"));
				else if ((filter.getOperator() == Filter.Operator.in) && (filter.getValue() != null))
					predicate = criteriaBuilder.and(predicate,root.get(filter.getProperty()).in(new Object[] { filter.getValue() }));
				else if (filter.getOperator() == Filter.Operator.isNull)
					predicate = criteriaBuilder.and(predicate,root.get(filter.getProperty()).isNull());
				else if (filter.getOperator() == Filter.Operator.isNotNull)
					predicate = criteriaBuilder.and(predicate,root.get(filter.getProperty()).isNotNull());
			}
		}
		criteriaQuery.where(predicate);
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	private void setOrders(CriteriaQuery<T> criteriaQuery, List<OrderBy> paramList) {
		if ((criteriaQuery == null) || (paramList == null)
				|| (paramList.isEmpty()))
			return;
		Root root = getRoot(criteriaQuery);
		if (root == null)
			return;
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		List list = new ArrayList();
		if (!criteriaQuery.getOrderList().isEmpty())
			list.addAll(criteriaQuery.getOrderList());
		Iterator<OrderBy> iterator = paramList.iterator();
		while (iterator.hasNext()) {
			OrderBy order = iterator.next();
			if (order.getOrderType() == OrderBy.OrderType.asc) {
				list.add(criteriaBuilder.asc(root.get(order.getOrderBy())));
			} else {
				if (order.getOrderType() != OrderBy.OrderType.desc)
					continue;
				list.add(criteriaBuilder.desc(root.get(order.getOrderBy())));
			}
		}
		criteriaQuery.orderBy(list);
	}
	
	public long count(Filter[] filters) {
	    CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
	    CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(this.entityClass);
	    criteriaQuery.select(criteriaQuery.from(this.entityClass));
	    return count(criteriaQuery, filters != null ? Arrays.asList(filters) : null).longValue();
	}
	
	@SuppressWarnings("unchecked")
	protected Long count(CriteriaQuery<T> paramCriteriaQuery, List<Filter> paramList){
	    Assert.notNull(paramCriteriaQuery);
	    Assert.notNull(paramCriteriaQuery.getSelection());
	    Assert.notEmpty(paramCriteriaQuery.getRoots());
	    CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
	    setParameter(paramCriteriaQuery, paramList);
	    CriteriaQuery criteriaQuery = localCriteriaBuilder.createQuery(Long.class);
	    Iterator iterator = paramCriteriaQuery.getRoots().iterator();
	    while (iterator.hasNext()) {
			Root root1 = (Root) iterator.next();
			Root root2 = criteriaQuery.from(root1.getJavaType());
			root2.alias(generatedAlias(root1));
			translationForm(root1, root2);
		}
	    Root root = getRoot(criteriaQuery, paramCriteriaQuery.getResultType());
	    criteriaQuery.select(localCriteriaBuilder.count(root));
	    if (paramCriteriaQuery.getGroupList() != null)
	    	criteriaQuery.groupBy(paramCriteriaQuery.getGroupList());
	    if (paramCriteriaQuery.getGroupRestriction() != null)
	    	criteriaQuery.having(paramCriteriaQuery.getGroupRestriction());
	    if (paramCriteriaQuery.getRestriction() != null)
	    	criteriaQuery.where(paramCriteriaQuery.getRestriction());
	    return (Long)this.entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findList(Integer first, Integer count, List<Filter> filters, List<OrderBy> orders) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(this.entityClass);
		criteriaQuery.select(criteriaQuery.from(this.entityClass));
		return findList(criteriaQuery, first, count, filters, orders);
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> findList(CriteriaQuery<T> paramCriteriaQuery, Integer first, Integer count, List<Filter> filters, List<OrderBy> orders) {
		Assert.notNull(paramCriteriaQuery);
		Assert.notNull(paramCriteriaQuery.getSelection());
		Assert.notEmpty(paramCriteriaQuery.getRoots());
		CriteriaBuilder localCriteriaBuilder = this.entityManager.getCriteriaBuilder();
		Root root = getRoot(paramCriteriaQuery);
		setParameter(paramCriteriaQuery, filters);
		setOrders(paramCriteriaQuery, orders);
		if (paramCriteriaQuery.getOrderList().isEmpty())
			paramCriteriaQuery.orderBy(new javax.persistence.criteria.Order[] { localCriteriaBuilder.desc(root.get("createDate")) });
		TypedQuery typedQuery = this.entityManager.createQuery(
				paramCriteriaQuery).setFlushMode(FlushModeType.COMMIT);
		if (first != null)
			typedQuery.setFirstResult(first.intValue());
		if (count != null)
			typedQuery.setMaxResults(count.intValue());
		return typedQuery.getResultList();
	}
	
	private synchronized String generatedAlias(Selection<?> paramSelection) {
	    if (paramSelection != null){
	      String str = paramSelection.getAlias();
	      if (str == null){
	        if (GeneratedAlias >= 1000L)
	        	GeneratedAlias = 0L;
	        str = "GeneratedAlias" + GeneratedAlias++;
	        paramSelection.alias(str);
	      }
	      return str;
	    }
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	private void translationForm(From<?, ?> paramFrom1, From<?, ?> paramFrom2) {
		Iterator localIterator = paramFrom1.getJoins().iterator();
		Object localObject1;
		Object localObject2;
		while (localIterator.hasNext()) {
			localObject1 = (Join) localIterator.next();
			localObject2 = paramFrom2.join(((Join) localObject1).getAttribute().getName(), ((Join) localObject1).getJoinType());
			((Join) localObject2).alias(generatedAlias((Selection) localObject1));
			translationForm((From) localObject1, (From) localObject2);
		}
		localIterator = paramFrom1.getFetches().iterator();
		while (localIterator.hasNext()) {
			localObject1 = (Fetch) localIterator.next();
			localObject2 = paramFrom2.fetch(((Fetch) localObject1).getAttribute().getName());
			translationFetch((Fetch) localObject1, (Fetch) localObject2);
		}
	}

	@SuppressWarnings("unchecked")
	private void translationFetch(Fetch<?, ?> paramFetch1,Fetch<?, ?> paramFetch2) {
		Iterator localIterator = paramFetch1.getFetches().iterator();
		while (localIterator.hasNext()) {
			Fetch localFetch1 = (Fetch) localIterator.next();
			Fetch localFetch2 = paramFetch2.fetch(localFetch1.getAttribute().getName());
			translationFetch(localFetch1, localFetch2);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Integer getCount(CriteriaQuery<T> criteriaQuery) {
		Assert.notNull(criteriaQuery);
		Assert.notNull(criteriaQuery.getSelection());
		Assert.notEmpty(criteriaQuery.getRoots());
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		
		CriteriaQuery localCriteriaQuery = criteriaBuilder.createQuery(Long.class);
		Iterator iterator = criteriaQuery.getRoots().iterator();
		while (iterator.hasNext()) {
			Root root1 = (Root) iterator.next();
			Root root2 = localCriteriaQuery.from(root1.getJavaType());
			root2.alias(generatedAlias(root1));
			translationForm(root1, root2);
		}
		Root localRoot = getRoot(localCriteriaQuery, criteriaQuery.getResultType());
		localCriteriaQuery.select(criteriaBuilder.count(localRoot));
		if (criteriaQuery.getGroupList() != null)
			localCriteriaQuery.groupBy(criteriaQuery.getGroupList());
		if (criteriaQuery.getGroupRestriction() != null)
			localCriteriaQuery.having(criteriaQuery.getGroupRestriction());
		if (criteriaQuery.getRestriction() != null)
			localCriteriaQuery.where(criteriaQuery.getRestriction());
		return Integer.valueOf(this.entityManager.createQuery(localCriteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult().toString());
	}
	
	@SuppressWarnings("unchecked")
	public Pager<T> findByPager(Pager<T> pager) {
		if (pager == null) {
			pager = new Pager();
		}
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		Root root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);
		return findByPager(criteriaQuery,pager);
	}
	
	@SuppressWarnings("unchecked")
	public Pager<T> findByPager(CriteriaQuery<T> criteriaQuery, Pager<T> pager) {
		Assert.notNull(criteriaQuery);
		Assert.notNull(criteriaQuery.getSelection());
		Assert.notEmpty(criteriaQuery.getRoots());
		if (pager == null)
			pager = new Pager();

		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		Root root = getRoot(criteriaQuery);
		if(pager.getFilters() != null && pager.getFilters().size()>0){
			setParameter(criteriaQuery, pager.getFilters());
		}
		String orderBy = pager.getOrderBy();
		OrderType orderType = pager.getOrderType();
		if (StringUtils.isNotEmpty(orderBy) && orderType != null) {
			if (orderType == OrderType.asc) {
				criteriaQuery.orderBy(new javax.persistence.criteria.Order[]{criteriaBuilder.asc(root.get(orderBy))});
			} else {
				criteriaQuery.orderBy(new javax.persistence.criteria.Order[]{criteriaBuilder.desc(root.get(orderBy))});
			}
		}
		Integer l = getCount(criteriaQuery);
		int i = (int) Math.ceil(l / pager.getPageSize());
		if (i < pager.getPageNumber())
			pager.setPageNumber(i);
		TypedQuery tq = this.entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		tq.setFirstResult((pager.getPageNumber() - 1) * pager.getPageSize());
		tq.setMaxResults(pager.getPageSize());
		pager.setTotalCount(l);
		pager.setList(tq.getResultList());
		return pager;
	}
}
