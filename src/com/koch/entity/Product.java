package com.koch.entity;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyClass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.io.ClassPathResource;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.koch.bean.BigDecimalNumericFieldBridge;
import com.koch.util.GlobalConstant;

@Indexed
@Entity
@Table(name="product")
@JsonFilter("product")
public class Product extends BaseEntity{
	private static final long serialVersionUID = -7591635736601845682L;
	
	private static String staticPath = null;
	
	static {
		try {
			File file = new ClassPathResource(GlobalConstant.SHOP_XML_PATH).getFile();
			Document document = new SAXReader().read(file);
			Element element = (Element) document.selectSingleNode("/shop/template[@id='productContent']");
			staticPath = element.attributeValue("staticPath");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public enum OrderType{
		topDesc, priceAsc, priceDesc, salesDesc, scoreDesc, dateDesc;
	}
	
	private String number;
	private String name;
	private String fullName;
	private Integer classifyId;
	private ProductCategory productCategory;
	private Brand brand;
	private Integer stock;
	private Integer lockStock;
	private String unit;
	private BigDecimal salePrice;
	private BigDecimal costPrice;
	private BigDecimal marketPrice;
	private Boolean isPublish;
	private String describtion;
	private String keywords;
	private String title;
	private String searchKey;
	private Integer score;
	private Boolean isVipPrice;
	private String vipPrice;
	private Boolean isGift;
	private Boolean isTop;
	private Boolean isShow;
	private BigDecimal weight;
	private String cargoSpace;
	private String remark;
	private String showImg;
	private Goods goods;
	private String introduction;
	private Integer salesCount;
	private Integer weekSalesCount;
	private Integer monthSalesCount;
	private Date weekSalesDate;
	private Date monthSalesDate;
	private String property0;
	private String property1;
	private String property2;
	private String property3;
	private String property4;
	private String property5;
	private String property6;
	private String property7;
	private String property8;
	private String property9;
	private String property10;
	private String property11;
	private String property12;
	private String property13;
	private String property14;
	private String property15;
	private String property16;
	private String property17;
	private String property18;
	private String property19; 
	
	private Set<Spec> specs = new HashSet<Spec>();
	private Map<SpecAttribute, String> specAttributes = new HashMap<SpecAttribute, String>();
	private List<ProductImage> productImages = new ArrayList<ProductImage>();
	private Map<Integer, String> parameterItems = new HashMap<Integer, String>();
	private Map<Grade, BigDecimal> memberPrice = new HashMap<Grade, BigDecimal>();
	private Set<Promotion> promotions = new HashSet<Promotion>();
	private Set<CartItem> cartItems = new HashSet<CartItem>();
	private Set<GiftItem> giftItems = new HashSet<GiftItem>();
	private Set<Member> members = new HashSet<Member>();
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();
	
	private Set<Tag> tags = new HashSet<Tag>();
	
	private Integer availableStock;
	
	
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@Pattern(regexp = "^[0-9a-zA-Z_-]+$")
	@Length(max = 200)
	@Column
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl=IKAnalyzer.class))
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Field(store=Store.YES, index=Index.NO)
	@Column(nullable=false)
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	@Column
	public Integer getClassifyId() {
		return classifyId;
	}
	public void setClassifyId(Integer classifyId) {
		this.classifyId = classifyId;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="productCategoryId")
	public ProductCategory getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="brandId")
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}
	@Column
	public String getDescribtion() {
		return describtion;
	}
	public void setDescribtion(String describtion) {
		this.describtion = describtion;
	}
	@Column
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	@Column
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@Length(max = 200)
	@Column
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	@Column
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsVipPrice() {
		return isVipPrice;
	}
	public void setIsVipPrice(Boolean isVipPrice) {
		this.isVipPrice = isVipPrice;
	}
	@Column
	public String getVipPrice() {
		return vipPrice;
	}
	public void setVipPrice(String vipPrice) {
		this.vipPrice = vipPrice;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsGift() {
		return isGift;
	}
	public void setIsGift(Boolean isGift) {
		this.isGift = isGift;
	}
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsTop() {
		return isTop;
	}
	public void setIsTop(Boolean isTop) {
		this.isTop = isTop;
	}
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	public Boolean getIsShow() {
		return isShow;
	}
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@Lob
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Field(store=Store.YES, index=Index.NO)
	@Length(max=200)
	@Column
	public String getShowImg() {
		return showImg;
	}
	public void setShowImg(String showImg) {
		this.showImg = showImg;
	}
	@Field(store = Store.YES, index = Index.NO)
	@Min(0L)
	@Column
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	@Field(store=Store.YES, index=Index.NO)
	@Column
	public Integer getLockStock() {
		return lockStock;
	}
	public void setLockStock(Integer lockStock) {
		this.lockStock = lockStock;
	}
	@Field(store = Store.YES, index = Index.NO)
	@Length(max = 200)
	@Column
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NumericField
	@FieldBridge(impl = BigDecimalNumericFieldBridge.class)
	@NotNull
	@Min(0L)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 18, scale = 4)
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
	@Min(0L)
	@Digits(integer = 12, fraction = 3)
	@Column(precision=18, scale=4)
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	@Min(0L)
	@Digits(integer = 12, fraction = 3)
	@Column(precision=18, scale=4)
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@Column(nullable = false)
	public Integer getSalesCount() {
		return salesCount;
	}
	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
	}
	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public Integer getWeekSalesCount() {
		return weekSalesCount;
	}
	public void setWeekSalesCount(Integer weekSalesCount) {
		this.weekSalesCount = weekSalesCount;
	}
	@Field(store = Store.YES, index = Index.NO)
	@Column(nullable = false)
	public Integer getMonthSalesCount() {
		return monthSalesCount;
	}
	public void setMonthSalesCount(Integer monthSalesCount) {
		this.monthSalesCount = monthSalesCount;
	}
	@Column
	public Date getWeekSalesDate() {
		return weekSalesDate;
	}
	public void setWeekSalesDate(Date weekSalesDate) {
		this.weekSalesDate = weekSalesDate;
	}
	@Column
	public Date getMonthSalesDate() {
		return monthSalesDate;
	}
	public void setMonthSalesDate(Date monthSalesDate) {
		this.monthSalesDate = monthSalesDate;
	}
	@Field(store=Store.YES, index=Index.NO)
	  @Min(0L)
	@Column
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	@Field(store = Store.YES, index = Index.NO)
	@Column
	public String getCargoSpace() {
		return cargoSpace;
	}
	public void setCargoSpace(String cargoSpace) {
		this.cargoSpace = cargoSpace;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="goodsId",nullable=false, updatable=false)
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	@Length(max=225)
	public String getProperty0() {
		return property0;
	}
	public void setProperty0(String property0) {
		this.property0 = property0;
	}
	@Length(max=225)
	public String getProperty1() {
		return property1;
	}
	public void setProperty1(String property1) {
		this.property1 = property1;
	}
	@Length(max=225)
	public String getProperty2() {
		return property2;
	}
	public void setProperty2(String property2) {
		this.property2 = property2;
	}
	@Length(max=225)
	public String getProperty3() {
		return property3;
	}
	public void setProperty3(String property3) {
		this.property3 = property3;
	}
	@Length(max=225)
	public String getProperty4() {
		return property4;
	}
	public void setProperty4(String property4) {
		this.property4 = property4;
	}
	@Length(max=225)
	public String getProperty5() {
		return property5;
	}
	public void setProperty5(String property5) {
		this.property5 = property5;
	}
	@Length(max=225)
	public String getProperty6() {
		return property6;
	}
	public void setProperty6(String property6) {
		this.property6 = property6;
	}
	@Length(max=225)
	public String getProperty7() {
		return property7;
	}
	public void setProperty7(String property7) {
		this.property7 = property7;
	}
	@Length(max=225)
	public String getProperty8() {
		return property8;
	}
	public void setProperty8(String property8) {
		this.property8 = property8;
	}
	@Length(max=225)
	public String getProperty9() {
		return property9;
	}
	public void setProperty9(String property9) {
		this.property9 = property9;
	}
	@Length(max=225)
	public String getProperty10() {
		return property10;
	}
	public void setProperty10(String property10) {
		this.property10 = property10;
	}
	@Length(max=225)
	public String getProperty11() {
		return property11;
	}
	public void setProperty11(String property11) {
		this.property11 = property11;
	}
	@Length(max=225)
	public String getProperty12() {
		return property12;
	}
	public void setProperty12(String property12) {
		this.property12 = property12;
	}
	@Length(max=225)
	public String getProperty13() {
		return property13;
	}
	public void setProperty13(String property13) {
		this.property13 = property13;
	}
	@Length(max=225)
	public String getProperty14() {
		return property14;
	}
	public void setProperty14(String property14) {
		this.property14 = property14;
	}
	@Length(max=225)
	public String getProperty15() {
		return property15;
	}
	public void setProperty15(String property15) {
		this.property15 = property15;
	}
	@Length(max=225)
	public String getProperty16() {
		return property16;
	}
	public void setProperty16(String property16) {
		this.property16 = property16;
	}
	@Length(max=225)
	public String getProperty17() {
		return property17;
	}
	public void setProperty17(String property17) {
		this.property17 = property17;
	}
	@Length(max=225)
	public String getProperty18() {
		return property18;
	}
	public void setProperty18(String property18) {
		this.property18 = property18;
	}
	@Length(max=225)
	public String getProperty19() {
		return property19;
	}
	public void setProperty19(String property19) {
		this.property19 = property19;
	}
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="product_spec", joinColumns={@JoinColumn(name="productId")}, inverseJoinColumns={@JoinColumn(name="specId")})  
	@OrderBy("orderList asc")
	@JsonIgnore
	public Set<Spec> getSpecs() {
		return specs;
	}
	public void setSpecs(Set<Spec> specs) {
		this.specs = specs;
	}

	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name="product_attribute",joinColumns={@JoinColumn(name="productId")})
	@MapKeyClass(SpecAttribute.class)
	@Column(name="value")
	public Map<SpecAttribute, String> getSpecAttributes() {
		return specAttributes;
	}
	public void setSpecAttributes(Map<SpecAttribute, String> specAttributes) {
		this.specAttributes = specAttributes;
	}
	
	@Valid
	@ElementCollection(targetClass=ProductImage.class,fetch=FetchType.LAZY)
	@CollectionTable(name="product_image",joinColumns={@JoinColumn(name="productId")})
	public List<ProductImage> getProductImages() {
		return productImages;
	}
	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name="product_parameter",joinColumns={@JoinColumn(name="productId")})
	@MapKeyClass(Integer.class)
	@Column(name="value")
	public Map<Integer, String> getParameterItems() {
		return parameterItems;
	}
	public void setParameterItems(Map<Integer, String> parameterItems) {
		this.parameterItems = parameterItems;
	}
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name="product_member_price",joinColumns={@JoinColumn(name="productId")})
	@MapKeyClass(Grade.class)
	@Column(name="price")
	public Map<Grade, BigDecimal> getMemberPrice() {
		return memberPrice;
	}
	public void setMemberPrice(Map<Grade, BigDecimal> memberPrice) {
		this.memberPrice = memberPrice;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="product_tag", joinColumns={@JoinColumn(name="productId")}, inverseJoinColumns={@JoinColumn(name="tagId")})  
	@OrderBy("orderList asc")
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	@ManyToMany(mappedBy="products", fetch=FetchType.LAZY)
	public Set<Promotion> getPromotions() {
		return promotions;
	}
	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}
	@OneToMany(mappedBy="product", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.REMOVE})
	public Set<CartItem> getCartItems() {
		return cartItems;
	}
	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}
	@OneToMany(mappedBy="product", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.ALL})
	public Set<GiftItem> getGiftItems() {
		return giftItems;
	}
	public void setGiftItems(Set<GiftItem> giftItems) {
		this.giftItems = giftItems;
	}
	@ManyToMany(mappedBy="favoriteProducts", fetch=FetchType.LAZY)
	public Set<Member> getMembers() {
		return members;
	}
	public void setMembers(Set<Member> members) {
		this.members = members;
	}
	@OneToMany(mappedBy="product", fetch=FetchType.LAZY)	
	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	//获取商品下在进行中的促销活动
	@Transient
	public Set<Promotion> getActivePromotions() {
		Set<Promotion> promotionSet = new HashSet<Promotion>();
		if (getPromotions() != null) {
			promotionSet.addAll(getPromotions());
		}
		if (getProductCategory() != null && getProductCategory().getPromotions() != null) {
			promotionSet.addAll(getProductCategory().getPromotions());
		}
		if (getBrand() != null && getBrand().getPromotions() != null) {
			promotionSet.addAll(getBrand().getPromotions());
		}
		TreeSet<Promotion> treeSet = new TreeSet<Promotion>();
		Iterator<Promotion> iterator = promotionSet.iterator();
		while (iterator.hasNext()) {
			Promotion promotion = iterator.next();
			if (promotion != null && promotion.hasBegun() && !promotion.hasEnded()) {
				treeSet.add(promotion);
			}
		}
		return treeSet;
	}
	
	
	//计算可用库存
	@Transient
	public Integer getAvailableStock() {
		Integer totalStock = 0;
		Integer lockStock = 0;
		if(this.getStock() != null)
			totalStock = this.getStock();
		if(this.getLockStock() != null)
			lockStock = this.getLockStock();
		return (totalStock - lockStock)>=0?(totalStock - lockStock):0;
	}
	
	//判断是否缺少库存
	@Transient
	public Boolean getIsOutOfStock() {
		if ((getStock() != null) && (getLockStock() != null)
				&& (getLockStock().intValue() >= getStock().intValue())) {
			return Boolean.valueOf(true);
		}
		return Boolean.valueOf(false);
	}
	
	//获取商品属于值
	@Transient
	public String getPropertyValue(Property property) {
		if ((property != null) && (property.getPropertyIndex() != null)){
			try {
				String str = "property" + property.getPropertyIndex();
				return (String) PropertyUtils.getProperty(this, str);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	//设置商品属性值
	@Transient
	public void setPropertyValue(Property property, String value) {
		if ((property != null) && (property.getPropertyIndex() != null)) {
			if (StringUtils.isEmpty(value))
				value = null;
			try {
				String str = "property" + property.getPropertyIndex();
				PropertyUtils.setProperty(this, str, value);
			} catch (IllegalAccessException localIllegalAccessException1) {
				localIllegalAccessException1.printStackTrace();
			} catch (NoSuchMethodException localNoSuchMethodException1) {
				localNoSuchMethodException1.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	//获取兄弟商品
	@Transient
	public List<Product> getSiblings() {
		List<Product> list = new ArrayList<Product>();
		if ((getGoods() != null) && (getGoods().getProducts() != null)) {
			Iterator<Product> itor = getGoods().getProducts().iterator();
			while (itor.hasNext()) {
				Product product = (Product) itor.next();
				if (!equals(product))
					list.add(product);
			}
		}
		return list;
	}
	
	@PrePersist
	public void prePersist() {
		if (getStock() == null) {
			setLockStock(Integer.valueOf(0));
		}
		setScore(0);
	}

	@PreUpdate
	public void preUpdate() {
		if (getStock() == null) {
			setLockStock(Integer.valueOf(0));
		}
	}
}
