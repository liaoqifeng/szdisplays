package com.koch.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.PreparedExpression;
import org.wltea.expression.datameta.Variable;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name="promotion")
@JsonFilter("promotion")
public class Promotion extends BaseEntity{ 
	private static final long serialVersionUID = 7922323854253803218L;
	
	private String name;
	private Integer orderList;
	private Date startDate;
	private Date endDate;
	private Integer minQuantity;
	private Integer maxQuantity;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private String introduction;
	private Boolean isAllowedCoupon;
	private Boolean isFreeDeliver;
	private String scoreExpression;
	private String priceExpression;
	private String title;
	
	private Set<Grade> grades = new HashSet<Grade>();
	private Set<ProductCategory> productCategorys = new HashSet<ProductCategory>();
	private Set<Product> products = new HashSet<Product>();
	private Set<Brand> brands = new HashSet<Brand>();
	private Set<Coupon> coupons = new HashSet<Coupon>();
	private List<GiftItem> giftItems = new ArrayList<GiftItem>();
	
	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	@Column
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Column
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@JsonProperty
	@Min(0L)
	@Column
	public Integer getMinQuantity() {
		return minQuantity;
	}
	public void setMinQuantity(Integer minQuantity) {
		this.minQuantity = minQuantity;
	}
	@JsonProperty
	@Min(0L)
	@Column
	public Integer getMaxQuantity() {
		return maxQuantity;
	}
	public void setMaxQuantity(Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
	}
	@JsonProperty
	@Min(0L)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 18, scale = 4)
	public BigDecimal getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}
	@JsonProperty
	@Min(0L)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 18, scale = 4)
	public BigDecimal getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}
	@Lob
	@Column
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	@NotNull
	@Column(nullable=false)
	public Boolean getIsAllowedCoupon() {
		return isAllowedCoupon;
	}
	public void setIsAllowedCoupon(Boolean isAllowedCoupon) {
		this.isAllowedCoupon = isAllowedCoupon;
	}
	@NotNull
	@Column(nullable=false)
	public Boolean getIsFreeDeliver() {
		return isFreeDeliver;
	}
	public void setIsFreeDeliver(Boolean isFreeDeliver) {
		this.isFreeDeliver = isFreeDeliver;
	}
	@Column
	public String getScoreExpression() {
		return scoreExpression;
	}
	public void setScoreExpression(String scoreExpression) {
		this.scoreExpression = scoreExpression;
	}
	@Column
	public String getPriceExpression() {
		return priceExpression;
	}
	public void setPriceExpression(String priceExpression) {
		this.priceExpression = priceExpression;
	}

	@JsonProperty
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="promotion_member_grade",joinColumns={@JoinColumn(name="promotionId")}, inverseJoinColumns={@JoinColumn(name="gradeId")})
	public Set<Grade> getGrades() {
		return grades;
	}
	public void setGrades(Set<Grade> grades) {
		this.grades = grades;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="promotion_product_category",joinColumns={@JoinColumn(name="promotionId")}, inverseJoinColumns={@JoinColumn(name="productCategoryId")})
	public Set<ProductCategory> getProductCategorys() {
		return productCategorys;
	}
	public void setProductCategorys(Set<ProductCategory> productCategorys) {
		this.productCategorys = productCategorys;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="promotion_product",joinColumns={@JoinColumn(name="promotionId")}, inverseJoinColumns={@JoinColumn(name="productId")})
	public Set<Product> getProducts() {
		return products;
	}
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="promotion_brand",joinColumns={@JoinColumn(name="promotionId")}, inverseJoinColumns={@JoinColumn(name="brandId")})
	public Set<Brand> getBrands() {
		return brands;
	}
	public void setBrands(Set<Brand> brands) {
		this.brands = brands;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="promotion_coupon",joinColumns={@JoinColumn(name="promotionId")}, inverseJoinColumns={@JoinColumn(name="couponId")})
	public Set<Coupon> getCoupons() {
		return coupons;
	}
	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}
	
	@Valid
	@OneToMany(mappedBy="promotion", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.ALL}, orphanRemoval=true)
	public List<GiftItem> getGiftItems() {
		return giftItems;
	}
	public void setGiftItems(List<GiftItem> giftItems) {
		this.giftItems = giftItems;
	}
	
	@Transient
	public BigDecimal calculatePrice(BigDecimal price,Integer quantity) {
		if(StringUtils.isNotEmpty(getPriceExpression())){
			List<Variable> variables = new ArrayList<Variable>();
			if(getPriceExpression().indexOf("价格") >= 0){
				variables.add(Variable.createVariable("价格", price.doubleValue()));
			}
			if(getPriceExpression().indexOf("数量") >= 0){
				variables.add(Variable.createVariable("数量", quantity));
			}
			PreparedExpression pe = ExpressionEvaluator.preparedCompile(getPriceExpression(), variables);
			Object result = pe.execute();
			if(result != null){
				price = new BigDecimal(result.toString());
			}
		}
		return price;
	}

	@Transient
	public Integer calculateScore(Integer score,BigDecimal price,Integer quantity) {
		if(StringUtils.isNotEmpty(getScoreExpression())){
			List<Variable> variables = new ArrayList<Variable>();
			if(getScoreExpression().indexOf("数量") >= 0){
				variables.add(Variable.createVariable("数量", quantity));
			}
			if(getScoreExpression().indexOf("积分") >= 0){
				variables.add(Variable.createVariable("积分", quantity));
			}
			PreparedExpression pe = ExpressionEvaluator.preparedCompile(getScoreExpression(), variables);
			Object result = pe.execute();
			if(result != null){
				score = Integer.valueOf(result.toString());
			}
		}
		return score;
	}
	
	@Transient
	public boolean hasBegun() {
		return (getStartDate() == null) || (new Date().after(getStartDate()));
	}

	@Transient
	public boolean hasEnded() {
		return (getEndDate() != null) && (new Date().after(getEndDate()));
	}
}
