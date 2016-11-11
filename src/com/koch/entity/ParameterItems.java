package com.koch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="parameter_items")
public class ParameterItems extends BaseEntity{ 
	private static final long serialVersionUID = -4552471593103504810L;
	
	private Parameter parameter;
	private String name;
	private Integer orderList;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parameterId")
	@JsonIgnore
	public Parameter getParameter() {
		return parameter;
	}
	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
	
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column
	public Integer getOrderList() {
		return orderList;
	}
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	
}
