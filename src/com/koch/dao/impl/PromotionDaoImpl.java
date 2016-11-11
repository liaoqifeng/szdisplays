package com.koch.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.dao.PromotionDao;
import com.koch.entity.Promotion;

@Repository("promotionDao")
public class PromotionDaoImpl extends BaseDaoImpl<Promotion> implements PromotionDao{
	
	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded, Integer count, List<Filter> filters, List<OrderBy> orders) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Promotion> criteriaQuery = criteriaBuilder.createQuery(Promotion.class);
		Root root = criteriaQuery.from(Promotion.class);
		criteriaQuery.select(root);
		Predicate predicate = criteriaBuilder.conjunction();
		if (hasBegun != null) {
			if (hasBegun.booleanValue()) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(
						root.get("startDate").isNull(), criteriaBuilder
								.lessThanOrEqualTo(root.get("startDate"),
										new Date())));
			} else {
				predicate = criteriaBuilder.and(new Predicate[] {
						predicate,
						root.get("startDate").isNotNull(),
						criteriaBuilder.greaterThan(root.get("startDate"),
								new Date()) });
			}
		}
		if (hasEnded != null) {
			if (hasEnded.booleanValue()) {
				predicate = criteriaBuilder.and(new Predicate[] {
						predicate,
						root.get("endDate").isNotNull(),
						criteriaBuilder.lessThan(root.get("endDate"),
								new Date()) });
			} else {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(
						root.get("endDate").isNull(), criteriaBuilder
								.greaterThanOrEqualTo(root.get("endDate"),
										new Date())));
			}
		}
		criteriaQuery.where(predicate);
		return super.findList(criteriaQuery, null, count, filters, orders);
	}
}
