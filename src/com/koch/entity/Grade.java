package com.koch.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koch.util.SpringUtil;
import com.sun.istack.internal.NotNull;


@Entity
@Table(name="grade")
public class Grade extends BaseEntity{ 
	private static final long serialVersionUID = 5610113079965660763L;
	// 会员等级类别（普通、系统）
	public enum GradeType {
		common, system
	};
	
	private String name;
	private Double discount;
	private GradeType gradeType;
	private BigDecimal expValue;
	private Integer orderList;
	
	private Set<Member> members = new HashSet<Member>();
	private Set<Promotion> promotions = new HashSet<Promotion>();
	
	private String typeName;

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false, unique = true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Enumerated
	@Column(name="typeId")
	public GradeType getGradeType() {
		return gradeType;
	}
	public void setGradeType(GradeType gradeType) {
		this.gradeType = gradeType;
	}
	@NotNull
	@Min(0L)
	@Digits(integer = 3, fraction = 3)
	@Column(nullable = false, precision = 18, scale = 4)
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	@Min(0L)
	@Digits(integer = 12, fraction = 3)
	@Column(unique = true, precision = 18, scale = 4)
	public BigDecimal getExpValue() {
		return expValue;
	}
	public void setExpValue(BigDecimal expValue) {
		this.expValue = expValue;
	}
	@Column
	public Integer getOrderList() {
		return orderList;
	}
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	@JsonIgnore
	@OneToMany(mappedBy = "grade", fetch = FetchType.LAZY)
	public Set<Member> getMembers() {
		return members;
	}
	public void setMembers(Set<Member> members) {
		this.members = members;
	}
	@JsonIgnore
	@ManyToMany(mappedBy="grades", fetch=FetchType.LAZY)
	public Set<Promotion> getPromotions() {
		return promotions;
	}
	public void setPromotions(Set<Promotion> promotions) {
		this.promotions = promotions;
	}
	
	@Transient
	public String getTypeName() {
		if(this.gradeType != null){
			return SpringUtil.getMessage("GradeType."+getGradeType(),new Object[0]);
		}
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@PreRemove
	public void preRemove() {
		Set<Promotion> set = getPromotions();
		if (set != null) {
			Iterator<Promotion> iterator = set.iterator();
			while (iterator.hasNext()) {
				Promotion promotion = iterator.next();
				promotion.getGrades().remove(this);
			}
		}
	}
	
}
