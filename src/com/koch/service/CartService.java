package com.koch.service;

import com.koch.entity.Cart;
import com.koch.entity.Member;

public interface CartService extends BaseService<Cart>{
	public Cart getCurrent();
	public void merge(Member member, Cart cart);
	public void evictExpired();
}
