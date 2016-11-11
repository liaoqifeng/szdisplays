package com.koch.service;

import java.math.BigDecimal;
import java.util.List;

import com.koch.bean.Pager;
import com.koch.entity.Admin;
import com.koch.entity.Grade;
import com.koch.entity.Member;
import com.koch.entity.Member.MemberStatus;

public interface MemberService extends BaseService<Member>{
	public BigDecimal getDeposit(Integer id);
	public boolean usernameExists(String username);
	public boolean emailExists(String email);
	public boolean emailUnique(String previousEmail, String currentEmail);
	public Member findByUsername(String username);
	public List<Member> findListByEmail(String email);
	public Pager<Member> findByPager(Grade grade,String name,MemberStatus status,Pager<Member> pager);
	public void save(Member member, Admin operator);
	public void update(Member member, BigDecimal modifyDeposit, String modifyDepositRemark, Integer modifyScore, Admin operator);
	public Member getCurrent();
}
