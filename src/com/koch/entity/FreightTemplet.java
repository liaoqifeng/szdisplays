package com.koch.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name="freight_templet")
public class FreightTemplet implements Serializable {
	private static final long serialVersionUID = -1210049273622414379L;
	
	private Integer id;
	private String name;
	private Deliverway deliverway;
	private String remark;
	private Integer orderList;
	private List<FreightTempletItem> freightTempletItems;
	private List<FreightTempletAttribute> freightTempletAttributes;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="deliverwayId")
	@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
	public Deliverway getDeliverway() {
		return deliverway;
	}
	public void setDeliverway(Deliverway deliverway) {
		this.deliverway = deliverway;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column
	public Integer getOrderList() {
		return orderList;
	}
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	@OneToMany(mappedBy="freightTemplet",fetch=FetchType.LAZY,orphanRemoval=true)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.REMOVE})
	@JsonIgnore
	public List<FreightTempletItem> getFreightTempletItems() {
		return freightTempletItems;
	}
	public void setFreightTempletItems(List<FreightTempletItem> freightTempletItems) {
		this.freightTempletItems = freightTempletItems;
	}
	@OneToMany(mappedBy="freightTemplet",fetch=FetchType.LAZY,cascade = { javax.persistence.CascadeType.ALL },orphanRemoval=true)
	@JsonIgnore
	public List<FreightTempletAttribute> getFreightTempletAttributes() {
		return freightTempletAttributes;
	}
	public void setFreightTempletAttributes(
			List<FreightTempletAttribute> freightTempletAttributes) {
		this.freightTempletAttributes = freightTempletAttributes;
	}
}
