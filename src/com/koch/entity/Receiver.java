package com.koch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="receiver")
public class Receiver extends BaseEntity{ 
	private static final long serialVersionUID = -2000512265567092119L;
	 public static final Integer MAX_RECEIVER_COUNT = 8;
	 
	private String receiver;
	private String mobile;
	private String name;
	private String phone;
	private String zipCode;
	private String address;
	private Boolean isDefault;
	private Area area;
	private Member member;
	private String areaName;
	
	@Column
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	@Column
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	@Column
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="areaId")
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
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
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	@PrePersist
	public void prePersist() {
		if (getArea() != null)
			setAreaName(getArea().getFullName());
	}

	@PreUpdate
	public void preUpdate() {
		if (getArea() != null)
			setAreaName(getArea().getFullName());
	}
}
