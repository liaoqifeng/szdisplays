package com.koch.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.bean.Setting;
import com.koch.bean.Setting.StockAllocationTime;
import com.koch.dao.CartDao;
import com.koch.dao.CouponInfoDao;
import com.koch.dao.DeliverInfoDao;
import com.koch.dao.DepositInfoDao;
import com.koch.dao.GradeDao;
import com.koch.dao.MemberDao;
import com.koch.dao.OrderDao;
import com.koch.dao.OrderItemDao;
import com.koch.dao.OrderLogDao;
import com.koch.dao.PaymentInfoDao;
import com.koch.dao.ProductDao;
import com.koch.dao.RefundsDao;
import com.koch.dao.ReturnsDao;
import com.koch.dao.SerialNumberDao;
import com.koch.entity.Admin;
import com.koch.entity.Cart;
import com.koch.entity.CartItem;
import com.koch.entity.Coupon;
import com.koch.entity.CouponInfo;
import com.koch.entity.DeliverInfo;
import com.koch.entity.DeliverInfoItem;
import com.koch.entity.Deliverway;
import com.koch.entity.DepositInfo;
import com.koch.entity.GiftItem;
import com.koch.entity.Grade;
import com.koch.entity.Member;
import com.koch.entity.Order;
import com.koch.entity.OrderItem;
import com.koch.entity.OrderLog;
import com.koch.entity.PaymentInfo;
import com.koch.entity.Paymentway;
import com.koch.entity.Product;
import com.koch.entity.Promotion;
import com.koch.entity.Receiver;
import com.koch.entity.Refunds;
import com.koch.entity.Returns;
import com.koch.entity.ReturnsItem;
import com.koch.entity.DepositInfo.DepositType;
import com.koch.entity.Order.DeliverStatus;
import com.koch.entity.Order.OrderStatus;
import com.koch.entity.Order.PaymentStatus;
import com.koch.entity.OrderLog.OrderLogType;
import com.koch.entity.PaymentInfo.PaymentInfoType;
import com.koch.entity.Paymentway.PaymentwayType;
import com.koch.entity.Refunds.RefundsType;
import com.koch.entity.SerialNumber.Type;
import com.koch.service.OrderService;
import com.koch.util.SettingUtils;

@Service
public class OrderServiceImpl extends BaseServiceImpl<Order> implements OrderService{
	@Resource
	private OrderDao orderDao;
	@Resource
	private OrderItemDao orderItemDao;
	@Resource
	private OrderLogDao orderLogDao;
	@Resource
	private PaymentInfoDao paymentInfoDao;
	@Resource
	private ProductDao productDao;
	@Resource
	private MemberDao memberDao;
	@Resource
	private DeliverInfoDao deliverInfoDao;
	@Resource
	private GradeDao gradeDao;
	@Resource
	private ReturnsDao returnsDao;
	@Resource
	private DepositInfoDao depositInfoDao;
	@Resource
	private RefundsDao refundsDao;
	@Resource
	private CouponInfoDao couponInfoDao;
	@Resource
	private SerialNumberDao serialNumberDao;
	@Resource
	private CartDao cartDao;
	
	@Resource
	public void setBaseDao(OrderDao orderDao) {
		super.setBaseDao(orderDao);
	}
	
	@Transactional
	public Order update(Order t,Admin operator) {
		Order order = orderDao.get(t.getId());
		
		if(order.getIsCalculateStock()){
			//计算库存前需要先把之前锁定的库存数据还原,然后再重新锁定库存
			Iterator<OrderItem> iterator = order.getOrderItems().iterator();
			while(iterator.hasNext()){
				OrderItem item = iterator.next();
				if(item != null){
					Product product = item.getProduct();
					if(product == null) continue;
					this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if(product != null && product.getLockStock() != null){
						product.setLockStock(product.getLockStock() - (item.getProductQuantity() - item.getDeliveryQuantity()));
						this.productDao.update(product);
						this.productDao.flush();
						this.productDao.build(product);
					}	
				}
			}
			
			iterator = t.getOrderItems().iterator();
			while(iterator.hasNext()){
				OrderItem item = iterator.next();
				if(item != null){
					Product product = item.getProduct();
					if(product == null) continue;
					this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if(product != null && product.getLockStock() != null){
						product.setLockStock(product.getLockStock() + (item.getProductQuantity() - item.getDeliveryQuantity()));
						this.productDao.update(product);
						this.productDao.flush();
						this.productDao.build(product);
					}	
				}
			}
		}
		
		OrderLog orderLog = new OrderLog();
	    orderLog.setType(OrderLogType.modify);
	    orderLog.setOperator(operator != null ? operator.getUsername() : null);
	    orderLog.setOrderNumber(order.getNumber());
	    orderLog.setOrder(order);
	    this.orderLogDao.save(orderLog);
	    
		return super.update(t);
	}

