package com.koch.base;

/**
 * controller 异常
 * @author koch
 * @date  2013-06-04
 */
public class ActionException extends Exception implements MessageAlertable,Logable{
	private static final long serialVersionUID = 5206708760964096095L;
	
	public ActionException(String msg,Throwable e){
		super(msg, e);
	}
	public ActionException(String msg){
		super(msg);
	}
}
