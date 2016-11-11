package com.koch.dao;

import java.math.BigDecimal;
import java.util.List;

import com.koch.bean.Pager;
import com.koch.entity.Grade;
import com.koch.entity.Member;
import com.koch.entity.Member.MemberStatus;

public interface MemberDao extends BaseDao<Member>{
	public BigDecimal getDeposit(Integer id);
	public boolean usernameExists(String username);
	public boolean emailExists(String email);
	public Member findByUsername(String username);
	public List<Member> findListByEmail(String email);
	public Pager<Member> findByPager(Grade grade,String name,MemberStatus status,Pager<Member> pager);
}