	/**
	 * 构建订单业务
	 * 1.通过购物车数据转换成订单数据
	 * 2.通过收货信息填充订单数据
	 * 3.通过配送方式计算订单运费(无配送方式则运费为0,订单是否搞促销免运费)
	 * 4.如果有抵用券,则转换抵用券数据
	 * 5.通过购物车常规商品数据生成订单项数据
	 * 6.判断是否使用发票
	 * 7.判断是否使用帐户余额支付(如果使用了帐户余额支付,则要改变订单状态,分为全部支付和部分支付)
	 * 8.判断使用哪种支付方式,如果支付方式不为空并且支付方式下的过期时间不为空,则要设置订单的过期时间
	 */
	@Transactional
	public Order build(Cart cart, Receiver receiver, Paymentway paymentway, Deliverway deliverway, CouponInfo couponInfo, boolean isInvoice, String invoiceTitle, boolean useDeposit, String remark){
		Assert.notNull(cart);
	    Assert.notNull(cart.getMember());
	    Assert.notEmpty(cart.getCartItems());
	    
	    Order order = new Order();
	    order.setDeliverStatus(DeliverStatus.unDeliver);
	    order.setDepositAmount(new BigDecimal(0));//设置帐户支付金额
	    order.setVoucherAmount(new BigDecimal(0));
	    order.setServiceAmount(new BigDecimal(0));
	    order.setAdjustLimit(new BigDecimal(0));
	    order.setPaidAmount(new BigDecimal(0));
	    order.setPromAmount(new BigDecimal(0));
	    order.setDeliveryAmount(new BigDecimal(0));
	    order.setScore(cart.getScore());
	    order.setRemark(remark);
	    order.setMember(cart.getMember());
	    if (receiver != null) {
	    	order.setReceiver(receiver.getReceiver());
	    	order.setAreaName(receiver.getAreaName());
	    	order.setAddress(receiver.getAddress());
	    	order.setZipCode(receiver.getZipCode());
	    	order.setPhone(receiver.getPhone());
	    	order.setArea(receiver.getArea());
	    }
	    
	    //加上促销信息
		if (!cart.getPromotions().isEmpty()) {
			StringBuffer promotionStr = new StringBuffer();
			Iterator<Promotion> iterator = cart.getPromotions().iterator();
			while (iterator.hasNext()) {
				Promotion promotion = iterator.next();
				if (promotion != null && promotion.getName() != null) {
					promotionStr.append(" " + promotion.getName());
				}
			}
			if (promotionStr.length() > 0) {
				promotionStr.deleteCharAt(0);
			}
			order.setPromotion(promotionStr.toString());
		}
	    
	    order.setPaymentway(paymentway);
		if (deliverway != null && paymentway != null && paymentway.getDeliverways().contains(deliverway)) {
			BigDecimal freight = deliverway.calculateFreight(cart.getWeight());

			// 如果促销时免运费,则运费为0
			Iterator<Promotion> promotions = cart.getPromotions().iterator();
			while (promotions.hasNext()) {
				Promotion promotion = promotions.next();
				if (!promotion.getIsFreeDeliver())
					continue;
				freight = new BigDecimal(0);
				break;
			}
			order.setDeliveryAmount(freight);
			order.setDeliverway(deliverway);
		} else {
			order.setDeliveryAmount(new BigDecimal(0));
		}
		
		order.setPromAmount(cart.getDiscount());//促销折扣金额
		
		if (couponInfo != null && cart.isCouponAllowed()) {
			this.couponInfoDao.lock(couponInfo, LockModeType.PESSIMISTIC_READ);
			if (!couponInfo.getIsUsed() && couponInfo.getCoupon() != null && cart.isValidCoupon(couponInfo.getCoupon())) {
				BigDecimal amount = couponInfo.getCoupon().calculatePrice(cart.getDiscountAfterAmount(), cart.getQuantity());
				amount = cart.getDiscountAfterAmount().subtract(amount);
				if (amount.compareTo(new BigDecimal(0)) > 0) {
					order.setVoucherAmount(amount);// 优惠券折扣金额
				}
				order.setCouponInfo(couponInfo);
			}
		}
		
		//生成正常订单商品
		List<OrderItem> orderItems = order.getOrderItems();
		Iterator<CartItem> cartItems = cart.getCartItems().iterator();
		while (cartItems.hasNext()) {
			CartItem cartItem = cartItems.next();
			if (cartItem != null && cartItem.getProduct() != null) {
				Product product = cartItem.getProduct();
				OrderItem orderItem = new OrderItem();
				orderItem.setProductNumber(product.getNumber());
				orderItem.setProductName(product.getName());
				orderItem.setProductFullName(product.getFullName());
				orderItem.setProductPrice(cartItem.getUnitPrice());
				orderItem.setProductWeight(product.getWeight());
				orderItem.setProductImage(product.getShowImg());
				orderItem.setIsGift(false);
				orderItem.setProductQuantity(cartItem.getQuantity());
				orderItem.setDeliveryQuantity(0);
				orderItem.setReturnQuantity(0);
				orderItem.setProduct(product);
				orderItem.setOrder(order);
				orderItems.add(orderItem);
			}
		}
		
		//生成赠送的订单商品
		Iterator<GiftItem> giftItems = cart.getGiftItems().iterator();
		while (giftItems.hasNext()) {
			GiftItem giftItem = giftItems.next();
			if (giftItem != null && giftItem.getProduct() != null) {
				Product product = giftItem.getProduct();
				OrderItem orderItem = new OrderItem();
				orderItem.setProductNumber(product.getNumber());
				orderItem.setProductName(product.getName());
				orderItem.setProductFullName(product.getFullName());
				orderItem.setProductPrice(new BigDecimal(0));
				orderItem.setProductWeight(product.getWeight());
				orderItem.setProductImage(product.getShowImg());
				orderItem.setIsGift(false);
				orderItem.setProductQuantity(giftItem.getQuantity());
				orderItem.setDeliveryQuantity(0);
				orderItem.setReturnQuantity(0);
				orderItem.setProduct(product);
				orderItem.setOrder(order);
				orderItems.add(orderItem);
			}
		}
		
		//处理发票数据
		Setting setting = SettingUtils.get();
		if (setting.getIsInvoiceEnabled() && isInvoice && StringUtils.isNotEmpty(invoiceTitle)) {
			order.setIsInvoice(true);
			order.setInvoiceTitle(invoiceTitle);
			order.setServiceAmount(order.calculateServiceAmount());
		} else {
			order.setIsInvoice(false);
			order.setServiceAmount(new BigDecimal(0));
		}
		//会员用预存款支付业务处理
		if (useDeposit) {
			Member member = cart.getMember();
			if (member.getDeposit().compareTo(order.getTotalAmount()) >= 0) {
				order.setPaidAmount(order.getTotalAmount());
				order.setDepositAmount(order.getTotalAmount());
			} else {
				order.setPaidAmount(member.getDeposit());
				order.setDepositAmount(member.getDeposit());
			}
		} else {
			order.setPaidAmount(new BigDecimal(0));
			order.setDepositAmount(new BigDecimal(0));
		}
		//判断订单状态和支付状态
		if (order.getAmountPayable().compareTo(new BigDecimal(0)) == 0) {
			order.setOrderStatus(OrderStatus.processed);
			order.setPayStatus(PaymentStatus.paid);
		} else if (order.getAmountPayable().compareTo(new BigDecimal(0)) > 0 && order.getPaidAmount().compareTo(new BigDecimal(0)) > 0) {
			order.setOrderStatus(OrderStatus.processed);
			order.setPayStatus(PaymentStatus.partPaid);
		} else {
			order.setOrderStatus(Order.OrderStatus.unprocessed);
			order.setPayStatus(PaymentStatus.unpaid);
		}
		//判断订单支付方式是否存在超时业务
		if (paymentway != null && paymentway.getTimeout() != null && order.getPayStatus() == PaymentStatus.unpaid) {
			order.setExpire(DateUtils.addMinutes(new Date(), paymentway.getTimeout().intValue()));
		}
	    return order;
	}
	/**
	 * 创建订单业务
	 * 1.构建订单
	 * 2.生成订单号
	 * 3.如果是在线支付,设置支付锁定过期时间
	 * 4.如果使用抵用券,则需要变更抵用券的状态为已使用
	 * 5.如果存在促销商品,则给订单加上促销信息
	 * 6.如果使用了帐户支付,生成帐户支付明细
	 * 7.判断是否需要计算库存(下单时计算、支付时计算并且已经支付一定或者全部金额时),如果确定计算库存,则要给商品的锁定库存加上订单总数量减去已经发货的数量
	 */
	@Transactional
	public Order create(Cart cart, Receiver receiver, Paymentway paymentway, Deliverway deliverway, CouponInfo couponInfo, boolean isInvoice, String invoiceTitle, boolean useDeposit, String remark, Admin operator){
		Assert.notNull(cart);
	    Assert.notNull(cart.getMember());
	    Assert.notEmpty(cart.getCartItems());
	    Assert.notNull(receiver);
	    Assert.notNull(paymentway);
	    Assert.notNull(deliverway);
	    
	    Order order = build(cart, receiver, paymentway, deliverway, couponInfo, isInvoice, invoiceTitle, useDeposit, remark);
	    order.setNumber(this.serialNumberDao.generate(Type.order));
	    if(paymentway.getType() == PaymentwayType.online){
	    	order.setLockExpire(DateUtils.addSeconds(new Date(), 10));
	    	order.setOperator(operator);
	    }
	    //如果使用了优惠券,则要把优惠券状态改成已使用
	    if(order.getCouponInfo() != null){
	    	couponInfo.setIsUsed(true);
	    	couponInfo.setUsedDate(new Date());
	    	couponInfo.setOrder(order);
	    	couponInfo.setMember(order.getMember());
	    	this.couponInfoDao.update(couponInfo);
	    }
	    //如果促销活动有赠送优惠券,则要给订单加上优惠券信息,到订单完成时则把优惠券赠送给会员
		Iterator<Promotion> promotions = cart.getPromotions().iterator();
		while (promotions.hasNext()) {
			Promotion promotion = promotions.next();
			Iterator<Coupon> coupons = promotion.getCoupons().iterator();
			while (coupons.hasNext()) {
				Coupon coupon = coupons.next();
				order.getCoupons().add(coupon);
			}
		}
		Setting setting = SettingUtils.get();
		//是否计算库存业务
	    if (setting.getStockAllocationTime() == Setting.StockAllocationTime.order || 
	    		(setting.getStockAllocationTime() == Setting.StockAllocationTime.payment && 
	    				(order.getPayStatus() == Order.PaymentStatus.partPaid || order.getPayStatus() == Order.PaymentStatus.paid))) {
	    	order.setIsCalculateStock(true);
	    } else {
	    	order.setIsCalculateStock(false);
	    }
		this.orderDao.save(order);
		
		OrderLog orderLog = new OrderLog();
	    orderLog.setType(OrderLogType.create);
	    orderLog.setOperator(operator != null ? operator.getUsername() : null);
	    orderLog.setOrderNumber(order.getNumber());
	    orderLog.setOrder(order);
	    this.orderLogDao.save(orderLog);
		
		//如果创建订单时已经存在支付金额,则要减去会员的预存款,并生成消费记录
		Member member = cart.getMember();
		if (order.getPaidAmount().compareTo(new BigDecimal(0)) > 0) {
			this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
			member.setDeposit(member.getDeposit().subtract(order.getPaidAmount()));
			this.memberDao.update(member);

			DepositInfo depositInfo = new DepositInfo();
			depositInfo.setType(operator != null ? DepositType.adminPayment : DepositType.memberPayment);
			depositInfo.setCredit(new BigDecimal(0));
			depositInfo.setDebit(order.getPaidAmount());
			depositInfo.setDeposit(member.getDeposit());
			depositInfo.setOperator(operator != null ? operator.getUsername() : null);
			depositInfo.setMember(member);
			depositInfo.setOrder(order);
			this.depositInfoDao.save(depositInfo);
		}
		
		//如果是订单创建时或者支付时并且已经支付金额,则要给商品加上锁定库存
		if (setting.getStockAllocationTime() == Setting.StockAllocationTime.order || 
	    		(setting.getStockAllocationTime() == Setting.StockAllocationTime.payment && 
	    				(order.getPayStatus() == Order.PaymentStatus.partPaid || order.getPayStatus() == Order.PaymentStatus.paid))) {
	      Iterator<OrderItem> iterator = order.getOrderItems().iterator();
			while (iterator.hasNext()) {
				OrderItem orderItem = iterator.next();
				if (orderItem != null) {
					Product product = orderItem.getProduct();
					this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if ((product != null) && (product.getStock() != null)) {
						product.setLockStock(product.getLockStock() + (orderItem.getProductQuantity() - orderItem.getDeliveryQuantity()));
						this.productDao.update(product);
						this.productDao.flush();
						this.productDao.build(product);
					}
				}
			}
	    }
		this.cartDao.delete(cart);//创建后清除购物车
		return order;
	}

