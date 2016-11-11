package com.koch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="gift_item",uniqueConstraints={@javax.persistence.UniqueConstraint(columnNames={"productId", "promotionId"})})
public class GiftItem extends BaseEntity{ 
	private static final long serialVersionUID = 265289156149429103L;
	
	private Promotion promotion;
	private Product product;
	private Integer quantity;
	
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="promotionId",nullable=false)
	public Promotion getPromotion() {
		return promotion;
	}
	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="productId",nullable=false)
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	@NotNull
	@Column(nullable=false)
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	

}
