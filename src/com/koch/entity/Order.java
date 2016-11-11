package com.koch.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.koch.bean.Setting;
import com.koch.util.SettingUtils;

@Entity
@Table(name = "orders")
@JsonFilter("orders")
public class Order extends BaseEntity {
	private static final long serialVersionUID = 1055234149396472724L;

	// 订单状态（未处理、已处理、已完成、已作废）
	public enum OrderStatus {
		unprocessed, processed, completed, invalid
	};

	// 付款状态（未支付、部分支付、已支付、部分退款、全额退款）
	public enum PaymentStatus {
		unpaid, partPaid, paid, partRefund, refunded
	};

	// 配送状态（未发货、部分发货、已发货、部分退货、已退货）
	public enum DeliverStatus {
		unDeliver, partDeliver, deliver, partReturns, returns
	};

	private String number; // 订单号
	private Member member; // 用户
	private Admin operator; //操作员
	private OrderStatus orderStatus;// 订单状态
	private PaymentStatus payStatus; // 支付状态
	private DeliverStatus deliverStatus; // 配送状态
	private BigDecimal depositAmount;// 存款支付金额
	private BigDecimal paidAmount; // 已支付金额
	private BigDecimal promAmount; // 促销额度
	private BigDecimal voucherAmount; // 抵用券额度
	private BigDecimal deliveryAmount; // 运费
	private CouponInfo couponInfo; // 抵用券
	private String promotion;//促销方式
	private BigDecimal adjustLimit; // 调整额度
	private BigDecimal serviceAmount; // 手续费
	private Integer score; // 赠送积分,
	private Paymentway paymentway; // 支付方式ID
	private String paymentwayName;
	private Deliverway deliverway; // 配送方式
	private String deliverwayName;
	private Boolean isInvoice; // 是否开发票'
	private String invoiceTitle; // 发票抬头
	private Boolean isCalculateStock;//是否计算库存 
	private String receiver; // 收货人
	private Area area; // 地区
	private String areaName;
	private String address; // 详细地址
	private String zipCode; // 邮编
	private String phone; // 手机号
	private String remark; // 附言
	private Date expire;//过期时间
	private Date lockExpire;//锁定过期时间
	private Boolean isExpired;
	private Boolean isLockExpired;
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();//订单商品信息
	private Set<PaymentInfo> paymentInfos = new HashSet<PaymentInfo>();//支付信息
	private Set<DeliverInfo> deliverInfos = new HashSet<DeliverInfo>();//配送信息
	private Set<Returns> returns = new HashSet<Returns>();//退货信息
	private Set<Refunds> refunds = new HashSet<Refunds>();//退款信息
	private Set<OrderLog> logs = new HashSet<OrderLog>();//订单日志
	private Set<DepositInfo> depositInfos = new HashSet<DepositInfo>();//订单消息记录
	private List<Coupon> coupons = new ArrayList<Coupon>();//促销赠送的优惠券
	
	private String orderStatusName;
	private String payStatusName;
	private String deliverStatusName;

	@Column
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId", nullable = false)
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler","fieldHandler" })
	public Member getMember() {
		return member;
	}
	
	public void setMember(Member member) {
		this.member = member;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "operatorId", nullable = false)
	public Admin getOperator() {
		return operator;
	}

	public void setOperator(Admin operator) {
		this.operator = operator;
	}

