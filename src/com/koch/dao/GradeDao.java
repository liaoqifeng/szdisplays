package com.koch.dao;

import java.math.BigDecimal;

import com.koch.entity.Grade;

public interface GradeDao extends BaseDao<Grade>{
	
	public Grade findByAmount(BigDecimal amount);
	public boolean nameExists(String name);
	public boolean amountExists(BigDecimal expValue);
	public Grade findSystem();
	
}
