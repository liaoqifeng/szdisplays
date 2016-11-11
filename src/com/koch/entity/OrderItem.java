package com.koch.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="order_item")
public class OrderItem extends BaseEntity {
	private static final long serialVersionUID = -6253715627370043484L;
	private Order order; //订单
	private Product product; //商品
	private String productName; //商品名称
	private String productFullName;//商品全称
	private String productImage;//商品图片
	private BigDecimal productPrice;//商品价格
	private String productNumber; //商品编号
	private BigDecimal productWeight;//商品重量
	private Integer productQuantity;//商品数量
	private Integer deliveryQuantity;//发货数量
	private Integer returnQuantity;//退货数量
	private Boolean isGift;//是否赠品
	
	private BigDecimal totalAmount;
	private BigDecimal totalWeight;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="orderId")
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="productId")
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	@Column
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	@Column
	public String getProductFullName() {
		return productFullName;
	}
	public void setProductFullName(String productFullName) {
		this.productFullName = productFullName;
	}
	@Column
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	@Column
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	@Column
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	@Column
	public Integer getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}
	@Column
	public BigDecimal getProductWeight() {
		return productWeight;
	}
	public void setProductWeight(BigDecimal productWeight) {
		this.productWeight = productWeight;
	}
	@Column
	public Integer getDeliveryQuantity() {
		return deliveryQuantity;
	}
	public void setDeliveryQuantity(Integer deliveryQuantity) {
		this.deliveryQuantity = deliveryQuantity;
	}
	@Column
	public Integer getReturnQuantity() {
		return returnQuantity;
	}
	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}
	@Column(nullable=false, updatable=false)
	public Boolean getIsGift() {
		return isGift;
	}
	public void setIsGift(Boolean isGift) {
		this.isGift = isGift;
	}
	@Transient
	public BigDecimal getTotalAmount() {
		if(getProductPrice() != null && getProductQuantity() != null)
			return (getProductPrice().multiply(new BigDecimal(getProductQuantity())));
		return new BigDecimal(0);
	}
	@Transient
	public BigDecimal getTotalWeight() {
		if(getProductWeight() != null && getProductQuantity() != null)
			return (getProductWeight().multiply(new BigDecimal(getProductQuantity().intValue())));
		return new BigDecimal(0);
	}
}
