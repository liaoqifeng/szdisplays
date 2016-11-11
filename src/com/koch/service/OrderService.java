package com.koch.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.entity.Admin;
import com.koch.entity.Cart;
import com.koch.entity.CouponInfo;
import com.koch.entity.DeliverInfo;
import com.koch.entity.Deliverway;
import com.koch.entity.Member;
import com.koch.entity.Order;
import com.koch.entity.PaymentInfo;
import com.koch.entity.Paymentway;
import com.koch.entity.Receiver;
import com.koch.entity.Refunds;
import com.koch.entity.Returns;

public interface OrderService extends BaseService<Order>{
	public Order update(Order t,Admin operator);
	public Order build(Cart cart, Receiver receiver, Paymentway paymentway, Deliverway deliverway, CouponInfo couponInfo, boolean isInvoice, String invoiceTitle, boolean useDeposit, String remark);
	public Order create(Cart cart, Receiver receiver, Paymentway paymentway, Deliverway deliverway, CouponInfo couponInfo, boolean isInvoice, String invoiceTitle, boolean useDeposit, String remark, Admin operator);
	public void process(Order order, Admin operator);
	public void complete(Order order, Admin operator);
	public void invalid(Order order, Admin operator);
	public void payment(Order order,PaymentInfo paymentInfo, Admin operator);
	public void deliver(Order order,DeliverInfo deliverInfo,Admin operator);
	public void refunds(Order order,Refunds refunds, Admin operator);
	public void returns(Order order,Returns returns, Admin operator);
	
	public Order findBySn(String number);
	public List<Order> findList(Member member, Integer count, List<Filter> filters, List<OrderBy> orders);
	public Pager<Order> findPage(Member member, Pager<Order> pager);
	public Pager<Order> findPage(Order.OrderStatus orderStatus,
			Order.PaymentStatus payStatus, Order.DeliverStatus deliverStatus,
			Boolean hasExpired, Pager<Order> pager);
	public Long count(Order.OrderStatus orderStatus,
			Order.PaymentStatus payStatus, Order.DeliverStatus deliverStatus,
			Boolean hasExpired);
	public Long waitingPaymentCount(Member member);
	public Long waitingShippingCount(Member member);
	public BigDecimal getSalesAmount(Date beginDate, Date endDate);
	public Integer getSalesVolume(Date beginDate, Date endDate);
	public void releaseStock();
}
