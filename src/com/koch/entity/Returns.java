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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="returns")
public class Returns extends BaseEntity{
	private static final long serialVersionUID = -9082284568341296258L;
	
	private String number;//退货单号
	private Order order;//订单号
	private Deliverway deliverway;//配送方式
	private String deliverwayName;
	private Logistics logistics;//物流公司
	private String logisticsName;
	private String deliverCode;//配送单号
	private BigDecimal deliverAmount;//运费
	private String shipper;//发货人
	private String zipCode;//邮编
	private String area;//地区
	private String address;//地址
	private String phone;//电话
	private String remark;//备注
	private String operator;//操作人
	
	private List<ReturnsItem> returnsItems = new ArrayList<ReturnsItem>();
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
	public String getShipper() {
		return shipper;
	}
	public void setShipper(String shipper) {
		this.shipper = shipper;
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
	@OneToMany(mappedBy = "returns", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	public List<ReturnsItem> getReturnsItems() {
		return returnsItems;
	}
	public void setReturnsItems(List<ReturnsItem> returnsItems) {
		this.returnsItems = returnsItems;
	}
	
}
