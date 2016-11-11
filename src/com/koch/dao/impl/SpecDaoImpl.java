package com.koch.dao.impl;

import org.springframework.stereotype.Repository;

import com.koch.dao.SpecDao;
import com.koch.entity.Spec;

@Repository("specDao")
public class SpecDaoImpl extends BaseDaoImpl<Spec> implements SpecDao{
	
}
