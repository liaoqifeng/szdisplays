package com.koch.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="product_category")
@JsonFilter("productCategory")
public class ProductCategory extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 97090000968318690L;

	public static final String PATH_SEPARATOR = ",";// 树路径分隔符
	
	private String name;
	private String brands;
	private String title;
	private String keywords;
	private String describtion;
	private ProductCategory parent;
	private String path;
	private Integer level;
	private Integer orderList;
	private Set<Product> productSet = new HashSet<Product>();// 商品
	private Set<Parameter> parameterSet = new HashSet<Parameter>();// 参数
	private Set<Property> propertySet = new HashSet<Property>();// 属性
	private Set<Promotion> promotions = new HashSet<Promotion>();//分类促销
	private Set<Brand> brandSet = new HashSet<Brand>();
	
	private Set<ProductCategory> children = new HashSet<ProductCategory>();
	
	private String text;
	private String state;
	
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getDescribtion() {
		return describtion;
	}
	public void setDescribtion(String describtion) {
		this.describtion = describtion;
	}
	@Column
	@JsonIgnore
	public String getBrands() {
		return brands;
	}
	public void setBrands(String brands) {
		this.brands = brands;
	}
	@Column
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parentId")
	public ProductCategory getParent() {
		return this.parent;
	}

	public void setParent(ProductCategory parent) {
		this.parent = parent;
	}
	@Column
	@JsonIgnore
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Column
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	@Column
	public Integer getOrderList() {
		return orderList;
	}
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	
	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
	@JsonIgnore
	public Set<Product> getProductSet() {
		return productSet;
	}
	public void setProductSet(Set<Product> productSet) {
		this.productSet = productSet;
	}
	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
	@JsonIgnore
	public Set<Parameter> getParameterSet() {
		return parameterSet;
	}
	public void setParameterSet(Set<Parameter> parameterSet) {
		this.parameterSet = parameterSet;
	}
	@OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
	@JsonIgnore
	public Set<Property> getPropertySet() {
		return propertySet;
	}
	public void setPropertySet(Set<Property> propertySet) {
		this.propertySet = propertySet;
	}
	@ManyToMany(mappedBy="productCategorys", fetch=FetchType.LAZY)
	public Set<Promotion> getPromotions() {
		return promotions;
	}
	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "product_category_brand",joinColumns={@JoinColumn(name="productCategoryId")}, inverseJoinColumns={@JoinColumn(name="brandId")})
	@OrderBy("orderList asc")
	public Set<Brand> getBrandSet() {
		return brandSet;
	}
	public void setBrandSet(Set<Brand> brandSet) {
		this.brandSet = brandSet;
	}
	@OneToMany(mappedBy="parent", fetch=FetchType.LAZY)
	@OrderBy("orderList asc")
	public Set<ProductCategory> getChildren() {
		return children;
	}
	public void setChildren(Set<ProductCategory> children) {
		this.children = children;
	}
	@Transient
	public String getText() {
		return this.name;
	}
	public void setText(String text) {
		this.text = this.name;
	}
	@Transient
	public String getState() {
		if(StringUtils.isEmpty(this.state))
			return "closed";
		return this.state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@Transient
	public List<Integer> getTreePaths() {
		List<Integer> paths = new ArrayList<Integer>();
		String[] array = StringUtils.split(getPath(), ",");
		if (array != null) {
			for (String str : array) {
				if(StringUtils.isEmpty(str)) continue;
				paths.add(Integer.valueOf(str));
			}
		}
		return paths;
	}
	
	@PrePersist
	public void prePersist() {
		
	}

	@PreUpdate
	public void preUpdate() {
		
	}
	
	@PreRemove
	public void preRemove() {
		Set<Promotion> set = getPromotions();
		if (set != null) {
			Iterator<Promotion> iterator = set.iterator();
			while (iterator.hasNext()) {
				Promotion promotion = iterator.next();
				promotion.getProductCategorys().remove(this);
			}
		}
	}
}
