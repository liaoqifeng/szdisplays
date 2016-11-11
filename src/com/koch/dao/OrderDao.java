package com.koch.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.entity.Member;
import com.koch.entity.Order;

public interface OrderDao extends BaseDao<Order>{
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
