package com.koch.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.koch.bean.Pager;
import com.koch.bean.Pager.OrderType;
import com.koch.dao.BrandDao;
import com.koch.entity.Brand;

@Repository("brandDao")
public class BrandDaoImpl extends BaseDaoImpl<Brand> implements BrandDao{
	
}
