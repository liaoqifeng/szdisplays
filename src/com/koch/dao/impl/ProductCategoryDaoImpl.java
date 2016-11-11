package com.koch.dao.impl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.koch.dao.ProductCategoryDao;
import com.koch.entity.ProductCategory;

@Repository("productCategoryDao")
public class ProductCategoryDaoImpl extends BaseDaoImpl<ProductCategory> implements ProductCategoryDao{
	
	public List<ProductCategory> findRoots(Integer count) {
		String sql = "select productCategory from ProductCategory productCategory where productCategory.parent is null order by productCategory.orderList asc";
		TypedQuery<ProductCategory> typedQuery = this.entityManager.createQuery(sql, ProductCategory.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			typedQuery.setMaxResults(count.intValue());
		}
		return typedQuery.getResultList();
	}
	
	public List<ProductCategory> findParents(ProductCategory productCategory, Integer count) {
		if (productCategory == null || productCategory.getParent() == null) {
			return Collections.emptyList();
		}
		String sql = "select productCategory from ProductCategory productCategory where productCategory.id in (:ids) order by productCategory.level asc";
		TypedQuery<ProductCategory> typedQuery = this.entityManager
				.createQuery(sql, ProductCategory.class).setFlushMode(
						FlushModeType.COMMIT).setParameter("ids",
						productCategory.getTreePaths());
		if (count != null) {
			typedQuery.setMaxResults(count.intValue());
		}
		return typedQuery.getResultList();
	}
	  
	public List<ProductCategory> findChildren(ProductCategory productCategory, Integer count) {
		String sql = "";
		TypedQuery<ProductCategory> typedQuery;
		if (productCategory != null) {
			sql = "select productCategory from ProductCategory productCategory where productCategory.path like :treePath order by productCategory.orderList asc";
			typedQuery = this.entityManager.createQuery(sql, ProductCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter(
							"treePath", "%" + ProductCategory.PATH_SEPARATOR + productCategory.getId() + ProductCategory.PATH_SEPARATOR + "%");
		} else {
			sql = "select productCategory from ProductCategory productCategory order by productCategory.orderList asc";
			typedQuery = this.entityManager.createQuery(sql, ProductCategory.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			typedQuery.setMaxResults(count.intValue());
		}
		return tree(typedQuery.getResultList(), productCategory);
	}
	  
	private List<ProductCategory> tree(List<ProductCategory> list,
			ProductCategory paramProductCategory) {
		List<ProductCategory> arrayList = new ArrayList<ProductCategory>();
		if (list != null) {
			Iterator<ProductCategory> iterator = list.iterator();
			while (iterator.hasNext()) {
				ProductCategory productCategory = iterator.next();
				if (productCategory.getParent() == paramProductCategory) {
					arrayList.add(productCategory);
					List<ProductCategory> cs = tree(list, productCategory);
					if(cs != null && cs.size() > 0){
						productCategory.setState("closed");
					}else{
						productCategory.setState("open");
					}
					arrayList.addAll(cs);
				}
			}
		}
		return arrayList;
	}
	
	@Override
	public Integer save(ProductCategory productCategory) {
		Integer id = super.save(productCategory);
		ProductCategory parent = productCategory.getParent();
		if (parent != null && parent.getId() != null) {
			if(StringUtils.isNotEmpty(parent.getPath())){
				productCategory.setPath(parent.getPath() + id + ProductCategory.PATH_SEPARATOR);
			}
		} else {
			productCategory.setPath(ProductCategory.PATH_SEPARATOR + id.toString() + ProductCategory.PATH_SEPARATOR);
		}
		productCategory.setLevel(productCategory.getTreePaths().size()-1);
		super.update(productCategory);
		return id;
	}
	
	@Override
	public ProductCategory update(ProductCategory productCategory) {
		ProductCategory parent = productCategory.getParent();
		if (parent != null && parent.getId() != null) {
			if(StringUtils.isNotEmpty(parent.getPath())){
				productCategory.setPath(parent.getPath() + productCategory.getId() + ProductCategory.PATH_SEPARATOR);
			}
		} else {
			productCategory.setPath(ProductCategory.PATH_SEPARATOR + productCategory.getId().toString()+ ProductCategory.PATH_SEPARATOR);
		}
		productCategory.setLevel(productCategory.getTreePaths().size()-1);
		return super.update(productCategory);
	}
}