	/**
	 * 订单确认业务
	 * 1.变更订单状态为已处理
	 */
	@Transactional
	public void process(Order order, Admin operator){
	    Assert.notNull(order);
	    order.setOrderStatus(OrderStatus.processed);
	    this.update(order);
	    OrderLog orderLog = new OrderLog();
	    orderLog.setType(OrderLogType.processed);
	    orderLog.setOperator(operator != null ? operator.getUsername() : null);
	    orderLog.setOrderNumber(order.getNumber());
	    orderLog.setOrder(order);
	    this.orderLogDao.save(orderLog);
	}

	/**
	 * 订单支付业务
	 * 1.如果有存款支付,支付时要减去用户的存款
	 * 2.判断是否需要计算库存,如果是在支付时计算库存,但是在下单时没有任何的支付金额 ,则需要 计算库存(锁定对应商品的库存 )
	 * 3.设置订单已支付金额为当前支付金额
	 * 4.改变订单支付状态,分为部分支付和全部支付
	 */
	@Transactional
	public void payment(Order order,PaymentInfo paymentInfo,Admin operator) {
		Assert.notNull(order);
		Assert.notNull(paymentInfo);
		this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
		this.paymentInfoDao.save(paymentInfo);
		
		//如果是存款支付,支付时要减去用户的存款
		if(paymentInfo.getType() == PaymentInfoType.deposit){
			Member member = order.getMember();
			this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
			member.setDeposit(member.getDeposit().subtract(paymentInfo.getPaidAmount()));
			this.memberDao.update(member);
			
			DepositInfo depositInfo= new DepositInfo();
			depositInfo.setType(operator != null ? DepositType.adminPayment : DepositType.memberPayment);
			depositInfo.setCredit(new BigDecimal(0));
			depositInfo.setDebit(paymentInfo.getPaidAmount());
			depositInfo.setDeposit(member.getDeposit());
			depositInfo.setOperator(operator != null ? operator.getUsername() : null);
			depositInfo.setMember(member);
			depositInfo.setOrder(order);
		    this.depositInfoDao.save(depositInfo);
		}
		
		//判断是否需要计算库存,如果是在支付时计算库存,但是在下单时没有任何的支付金额 ,则需要 计算库存(锁定对应商品的库存 )
		Setting setting = SettingUtils.get();
		if(!order.getIsCalculateStock() && setting.getStockAllocationTime() == Setting.StockAllocationTime.payment){
			Iterator<OrderItem> iterator = order.getOrderItems().iterator();
			while(iterator.hasNext()){
				OrderItem item = iterator.next();
				if(item != null){
					Product product = item.getProduct();
					this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if(product != null && product.getLockStock() != null){
						product.setLockStock(product.getLockStock() + item.getProductQuantity() - item.getDeliveryQuantity());
						this.productDao.update(product);
						this.productDao.flush();
						this.productDao.build(product);
					}	
				}
			}
			order.setIsCalculateStock(true);
		}
		
		order.setPaidAmount(order.getPaidAmount().add(paymentInfo.getPaidAmount()));
	    order.setExpire(null);
	    
	    if (order.getPaidAmount().compareTo(order.getTotalAmount()) >= 0){//全部支付
	    	order.setOrderStatus(OrderStatus.processed);
	    	order.setPayStatus(PaymentStatus.paid);
	    } else if (order.getPaidAmount().compareTo(new BigDecimal(0)) > 0){//部分支付
	    	order.setOrderStatus(OrderStatus.processed);
	    	order.setPayStatus(PaymentStatus.partPaid);
	    }
	    this.orderDao.update(order);
	    
	    OrderLog orderLog = new OrderLog();
	    orderLog.setType(OrderLogType.payment);
	    orderLog.setOperator(operator != null ? operator.getUsername() : null);
	    orderLog.setOrderNumber(order.getNumber());
	    orderLog.setOrder(order);
	    this.orderLogDao.save(orderLog);
	}
	
