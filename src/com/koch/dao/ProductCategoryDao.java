package com.koch.dao;

import java.util.List;

import com.koch.entity.ProductCategory;

public interface ProductCategoryDao extends BaseDao<ProductCategory>{
	
	public List<ProductCategory> findRoots(Integer count);
	public List<ProductCategory> findParents(ProductCategory productCategory, Integer count);
	public List<ProductCategory> findChildren(ProductCategory productCategory, Integer count);
}
