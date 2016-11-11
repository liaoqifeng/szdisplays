package com.koch.service;

import java.util.Date;
import java.util.List;

import com.koch.entity.Article;
import com.koch.entity.ArticleCategory;

public interface ArticleService extends BaseService<Article>{
	public List<Article> findList(ArticleCategory articleCategory, Date beginDate, Date endDate, Integer first, Integer count);
}
