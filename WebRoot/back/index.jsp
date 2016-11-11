<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.koch.util.GlobalConstant"%>
<% 
	String path = request.getContextPath();
	Object object = session.getAttribute(GlobalConstant.BACK_SESSION_USER);
	if(object == null){
		response.sendRedirect(path+"/back/login.jsp");
	}else{
		response.sendRedirect(path+"/back/main.jsp");
	}
%>