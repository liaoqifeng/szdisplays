package com.koch.dao.impl;

import java.util.Date;

import javax.persistence.FlushModeType;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import com.koch.dao.CartDao;
import com.koch.entity.Cart;

@Repository("cartDao")
public class CartDaoImpl extends BaseDaoImpl<Cart> implements CartDao{
	public void evictExpired() {
		String str = "delete from Cart cart where cart.modifyDate <= :expire";
		this.entityManager.createQuery(str).setFlushMode(FlushModeType.COMMIT).setParameter("expire", DateUtils.addSeconds(new Date(), -604800)).executeUpdate();
	}
}
