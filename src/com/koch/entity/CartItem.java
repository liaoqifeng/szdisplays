package com.koch.entity;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.koch.bean.Setting;
import com.koch.util.SettingUtils;


@Entity
@Table(name="cart_item")
@JsonFilter("cartItem")
public class CartItem extends BaseEntity{ 
	private static final long serialVersionUID = 7294114151025218265L;
	public static final Integer MAX_QUANTITY = 10000;
	
	private Cart cart;
	private Product product;
	private Integer quantity;
	
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cartId",nullable=false)
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
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
	@Column
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
	@Transient
	public Integer getScore() {
		if ((getProduct() != null) && (getProduct().getScore() != null) && (getQuantity() != null))
			return getProduct().getScore() * getQuantity();
		return 0;
	}

	@Transient
	public BigDecimal getWeight() {
		if ((getProduct() != null) && (getProduct().getWeight() != null) && (getQuantity() != null))
			return getProduct().getWeight().multiply(new BigDecimal(getQuantity()));
		return new BigDecimal(0);
	}

	//
	@Transient
	public BigDecimal getUnitPrice() {
		if ((getProduct() != null) && (getProduct().getSalePrice() != null)) {
			Setting setting = SettingUtils.get();
			if ((getCart() != null) && (getCart().getMember() != null) && (getCart().getMember().getGrade() != null)) {
				Grade grade = getCart().getMember().getGrade();
				Map<Grade, BigDecimal> map = getProduct().getMemberPrice();
				if ((map != null) && (!map.isEmpty()) && (map.containsKey(grade)))
					return setting.setScale(map.get(grade));
				if (grade.getDiscount() != null)
					return setting.setScale(getProduct().getSalePrice().multiply(new BigDecimal(grade.getDiscount().doubleValue())));
			}
			return setting.setScale(getProduct().getSalePrice());
		}
		return new BigDecimal(0);
	}

	@Transient
	public BigDecimal getSubtotal() {
		if (getQuantity() != null)
			return getUnitPrice().multiply(new BigDecimal(getQuantity().intValue()));
		return new BigDecimal(0);
	}

	//判断商品是否可用库存不足
	@Transient
	public boolean getIsLowStock() {
		return (getQuantity() != null) && (getProduct() != null) && (getProduct().getStock() != null) && (getQuantity().intValue() > getProduct().getAvailableStock().intValue());
	}

	//购物车内添加商品数量
	@Transient
	public void add(int quantity) {
		if (quantity > 0)
			if (getQuantity() != null)
				setQuantity(Integer.valueOf(getQuantity().intValue() + quantity));
			else
				setQuantity(Integer.valueOf(quantity));
	}
	
}
