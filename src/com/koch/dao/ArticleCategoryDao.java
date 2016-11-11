package com.koch.dao;

import java.util.List;

import com.koch.entity.ArticleCategory;

public interface ArticleCategoryDao extends BaseDao<ArticleCategory>{
	public List<ArticleCategory> findRoots(Integer count);
	public List<ArticleCategory> findParents(ArticleCategory articleCategory, Integer count);
	public List<ArticleCategory> findChildren(ArticleCategory articleCategory, Integer count);
}