	/**
	 * 订单发货业务
	 * 1.如果是发货时计算库存,则进行库存分配
	 * 2.减去对应商品的库存数量
	 * 3.释放锁定过的库存数量
	 * 4.订单明细配送数量加上当次的配送数量
	 * 5.改变订单的配送状态(分为全部配送和部分配送)
	 */
	@Transactional
	public void deliver(Order order, DeliverInfo deliverInfo, Admin operator) {
		Assert.notNull(order);
		Assert.notNull(deliverInfo);
		this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
		
		Setting setting = SettingUtils.get();
		//计算库存业务
		if(!order.getIsCalculateStock() && setting.getStockAllocationTime() == StockAllocationTime.deliver){
			Iterator<OrderItem> iterator = order.getOrderItems().iterator();
			while(iterator.hasNext()){
				OrderItem item = iterator.next();
				if(item != null){
					Product product = item.getProduct();
					this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if(product != null && product.getLockStock() != null){
						product.setLockStock(product.getLockStock() + item.getProductQuantity() - item.getDeliveryQuantity());
						this.productDao.update(product);
						this.productDao.flush();
						this.productDao.build(product);
					}	
				}
			}
			order.setIsCalculateStock(true);
		}
		
		deliverInfo.setOrder(order);
		this.deliverInfoDao.save(deliverInfo);
		
		Iterator<DeliverInfoItem> iterator = deliverInfo.getDeliverInfoItems().iterator();
		while(iterator.hasNext()){
			DeliverInfoItem item = iterator.next();
			OrderItem orderItem = order.getOrderItem(item.getProductNumber());
			if(orderItem != null){
				Product product = orderItem.getProduct();
				this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
				if(product != null){
					if(product.getStock() != null){
						product.setStock(product.getStock().intValue()-item.getProductQuantity().intValue());
						//如果之前锁定过库存,则发货成功之后需要为商品之前锁定的数量进行解锁
						if(order.getIsCalculateStock()){
							product.setLockStock(product.getLockStock()-item.getProductQuantity().intValue());
						}
					}
					this.productDao.update(product);
					this.productDao.flush();
					this.productDao.build(product);
				}
			}
			
			this.orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
			//发货成功后,需要更新订单项的发货数量
			orderItem.setDeliveryQuantity(orderItem.getDeliveryQuantity().intValue() + item.getProductQuantity().intValue());
		}
		if (order.getTotalDeliverQuantity() >= order.getQuantity()){
			order.setDeliverStatus(DeliverStatus.deliver);
	    }else if (order.getTotalDeliverQuantity() > 0){
	    	order.setDeliverStatus(DeliverStatus.partDeliver);
	    }
	    order.setExpire(null);
	    this.orderDao.update(order);
		
		OrderLog orderLog = new OrderLog();
	    orderLog.setType(OrderLogType.deliver);
	    orderLog.setOperator(operator != null ? operator.getUsername() : null);
	    orderLog.setOrderNumber(order.getNumber());
	    orderLog.setOrder(order);
	    this.orderLogDao.save(orderLog);
	}
	
