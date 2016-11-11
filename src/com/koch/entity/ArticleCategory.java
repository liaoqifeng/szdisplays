package com.koch.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koch.util.GlobalConstant.Language;


@Entity
@Table(name="article_category")
@JsonFilter("articleCategory")
public class ArticleCategory extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1285405654130584741L;

	public static final String PATH_SEPARATOR = ",";// 树路径分隔符
	
	private String name;
	private String title;
	private String keywords;
	private String describtion;
	private ArticleCategory parent;
	private String path;
	private Integer level;
	private Integer orderList;
	
	private Set<ArticleCategory> children = new HashSet<ArticleCategory>();
	private Set<Article> articles = new HashSet<Article>();
	
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
	@Column
	@JsonIgnore
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Column
	public Integer getOrderList() {
		return orderList;
	}
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parentId")
	public ArticleCategory getParent() {
		return parent;
	}
	public void setParent(ArticleCategory parent) {
		this.parent = parent;
	}
	@Column
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	@OneToMany(mappedBy="parent", fetch=FetchType.LAZY)
	@OrderBy("orderList asc")
	public Set<ArticleCategory> getChildren() {
		return children;
	}
	public void setChildren(Set<ArticleCategory> children) {
		this.children = children;
	}
	@OneToMany(mappedBy="articleCategory", fetch=FetchType.LAZY)
	public Set<Article> getArticles() {
		return articles;
	}
	public void setArticles(Set<Article> articles) {
		this.articles = articles;
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
	
}
