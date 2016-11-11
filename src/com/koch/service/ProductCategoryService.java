package com.koch.service;

import java.util.List;

import com.koch.entity.ProductCategory;

public interface ProductCategoryService extends BaseService<ProductCategory>{
	public List<ProductCategory> findRoots();
	public List<ProductCategory> findRoots(Integer count);
	public List<ProductCategory> findTree();
	public List<ProductCategory> findChildren(ProductCategory productCategory);
}
