package com.koch.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.koch.util.SpringUtil;


@Entity
@Table(name="tag")
@JsonFilter("tag")
public class Tag extends BaseEntity{ 
	private static final long serialVersionUID = -1651813584497031365L;
	public enum Type{
		product,  article;
	}
	private String name;
	private Type type;
	private String image;
	private String remark;
	private Integer orderList;
	
	private Set<Article> articles = new HashSet<Article>();
	private Set<Product> products = new HashSet<Product>();
	
	private String typeName;
	
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Enumerated
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	@Column
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column
	public Integer getOrderList() {
		return orderList;
	}
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	@ManyToMany(mappedBy="tags", fetch=FetchType.LAZY)
	public Set<Article> getArticles() {
		return articles;
	}
	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}
	@ManyToMany(mappedBy="tags", fetch=FetchType.LAZY)
	public Set<Product> getProducts() {
		return products;
	}
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	@Transient
	public String getTypeName() {
		if(this.getType() != null) return SpringUtil.getMessage("TagType."+this.getType());
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@PreRemove
	public void preRemove() {
		Set<Article> articleSet = getArticles();
		if (articleSet != null) {
			Iterator<Article> iterator = articleSet.iterator();
			while (iterator.hasNext()) {
				Article article = iterator.next();
				article.getTags().remove(this);
			}
		}
		Set<Product> productSet = getProducts();
		if (productSet != null) {
			Iterator<Product> iterator = productSet.iterator();
			while (iterator.hasNext()) {
				Product product = iterator.next();
				product.getTags().remove(this);
			}
		}
	}
	
}
