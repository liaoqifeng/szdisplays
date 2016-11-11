package com.koch.dao.impl;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.koch.bean.Pager;
import com.koch.dao.MemberDao;
import com.koch.entity.Grade;
import com.koch.entity.Member;
import com.koch.entity.Member.MemberStatus;

@Repository("memberDao")
public class MemberDaoImpl extends BaseDaoImpl<Member> implements MemberDao{
	
	public boolean usernameExists(String username) {
		if (username == null)
			return false;
		String sql = "select count(*) from Member members where lower(members.username) = lower(:username)";
		Long count = (Long) this.entityManager.createQuery(sql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("username",username).getSingleResult();
		return count.longValue() > 0L;
	}
	
	public BigDecimal getDeposit(Integer id) {
		String sql = "select members.deposit from Member members where members.id = :id";
		BigDecimal deposit = (BigDecimal)this.entityManager.createQuery(sql, BigDecimal.class).setFlushMode(FlushModeType.COMMIT).setParameter("id",id).getSingleResult();
		return deposit
		;
	}

	public boolean emailExists(String email) {
		if (email == null)
			return false;
		String sql = "select count(*) from Member members where lower(members.email) = lower(:email)";
		Long count = (Long) this.entityManager.createQuery(sql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getSingleResult();
		return count.longValue() > 0L;
	}

	public Member findByUsername(String username) {
		if (username == null)
			return null;
		try {
			String str = "select members from Member members where lower(members.username) = lower(:username)";
			return (Member) this.entityManager.createQuery(str, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
		} catch (NoResultException e) { }
		return null;
	}

	public List<Member> findListByEmail(String email) {
		if (email == null)
			return Collections.emptyList();
		String sql = "select members from Member members where lower(members.email) = lower(:email)";
		return this.entityManager.createQuery(sql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public Pager<Member> findByPager(Grade grade,String name,MemberStatus status,Pager<Member> pager){
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Member.class);
		Root root = criteriaQuery.from(Member.class);
		criteriaQuery.select(root);
		Predicate predicate = criteriaBuilder.conjunction();
		if(grade != null && grade.getId() != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("grade"), grade));
		if(StringUtils.isNotEmpty(name))
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("username"), "%"+name+"%"));
		if(status != null)
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), status));
		criteriaQuery.where(predicate);
		return super.findByPager(criteriaQuery, pager);
	}
	
}
