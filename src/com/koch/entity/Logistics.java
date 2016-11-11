package com.koch.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="logistics")
@JsonFilter("logistics")
public class Logistics extends BaseEntity{
	private static final long serialVersionUID = 2923120201189797484L;
	
	private String name;
	private String url;
	private Integer orderList;
	private Set<Deliverway> deliverways = new HashSet<Deliverway>();

	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column
	public Integer getOrderList() {
		return orderList;
	}
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	@JsonIgnore
	@OneToMany(mappedBy = "logistics", fetch = FetchType.LAZY)
	public Set<Deliverway> getDeliverways() {
		return deliverways;
	}
	public void setDeliverways(Set<Deliverway> deliverways) {
		this.deliverways = deliverways;
	}
	
	@PreRemove
	public void preRemove() {
		Set<Deliverway> deliverwaySet = getDeliverways();
		if (deliverwaySet != null) {
			Iterator<Deliverway> iterator = deliverwaySet.iterator();
			while (iterator.hasNext()) {
				Deliverway deliverway = iterator.next();
				deliverway.setLogistics(null);
			}
		}
	}
}