	@Enumerated
	@Column
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Enumerated
	@Column
	public PaymentStatus getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PaymentStatus payStatus) {
		this.payStatus = payStatus;
	}

	@Enumerated
	@Column
	public DeliverStatus getDeliverStatus() {
		return deliverStatus;
	}

	public void setDeliverStatus(DeliverStatus deliverStatus) {
		this.deliverStatus = deliverStatus;
	}

	@Column
	public BigDecimal getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}

	@Column
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	@Column
	public BigDecimal getPromAmount() {
		return promAmount;
	}

	public void setPromAmount(BigDecimal promAmount) {
		this.promAmount = promAmount;
	}

	@Column
	public BigDecimal getVoucherAmount() {
		return voucherAmount;
	}

	public void setVoucherAmount(BigDecimal voucherAmount) {
		this.voucherAmount = voucherAmount;
	}

	@Column
	public BigDecimal getDeliveryAmount() {
		return deliveryAmount;
	}

	public void setDeliveryAmount(BigDecimal deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}
	
	@OneToOne(mappedBy="order",fetch=FetchType.LAZY)
	public CouponInfo getCouponInfo() {
		return couponInfo;
	}

	public void setCouponInfo(CouponInfo couponInfo) {
		this.couponInfo = couponInfo;
	}
	
	@Column
	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	@Column
	public BigDecimal getAdjustLimit() {
		return adjustLimit;
	}

	public void setAdjustLimit(BigDecimal adjustLimit) {
		this.adjustLimit = adjustLimit;
	}

	@Column
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Column
	public BigDecimal getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paymentwayId")
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler","fieldHandler" })
	public Paymentway getPaymentway() {
		return paymentway;
	}

	public void setPaymentway(Paymentway paymentway) {
		this.paymentway = paymentway;
	}

	@Column
	public String getPaymentwayName() {
		return paymentwayName;
	}

	public void setPaymentwayName(String paymentwayName) {
		this.paymentwayName = paymentwayName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deliverwayId")
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler","fieldHandler" })
	public Deliverway getDeliverway() {
		return deliverway;
	}

	public void setDeliverway(Deliverway deliverway) {
		this.deliverway = deliverway;
	}
	
	@Column
	public String getDeliverwayName() {
		return deliverwayName;
	}

	public void setDeliverwayName(String deliverwayName) {
		this.deliverwayName = deliverwayName;
	}

	@Column
	public Boolean getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(Boolean isInvoice) {
		this.isInvoice = isInvoice;
	}

	@Column
	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}
	
	@Column
	public Boolean getIsCalculateStock() {
		return isCalculateStock;
	}

	public void setIsCalculateStock(Boolean isCalculateStock) {
		this.isCalculateStock = isCalculateStock;
	}

	@Column
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "areaId")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@Column
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Column
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@OrderBy("createDate desc")
	public Set<PaymentInfo> getPaymentInfos() {
		return paymentInfos;
	}

	public void setPaymentInfos(Set<PaymentInfo> paymentInfos) {
		this.paymentInfos = paymentInfos;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@OrderBy("createDate desc")
	public Set<DeliverInfo> getDeliverInfos() {
		return deliverInfos;
	}

	public void setDeliverInfos(Set<DeliverInfo> deliverInfos) {
		this.deliverInfos = deliverInfos;
	}
	
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@OrderBy("createDate desc")
	public Set<Returns> getReturns() {
		return returns;
	}

	public void setReturns(Set<Returns> returns) {
		this.returns = returns;
	}
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@OrderBy("createDate desc")
	public Set<Refunds> getRefunds() {
		return refunds;
	}

	public void setRefunds(Set<Refunds> refunds) {
		this.refunds = refunds;
	}
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	@OrderBy("createDate desc")
	public Set<OrderLog> getLogs() {
		return logs;
	}
	public void setLogs(Set<OrderLog> logs) {
		this.logs = logs;
	}
	@OneToMany(mappedBy="order", fetch=FetchType.LAZY)	
	public Set<DepositInfo> getDepositInfos() {
		return depositInfos;
	}

	public void setDepositInfos(Set<DepositInfo> depositInfos) {
		this.depositInfos = depositInfos;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="order_coupon",joinColumns={@JoinColumn(name="orderId")}, inverseJoinColumns={@JoinColumn(name="couponId")})
	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Column(name="expire",nullable = false, updatable = false)
	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}
	@Column(name="lockExpire",nullable = false, updatable = false)
	public Date getLockExpire() {
		return lockExpire;
	}

	public void setLockExpire(Date lockExpire) {
		this.lockExpire = lockExpire;
	}
	
	//判断订单是否过期
	@Transient
	public Boolean getIsExpired() {
		return (getExpire() != null) && (new Date().after(getExpire()));
	}
	
	//判断订单是否锁定过期
	@Transient
	public Boolean getIsLockExpired(Admin operator) {
		return (getLockExpire() != null) && (new Date().before(getLockExpire()))  && (getOperator() != operator);
	}
	
	//计算商品总金额
	@Transient
	public BigDecimal getProductAmount() {
		BigDecimal amounts = new BigDecimal(0);
		if (getOrderItems() != null) {
			Iterator<OrderItem> itor = getOrderItems().iterator();
			while (itor.hasNext()) {
				OrderItem orderItem = (OrderItem) itor.next();
				if ((orderItem != null) && (orderItem.getTotalAmount() != null))
					amounts = amounts.add(orderItem.getTotalAmount());
			}
		}
		return amounts;
	}
	
	//计算订单总金额
	@Transient
	public BigDecimal getTotalAmount() {
		BigDecimal amounts = getProductAmount();
		amounts = amounts.add(getDeliveryAmount()==null?new BigDecimal(0):getDeliveryAmount());//加运费
		//amounts = amounts.subtract(getDepositAmount()==null?new BigDecimal(0):getDepositAmount());//减帐户支付金额
		amounts = amounts.subtract(getPromAmount()==null?new BigDecimal(0):getPromAmount());//减促销金额
		amounts = amounts.subtract(getVoucherAmount()==null?new BigDecimal(0):getVoucherAmount());//减优惠券
		amounts = amounts.add(getAdjustLimit()==null?new BigDecimal(0):getAdjustLimit());//加手工调整金额
		amounts = amounts.subtract(getServiceAmount()==null?new BigDecimal(0):getServiceAmount());//减去手续费
		return amounts;
	}
	
	//计算需要支付金额(总金额-已支付金额)
	@Transient
	public BigDecimal getAmountPayable() {
		BigDecimal amount = getTotalAmount().subtract(getPaidAmount());
		return amount.compareTo(new BigDecimal(0)) > 0 ? amount : new BigDecimal(0);
	}

	//计算商品总重量
	@Transient
	public BigDecimal getWeight() {
		BigDecimal weights = new BigDecimal(0);
		if (getOrderItems() != null) {
			Iterator<OrderItem> itor = getOrderItems().iterator();
			while (itor.hasNext()) {
				OrderItem orderItem = (OrderItem) itor.next();
				if ((orderItem != null) && (orderItem.getProductWeight() != null))
					weights = weights.add(orderItem.getTotalWeight());
			}
		}
		return weights;
	}

	//计算商品总数量
	@Transient
	public Integer getQuantity() {
		Integer i = 0;
		if (getOrderItems() != null) {
			Iterator<OrderItem> itor = getOrderItems().iterator();
			while (itor.hasNext()) {
				OrderItem orderItem = (OrderItem) itor.next();
				if ((orderItem != null) && (orderItem.getProductQuantity() != null))
					i += orderItem.getProductQuantity();
			}
		}
		return i;
	}

	//计算商品总配送数量
	@Transient
	public Integer getTotalDeliverQuantity() {
		Integer i = 0;
		if (getOrderItems() != null) {
			Iterator<OrderItem> itor = getOrderItems().iterator();
			while (itor.hasNext()) {
				OrderItem orderItem = (OrderItem) itor.next();
				if (orderItem != null && orderItem.getDeliveryQuantity() != null)
					i += orderItem.getDeliveryQuantity();
			}
		}
		return i;
	}
	
	//计算商品总退货数量
	@Transient
	public int getTotalReturnQuantity() {
		int i = 0;
		if (getOrderItems() != null) {
			Iterator<OrderItem> itor = getOrderItems().iterator();
			while (itor.hasNext()) {
				OrderItem orderItem = itor.next();
				if (orderItem == null || orderItem.getReturnQuantity() == null)
					continue;
				i += orderItem.getReturnQuantity().intValue();
			}
		}
		return i;
	}
	
	//根据商品编号获取订单项
	@Transient
	public OrderItem getOrderItem(String number) {
		if (number != null && getOrderItems() != null) {
			Iterator<OrderItem> iterator = getOrderItems().iterator();
			while (iterator.hasNext()) {
				OrderItem orderItem = (OrderItem) iterator.next();
				if (orderItem != null && number.equalsIgnoreCase(orderItem.getProductNumber()))
					return orderItem;
			}
		}
		return null;
	}
	
	//计算手续费
	@Transient
	public BigDecimal calculateServiceAmount() {
		Setting setting = SettingUtils.get();
		BigDecimal amount = new BigDecimal(0);
		if (setting.getIsTaxPriceEnabled()) {
			amount = getProductAmount().subtract(getPromAmount()).subtract(getVoucherAmount()).multiply(new BigDecimal(setting.getTaxRate().toString()));
		} else {
			amount = new BigDecimal(0);
		}
		return setting.setScale(amount);
	}
	
	@Transient
	public String getOrderStatusName() {
		return orderStatusName;
	}

	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}

	@Transient
	public String getPayStatusName() {
		return payStatusName;
	}

	public void setPayStatusName(String payStatusName) {
		this.payStatusName = payStatusName;
	}

	@Transient
	public String getDeliverStatusName() {
		return deliverStatusName;
	}

	public void setDeliverStatusName(String deliverStatusName) {
		this.deliverStatusName = deliverStatusName;
	}

	
	@PrePersist
	public void prePersist() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
		if (getPaymentway() != null) {
			setPaymentwayName(getPaymentway().getName());
		}
		if (getDeliverway() != null) {
			setDeliverwayName(getDeliverway().getName());
		}
	}

	@PreUpdate
	public void preUpdate() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
		if (getPaymentway() != null) {
			setPaymentwayName(getPaymentway().getName());
		}
		if (getDeliverway() != null) {
			setDeliverwayName(getDeliverway().getName());
		}
	}
	
	@PreRemove
	public void preRemove() {
		Set<DepositInfo> set = getDepositInfos();
		if (set != null) {
			Iterator<DepositInfo> iterator = set.iterator();
			while (iterator.hasNext()) {
				DepositInfo depositInfo = iterator.next();
				depositInfo.setOrder(null);
			}
		}
	}
}
