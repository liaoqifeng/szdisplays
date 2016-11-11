<%@page contentType="text/html; charset=UTF-8" isErrorPage="true"%>
<%@page import="org.springframework.util.StringUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
  <head>
    <title>出错了啦</title>
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
    <meta http-equiv="description" content="this is my page" />
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
  </head>
<body style="width:100%; height:100%;margin:0 auto;">
	<div style="width:auto;margin:5px 20px;height:300px;margin-top:200px; background:url(images/err.jpg) no-repeat 300px 100px;">
		<div style="float:left;width:auto;margin-top:130px;margin-left:400px;height:30px;line-height:30px;text-align:left;">
			<span style="width:100%;height:30px;line-height:30px;margin:0px;padding:0px; font-size:16px; font-family:Arial, Helvetica, sans-serif; color:#2A5FAA;">
				系统错误，请联系管理员！
			    <%
			    //isErrorPage="true" 才可以获取exception 对象
			    String errorMsg =  exception.getMessage();
			    if(StringUtils.hasText(errorMsg)){
			    %>
			      错误原因如下：<%=errorMsg %>
			    <%
			    }
			    %>
			</span></div>
		<div style="clear:both;float:left;width:100%;margin-top:50px;height:30px;line-height:30px;text-align:center;">
	</div>
</body>
</html>
