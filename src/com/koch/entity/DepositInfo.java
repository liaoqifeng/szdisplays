package com.koch.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="deposit_info")
public class DepositInfo extends BaseEntity{ 
	private static final long serialVersionUID = 7683966936627156719L;
	
	//帐户流水(用户充值、管理员充值、用户支付、管理员支付、管理员扣款、管理员退款)
	public enum DepositType{
		memberRecharge, adminRecharge, memberPayment, adminPayment, adminChargeback,  adminRefunds;
	}
	
	private DepositType type;
	private Order order;
	private Member member;
	private Paymentway paymentway;
	private PaymentInfo paymentInfo;
	private BigDecimal credit;
	private BigDecimal debit;
	private BigDecimal deposit;
	private String operator;
	private String remark;
	
	@Enumerated
	@Column
	public DepositType getType() {
		return type;
	}
	public void setType(DepositType type) {
		this.type = type;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="orderId")
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="memberId")
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="paymentwayId")
	public Paymentway getPaymentway() {
		return paymentway;
	}
	public void setPaymentway(Paymentway paymentway) {
		this.paymentway = paymentway;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="paymentInfoId")
	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	@Column
	public BigDecimal getCredit() {
		return credit;
	}
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	@Column
	public BigDecimal getDebit() {
		return debit;
	}
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}
	@Column
	public BigDecimal getDeposit() {
		return deposit;
	}
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}
	@Column
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
