package com.koch.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.koch.util.SpringUtil;

@Entity
@Table(name = "payment_info")
@JsonFilter("paymentInfo")
public class PaymentInfo extends BaseEntity{
	private static final long serialVersionUID = -531833340073098802L;
	public enum PaymentInfoStatus {
		success, wait, failure;
	};
	public enum PaymentInfoType {
		online, offline, deposit;
	};
	
	private String number;
	private Member member;
	private Order order;
	private PaymentInfoStatus status;
	private String statusName;
	private PaymentInfoType type;
	private String typeName;
	private Paymentway paymentway;
	private String paymentwayName;
	private String bank;
	private String account;
	private BigDecimal paidAmount;
	private String payer;
	private String remark;
	private String operator;
	private DepositInfo depositInfo;
	
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
	public PaymentInfoStatus getStatus() {
		return status;
	}
	public void setStatus(PaymentInfoStatus status) {
		this.status = status;
	}
	@Enumerated
	@Column
	public PaymentInfoType getType() {
		return type;
	}
	public void setType(PaymentInfoType type) {
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
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
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
	@OneToOne(mappedBy="paymentInfo", fetch=FetchType.LAZY)	
	public DepositInfo getDepositInfo() {
		return depositInfo;
	}
	public void setDepositInfo(DepositInfo depositInfo) {
		this.depositInfo = depositInfo;
	}
	@Transient
	public String getStatusName() {
		if(this.getStatus() != null){
			return SpringUtil.getMessage("PaymentInfoStatus."+this.getStatus());
		}
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	@Transient
	public String getTypeName() {
		if(this.getType() != null){
			return SpringUtil.getMessage("PaymentInfoType."+this.getType());
		}
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
