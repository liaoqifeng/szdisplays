package com.koch.service;

import com.koch.entity.Admin;


public interface AdminService extends BaseService<Admin>{
	public Admin getCurrent();
}
