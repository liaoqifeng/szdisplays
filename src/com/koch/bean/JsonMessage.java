package com.koch.bean;

import com.koch.util.SpringUtil;

public class JsonMessage {
	public enum Type{
		success, warn, error;
	}
	public JsonMessage(Type type, String content, Object... args) {
		super();
		this.type = type;
		this.content = SpringUtil.getMessage(content, args);
	}
	
	
	public static JsonMessage success(String content,Object... args) {
		return new JsonMessage(Type.success, content, args);
	}

	public static JsonMessage warn(String content,Object... args) {
		return new JsonMessage(Type.warn, content, args);
	}

	public static JsonMessage error(String content,Object... args) {
		return new JsonMessage(Type.error, content, args);
	}

	private Type type;
	private String content;
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
