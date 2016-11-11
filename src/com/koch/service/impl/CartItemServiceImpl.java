package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.CartItemDao;
import com.koch.entity.CartItem;
import com.koch.service.CartItemService;

@Service
public class CartItemServiceImpl extends BaseServiceImpl<CartItem> implements CartItemService {
	@Resource
	private CartItemDao cartItemDao;

	@Resource
	public void setBaseDao(CartItemDao cartItemDao) {
		super.setBaseDao(cartItemDao);
	}

}
