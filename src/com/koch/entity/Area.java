package com.koch.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="area")
public class Area extends BaseEntity{ 
	private static final long serialVersionUID = -5536395598743435594L;
	private String name;
	private String path;
	private Area parent;
	private String fullName;
	private Integer orderList;
	private Set<Area> children = new HashSet<Area>();
	
	private Set<Member> members = new HashSet<Member>();
	private Set<Receiver> receivers = new HashSet<Receiver>();
	private Set<Order> orders = new HashSet<Order>();
	
	private String state;
	
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentId")
	public Area getParent() {
		return parent;
	}
	public void setParent(Area parent) {
		this.parent = parent;
	}
	@Column
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	@Column
	public Integer getOrderList() {
		return orderList;
	}
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	@JsonIgnore
	@OneToMany(mappedBy="parent", fetch=FetchType.LAZY, cascade={CascadeType.REMOVE})
	@OrderBy("orderList asc")
	public Set<Area> getChildren() {
		return children;
	}
	public void setChildren(Set<Area> children) {
		this.children = children;
	}
	@JsonIgnore
	@OneToMany(mappedBy="area", fetch=FetchType.LAZY)
	public Set<Member> getMembers() {
		return members;
	}
	public void setMembers(Set<Member> members) {
		this.members = members;
	}
	@JsonIgnore
	@OneToMany(mappedBy="area", fetch=FetchType.LAZY)
	public Set<Receiver> getReceivers() {
		return receivers;
	}
	public void setReceivers(Set<Receiver> receivers) {
		this.receivers = receivers;
	}
	@JsonIgnore
	@OneToMany(mappedBy="area", fetch=FetchType.LAZY)
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	@Transient
	public String getState() {
		if(StringUtils.isEmpty(this.state))
			return "closed";
		return this.state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@Transient
	public List<Integer> getTreePaths() {
		List<Integer> paths = new ArrayList<Integer>();
		String[] array = StringUtils.split(getPath(), ",");
		if (array != null) {
			for (String str : array) {
				if(StringUtils.isEmpty(str)) continue;
				paths.add(Integer.valueOf(str));
			}
		}
		return paths;
	}
	
	@PrePersist
	public void prePersist() {
		Area area = getParent();
		if (area != null) {
			setFullName(area.getFullName() + getName());
			setPath(area.getPath() + area.getId() + ",");
		} else {
			setFullName(getName());
			setPath(",");
		}
	}
	  
	@PreUpdate
	public void preUpdate() {
		Area area = getParent();
		if (area != null) {
			setFullName(area.getFullName() + getName());
		} else {
			setFullName(getName());
		}
	}
	  
	@PreRemove
	public void preRemove() {
		Set<Member> memberSet = getMembers();
		if (memberSet != null) {
			Iterator<Member> iterator = memberSet.iterator();
			while (iterator.hasNext()) {
				Member member = iterator.next();
				member.setArea(null);
			}
		}
		Set<Receiver> receiverSet = getReceivers();
		if (receiverSet != null) {
			Iterator<Receiver> iterator = receiverSet.iterator();
			while (iterator.hasNext()) {
				Receiver receiver = iterator.next();
				receiver.setArea(null);
			}
		}
		Set<Order> orderSet = getOrders();
		if (orderSet != null) {
			Iterator<Order> iterator = orderSet.iterator();
			while (iterator.hasNext()) {
				Order order = iterator.next();
				order.setArea(null);
			}
		}
	}
	  
	public String toString() {
		return getFullName();
	}
	
}
