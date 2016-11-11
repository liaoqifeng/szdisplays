package com.koch.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.entity.Brand;
import com.koch.entity.Product;
import com.koch.entity.ProductCategory;
import com.koch.entity.Promotion;
import com.koch.entity.Property;
import com.koch.entity.Tag;


public interface ProductService extends BaseService<Product>{
	public boolean numberExists(String number);
	public Product findByNumber(String number);
	public List<Product> findList(ProductCategory productCategory, Brand brand,
			Promotion promotion, List<Tag> tags,
			Map<Property, String> propertyValue, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isPublish, Boolean isShow,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock,
			Boolean isStockAlert, Product.OrderType orderType, Integer count,
			List<Filter> filters, List<OrderBy> orders);
	public List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count);
	public Pager findByPager(ProductCategory productCategory,Brand brand,Boolean isPublish,Boolean isGift,String name,String number,Pager pager);
}
