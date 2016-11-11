package com.koch.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.koch.bean.OrderListComparator;
import com.koch.dao.ArticleCategoryDao;
import com.koch.entity.ArticleCategory;
import com.koch.service.ArticleCategoryService;

@Service
public class ArticleCategoryServiceImpl extends BaseServiceImpl<ArticleCategory> implements ArticleCategoryService{
	@Resource
	private ArticleCategoryDao articleCategoryDao;
	@Resource
	public void setBaseDao(ArticleCategoryDao articleCategoryDao) {
		super.setBaseDao(articleCategoryDao);
	}
	
	@Cacheable(cacheName="articleCategory")
	public List<ArticleCategory> getAll() {
		return super.getAll();
	}
	
	@Transactional(readOnly = true)
	public List<ArticleCategory> findChildren(ArticleCategory articleCategory) {
		return articleCategoryDao.findChildren(articleCategory, null);
	}

	@Transactional(readOnly = true)
	public List<ArticleCategory> findRoots() {
		return articleCategoryDao.findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<ArticleCategory> findRoots(Integer count) {
		return articleCategoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	public List<ArticleCategory> findTree() {
		return articleCategoryDao.findChildren(null, null);
	}

	@Transactional
	@TriggersRemove(cacheName="articleCategory",removeAll=true)
	public void delete(Integer id) {
		super.delete(id);
	}
	@Transactional
	@TriggersRemove(cacheName="articleCategory",removeAll=true)
	public Integer save(ArticleCategory t) {
		return super.save(t);
	}
	@Transactional
	@TriggersRemove(cacheName="articleCategory",removeAll=true)
	public ArticleCategory update(ArticleCategory t) {
		return super.update(t);
	}
	
	
	
}
