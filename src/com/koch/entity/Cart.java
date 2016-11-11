package com.koch.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.time.DateUtils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.koch.bean.Setting;
import com.koch.util.SettingUtils;


@Entity
@Table(name="cart")
@JsonFilter("cart")
public class Cart extends BaseEntity{ 
	private static final long serialVersionUID = 8654372509455570366L;
	public static final int TIMEOUT = 604800;
	public static final Integer MAX_PRODUCT_COUNT = 100;
	public static final String ID_COOKIE_NAME = "cart_id";
	public static final String KEY_COOKIE_NAME = "cart_key";
	
	private String cartKey;
	private Member member;
	private Boolean isReload;
	
	private Set<CartItem> cartItems = new HashSet<CartItem>();
	
	@Column(nullable=false, updatable=false)
	public String getCartKey() {
		return cartKey;
	}
	public void setCartKey(String cartKey) {
		this.cartKey = cartKey;
	}
	
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="memberId",nullable=false)
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	
	@OneToMany(mappedBy="cart", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	public Set<CartItem> getCartItems() {
		return cartItems;
	}
	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}
	
	@Column
	public Boolean getIsReload() {
		return isReload;
	}
	public void setIsReload(Boolean isReload) {
		this.isReload = isReload;
	}
	//计算商品总积分
	@Transient
	public int getScore() {
		Integer score = 0;
		if (getCartItems() != null) {
			Iterator<CartItem> iterator = getCartItems().iterator();
			while (iterator.hasNext()) {
				CartItem cartItem = iterator.next();
				if (cartItem == null)
					continue;
				score += cartItem.getScore();
			}
		}
		//促销时存在积分增加
		Iterator<Promotion> iterator = getPromotions().iterator();
		while (iterator.hasNext()) {
			Promotion promotion = iterator.next();
			score = promotion.calculateScore(score, getDiscountAfterAmount(), getQuantity());
		}
		return score;
	}

	//计算商品总重量
	@Transient
	public BigDecimal getWeight() {
		BigDecimal w = new BigDecimal(0);
		if (getCartItems() != null) {
			Iterator<CartItem> iterator = getCartItems().iterator();
			while (iterator.hasNext()) {
				CartItem cartItem = iterator.next();
				
				if (cartItem == null)
					continue;
				BigDecimal weight = cartItem.getWeight();
				w = w.add(weight);
			}
		}
		return w;
	}

	//计算商品总数量
	@Transient
	public int getQuantity() {
		int i = 0;
		if (getCartItems() != null) {
			Iterator<CartItem> iterator = getCartItems().iterator();
			while (iterator.hasNext()) {
				CartItem cartItem = (CartItem) iterator.next();
				if ((cartItem == null) || (cartItem.getQuantity() == null))
					continue;
				i += cartItem.getQuantity().intValue();
			}
		}
		return i;
	}

	//计算总金额
	@Transient
	public BigDecimal getTotalPrice() {
		BigDecimal price = new BigDecimal(0);
		if (getCartItems() != null) {
			Iterator<CartItem> iterator = getCartItems().iterator();
			while (iterator.hasNext()) {
				CartItem cartItem = iterator.next();
				if ((cartItem == null) || (cartItem.getSubtotal() == null))
					continue;
				price = price.add(cartItem.getSubtotal());
			}
		}
		return price;
	}

	//计算促销折扣后的金额
	@Transient
	public BigDecimal getDiscountAfterAmount() {
		Setting setting = SettingUtils.get();
		BigDecimal totalPrice = getTotalPrice();
		//促销后的金额
		Iterator<Promotion> iterator = getPromotions().iterator();
		while (iterator.hasNext()) {
			Promotion promotion = iterator.next();
			totalPrice = promotion.calculatePrice(totalPrice, getQuantity());
		}
		return setting.setScale(totalPrice);
	}

	//计算折扣金额(总金额减去折扣后的金额)
	@Transient
	public BigDecimal getDiscount() {
		BigDecimal price = getTotalPrice().subtract(getDiscountAfterAmount());
		return price.compareTo(new BigDecimal(0)) > 0 ? price : new BigDecimal(0);
	}

	//验证优惠券是否可用
	@Transient
	public boolean isValidCoupon(Coupon coupon) {
		if (coupon == null || !coupon.getIsEnabled() || !coupon.hasBegun() || coupon.hasExpired()) {
			return false;
		}
		return (coupon.getMinPrice() == null || coupon.getMinPrice().compareTo(getDiscountAfterAmount()) <= 0)
				&& (coupon.getMaxPrice() == null || coupon.getMaxPrice().compareTo(getDiscountAfterAmount()) >= 0);
	}
	
	//根据商品获取购物车项
	@Transient
	public CartItem getCartItem(Product product) {
		if ((product != null) && (getCartItems() != null)) {
			Iterator<CartItem> iterator = getCartItems().iterator();
			while (iterator.hasNext()) {
				CartItem cartItem = iterator.next();
				if (cartItem != null && cartItem.getProduct() == product) {
					return cartItem;
				}
			}
		}
		return null;
	}
	
	//判断商品存在于购物车内
	@Transient
	public boolean contains(Product product) {
		if ((product != null) && (getCartItems() != null)) {
			Iterator<CartItem> iterator = getCartItems().iterator();
			while (iterator.hasNext()) {
				CartItem cartItem = (CartItem) iterator.next();
				if ((cartItem != null)
						&& (cartItem.getProduct() == product)) {
					return true;
				}
			}
		}
		return false;
	}
	
	//获取令牌
	@Transient
	public String getToken() {
		HashCodeBuilder builder = new HashCodeBuilder(17, 37)
				.append(getCartKey());
		if (getCartItems() != null) {
			Iterator<CartItem> iterator = getCartItems().iterator();
			while (iterator.hasNext()) {
				CartItem cartItem = (CartItem) iterator.next();
				builder.append(cartItem.getProduct()).append(
						cartItem.getQuantity()).append(cartItem.getUnitPrice());
			}
		}
		return DigestUtils.md5Hex(builder.toString());
	}
	
	//判断购物车类是否存在库存不足的商品
	@Transient
	public boolean getIsLowStock() {
		if (getCartItems() != null) {
			Iterator<CartItem> iterator = getCartItems().iterator();
			while (iterator.hasNext()) {
				CartItem cartItem = iterator.next();
				if ((cartItem != null) && (cartItem.getIsLowStock())) {
					return true;
				}
			}
		}
		return false;
	}
	
	//判断是否可期
	@Transient
	public boolean hasExpired() {
		return new Date().after(DateUtils.addSeconds(getModifyDate(), TIMEOUT));
	}
	
	//获取所有促销活动
	@Transient
	public Set<Promotion> getPromotions() {
		Set<Promotion> promotionSet = new HashSet<Promotion>();
		if (getCartItems() != null) {
			Iterator<CartItem> iterator = getCartItems().iterator();
			while (iterator.hasNext()) {
				CartItem cartItem = iterator.next();
				if ((cartItem != null) && (cartItem.getProduct() != null)) {
					promotionSet.addAll(cartItem.getProduct().getActivePromotions());//获取商品正在进行中的促销活动
				}
			}
		}
		Set<Promotion> treeSet = new TreeSet<Promotion>();
		Iterator<Promotion> iterator = promotionSet.iterator();
		while (iterator.hasNext()) {
			Promotion promotion = iterator.next();
			if (isReachPromotionCondition(promotion)) {
				treeSet.add(promotion);
			}
		}
		return treeSet;
	}
	
	//会员当前购物车内的价格、数量是否达到促销活动条件
	@Transient
	private boolean isReachPromotionCondition(Promotion promotion) {
		if (promotion == null || !promotion.hasBegun() || promotion.hasEnded()) {
			return false;
		}
		if (promotion.getGrades() == null || getMember() == null || getMember().getGrade() == null || !promotion.getGrades().contains(getMember().getGrade())) {
			return false;
		}
		BigDecimal price = new BigDecimal(0);
		if (getCartItems() != null) {
			Iterator<CartItem> iterator = getCartItems().iterator();
			while (iterator.hasNext()) {
				CartItem cartItem = iterator.next();
				if (cartItem != null) {
					Product product = cartItem.getProduct();
					if (product != null) {
						if (product.getPromotions() != null && product.getPromotions().contains(promotion)) {
							price = price.add(cartItem.getSubtotal());
						} else if (product.getProductCategory() != null && product.getProductCategory().getPromotions().contains(promotion)) {
							price = price.add(cartItem.getSubtotal());
						} else if (product.getBrand() != null && product.getBrand().getPromotions().contains(promotion)) {
							price = price.add(cartItem.getSubtotal());
						}
					}
				}
			}
		}
		return (promotion.getMinPrice() == null || promotion.getMinPrice().compareTo(price) <= 0) && (promotion.getMaxPrice() == null || promotion.getMaxPrice().compareTo(price) >= 0);
	}
	
	//获取促销活动赠送的赠品
	@Transient
	public Set<GiftItem> getGiftItems() {
		Set<GiftItem> giftItemSet = new HashSet<GiftItem>();
		Iterator<Promotion> iterator = getPromotions().iterator();
		while (iterator.hasNext()) {
			Promotion promotion = iterator.next();
			if (promotion.getGiftItems() != null) {
				Iterator<GiftItem> giftItems = promotion.getGiftItems().iterator();
				while (giftItems.hasNext()) {
					GiftItem giftItem1 = giftItems.next();
					GiftItem giftItem2 = (GiftItem) CollectionUtils.find(giftItemSet, new CartPredicate(this, giftItem1));
					if (giftItem2 != null) {
						giftItem2.setQuantity(giftItem2.getQuantity() + giftItem1.getQuantity());
					} else {
						giftItemSet.add(giftItem1);
					}
				}
			}
		}
		return giftItemSet;
	}

	//判断是否可以使用优惠券
	@Transient
	public boolean isCouponAllowed() {
		Iterator<Promotion> iterator = getPromotions().iterator();
		while (iterator.hasNext()) {
			Promotion promotion = iterator.next();
			if (promotion != null && !promotion.getIsAllowedCoupon()) {
				return false;
			}
		}
		return true;
	}
	
	
	//判断购物车是否有商品
	@Transient
	public boolean isEmpty() {
		return (getCartItems() == null) || (getCartItems().isEmpty());
	}
	
	class CartPredicate implements Predicate {
		private GiftItem giftItem;
		private Cart cart;

		public CartPredicate(Cart cart, GiftItem giftItem) {
			this.cart = cart;
			this.giftItem = giftItem;
		}

		public boolean evaluate(Object object) {
			GiftItem item = (GiftItem) object;
			return item != null && item.getProduct().equals(this.giftItem.getProduct());
		}
	}
}
