package com.koch.dao.impl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.koch.dao.ArticleCategoryDao;
import com.koch.entity.ArticleCategory;

@Repository("articleCategoryDao")
public class ArticleCategoryDaoImpl extends BaseDaoImpl<ArticleCategory> implements ArticleCategoryDao{
	
	public List<ArticleCategory> findRoots(Integer count) {
		String sql = "select articleCategory from ArticleCategory articleCategory where articleCategory.parent is null order by articleCategory.orderList asc";
		TypedQuery<ArticleCategory> typedQuery = this.entityManager.createQuery(sql, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			typedQuery.setMaxResults(count.intValue());
		}
		return typedQuery.getResultList();
	}
	
	public List<ArticleCategory> findParents(ArticleCategory articleCategory, Integer count) {
		if (articleCategory == null || articleCategory.getParent() == null) {
			return Collections.emptyList();
		}
		String sql = "select articleCategory from ArticleCategory articleCategory where articleCategory.id in (:ids) order by articleCategory.level asc";
		TypedQuery<ArticleCategory> typedQuery = this.entityManager.createQuery(sql, ArticleCategory.class).setFlushMode(
						FlushModeType.COMMIT).setParameter("ids",articleCategory.getTreePaths());
		if (count != null) {
			typedQuery.setMaxResults(count.intValue());
		}
		return typedQuery.getResultList();
	}
	  
	public List<ArticleCategory> findChildren(ArticleCategory articleCategory, Integer count) {
		String sql = "";
		TypedQuery<ArticleCategory> typedQuery;
		if (articleCategory != null) {
			sql = "select articleCategory from ArticleCategory articleCategory where articleCategory.path like :treePath order by articleCategory.orderList asc";
			typedQuery = this.entityManager.createQuery(sql, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter(
							"treePath", "%" + articleCategory.PATH_SEPARATOR + articleCategory.getId() + articleCategory.PATH_SEPARATOR + "%");
		} else {
			sql = "select articleCategory from ArticleCategory articleCategory order by articleCategory.orderList asc";
			typedQuery = this.entityManager.createQuery(sql, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			typedQuery.setMaxResults(count.intValue());
		}
		return tree(typedQuery.getResultList(), articleCategory);
	}
	  
	private List<ArticleCategory> tree(List<ArticleCategory> list, ArticleCategory paramarticleCategory) {
		List<ArticleCategory> arrayList = new ArrayList<ArticleCategory>();
		if (list != null) {
			Iterator<ArticleCategory> iterator = list.iterator();
			while (iterator.hasNext()) {
				ArticleCategory articleCategory = iterator.next();
				if (articleCategory.getParent() == paramarticleCategory) {
					arrayList.add(articleCategory);
					List<ArticleCategory> cs = tree(list, articleCategory);
					if(cs != null && cs.size() > 0){
						articleCategory.setState("closed");
					}else{
						articleCategory.setState("open");
					}
					arrayList.addAll(cs);
				}
			}
		}
		return arrayList;
	}
	
	@Override
	public Integer save(ArticleCategory articleCategory) {
		Integer id = super.save(articleCategory);
		ArticleCategory parent = articleCategory.getParent();
		if (parent != null && parent.getId() != null) {
			if(StringUtils.isNotEmpty(parent.getPath())){
				articleCategory.setPath(parent.getPath() + id + articleCategory.PATH_SEPARATOR);
			}
		} else {
			articleCategory.setPath(articleCategory.PATH_SEPARATOR + id.toString() + articleCategory.PATH_SEPARATOR);
		}
		articleCategory.setLevel(articleCategory.getTreePaths().size()-1);
		super.update(articleCategory);
		return id;
	}
	
	@Override
	public ArticleCategory update(ArticleCategory articleCategory) {
		ArticleCategory parent = articleCategory.getParent();
		if (parent != null && parent.getId() != null) {
			if(StringUtils.isNotEmpty(parent.getPath())){
				articleCategory.setPath(parent.getPath() + articleCategory.getId() + articleCategory.PATH_SEPARATOR);
			}
		} else {
			articleCategory.setPath(articleCategory.PATH_SEPARATOR + articleCategory.getId().toString()+ articleCategory.PATH_SEPARATOR);
		}
		articleCategory.setLevel(articleCategory.getTreePaths().size()-1);
		return super.update(articleCategory);
	}
	
}
