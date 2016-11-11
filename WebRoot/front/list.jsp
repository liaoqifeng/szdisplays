<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<title>商品列表</title>
		<%@ include file="/front/common/metainfo.jsp" %>
		<link rel="stylesheet" type="text/css" href="${path }/front/css/style.css"/>
	</head>
	<body>
		<div class="container">
			<jsp:include page="/menu.shtml" />
			<div class="banner">
				<%@ include file="/front/common/banner.jsp" %>
			</div>
			<div class="main">
				<div class="title">
					<div class="label fl">${title }</div>
				</div>
				<c:if test="${list == null || fn:length(list)==0}">
				<div class="tc mt50">暂无数据列表</div>
				</c:if>
				<c:if test="${list != null && fn:length(list) > 0}">
				<div class="list">
					<ul>
					<c:forEach var="row" items="${list}">
						<li onclick="window.location.href='${path}/get.shtml?id=${row.id }'">
							<div class="img"><img src="${row.showImg }" width="100%" height="170"/></div>
							<span>${row.name }</span>
						</li>
					</c:forEach>
					</ul>
				</div>
				</c:if>
			</div>
			<div class="clearfix"></div>
			<div class="footer"></div>
		</div>
	</body>
</html>
