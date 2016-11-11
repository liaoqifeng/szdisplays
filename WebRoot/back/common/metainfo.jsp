<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<% request.setAttribute("path",request.getContextPath()); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<link rel="stylesheet" type="text/css" href="${path }/back/css/easyui.css"/>
<link rel="stylesheet" type="text/css" href="${path }/back/css/icon.css"/>
<link rel="stylesheet" type="text/css" href="${path }/back/css/style.css"/>
<script type="text/javascript" src="${path }/common/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${path }/back/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${path }/common/js/jquery.json-2.4.js"></script>
<script type="text/javascript" src="${path }/common/js/jquery.timer.js"></script>
<script type="text/javascript" src="${path }/common/js/jquery.tools.js"></script>
<script type="text/javascript" src="${path }/back/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${path }/common/js/jquery.ajaxfileupload.js" charset="utf-8" ></script>
<script type="text/javascript" src="${path }/common/js/jquery.validate.js"></script>
<script type="text/javascript" src="${path }/common/js/jquery.validate.methods.js"></script>
<script type="text/javascript" src="${path }/back/js/jquery.common.js"></script>

<script type="text/javascript">
	$().ready(function(){
		$("a[button='back']").click(function(){
			window.history.back();
		});
		$("a[button='reload']").click(function(){
			window.location.reload();
		});
		${fn:message(message)}
	});
</script>