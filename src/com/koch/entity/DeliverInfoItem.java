package com.koch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="deliver_info_item")
public class DeliverInfoItem extends BaseEntity{
	private static final long serialVersionUID = 5065244895118803646L;
	
	private DeliverInfo deliverInfo;
	private String productNumber;
	private String productName;
	private Integer productQuantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deliverInfoId", nullable = false)
	public DeliverInfo getDeliverInfo() {
		return deliverInfo;
	}
	public void setDeliverInfo(DeliverInfo deliverInfo) {
		this.deliverInfo = deliverInfo;
	}
	@Column
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	@Column
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	@Column
	public Integer getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}
}
