package com.koch.dao.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.koch.bean.Filter;
import com.koch.bean.OrderBy;
import com.koch.bean.Pager;
import com.koch.dao.OrderDao;
import com.koch.entity.Member;
import com.koch.entity.Order;
import com.koch.entity.OrderItem;
import com.koch.entity.Product;

@Repository("orderDao")
public class OrderDaoImpl extends BaseDaoImpl<Order> implements OrderDao{
	
	public Order findBySn(String number) {
		if (number == null) {
			return null;
		}
		String str = "select orders from Order orders where lower(orders.number) = lower(:number)";
		try {
			return this.entityManager.createQuery(str, Order.class).setFlushMode(FlushModeType.COMMIT).setParameter("number",number).getSingleResult();
		} catch (NoResultException e) {
		}
		return null;
	}
	  
	public List<Order> findList(Member member, Integer count, List<Filter> filters, List<OrderBy> orders) {
		if (member == null) {
			return Collections.emptyList();
		}
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		return super.findList(criteriaQuery, null, count, filters, orders);
	}
	  
	public Pager<Order> findPage(Member member, Pager<Order> pager) {
		if (member == null) {
			return new Pager<Order>();
		}
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		return super.findByPager(criteriaQuery, pager);
	}
	  
	public Pager<Order> findPage(Order.OrderStatus orderStatus,
			Order.PaymentStatus payStatus, Order.DeliverStatus deliverStatus,
			Boolean hasExpired, Pager<Order> pager) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate predicate = criteriaBuilder.conjunction();
		if (orderStatus != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
		}
		if (payStatus != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("payStatus"), payStatus));
		}
		if (deliverStatus != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("deliverStatus"), deliverStatus));
		}
		if (hasExpired != null) {
			if (hasExpired.booleanValue()) {
				predicate = criteriaBuilder.and(new Predicate[] {predicate,root.get("expire").isNotNull(),criteriaBuilder.lessThan(root.get("expire"), new Date()) });
			} else {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("expire"),new Date())));
			}
		}
		criteriaQuery.where(predicate);
		return super.findByPager(criteriaQuery, pager);
	}
	  
	public Long count(Order.OrderStatus orderStatus,
			Order.PaymentStatus payStatus, Order.DeliverStatus deliverStatus,
			Boolean hasExpired) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate predicate = criteriaBuilder.conjunction();
		if (orderStatus != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
		}
		if (payStatus != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("payStatus"), payStatus));
		}
		if (deliverStatus != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("deliverStatus"), deliverStatus));
		}
		if (hasExpired != null) {
			if (hasExpired.booleanValue()) {
				predicate = criteriaBuilder.and(new Predicate[] {predicate,root.get("expire").isNotNull(),criteriaBuilder.lessThan(root.get("expire"), new Date()) });
			} else {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("expire"),new Date())));
			}
		}
		criteriaQuery.where(predicate);
		return super.count(criteriaQuery, null);
	}
	  
	public Long waitingPaymentCount(Member member) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate predicate = criteriaBuilder.conjunction();
		predicate = criteriaBuilder.and(new Predicate[] {predicate,
				criteriaBuilder.notEqual(root.get("orderStatus"),Order.OrderStatus.completed),
				criteriaBuilder.notEqual(root.get("orderStatus"),Order.OrderStatus.invalid) });
		predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(
				criteriaBuilder.equal(root.get("paymentStatus"),
						Order.PaymentStatus.unpaid), criteriaBuilder.equal(root.get("payStatus"), Order.PaymentStatus.partPaid)));
		predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(root.get(
				"expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("expire"), new Date())));
		if (member != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("member"), member));
		}
		criteriaQuery.where(predicate);
		return super.count(criteriaQuery, null);
	}
	  
	public Long waitingShippingCount(Member member) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate predicate = criteriaBuilder.conjunction();
		predicate = criteriaBuilder.and(new Predicate[] {
				predicate,criteriaBuilder.notEqual(root.get("orderStatus"),Order.OrderStatus.completed),
				criteriaBuilder.notEqual(root.get("orderStatus"),Order.OrderStatus.invalid),
				criteriaBuilder.equal(root.get("payStatus"),Order.PaymentStatus.paid),
				criteriaBuilder.equal(root.get("deliverStatus"),Order.DeliverStatus.unDeliver) });
		predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.get("expire"), new Date())));
		if (member != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("member"), member));
		}
		criteriaQuery.where(predicate);
		return super.count(criteriaQuery, null);
	}
	  
	public BigDecimal getSalesAmount(Date beginDate, Date endDate) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(BigDecimal.class);
		Root root = criteriaQuery.from(Order.class);
		criteriaQuery.select(criteriaBuilder.sum(root.get("paidAmount")));
		Predicate predicate = criteriaBuilder.conjunction();
		predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("orderStatus"), Order.OrderStatus.completed));
		if (beginDate != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), beginDate));
		}
		if (endDate != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), endDate));
		}
		criteriaQuery.where(predicate);
		return (BigDecimal) this.entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult();
	}
	  
	public Integer getSalesVolume(Date beginDate, Date endDate) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Integer.class);
		Root root = criteriaQuery.from(Order.class);
		criteriaQuery.select(criteriaBuilder.sum(root.join("orderItems").get("deliveryQuantity")));
		Predicate predicate = criteriaBuilder.conjunction();
		predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("orderStatus"), Order.OrderStatus.completed));
		if (beginDate != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), beginDate));
		}
		if (endDate != null) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), endDate));
		}
		criteriaQuery.where(predicate);
		return (Integer) this.entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getSingleResult();
	}
	  
	public void releaseStock() {
		String str = "select orders from Order orders where orders.isCalculateStock = :isCalculateStock and orders.expire is not null and orders.expire <= :now";
		List<Order> list = this.entityManager.createQuery(str, Order.class).setParameter("isCalculateStock", Boolean.valueOf(true)).setParameter("now", new Date()).getResultList();
		if (list != null) {
			Iterator<Order> itor = list.iterator();
			while (itor.hasNext()) {
				Order order = itor.next();
				if (order != null && order.getOrderItems() != null) {
					Iterator<OrderItem> itors = order.getOrderItems().iterator();
					while (itors.hasNext()) {
						OrderItem orderItem = itors.next();
						if (orderItem != null) {
							Product product = orderItem.getProduct();
							if (product != null) {
								this.entityManager.lock(product,LockModeType.PESSIMISTIC_WRITE);
								product.setLockStock(product.getLockStock().intValue() - (orderItem.getProductQuantity().intValue() - orderItem.getDeliveryQuantity().intValue()));
							}
						}
					}
					order.setIsCalculateStock(false);
				}
			}
		}
	}
}
