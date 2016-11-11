package com.koch.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Auth implements Serializable {
	private static final long serialVersionUID = 5685993781109225445L;
	public static final String CACHE_NAME = "auth";
	public static final Integer CACHE_KEY = Integer.valueOf(0);
	
	private String name;
	private String perms;
	private String url;
	private String value;
	private Integer order;
	private List<Auth> nodes = new ArrayList<Auth>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getPerms() {
		return perms;
	}
	public void setPerms(String perms) {
		this.perms = perms;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<Auth> getNodes() {
		return nodes;
	}
	public void setNodes(List<Auth> nodes) {
		this.nodes = nodes;
	}
	
	
}
