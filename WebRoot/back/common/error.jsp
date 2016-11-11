<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<script type="text/javascript" src="/common/js/jquery-1.6.1.js"></script> 
<script type="text/javascript" src="/back/js/wbox.js"></script> 
<link rel="stylesheet" type="text/css" href="/back/images/wbox.css" />
<style type="text/css">
	body{
		width:920px;
		margin:0 auto;
		background-color:#E6EAEE;
	}
	h2{
		font-size:15px;
	}
	.demo {
		width:360px;
	}
</style>
</head> 
<body>
<script type="text/javascript"> 
	$().ready(function(){
		$('#setPos').wBox({
			title:"错误提示",
			show:true,
			html:"<div class='demo' style='padding-top:30px;height:120px;vertical-align: middle;text-align: center;'><font>${errorMessage}</font></div>",
			timeout:4000,
			url:"${redirectionUrl}"
		});
	}); 
</script> 
</body>
</html>