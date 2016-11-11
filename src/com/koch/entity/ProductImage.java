package com.koch.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Embeddable
public class ProductImage implements Serializable{ 
	private static final long serialVersionUID = -239844466333918262L;

	private String large;
	private String title;
	private String thumbnail;
	private String source;
	private String medium;
	private Integer orderList;
	private MultipartFile file;

	@Column
	public String getLarge() {
		return large;
	}
	public void setLarge(String large) {
		this.large = large;
	}
	@Column
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	@Column
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	@Column
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	@Column
	public Integer getOrderList() {
		return orderList;
	}
	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}
	
	@Transient
	public MultipartFile getFile(){
	    return this.file;
	}
	public void setFile(MultipartFile file){
	    this.file = file;
	}

	@Transient
	public boolean isEmpty(){
	    return ((getFile() == null) || (getFile().isEmpty())) && ((StringUtils.isEmpty(getSource())) || (StringUtils.isEmpty(getLarge())) || (StringUtils.isEmpty(getMedium())) || (StringUtils.isEmpty(getThumbnail())));
	}
}
