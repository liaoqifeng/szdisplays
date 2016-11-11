package com.koch.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name="freight_templet_item")
public class FreightTempletItem implements Serializable {
	private static final long serialVersionUID = -3834222632632820801L;
	private Integer id;
	private FreightTemplet freightTemplet;
	private Integer provinceId;
	private String cityItem;
	private Double firstWeight;
	private Double conWeight;
	private Double firstPrice;
	private Double conPrice;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="freighttempletId",referencedColumnName="id")
	@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
	public FreightTemplet getFreightTemplet() {
		return freightTemplet;
	}
	public void setFreightTemplet(FreightTemplet freightTemplet) {
		this.freightTemplet = freightTemplet;
	}
	@Column
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	@Column
	public String getCityItem() {
		return cityItem;
	}
	public void setCityItem(String cityItem) {
		this.cityItem = cityItem;
	}
	@Column
	public Double getFirstWeight() {
		return firstWeight;
	}
	public void setFirstWeight(Double firstWeight) {
		this.firstWeight = firstWeight;
	}
	@Column
	public Double getConWeight() {
		return conWeight;
	}
	public void setConWeight(Double conWeight) {
		this.conWeight = conWeight;
	}
	@Column
	public Double getFirstPrice() {
		return firstPrice;
	}
	public void setFirstPrice(Double firstPrice) {
		this.firstPrice = firstPrice;
	}
	@Column
	public Double getConPrice() {
		return conPrice;
	}
	public void setConPrice(Double conPrice) {
		this.conPrice = conPrice;
	}
	
	
}
