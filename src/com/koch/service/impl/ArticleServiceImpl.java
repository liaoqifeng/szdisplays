package com.koch.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koch.dao.ArticleDao;
import com.koch.entity.Article;
import com.koch.entity.ArticleCategory;
import com.koch.service.ArticleService;

@Service
public class ArticleServiceImpl extends BaseServiceImpl<Article> implements ArticleService{
	@Resource
	private ArticleDao articleDao;
	@Resource
	public void setBaseDao(ArticleDao eDao) {
		super.setBaseDao(eDao);
	}
	
	@Transactional(readOnly=true)
	public List<Article> findList(ArticleCategory articleCategory, Date beginDate, Date endDate, Integer first, Integer count){
		return articleDao.findList(articleCategory, beginDate, endDate, first, count);
	}
}
