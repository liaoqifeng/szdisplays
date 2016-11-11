package com.koch.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koch.bean.Pager;
import com.koch.dao.CouponInfoDao;
import com.koch.entity.Coupon;
import com.koch.entity.CouponInfo;
import com.koch.entity.Member;
import com.koch.service.CouponInfoService;

@Service
public class CouponInfoServiceImpl extends BaseServiceImpl<CouponInfo> implements CouponInfoService{
	@Resource
	public CouponInfoDao couponInfoDao;
	
	@Resource
	public void setBaseDao(CouponInfoDao couponInfoDao) {
		super.setBaseDao(couponInfoDao);
	}

	@Transactional
	public List<CouponInfo> build(Coupon coupon, Member member, Integer count) {
		return couponInfoDao.build(coupon, member, count);
	}

	@Transactional
	public CouponInfo build(Coupon coupon, Member member) {
		return couponInfoDao.build(coupon, member);
	}

	@Transactional(readOnly=true)
	public boolean codeExists(String code) {
		return couponInfoDao.codeExists(code);
	}

	@Transactional(readOnly=true)
	public Long count(Coupon coupon, Member member, Boolean hasBegun,
			Boolean hasExpired, Boolean isUsed) {
		return couponInfoDao.count(coupon, member, hasBegun, hasExpired, isUsed);
	}

	@Transactional(readOnly=true)
	public CouponInfo findByCode(String code) {
		return couponInfoDao.findByCode(code);
	}

	@Transactional(readOnly=true)
	public Pager findPage(Member member, Pager pager) {
		return couponInfoDao.findPage(member, pager);
	}
	
	@Transactional(readOnly=true)
	public Pager findByPage(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed,Pager pager){
		return couponInfoDao.findByPage(coupon, member, hasBegun, hasExpired, isUsed, pager);
	}
}
