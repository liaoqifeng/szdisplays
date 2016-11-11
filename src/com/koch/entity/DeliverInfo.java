package com.koch.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="deliver_info")
public class DeliverInfo extends BaseEntity{
	private static final long serialVersionUID = -4372748028441038725L;
	
	private String number;
	private Order order;
	private Deliverway deliverway;
	private String deliverwayName;
	private Logistics logistics;
	private String logisticsName;
	private String deliverCode;
	private BigDecimal deliverAmount;
	private String receiver;
	private String zipCode;
	private String area;
	private String address;
	private String phone;
	private String remark;
	private String operator;
	
	private List<DeliverInfoItem> deliverInfoItems = new ArrayList<DeliverInfoItem>();
	@Column
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderId", nullable = false)
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deliverwayId", nullable = false)
	public Deliverway getDeliverway() {
		return deliverway;
	}
	public void setDeliverway(Deliverway deliverway) {
		this.deliverway = deliverway;
	}
	@Column
	public String getDeliverwayName() {
		return deliverwayName;
	}
	public void setDeliverwayName(String deliverwayName) {
		this.deliverwayName = deliverwayName;
	}
	@Column
	public String getLogisticsName() {
		return logisticsName;
	}
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "logisticsId")
	public Logistics getLogistics() {
		return logistics;
	}
	public void setLogistics(Logistics logistics) {
		this.logistics = logistics;
	}
	@Column
	public String getDeliverCode() {
		return deliverCode;
	}
	public void setDeliverCode(String deliverCode) {
		this.deliverCode = deliverCode;
	}
	@Column
	public BigDecimal getDeliverAmount() {
		return deliverAmount;
	}
	public void setDeliverAmount(BigDecimal deliverAmount) {
		this.deliverAmount = deliverAmount;
	}
	@Column
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	@Column
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	@Column
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	@Column
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	@JsonIgnore
	@OneToMany(mappedBy = "deliverInfo", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	public List<DeliverInfoItem> getDeliverInfoItems() {
		return deliverInfoItems;
	}
	public void setDeliverInfoItems(List<DeliverInfoItem> deliverInfoItems) {
		this.deliverInfoItems = deliverInfoItems;
	}
	
}
