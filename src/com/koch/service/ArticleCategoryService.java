package com.koch.service;

import java.util.List;

import com.koch.entity.ArticleCategory;

public interface ArticleCategoryService extends BaseService<ArticleCategory>{
	public List<ArticleCategory> findRoots();
	public List<ArticleCategory> findRoots(Integer count);
	public List<ArticleCategory> findTree();
	public List<ArticleCategory> findChildren(ArticleCategory articleCategory);
}
