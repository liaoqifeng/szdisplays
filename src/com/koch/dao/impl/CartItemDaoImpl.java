package com.koch.dao.impl;

import org.springframework.stereotype.Repository;

import com.koch.dao.CartItemDao;
import com.koch.entity.CartItem;

@Repository("cartItemDao")
public class CartItemDaoImpl extends BaseDaoImpl<CartItem> implements CartItemDao{
	
}
