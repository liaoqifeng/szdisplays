package com.koch.dao.impl;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.koch.dao.GradeDao;
import com.koch.entity.Grade;
import com.koch.entity.Product;
import com.koch.entity.Grade.GradeType;

@Repository("gradeDao")
public class GradeDaoImpl extends BaseDaoImpl<Grade> implements GradeDao{
	
	public Grade findByAmount(BigDecimal amount) {
		if (amount == null)
			return null;
		String sql = "select grade from Grade grade where grade.expValue <= :amount order by grade.expValue desc";
		
		return this.entityManager.createQuery(sql, Grade.class).setFlushMode(FlushModeType.COMMIT).setParameter("amount", amount).setMaxResults(1).getSingleResult();
	}
	
	public boolean nameExists(String name) {
		if (name == null) {
			return false;
		}
		String str = "select count(*) from Grade grade where lower(grade.name) = lower(:name)";
		Long count = this.entityManager.createQuery(str, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("name", name).getSingleResult();
		return count.longValue() > 0L;
	}
	  
	public boolean amountExists(BigDecimal expValue) {
		if (expValue == null) {
			return false;
		}
		String sql = "select count(*) from Grade grade where grade.expValue = :expValue";
		Long count = this.entityManager.createQuery(sql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("expValue", expValue).getSingleResult();
		return count.longValue() > 0L;
	}
	
	public Grade findSystem() {
		try {
			String sql = "select grade from Grade grade where grade.gradeType = 'system'";
			return this.entityManager.createQuery(sql, Grade.class).setFlushMode(FlushModeType.COMMIT).getSingleResult();
		} catch (NoResultException e) {
		}
		return null;
	}
	
	  
	@Override
	public Integer save(Grade grade) {
		 Assert.notNull(grade);
		if (grade.getGradeType() == GradeType.system) {
			String sql = "update Grade grade set grade.gradeType = :common where grade.gradeType = :system";
			this.entityManager.createQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("common", GradeType.common).setParameter("system", GradeType.system).executeUpdate();
		}
		return super.save(grade);
	}

	@Override
	public Grade update(Grade entity) {
		Assert.notNull(entity);
		if (entity.getGradeType() == GradeType.system) {
			String sql = "update Grade grade set grade.gradeType = :common where grade.gradeType = :system and grade != :grade";
			this.entityManager.createQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("common", GradeType.common).setParameter("system", GradeType.system).setParameter("grade", entity).executeUpdate();
		}
		return super.update(entity);
	}

	@Override
	public void delete(Grade entity) {
		if (entity != null && entity.getGradeType() != GradeType.system) {
			String sql = "select product from Product product join product.memberPrice memberPrice where index(memberPrice) = :grade";
			List<Product> products = this.entityManager.createQuery(sql, Product.class).setFlushMode(FlushModeType.COMMIT).setParameter("grade", entity).getResultList();
			for (int i = 0; i < products.size(); i++) {
				Product product = products.get(i);
				product.getMemberPrice().remove(entity);
				if (i % 20 == 0) {
					super.flush();
					super.clear();
				}
			}
			super.delete(super.update(entity));
		}
	}
}