	/**
	 * 完成订单业务(注意:订单完成后才会给会员加上赠送积分或者优惠券)
	 * 1.如果配送状态是部分发货或者全部发货状态,则给用户加上积分
	 * 2.如果配送状态是部分发货或者全部发货状态并且在促销活动赠送的抵用券,则给用户生成抵用券
	 * 3.如果配送状态是未发货或者退货状态并且之前用过抵用券,则退还抵用券
	 * 4.给用户总消费金额加上本次订单已支付金额,并且如果消费金额达到升级状态,则给用户升级
	 * 5.计算库存,商品要进行最后库存释放,需要释放掉还没有发货的数量
	 * 6.计算商品的销售数量
	 */
	@Transactional
	public void complete(Order order, Admin operator) {
		Assert.notNull(order);
		Member member = order.getMember();
		this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
		
		if(order.getDeliverStatus() == DeliverStatus.partDeliver || order.getDeliverStatus() == DeliverStatus.deliver){
			member.setScore(member.getScore() + order.getScore());
			
			//订单完成后,如果存 在促销赠送的优惠券,则给会员加上
			Iterator<Coupon> iterator = order.getCoupons().iterator();
			while (iterator.hasNext()) {
				Coupon Coupon = iterator.next();
				this.couponInfoDao.build(Coupon, member);
			}
		}
		//会员是否升级业务
		member.setAmount(member.getAmount().add(order.getPaidAmount()));
		Grade grade = gradeDao.findByAmount(member.getAmount());
		if(grade != null && grade.getExpValue().compareTo(member.getGrade().getExpValue()) > 0){
			member.setGrade(grade);
		}
		this.memberDao.update(member);
		
		if(!order.getIsCalculateStock()){//计算库存逻辑
			Iterator<OrderItem> iterator = order.getOrderItems().iterator();
			while(iterator.hasNext()){
				OrderItem item = iterator.next();
				if(item != null){
					Product product = item.getProduct();
					this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
					if(product != null && product.getLockStock() != null){
						product.setLockStock(product.getLockStock() - (item.getProductQuantity() - item.getDeliveryQuantity()));
						this.productDao.update(product);
						this.productDao.flush();
						this.productDao.build(product);
					}	
				}
			}
			order.setIsCalculateStock(false);
		}
		
		Iterator<OrderItem> orderItems = order.getOrderItems().iterator();
		while(orderItems.hasNext()){
			OrderItem item = orderItems.next();
			if(item == null) continue;
			Product product = item.getProduct();
			if(product == null) continue;
			Integer quantity = item.getProductQuantity();
		    Calendar calendar = Calendar.getInstance();
		    Calendar weekCalendar = DateUtils.toCalendar(product.getWeekSalesDate());
		    Calendar monthCalendar = DateUtils.toCalendar(product.getMonthSalesDate());
		    this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
		    if (calendar.get(Calendar.YEAR) != weekCalendar.get(Calendar.YEAR) || calendar.get(Calendar.WEEK_OF_YEAR) > weekCalendar.get(Calendar.WEEK_OF_YEAR))
		    	product.setWeekSalesCount(quantity);
		    else
		    	product.setWeekSalesCount(product.getWeekSalesCount() + quantity);
		    
		    if (calendar.get(Calendar.YEAR) != monthCalendar.get(Calendar.YEAR) || calendar.get(Calendar.MONTH) > weekCalendar.get(Calendar.MONTH))
		    	product.setMonthSalesCount(quantity);
		    else
		    	product.setMonthSalesCount(product.getMonthSalesCount() + quantity);
		    
		    product.setSalesCount(product.getSalesCount() + quantity);
		    product.setWeekSalesDate(new Date());
		    product.setMonthSalesDate(new Date());
		    this.productDao.update(product);
			this.productDao.flush();
			this.productDao.build(product);
		    
		}
		
		order.setOrderStatus(OrderStatus.completed);
		order.setExpire(null);
		this.orderDao.update(order);
		
		OrderLog orderLog = new OrderLog();
	    orderLog.setType(OrderLogType.complete);
	    orderLog.setOperator(operator != null ? operator.getUsername() : null);
	    orderLog.setOrderNumber(order.getNumber());
	    orderLog.setOrder(order);
	    this.orderLogDao.save(orderLog);
	}

