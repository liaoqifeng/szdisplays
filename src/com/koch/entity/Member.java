package com.koch.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.koch.util.SpringUtil;


@Entity
@Table(name="member")
@JsonFilter("member")
public class Member extends BaseEntity{
	private static final long serialVersionUID = 429890179632301964L;
	
	public enum Gender {
		male, female;
	};
	
	public enum MemberStatus {
		active, lock, disable;
	};
	
	private String username;
	private String password;
	private String realname;
	private Gender gender;
	private Date birthday;
	private String email;
	private Area area;
	private String address;
	private String zipCode;
	private Grade grade;
	private String phone;
	private String telephone;
	private Question question;
	private String answer;
	private String qq;
	private Integer score;
	private BigDecimal deposit;
	private BigDecimal amount;
	private Double exp;
	private String registerIp;
	private MemberStatus status;
	private Integer loginFailureCount;
	private Date lockedDate;
	private Cart cart;
	
    private Set<Order> orders = new HashSet<Order>();
    private Set<PaymentInfo> paymentInfos = new HashSet<PaymentInfo>();
    private Set<DepositInfo> depositInfos = new HashSet<DepositInfo>();
    private Set<CouponInfo> couponInfos = new HashSet<CouponInfo>();
    private Set<Product> favoriteProducts = new HashSet<Product>();
    private Set<Receiver> receivers = new HashSet<Receiver>();
	
    private String statusName;
    
	@Column
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	@Enumerated
	@Column
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	@Temporal(TemporalType.DATE)
	@Column
	@JsonIgnore
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	@Column
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="areaId")
	@JsonIgnore
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	@Column
	@JsonIgnore
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column
	@JsonIgnore
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="gradeId")
	@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	@Column
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="questionId")
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	@Column
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	@Column
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	@Column
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	@Column
	public BigDecimal getDeposit() {
		return deposit;
	}
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}
	@Column
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Column
	public Double getExp() {
		return exp;
	}
	public void setExp(Double exp) {
		this.exp = exp;
	}
	@Column
	public String getRegisterIp() {
		return registerIp;
	}
	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}
	@Enumerated
	@Column
	public MemberStatus getStatus() {
		return status;
	}
	public void setStatus(MemberStatus status) {
		this.status = status;
	}
	@Column
	public Integer getLoginFailureCount() {
		return loginFailureCount;
	}
	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}
	@Temporal(TemporalType.DATE)
	@Column
	public Date getLockedDate() {
		return lockedDate;
	}
	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}
	@OneToOne(mappedBy="member", fetch=FetchType.LAZY, cascade={javax.persistence.CascadeType.REMOVE})
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.REMOVE})
	@OrderBy("createDate desc")
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.REMOVE})
	@OrderBy("createDate desc")
	public Set<PaymentInfo> getPaymentInfos() {
		return paymentInfos;
	}
	public void setPaymentInfos(Set<PaymentInfo> paymentInfos) {
		this.paymentInfos = paymentInfos;
	}
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.REMOVE})
	@OrderBy("createDate desc")
	public Set<DepositInfo> getDepositInfos() {
		return depositInfos;
	}
	public void setDepositInfos(Set<DepositInfo> depositInfos) {
		this.depositInfos = depositInfos;
	}
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.REMOVE})
	@OrderBy("createDate desc")
	public Set<CouponInfo> getCouponInfos() {
		return couponInfos;
	}
	public void setCouponInfos(Set<CouponInfo> couponInfos) {
		this.couponInfos = couponInfos;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "member_favorite")
	@OrderBy("createDate desc")
	public Set<Product> getFavoriteProducts() {
		return favoriteProducts;
	}
	public void setFavoriteProducts(Set<Product> favoriteProducts) {
		this.favoriteProducts = favoriteProducts;
	}
	
	@OneToMany(mappedBy="member", fetch=FetchType.LAZY, cascade={CascadeType.REMOVE})
	@OrderBy("isDefault desc, createDate desc")	
	public Set<Receiver> getReceivers() {
		return receivers;
	}
	public void setReceivers(Set<Receiver> receivers) {
		this.receivers = receivers;
	}
	
	@Transient
	public String getStatusName() {
		if(this.status != null){
			return SpringUtil.getMessage("MemberStatus."+this.status, null);
		}
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	
	
}
