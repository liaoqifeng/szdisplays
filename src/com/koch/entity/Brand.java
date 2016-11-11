package com.koch.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="brand")
@JsonFilter("brand")
public class Brand extends BaseEntity{ 
	private static final long serialVersionUID = 1427890100396967575L;
	// 品牌显示类型（文字、图片）
	public enum BrandType {
		literal, image
	};
	
	private String name;
	private BrandType brandType;
	private String image;
	private String url;
	private String describtion;
	private Integer orderList;
	private Set<Product> productSet = new HashSet<Product>();// 商品
	private Set<Promotion> promotions = new HashSet<Promotion>();//品牌促销
	private Set<ProductCategory> productCategorys = new HashSet<ProductCategory>();
	
	private String text;
	
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Enumerated
	@Column(name="typeId")
	public BrandType getBrandType() {
		return brandType;
	}
	public void setBrandType(BrandType brandType) {
		this.brandType = brandType;
	}
	@Column
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	@Column
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	@OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
	@JsonIgnore
	public Set<Product> getProductSet() {
		return productSet;
	}
	public void setProductSet(Set<Product> productSet) {
		this.productSet = productSet;
	}
	@ManyToMany(mappedBy="brands", fetch=FetchType.LAZY)
	public Set<Promotion> getPromotions() {
		return promotions;
	}
	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}
	@ManyToMany(mappedBy = "brandSet", fetch = FetchType.LAZY)
	@OrderBy("orderList asc")
	public Set<ProductCategory> getProductCategorys() {
		return productCategorys;
	}
	public void setProductCategorys(Set<ProductCategory> productCategorys) {
		this.productCategorys = productCategorys;
	}
	@Transient
	public String getText() {
		return this.name;
	}
	public void setText(String text) {
		this.text = this.name;
	}
	
	
}
