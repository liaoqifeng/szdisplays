package com.koch.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.koch.util.SpringUtil;

@Entity
@Table(name = "refunds")
@JsonFilter("refunds")
public class Refunds extends BaseEntity{
	private static final long serialVersionUID = -3372782222988170671L;

	public enum RefundsType {
		online, offline, deposit;
	};
	private String number;
	private Member member;
	private Order order;
	private RefundsType type;
	private String typeName;
	private Paymentway paymentway;
	private String paymentwayName;
	private String bank;
	private String account;
	private BigDecimal paidAmount;
	private String payee;
	private String remark;
	private String operator;
	
	@Column
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId", nullable = false)
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderId", nullable = false)
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	@Enumerated
	@Column
	public RefundsType getType() {
		return type;
	}
	public void setType(RefundsType type) {
		this.type = type;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paymentwayId", nullable = false)
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
	@Column
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	@Column
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	@Column
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	@Column
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	@Transient
	public String getTypeName() {
		if(this.getType() != null)
			return SpringUtil.getMessage("RefundsType."+this.getType());
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@PrePersist
	public void prePersist() {
		if(getPaymentway() != null){
			setPaymentwayName(getPaymentway().getName());
		}
	}
}
