package com.koch.bean;


public class OrderBy {
	// 排序方式
	public enum OrderType{
		asc, desc
	}
	public OrderBy(){}
	public OrderBy(String orderBy) {
		super();
		this.orderBy = orderBy;
	}
	public OrderBy(String orderBy, OrderType orderType) {
		super();
		this.orderBy = orderBy;
		this.orderType = orderType;
	}
	private String orderBy = null;// 排序字段
	private OrderType orderType = OrderType.asc;// 排序方式
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public OrderType getOrderType() {
		return orderType;
	}
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	
}
