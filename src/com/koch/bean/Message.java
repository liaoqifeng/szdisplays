package com.koch.bean;

public class Message {
	
	public Message(String url, String message) {
		super();
		this.url = url;
		this.message = message;
	}
	public Message(String url, String message,String target) {
		super();
		this.url = url;
		this.message = message;
		this.target = target;
	}
	
	public Message(String url, String message, String target, String go) {
		super();
		this.url = url;
		this.message = message;
		this.target = target;
		this.go = go;
	}

	private String url;
	private String message;
	private String target = "this";   //值  this 时,js调用window.location.href, 值top时,js调用top.location.href
	private String go = "front";  //值 front时,页面跳转location.href   值bakc时,页面回退history.go(-1)
	
	public static Message error(String content){
	    return new Message("",content,"this","back");
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getGo() {
		return go;
	}
	public void setGo(String go) {
		this.go = go;
	}
	
	
	
}
