<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%String path = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<script type="text/javascript" src="<%=path %>/common/js/jquery-1.11.0.min.js"></script> 
<script type="text/javascript" src="<%=path %>/back/js/jquery.wbox.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=path %>/back/css/wbox.css" />
<style type="text/css">
	body{
		width:920px;
		margin:0 auto;
	}
	h2{
		font-size:15px;
	}
	.box {
		width:360px;
	}
</style>
</head> 
<body>
<script type="text/javascript"> 
	$().ready(function(){
		$('#setPos').wBox({
			title:"信息提示",
			show:true,
			html:"<div class='box' style='padding-top:50px;height:120px;vertical-align: middle;text-align: center;'><font>${message.message}</font></div>",
			timeout:2000,
			hrefTarget:"${message.target}",
			go:"${message.go}",
			url:"<%=path%>${message.url}"
		});
	}); 
</script> 
</body>
</html>