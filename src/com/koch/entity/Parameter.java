package com.koch.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="parameter")
public class Parameter extends BaseEntity{ 
	private static final long serialVersionUID = -6592162538757909064L;
	private String name;
	private String value;
	private ProductCategory productCategory;
	private Integer orderList;
	private List<ParameterItems> parameterItems;
	
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="productCategoryId")
	@JsonIgnoreProperties(value={"handler","fieldHandler"})
	public ProductCategory getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}
	@Column
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Column
	public Integer getOrderList() {
		return orderList;
	}
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	
	@OneToMany(mappedBy = "parameter", fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.ALL }, orphanRemoval = true)
	@OrderBy("orderList asc")
	@JsonIgnore
	public List<ParameterItems> getParameterItems() {
		return parameterItems;
	}
	public void setParameterItems(List<ParameterItems> parameterItems) {
		this.parameterItems = parameterItems;
	}
	
	
}
