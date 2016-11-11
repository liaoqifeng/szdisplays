package com.koch.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koch.bean.Auth;
import com.koch.util.JsonUtil;

/**
 * 实体类 - 角色
 */

@Entity
public class Role extends BaseEntity{
	private static final long serialVersionUID = -9096422656291320086L;
	
	private String name;// 角色名称
	private String value;// 角色标识
	private Boolean isSystem;// 是否为系统内置角色
	private String authJson;//资源集合
	private String description;// 描述
	private Set<Admin> admins = new HashSet<Admin>();
	private List<String> auths = new ArrayList<String>();
	
	@Column(nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(nullable = false, unique = true)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(nullable = false, updatable = false)
	public Boolean getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}
	@Lob
	@Column(name="auths")
	public String getAuthJson() {
		return authJson;
	}

	public void setAuthJson(String authJson) {
		this.authJson = authJson;
	}

	@Column
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@JsonIgnore
	@ManyToMany(mappedBy="roles", fetch=FetchType.LAZY)
	public Set<Admin> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}

	@Transient
	public List<String> getAuths() {
		if(StringUtils.isNotEmpty(getAuthJson())){
			return JsonUtil.toList(getAuthJson(), String.class);
		}
		return auths;
	}

	public void setAuths(List<String> auths) {
		if(auths != null && auths.size() > 0){
			this.authJson = JsonUtil.toJson(auths);
		}else{
			this.authJson = "";
		}
		
	}
	
	@Transient
	public Boolean hasPerms(Auth auth){
		if(getAuths() != null && getAuths().size() > 0){
			return getAuths().contains(auth.getPerms());
		}else{
			return false;
		}
	}
	
}