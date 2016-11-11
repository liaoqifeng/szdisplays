package com.koch.service.impl;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.koch.dao.GoodsDao;
import com.koch.dao.ProductDao;
import com.koch.service.GoodsService;
import com.koch.entity.Goods;
import com.koch.entity.Product;

@Service
public class GoodsServiceImpl extends BaseServiceImpl<Goods> implements GoodsService{
	@Resource
	private GoodsDao goodsDao;
	@Resource
	private ProductDao productDao;
	
	@Resource
	public void setBaseDao(GoodsDao goodsDao) {
		super.setBaseDao(goodsDao);
	}
	
	@Transactional
	public Integer save(Goods goods) {
		Assert.notNull(goods);
		super.save(goods);
		this.goodsDao.flush();
		if (goods.getProducts() != null) {
			Iterator<Product> itor = goods.getProducts().iterator();
			while (itor.hasNext()) {
				Product product = (Product) itor.next();
				this.productDao.build(product);
			}
		}
		return goods.getId();
	}

	@Transactional
	public Goods update(Goods goods) {
		Assert.notNull(goods);
		Set<Product> prodcutSet = new HashSet<Product>();
		CollectionUtils.select(goods.getProducts(), new GoodsPredicate(this), prodcutSet);
		List<Product> products = this.productDao.findList(goods, prodcutSet);
		Iterator<Product> iterator = products.iterator();
		while (iterator.hasNext()) {
			Product product = iterator.next();
			this.productDao.delete(product);
		}
		Goods g = super.update(goods);
		this.goodsDao.flush();
		if (g.getProducts() != null) {
			Iterator<Product> itor = g.getProducts().iterator();
			while (itor.hasNext()) {
				Product product = itor.next();
				this.productDao.build(product);
			}
		}
		return goods;
	}

	
	
	class GoodsPredicate implements Predicate{
		GoodsPredicate(GoodsServiceImpl goodsServiceImpl) {}
	  
	  @Override
		public boolean evaluate(Object arg0) {
			Product product = (Product)arg0;
		    return product != null && product.getId() != null;
		}
	}
}
