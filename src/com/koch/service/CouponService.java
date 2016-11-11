package com.koch.service;

import com.koch.bean.Pager;
import com.koch.entity.Coupon;


public interface CouponService extends BaseService<Coupon>{
	public Pager findPage(String name, Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pager pager);
}
