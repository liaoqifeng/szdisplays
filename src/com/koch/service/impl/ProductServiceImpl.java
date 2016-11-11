package com.koch.service.impl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.dao.ProductDao;
import com.koch.service.ProductService;
import com.koch.util.SerialNumberUtil;
import com.koch.entity.Brand;
import com.koch.entity.Product;
import com.koch.entity.ProductCategory;
import com.koch.entity.Promotion;
import com.koch.entity.Property;
import com.koch.entity.Tag;

@Service
public class ProductServiceImpl extends BaseServiceImpl<Product> implements ProductService{
	@Resource
	private ProductDao productDao;
	@Resource
	public void setBaseDao(ProductDao productDao) {
		super.setBaseDao(productDao);
	}
	
	public boolean numberExists(String number){
		return productDao.numberExists(number);
	}
	
	public Product findByNumber(String number){
		return productDao.findByNumber(number);
	}
	
	public List<Product> findList(ProductCategory productCategory, Brand brand,
			Promotion promotion, List<Tag> tags,
			Map<Property, String> propertyValue, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isPublish, Boolean isShow,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock,
			Boolean isStockAlert, Product.OrderType orderType, Integer count,
			List<Filter> filters, List<OrderBy> orders){
		return productDao.findList(productCategory, brand, promotion, tags, propertyValue, startPrice, endPrice, isPublish, isShow, isTop, isGift, isOutOfStock, isStockAlert, orderType, count, filters, orders);
	}
	
	public List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count){
		return productDao.findList(productCategory, beginDate, endDate, first, count);
	}
	
	public Pager findByPager(ProductCategory productCategory,Brand brand,Boolean isPublish,Boolean isGift,String name,String number,Pager pager){
		return productDao.findByPager(productCategory, brand, isPublish, isGift, name, number, pager);
	}

	@Transactional
	public Integer save(Product t) {
		Assert.notNull(t);
	    Product product = super.update(t);
	    this.productDao.flush();
	    this.productDao.build(product);
	    return product.getId();
	}

	@Transactional
	public void delete(Integer[] ids) {
		if(ids != null){
			for(Integer id : ids){
				productDao.delete(productDao.get(id));
			}
		}
	}
}
