package com.koch.dao;

import com.koch.entity.Cart;

public interface CartDao extends BaseDao<Cart>{
	public void evictExpired();
}
