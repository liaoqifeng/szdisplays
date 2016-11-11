package com.koch.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.koch.dao.ProductCategoryDao;
import com.koch.entity.ProductCategory;
import com.koch.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategory> implements ProductCategoryService{
	@Resource
	private ProductCategoryDao productCategoryDao;
	@Resource
	public void setBaseDao(ProductCategoryDao productCategoryDao) {
		super.setBaseDao(productCategoryDao);
	}
	
	@Override
	@Cacheable(cacheName="productCategory")
	public List<ProductCategory> getAll() {
		return super.getAll();
	}
	
	@Transactional(readOnly = true)
	public List<ProductCategory> findRoots(Integer count){
		return this.productCategoryDao.findRoots(count);
	}
	
	@Transactional(readOnly = true)
	public List<ProductCategory> findRoots(){
		return this.productCategoryDao.findRoots(null);
	}
	
	@Transactional(readOnly = true)
	public List<ProductCategory> findTree() {
		return this.productCategoryDao.findChildren(null, null);
	}

	@Transactional(readOnly = true)
	public List<ProductCategory> findChildren(ProductCategory productCategory) {
		return this.productCategoryDao.findChildren(productCategory, null);
	}

	@Transactional
	@TriggersRemove(cacheName="productCategory",removeAll=true)
	public void delete(Integer id) {
		super.delete(id);
	}
	@Transactional
	@TriggersRemove(cacheName="productCategory",removeAll=true)
	public Integer save(ProductCategory t) {
		return super.save(t);
	}
	@Transactional
	@TriggersRemove(cacheName="productCategory",removeAll=true)
	public ProductCategory update(ProductCategory t) {
		return super.update(t);
	}
	
}
