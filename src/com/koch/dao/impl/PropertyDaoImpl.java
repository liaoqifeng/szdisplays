package com.koch.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.koch.dao.PropertyDao;
import com.koch.entity.Property;

@Repository("propertyDao")
public class PropertyDaoImpl extends BaseDaoImpl<Property> implements PropertyDao {
	public void delete(Property entity) {
		if (entity != null){
	      String str1 = "property" + entity.getPropertyIndex();
	      String str2 = "update Product product set product." + str1 + " = null where product.productCategory = :productCategory";
	      this.entityManager.createQuery(str2).setFlushMode(FlushModeType.COMMIT).setParameter("productCategory", entity.getProductCategory()).executeUpdate();
	      super.delete(entity);
	    }
	}

	public Integer save(Property entity) {
		Assert.notNull(entity);
		String str = "select model.propertyIndex from Property model where model.productCategory = :productCategory";
		List<Integer> list = this.entityManager.createQuery(str, Integer.class).setFlushMode(FlushModeType.COMMIT).setParameter("productCategory",entity.getProductCategory()).getResultList();
		for (int i = 0; i < 20; i++){
			if (!list.contains(Integer.valueOf(i))) {
				entity.setPropertyIndex(Integer.valueOf(i));
				super.save(entity);
				break;
			}
		}
		return Integer.valueOf(super.getIdentifier(entity).toString());
	}
}
