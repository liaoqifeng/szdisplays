package com.koch.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koch.dao.AdminDao;
import com.koch.entity.Admin;
import com.koch.service.AdminService;
import com.koch.util.GlobalConstant;

@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService{
	
	@Autowired
	private  HttpServletRequest request; 

	@Resource
	public AdminDao adminDao;
	
	@Resource
	public void setBaseDao(AdminDao adminDao) {
		super.setBaseDao(adminDao);
	}
	
	public Admin getCurrent(){
		return (Admin)request.getSession().getAttribute(GlobalConstant.BACK_SESSION_USER);
	}
}
