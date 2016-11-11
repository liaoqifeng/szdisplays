package com.koch.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.bean.Pager;
import com.koch.dao.CouponDao;
import com.koch.entity.Coupon;
import com.koch.service.CouponService;

@Service
public class CouponServiceImpl extends BaseServiceImpl<Coupon> implements CouponService{
	@Resource
	public CouponDao couponDao;
	
	@Resource
	public void setBaseDao(CouponDao couponDao) {
		super.setBaseDao(couponDao);
	}
	
	public Pager findPage(String name, Boolean isEnabled, Boolean isExchange, Boolean hasExpired, Pager pager){
		return couponDao.findPage(name, isEnabled, isExchange, hasExpired, pager);
	}
}
