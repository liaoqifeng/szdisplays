package com.koch.service;

import java.math.BigDecimal;

import com.koch.bean.Pager;
import com.koch.entity.Article;
import com.koch.entity.Product;



public interface SearchService{
	 public void index();

	  public void index(Class<?> paramClass);

	  public void index(Article article);

	  public void index(Product paramProduct);

	  public void purge();

	  public void purge(Class<?> paramClass);

	  public void purge(Article article);

	  public void purge(Product product);

	  public Pager search(String keyword, Pager pager);

	  public Pager search(String keyword, BigDecimal startPrice, BigDecimal endPrice, Product.OrderType orderType, Pager pager);
}
