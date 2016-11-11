package com.koch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;


@Entity
@Table(name="serial_number")
public class SerialNumber extends BaseEntity{ 
	private static final long serialVersionUID = -5378612186973990348L;
	
	public enum Type{
		product,  order,  payment,  refunds,  deliver,  returns;
	}
	private Type type;
	private Integer lastValue;
	
	@Enumerated
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	@Column
	public Integer getLastValue() {
		return lastValue;
	}
	public void setLastValue(Integer lastValue) {
		this.lastValue = lastValue;
	}
	
	
}