	/**
	 * 作废订单
	 * 1.如果用到抵用券支付,则要释放抵用券状态
	 * 2.释放计算库存时锁定的库存,解锁还未发货的库存
	 * 3.更新订单状态
	 */
	@Transactional
	public void invalid(Order order, Admin operator) {
		Assert.notNull(order);
		
		CouponInfo couponInfo = order.getCouponInfo();
		if (couponInfo != null) {
			couponInfo.setIsUsed(false);
			couponInfo.setUsedDate(null);
			this.couponInfoDao.update(couponInfo);
			order.setCouponInfo(null);
			this.orderDao.update(order);
		}
		
		if (order.getIsCalculateStock()) {
			Iterator<OrderItem> orderItems = order.getOrderItems().iterator();
			while (orderItems.hasNext()) {
				OrderItem orderItem = orderItems.next();
				if (orderItem == null)
					continue;
				Product product = orderItem.getProduct();
				this.productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
				if (product == null || product.getStock() != null)
					continue;
				product.setLockStock(product.getLockStock() - (orderItem.getProductQuantity() - orderItem.getDeliveryQuantity()));
				this.productDao.update(product);
				this.productDao.flush();
				this.productDao.build(product);
			}
			order.setIsCalculateStock(false);
		}
		
	    this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
	    order.setOrderStatus(Order.OrderStatus.invalid);
	    order.setExpire(null);
	    this.orderDao.update(order);
		
	    OrderLog orderLog = new OrderLog();
	    orderLog.setType(OrderLogType.invalid);
	    orderLog.setOperator(operator != null ? operator.getUsername() : null);
	    orderLog.setOrderNumber(order.getNumber());
	    orderLog.setOrder(order);
	    this.orderLogDao.save(orderLog);
	}

