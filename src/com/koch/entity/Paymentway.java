package com.koch.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity
@Table(name="payment_way")
@JsonFilter("paymentway")
public class Paymentway extends BaseEntity {
	private static final long serialVersionUID = 2442162372566750093L;
	
	public enum PaymentwayType{
		online,offline;
	}
	
	private String name;
	private PaymentwayType type;
	private Integer timeout;
	private String image;
	private String remark;
	private String describtion;
	private Integer orderList;
	
	private Set<Deliverway> deliverways = new HashSet<Deliverway>();
	private Set<Order> orders = new HashSet<Order>();
	
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Enumerated
	@Column
	public PaymentwayType getType() {
		return type;
	}
	public void setType(PaymentwayType type) {
		this.type = type;
	}
	@Column
	public Integer getTimeout() {
		return timeout;
	}
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	@Column
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column
	public String getDescribtion() {
		return describtion;
	}
	public void setDescribtion(String describtion) {
		this.describtion = describtion;
	}
	@Column
	public Integer getOrderList() {
		return orderList;
	}
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="payment_deliver_way",joinColumns={@JoinColumn(name="paymentwayId")}, inverseJoinColumns={@JoinColumn(name="deliverwayId")})
	@OrderBy("orderList asc")
	public Set<Deliverway> getDeliverways() {
		return deliverways;
	}
	public void setDeliverways(Set<Deliverway> deliverways) {
		this.deliverways = deliverways;
	}

	@OneToMany(mappedBy = "paymentway", fetch = FetchType.LAZY)
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
	@PreRemove
	public void preRemove() {
		Set<Order> orderSet = getOrders();
		if (orderSet != null) {
			Iterator<Order> iterator = orderSet.iterator();
			while (iterator.hasNext()) {
				Order order = iterator.next();
				order.setPaymentway(null);
			}
		}
	}
	
}
