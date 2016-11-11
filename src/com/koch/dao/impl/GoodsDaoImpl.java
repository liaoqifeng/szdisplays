package com.koch.dao.impl;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.koch.dao.GoodsDao;
import com.koch.dao.ProductDao;
import com.koch.entity.Goods;
import com.koch.entity.Product;

@Repository("goodsDao")
public class GoodsDaoImpl extends BaseDaoImpl<Goods> implements GoodsDao{
	@Resource
	private ProductDao productDao;
	
	public Integer save(Goods entity) {
		Assert.notNull(entity);
		if (entity.getProducts() != null) {
			Iterator<Product> iterator = entity.getProducts().iterator();
			while (iterator.hasNext()) {
				Product product = iterator.next();
				this.productDao.build(product);
			}
		}
		return super.save(entity);
	}

	public Goods update(Goods entity) {
		Assert.notNull(entity);
		if (entity.getProducts() != null) {
			Iterator<Product> iterator = entity.getProducts().iterator();
			while (iterator.hasNext()) {
				Product product = iterator.next();
				if (product.getId() != null) {
					String sql = "";
					if (!product.getIsGift()) {//如果此商品不是赠品,则要删除赠品表中的商品
						sql = "delete from GiftItem giftItem where giftItem.product = :product";
						this.entityManager.createQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).executeUpdate();
					}
					if (!product.getIsPublish() || product.getIsGift()) {//如果此商品未上架或者是赠品,则要删除购物车内的商品
						sql = "delete from CartItem cartItem where cartItem.product = :product";
						this.entityManager.createQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).executeUpdate();
					}
				}
				this.productDao.build(product);
			}
		}
		return super.update(entity);
	}
	
}
