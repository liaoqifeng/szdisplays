package com.koch.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.koch.bean.Pager;
import com.koch.entity.Admin;

public interface AdminDao extends BaseDao<Admin>{
	public List<Admin> loadAll();
	
	public Pager findAdminByPagerNotSelf(Pager pager,Integer self);
	
	public Admin getAdminByUsername(String username);
}
