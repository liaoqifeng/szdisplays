package com.koch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_log")
public class OrderLog extends BaseEntity{
	private static final long serialVersionUID = 3141539819888995108L;
	public enum OrderLogType {
		create, modify, processed, payment, refunds, deliver, returns, complete, invalid, other;
	};
	private Order order;
	private String orderNumber;
	private OrderLogType type;
	private String operator;
	private String info;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderId", nullable = false)
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	@Column
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	@Enumerated
	@Column
	public OrderLogType getType() {
		return type;
	}
	public void setType(OrderLogType type) {
		this.type = type;
	}
	@Column
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	@Column
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
}
