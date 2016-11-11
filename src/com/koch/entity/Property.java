package com.koch.entity;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name="property")
public class Property extends BaseEntity{ 
	private static final long serialVersionUID = -6592162538757909064L;
	private String name;
	private String value;
	private ProductCategory productCategory;
	private Integer orderList;
	private Integer propertyIndex;
	
	private List<String> options;
	
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
	@Column
	public Integer getPropertyIndex() {
		return propertyIndex;
	}
	public void setPropertyIndex(Integer propertyIndex) {
		this.propertyIndex = propertyIndex;
	}
	@NotEmpty
	@ElementCollection(targetClass=String.class,fetch=FetchType.LAZY)
	@CollectionTable(name="property_option",joinColumns={@JoinColumn(name="propertyId")})
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	
	
}
