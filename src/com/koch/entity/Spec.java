package com.koch.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javax.validation.constraints.Min;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.koch.util.SpringUtil;


@Entity
@Table(name="spec")
@JsonFilter("spec")
public class Spec extends BaseEntity implements Comparable<Spec>{ 
	private static final long serialVersionUID = -574477858069263286L;
	// 规格显示类型（文字、图片）
	public enum SpecType{
		literal, image
	}
	
	private String name;
	private String value;
	private SpecType specType;
	private String remark;
	private Integer orderList;
	private String typeName;
	private List<SpecAttribute> specAttributes = new ArrayList<SpecAttribute>();
	private Set<Product> products = new HashSet<Product>();
	private SpecAttribute specAttribute;
	
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Enumerated
	@Column(name="typeId")
	public SpecType getSpecType() {
		return specType;
	}
	public void setSpecType(SpecType specType) {
		this.specType = specType;
	}
	@Column
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@Min(0L)
	@Column
	public Integer getOrderList() {
		return orderList;
	}
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	@OneToMany(mappedBy="spec",fetch = FetchType.LAZY, cascade = { javax.persistence.CascadeType.ALL }, orphanRemoval = true)
	@OrderBy("orderList asc")
	public List<SpecAttribute> getSpecAttributes() {
		return specAttributes;
	}
	public void setSpecAttributes(List<SpecAttribute> specAttributes) {
		this.specAttributes = specAttributes;
	}
	@ManyToMany(mappedBy="specs", fetch=FetchType.LAZY)
	public Set<Product> getProducts() {
		return products;
	}
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	@Transient
	public String getTypeName() {
		if(getSpecType() != null){
			return SpringUtil.getMessage("SpecType."+getSpecType(), new Object[0]);
		}
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@Transient
	public SpecAttribute getSpecAttribute() {
		return specAttribute;
	}
	public void setSpecAttribute(SpecAttribute specAttribute) {
		this.specAttribute = specAttribute;
	}
	
	public int compareTo(Spec spec) {
	    return new CompareToBuilder().append(getOrderList(), spec.getOrderList()).append(getId(), spec.getId()).toComparison();
	}
}
