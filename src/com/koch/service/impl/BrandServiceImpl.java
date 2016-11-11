package com.koch.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.koch.dao.BrandDao;
import com.koch.dao.RoleDao;
import com.koch.service.BrandService;
import com.koch.service.RoleService;
import com.koch.entity.Brand;
import com.koch.entity.Role;

@Service
public class BrandServiceImpl extends BaseServiceImpl<Brand> implements BrandService{
	@Resource
	private BrandDao brandDao;
	@Resource
	public void setBaseDao(BrandDao brandDao) {
		super.setBaseDao(brandDao);
	}
}
