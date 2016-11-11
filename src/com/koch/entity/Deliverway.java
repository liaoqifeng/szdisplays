package com.koch.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koch.bean.Setting;
import com.koch.util.SettingUtils;

@Entity
@Table(name="deliver_way")
@JsonFilter("deliverway")
public class Deliverway extends BaseEntity{
	private static final long serialVersionUID = -8162746535685737349L;
	private String name;
	private Logistics logistics;
	private BigDecimal firstWeight;
	private BigDecimal conWeight;
	private BigDecimal firstPrice;
	private BigDecimal conPrice;
	private String image;
	private String describtion;
	private Integer orderList;
	
	private Set<Paymentway> paymentways = new HashSet<Paymentway>();
	private Set<Order> orders = new HashSet<Order>();
	
	@NotEmpty
	@Length(max=200)
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="logisticsId")
	@JsonIgnore
	public Logistics getLogistics() {
		return logistics;
	}
	public void setLogistics(Logistics logistics) {
		this.logistics = logistics;
	}
	
	@NotNull
	@Min(0L)
	@Column(nullable=false)
	public BigDecimal getFirstWeight() {
		return firstWeight;
	}
	public void setFirstWeight(BigDecimal firstWeight) {
		this.firstWeight = firstWeight;
	}
	@NotNull
	@Min(1L)
	@Column(nullable=false)
	public BigDecimal getConWeight() {
		return conWeight;
	}
	public void setConWeight(BigDecimal conWeight) {
		this.conWeight = conWeight;
	}
	
	@NotNull
	@Min(0L)
	@Digits(integer=12, fraction=3)
	@Column(nullable=false, precision=18, scale=4)
	public BigDecimal getFirstPrice() {
		return firstPrice;
	}
	public void setFirstPrice(BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}
	@NotNull
	@Min(0L)
	@Digits(integer=12, fraction=3)
	@Column(nullable=false, precision=18, scale=4)
	public BigDecimal getConPrice() {
		return conPrice;
	}
	public void setConPrice(BigDecimal conPrice) {
		this.conPrice = conPrice;
	}
	@Column
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	@Lob
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
	
	@ManyToMany(mappedBy="deliverways", fetch=FetchType.LAZY)
	public Set<Paymentway> getPaymentways() {
		return paymentways;
	}
	public void setPaymentways(Set<Paymentway> paymentways) {
		this.paymentways = paymentways;
	}
	@OneToMany(mappedBy="paymentway", fetch=FetchType.LAZY)
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
	@Transient
	public BigDecimal calculateFreight(BigDecimal weight) {
		Setting setting = SettingUtils.get();
		BigDecimal freight = new BigDecimal(0);
		if (weight != null) {
			if (weight.compareTo(getFirstWeight()) <= 0 || getConWeight().compareTo(new BigDecimal(0)) == 0) {
				freight = getFirstPrice();
			} else {
				double d = Math.ceil(weight.subtract(getFirstWeight()).divide(getConWeight()).doubleValue());
				freight = getFirstPrice().add(getConPrice().multiply(new BigDecimal(d)));
			}
		}
		return setting.setScale(freight);
	}
	  
	@PreRemove
	public void preRemove() {
		Set<Paymentway> set = getPaymentways();
		if (set != null) {
			Iterator<Paymentway> iterator = set.iterator();
			while (iterator.hasNext()) {
				Paymentway paymentway = iterator.next();
				paymentway.getDeliverways().remove(this);
			}
		}
		Set<Order> orderSet = getOrders();
		if (orderSet != null) {
			Iterator<Order> iterator = orderSet.iterator();
			while (iterator.hasNext()) {
				Order order = iterator.next();
				order.setDeliverway(null);
			}
		}
	}
	
}