	/**
	 * 订单退款业务
	 * 1.如果是退款到预存款帐户,则给用户加上退款
	 * 2.变更订单支付状态,为分部分退款和全部退款
	 */
	@Transactional
	public void refunds(Order order,Refunds refunds, Admin operator) {
		Assert.notNull(order);
	    Assert.notNull(refunds);
	    this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
	    refunds.setOrder(order);
	    this.refundsDao.save(refunds);
	    if (refunds.getType() == RefundsType.deposit)
	    {
	      Member member = order.getMember();
	      this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
	      member.setDeposit(member.getDeposit().add(refunds.getPaidAmount()));
	      this.memberDao.update(member);
	      
	      DepositInfo info = new DepositInfo();
	      info.setType(DepositType.adminRefunds);
	      info.setCredit(refunds.getPaidAmount());
	      info.setDebit(new BigDecimal(0));
	      info.setDeposit(member.getDeposit());
	      info.setOperator(operator != null ? operator.getUsername() : null);
	      info.setMember(member);
	      info.setOrder(order);
	      this.depositInfoDao.save(info);
	    }
	    
	    order.setPaidAmount(order.getPaidAmount().subtract(refunds.getPaidAmount()));
	    order.setExpire(null);
	    if (order.getPaidAmount().compareTo(new BigDecimal(0)) == 0)
	      order.setPayStatus(PaymentStatus.refunded);
	    else if (order.getPaidAmount().compareTo(new BigDecimal(0)) > 0)
	      order.setPayStatus(PaymentStatus.partRefund);
	    this.orderDao.update(order);
	    
	    OrderLog orderLog = new OrderLog();
	    orderLog.setType(OrderLogType.refunds);
	    orderLog.setOperator(operator != null ? operator.getUsername() : null);
	    orderLog.setOrderNumber(order.getNumber());
	    orderLog.setOrder(order);
	    this.orderLogDao.save(orderLog);
		
	}

