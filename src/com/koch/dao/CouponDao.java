package com.koch.dao;

import com.koch.bean.Pager;
import com.koch.entity.Coupon;

public interface CouponDao extends BaseDao<Coupon>{
	public Pager findPage(String name, Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pager pager);
}
