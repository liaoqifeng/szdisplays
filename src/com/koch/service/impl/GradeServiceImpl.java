package com.koch.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.koch.bean.OrderBy;
import com.koch.bean.OrderBy.OrderType;
import com.koch.dao.GradeDao;
import com.koch.service.GradeService;
import com.koch.entity.Grade;
import com.koch.entity.Grade.GradeType;

@Service
public class GradeServiceImpl extends BaseServiceImpl<Grade> implements GradeService{
	@Resource
	private GradeDao gradeDao;
	@Resource
	public void setBaseDao(GradeDao gradeDao) {
		super.setBaseDao(gradeDao);
	}
	
	@Cacheable(cacheName="gradeCache")
	public List<Grade> getAll() {
		return super.getAll(new OrderBy("orderList",OrderType.asc));
	}

	@Transactional
	@TriggersRemove(cacheName="gradeCache",removeAll=true)
	public void delete(Grade t) {
		super.delete(t);
	}

	@Transactional
	@TriggersRemove(cacheName="gradeCache",removeAll=true)
	public void delete(Integer id) {
		super.delete(id);
	}

	@Transactional
	@TriggersRemove(cacheName="gradeCache",removeAll=true)
	public Integer save(Grade t) {
		return super.save(t);
	}

	@Transactional
	@TriggersRemove(cacheName="gradeCache",removeAll=true)
	public Grade update(Grade t) {
		return super.update(t);
	}
	
	@Transactional(readOnly=true)
	public boolean nameExists(String name){
		return this.gradeDao.nameExists(name);
	}
	
	@Transactional(readOnly = true)
	public boolean nameUnique(String previousName, String currentName) {
		if (StringUtils.equalsIgnoreCase(previousName, currentName)) {
			return true;
		}
		return !this.gradeDao.nameExists(currentName);
	}
	
	@Transactional(readOnly = true)
	public boolean amountExists(BigDecimal expValue){
		return this.gradeDao.amountExists(expValue);
	}
	
	@Transactional(readOnly = true)
	public boolean amountUnique(BigDecimal previousAmount, BigDecimal currentAmount) {
		if (previousAmount != null && previousAmount.compareTo(currentAmount) == 0) {
			return true;
		}
		return !this.gradeDao.amountExists(currentAmount);
	}
	
	@Transactional(readOnly = true)
	public Grade findSystem(){
		return this.gradeDao.findSystem();
	}
	
}