	/**
	 * 订单退货业务
	 * 1.更新订单项的退货数量
	 * 2.更新订单发货状态,分为部分退货和全部退货
	 */
	@Transactional
	public void returns(Order order,Returns returns, Admin operator) {
		Assert.notNull(order);
	    Assert.notNull(returns);
	    Assert.notEmpty(returns.getReturnsItems());
	    
	    this.orderDao.lock(order, LockModeType.PESSIMISTIC_WRITE);
	    returns.setOrder(order);
	    this.returnsDao.save(returns);
	    
	    Iterator<ReturnsItem> returnsItems = returns.getReturnsItems().iterator();
		while (returnsItems.hasNext()) {
			ReturnsItem item = returnsItems.next();
			OrderItem orderItem = order.getOrderItem(item.getProductNumber());
			if (orderItem == null)
				continue;
			this.orderItemDao.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
			orderItem.setReturnQuantity(orderItem.getReturnQuantity() + item.getProductQuantity());
		}
	    
		if (order.getTotalReturnQuantity() >= order.getTotalDeliverQuantity())
			order.setDeliverStatus(DeliverStatus.returns);
		else if (order.getTotalReturnQuantity() > 0)
			order.setDeliverStatus(DeliverStatus.partReturns);
		order.setExpire(null);
		this.orderDao.update(order);
		    
		OrderLog orderLog = new OrderLog();
	    orderLog.setType(OrderLogType.returns);
	    orderLog.setOperator(operator != null ? operator.getUsername() : null);
	    orderLog.setOrderNumber(order.getNumber());
	    orderLog.setOrder(order);
	    this.orderLogDao.save(orderLog);
	}
	
	
	public Order findBySn(String number){
		return this.orderDao.findBySn(number);
	}
	
	public List<Order> findList(Member member, Integer count, List<Filter> filters, List<OrderBy> orders){
		return this.orderDao.findList(member, count, filters, orders);
	}
	
	public Pager<Order> findPage(Member member, Pager<Order> pager){
		return this.orderDao.findPage(member, pager);
	}
	
	public Pager<Order> findPage(Order.OrderStatus orderStatus,
			Order.PaymentStatus payStatus, Order.DeliverStatus deliverStatus,
			Boolean hasExpired, Pager<Order> pager){
		return this.orderDao.findPage(orderStatus, payStatus, deliverStatus, hasExpired, pager);
	}
	
	public Long count(Order.OrderStatus orderStatus,
			Order.PaymentStatus payStatus, Order.DeliverStatus deliverStatus,
			Boolean hasExpired){
		return this.orderDao.count(orderStatus, payStatus, deliverStatus, hasExpired);
	}
	
	public Long waitingPaymentCount(Member member){
		return this.orderDao.waitingPaymentCount(member);
	}
	
	public Long waitingShippingCount(Member member){
		return this.orderDao.waitingShippingCount(member);
	}
	
	public BigDecimal getSalesAmount(Date beginDate, Date endDate){
		return this.orderDao.getSalesAmount(beginDate, endDate);
	}
	
	public Integer getSalesVolume(Date beginDate, Date endDate){
		return this.getSalesVolume(beginDate, endDate);
	}
	
	public void releaseStock(){
		this.orderDao.releaseStock();
	}
}
