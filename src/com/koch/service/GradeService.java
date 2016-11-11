package com.koch.service;

import java.math.BigDecimal;

import com.koch.entity.Grade;

public interface GradeService extends BaseService<Grade>{
	
	public boolean nameExists(String name);
	public boolean nameUnique(String previousName, String currentName);
	public boolean amountExists(BigDecimal expValue);
	public boolean amountUnique(BigDecimal previousAmount, BigDecimal currentAmount);
	public Grade findSystem();
}
