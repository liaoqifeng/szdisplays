package com.koch.service.impl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.koch.bean.Filter;
import com.koch.bean.Pager;
import com.koch.dao.ArticleDao;
import com.koch.dao.ProductDao;
import com.koch.entity.Article;
import com.koch.entity.Product;
import com.koch.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@PersistenceContext
	protected EntityManager entityManager;
	@Resource
	private ArticleDao articleDao;
	@Resource
	private ProductDao productDao;

	public void index() {
		index(Article.class);
		index(Product.class);
	}

	public void index(Class<?> type) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(this.entityManager);
		int i;
		List list;
		Iterator iterator;
		if (type == Article.class)
			for (i = 0; i < this.articleDao.count(new Filter[0]); i += 20) {
				list = this.articleDao.findList(Integer.valueOf(i), Integer.valueOf(20), null, null);
				iterator = list.iterator();
				while (iterator.hasNext()) {
					Article article = (Article) iterator.next();
					fullTextEntityManager.index(article);
				}
				fullTextEntityManager.flushToIndexes();
				fullTextEntityManager.clear();
				this.articleDao.clear();
			}
		else if (type == Product.class)
			for (i = 0; i < this.productDao.count(new Filter[0]); i += 20) {
				list = this.productDao.findList(Integer.valueOf(i), Integer.valueOf(20), null, null);
				iterator = list.iterator();
				while (iterator.hasNext()) {
					Product product = (Product) iterator.next();
					fullTextEntityManager.index(product);
				}
				fullTextEntityManager.flushToIndexes();
				fullTextEntityManager.clear();
				this.productDao.clear();
			}
	}

	public void index(Article article) {
		if (article != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(this.entityManager);
			fullTextEntityManager.index(article);
		}
	}

	public void index(Product product) {
		if (product != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(this.entityManager);
			fullTextEntityManager.index(product);
		}
	}

	public void purge() {
		purge(Article.class);
		purge(Product.class);
	}

	public void purge(Class<?> type) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(this.entityManager);
		if (type == Article.class)
			fullTextEntityManager.purgeAll(Article.class);
		else if (type == Product.class)
			fullTextEntityManager.purgeAll(Product.class);
	}

	public void purge(Article article) {
		if (article != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(this.entityManager);
			fullTextEntityManager.purge(Article.class, article.getId());
		}
	}

	public void purge(Product product) {
		if (product != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(this.entityManager);
			fullTextEntityManager.purge(Product.class, product.getId());
		}
	}

	@Transactional(readOnly = true)
	public Pager search(String keyword, Pager pager) {
		if (StringUtils.isEmpty(keyword))
			return new Pager();
		if (pager == null)
			pager = new Pager();
		try {
			String str = QueryParser.escape(keyword);
			QueryParser queryParser = new QueryParser(Version.LUCENE_35,"title", new IKAnalyzer());
			queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query query = queryParser.parse(str);
			FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("title", str),0.5F);
			TermQuery contentTermQuery = new TermQuery(new Term("content", str));
			TermQuery publishTermQuery = new TermQuery(new Term("isPublish", "true"));
			BooleanQuery booleanQuery1 = new BooleanQuery();
			BooleanQuery booleanQuery2 = new BooleanQuery();
			booleanQuery1.add(query, BooleanClause.Occur.SHOULD);
			booleanQuery1.add(fuzzyQuery, BooleanClause.Occur.SHOULD);
			booleanQuery1.add(contentTermQuery, BooleanClause.Occur.SHOULD);
			booleanQuery2.add(publishTermQuery, BooleanClause.Occur.MUST);
			booleanQuery2.add(booleanQuery1, BooleanClause.Occur.MUST);
			FullTextEntityManager localFullTextEntityManager = Search.getFullTextEntityManager(this.entityManager);
			FullTextQuery fullTextQuery = localFullTextEntityManager.createFullTextQuery(booleanQuery2,new Class[] { Article.class });
			fullTextQuery.setSort(new Sort(new SortField[] { new SortField("isTop", 3, true), new SortField(null, 0), new SortField("createDate", 6, true) }));
			fullTextQuery.setFirstResult((pager.getPageNumber() - 1) * pager.getPageSize());
			fullTextQuery.setMaxResults(pager.getPageSize());
			pager.setTotalCount(fullTextQuery.getResultSize());
			pager.setList(fullTextQuery.getResultList());
			return pager;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Pager();
	}

	@Transactional(readOnly = true)
	public Pager search(String keyword, BigDecimal startPrice, BigDecimal endPrice, Product.OrderType orderType, Pager pager) {
		if (StringUtils.isEmpty(keyword))
			return new Pager();
		if (pager == null)
			pager = new Pager();
		try {
			String str = QueryParser.escape(keyword);
			TermQuery localTermQuery1 = new TermQuery(new Term("sn", str));
			Query localQuery1 = new QueryParser(Version.LUCENE_35, "keyword", new IKAnalyzer()).parse(str);
			QueryParser queryParser = new QueryParser(Version.LUCENE_35, "name", new IKAnalyzer());
			queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query localQuery2 = queryParser.parse(str);
			FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("name", str), 0.5F);
			TermQuery localTermQuery2 = new TermQuery(new Term("introduction", str));
			TermQuery localTermQuery3 = new TermQuery(new Term("isPublish", "true"));
			TermQuery localTermQuery4 = new TermQuery( new Term("isShow", "true"));
			TermQuery localTermQuery5 = new TermQuery(new Term("isGift", "false"));
			BooleanQuery localBooleanQuery1 = new BooleanQuery();
			BooleanQuery localBooleanQuery2 = new BooleanQuery();
			localBooleanQuery1.add(localTermQuery1, BooleanClause.Occur.SHOULD);
			localBooleanQuery1.add(localQuery1, BooleanClause.Occur.SHOULD);
			localBooleanQuery1.add(localQuery2, BooleanClause.Occur.SHOULD);
			localBooleanQuery1.add(fuzzyQuery, BooleanClause.Occur.SHOULD);
			localBooleanQuery1.add(localTermQuery2, BooleanClause.Occur.SHOULD);
			localBooleanQuery2.add(localTermQuery3, BooleanClause.Occur.MUST);
			localBooleanQuery2.add(localTermQuery4, BooleanClause.Occur.MUST);
			localBooleanQuery2.add(localTermQuery5, BooleanClause.Occur.MUST);
			localBooleanQuery2.add(localBooleanQuery1, BooleanClause.Occur.MUST);
			
			BigDecimal price = null;
			if ((startPrice != null) && (endPrice != null) && (startPrice.compareTo(endPrice) > 0)) {
				price = startPrice;
				startPrice = endPrice;
				endPrice = price;
			}
			NumericRangeQuery<Double> numericRangeQuery = null;
			if ((startPrice != null) && (startPrice.compareTo(new BigDecimal(0)) >= 0) && (endPrice != null) && (endPrice.compareTo(new BigDecimal(0)) >= 0)) {
				numericRangeQuery = NumericRangeQuery.newDoubleRange("price", Double.valueOf(startPrice.doubleValue()), Double.valueOf(endPrice.doubleValue()), true, true);
				localBooleanQuery2.add( numericRangeQuery, BooleanClause.Occur.MUST);
			} else if ((startPrice != null) && (startPrice.compareTo(new BigDecimal(0)) >= 0)) {
				numericRangeQuery = NumericRangeQuery.newDoubleRange("price", Double.valueOf(startPrice.doubleValue()), null, true, false);
				localBooleanQuery2.add(numericRangeQuery, BooleanClause.Occur.MUST);
			} else if ((endPrice != null) && (endPrice.compareTo(new BigDecimal(0)) >= 0)) {
				numericRangeQuery = NumericRangeQuery.newDoubleRange("price", null, Double.valueOf(endPrice.doubleValue()), false, true);
				localBooleanQuery2.add(numericRangeQuery, BooleanClause.Occur.MUST);
			}
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(this.entityManager);
			FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(localBooleanQuery2, new Class[] { Product.class });
			SortField[] arrayOfSortField = null;
			if (orderType == Product.OrderType.priceAsc)
				arrayOfSortField = new SortField[] { new SortField("price", 7, false), new SortField("createDate", 6, true) };
			else if (orderType == Product.OrderType.priceDesc)
				arrayOfSortField = new SortField[] { new SortField("price", 7, true), new SortField("createDate", 6, true) };
			else if (orderType == Product.OrderType.salesDesc)
				arrayOfSortField = new SortField[] { new SortField("sales", 4, true), new SortField("createDate", 6, true) };
			else if (orderType == Product.OrderType.scoreDesc)
				arrayOfSortField = new SortField[] { new SortField("score", 4, true), new SortField("createDate", 6, true) };
			else if (orderType == Product.OrderType.dateDesc)
				arrayOfSortField = new SortField[] { new SortField( "createDate", 6, true) };
			else
				arrayOfSortField = new SortField[] { new SortField("isTop", 3, true), new SortField(null, 0), new SortField("modifyDate", 6, true) };
			fullTextQuery.setSort(new Sort(arrayOfSortField));
			fullTextQuery.setFirstResult((pager.getPageNumber() - 1) * pager.getPageSize());
			fullTextQuery.setMaxResults(pager.getPageSize());
			pager.setTotalCount(fullTextQuery.getResultSize());
			pager.setList(fullTextQuery.getResultList());
			return pager;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Pager();
	}
}
