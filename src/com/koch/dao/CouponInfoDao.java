package com.koch.dao;

import java.util.List;

import com.koch.bean.Pager;
import com.koch.entity.Coupon;
import com.koch.entity.CouponInfo;
import com.koch.entity.Member;

public interface CouponInfoDao extends BaseDao<CouponInfo>{
	
	public boolean codeExists(String code);
	public CouponInfo findByCode(String code);
	public CouponInfo build(Coupon coupon, Member member);
	public List<CouponInfo> build(Coupon coupon, Member member, Integer count);
	public Pager findPage(Member member, Pager pager);
	public Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed);
	public Pager findByPage(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed,Pager pager);
}
