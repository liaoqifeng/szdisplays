package com.koch.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.koch.bean.Pager;
import com.koch.dao.CouponInfoDao;
import com.koch.entity.Coupon;
import com.koch.entity.CouponInfo;
import com.koch.entity.Member;

@Repository("couponInfoDao")
public class CouponInfoDaoImpl extends BaseDaoImpl<CouponInfo> implements CouponInfoDao {
	public boolean codeExists(String code) {
		if (code == null)
			return false;
		String sql = "select count(*) from CouponInfo couponInfo where lower(couponInfo.code) = lower(:code)";
		Long count = (Long) this.entityManager.createQuery(sql, Long.class)
				.setFlushMode(FlushModeType.COMMIT).setParameter("code", code)
				.getSingleResult();
		return count.longValue() > 0L;
	}

	public CouponInfo findByCode(String code) {
		if (code == null)
			return null;
		try {
			String sql = "select couponInfo from CouponInfo couponInfo where lower(couponInfo.code) = lower(:code)";
			return (CouponInfo) this.entityManager.createQuery(sql,
					CouponInfo.class).setFlushMode(FlushModeType.COMMIT)
					.setParameter("code", code).getSingleResult();
		} catch (NoResultException e) {
		}
		return null;
	}

	public CouponInfo build(Coupon coupon, Member member) {
		Assert.notNull(coupon);
		CouponInfo couponInfo = new CouponInfo();
		String code = UUID.randomUUID().toString().toUpperCase();
		couponInfo.setCode(coupon.getPrefix() + code.substring(0, 8)
				+ code.substring(9, 13) + code.substring(14, 18)
				+ code.substring(19, 23) + code.substring(24));
		couponInfo.setIsUsed(Boolean.valueOf(false));
		couponInfo.setCoupon(coupon);
		couponInfo.setMember(member);
		super.save(couponInfo);
		return couponInfo;
	}

	public List<CouponInfo> build(Coupon coupon, Member member, Integer count) {
		Assert.notNull(coupon);
		Assert.notNull(count);
		List<CouponInfo> list = new ArrayList<CouponInfo>();
		for (int i = 0; i < count.intValue(); i++) {
			CouponInfo couponInfo = build(coupon, member);
			list.add(couponInfo);
			if (i % 20 != 0)
				continue;
			super.flush();
			super.clear();
		}
		return list;
	}

	public Pager findPage(Member member, Pager pager) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponInfo> criteriaQuery = criteriaBuilder.createQuery(CouponInfo.class);
		Root<CouponInfo> root = criteriaQuery.from(CouponInfo.class);
		criteriaQuery.select(root);
		if (member != null)
			criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		return super.findByPager(criteriaQuery, pager);
	}

	@SuppressWarnings("unchecked")
	public Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponInfo> criteriaQuery = criteriaBuilder.createQuery(CouponInfo.class);
		Root<CouponInfo> root = criteriaQuery.from(CouponInfo.class);
		criteriaQuery.select(root);
		Predicate predicate = criteriaBuilder.conjunction();
		Path path = root.get("coupon");
		if (coupon != null && coupon.getId() != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
					path, coupon));
		if (member != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
					root.get("member"), member));
		if (hasBegun != null)
			if (hasBegun.booleanValue())
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(path.get("startDate").isNull(),criteriaBuilder.lessThanOrEqualTo(path.get("startDate"), new Date())));
			else
				predicate = criteriaBuilder.and(new Predicate[] { predicate, path.get("startDate").isNotNull(), criteriaBuilder.greaterThan(path.get("startDate"),new Date()) });
		if (hasExpired != null)
			if (hasExpired.booleanValue())
				predicate = criteriaBuilder.and(new Predicate[] { predicate, path.get("endDate").isNotNull(), criteriaBuilder.lessThan(path.get("endDate"), new Date()) });
			else
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(path.get("endDate").isNull(), criteriaBuilder.greaterThanOrEqualTo(path.get("endDate"), new Date())));
		if (isUsed != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isUsed"), isUsed));
		criteriaQuery.where(predicate);
		return super.count(criteriaQuery, null);
	}
	
	public Pager findByPage(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed,Pager pager) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<CouponInfo> criteriaQuery = criteriaBuilder.createQuery(CouponInfo.class);
		Root<CouponInfo> root = criteriaQuery.from(CouponInfo.class);
		criteriaQuery.select(root);
		Predicate predicate = criteriaBuilder.conjunction();
		Path path = root.get("coupon");
		if (coupon != null && coupon.getId() != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
					path, coupon));
		if (member != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
					root.get("member"), member));
		if (hasBegun != null)
			if (hasBegun.booleanValue())
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(path.get("startDate").isNull(),criteriaBuilder.lessThanOrEqualTo(path.get("startDate"), new Date())));
			else
				predicate = criteriaBuilder.and(new Predicate[] { predicate, path.get("startDate").isNotNull(), criteriaBuilder.greaterThan(path.get("startDate"),new Date()) });
		if (hasExpired != null)
			if (hasExpired.booleanValue())
				predicate = criteriaBuilder.and(new Predicate[] { predicate, path.get("endDate").isNotNull(), criteriaBuilder.lessThan(path.get("endDate"), new Date()) });
			else
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(path.get("endDate").isNull(), criteriaBuilder.greaterThanOrEqualTo(path.get("endDate"), new Date())));
		if (isUsed != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isUsed"), isUsed));
		criteriaQuery.where(predicate);
		return super.findByPager(criteriaQuery, pager);
	}
}
