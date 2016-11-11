package com.koch.dao.impl;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.koch.bean.Pager;
import com.koch.dao.CouponDao;
import com.koch.entity.Coupon;

@Repository("couponDao")
public class CouponDaoImpl extends BaseDaoImpl<Coupon> implements CouponDao {
	
	@SuppressWarnings("unchecked")
	public Pager findPage(String name,Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pager pager) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Coupon> criteriaQuery = criteriaBuilder.createQuery(Coupon.class);
		Root root = criteriaQuery.from(Coupon.class);
		criteriaQuery.select(root);
		Predicate predicate = criteriaBuilder.conjunction();
		if(StringUtils.isNotEmpty(name)){
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%"+name+"%"));
		}
		if (isEnabled != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isEnabled"), isEnabled));
		if (isExchange != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isExchange"), isExchange));
		if (hasExpired != null)
			if (hasExpired)
				predicate = criteriaBuilder.and(new Predicate[] { predicate, root.get("endDate").isNotNull(), criteriaBuilder.lessThan(root.get("endDate"), new Date()) });
			else
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(root.get("endDate").isNull(), criteriaBuilder .greaterThanOrEqualTo(root.get("endDate"), new Date())));
		criteriaQuery.where(predicate);
		return super.findByPager(criteriaQuery, pager);
	}
}
