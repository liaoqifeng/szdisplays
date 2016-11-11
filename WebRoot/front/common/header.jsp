<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% request.setAttribute("path",request.getContextPath()); %>
<script>
$().ready(function(){
	$("#menu li").mouseenter(function(){
		var $this = $(this);
		var $panel = $this.find(".menu_wrap").show().find(".panel_1").slideDown();
		
	}).mouseleave(function(){
		var $this = $(this);
		var $panel = $this.find(".panel_1").slideUp(function(){
			$(this).parent().hide();
		});
	});
	$(".panel_1").find("dd").mouseenter(function(){
		var $this = $(this);
		var $panel = $this.find(".panel_2").css("top",$this.position().top).slideDown();
	}).mouseleave(function(){
		var $this = $(this);
		var $panel = $this.find(".panel_2").slideUp(function(){});
	});
});
</script>
<div class="header">
	<div class="logo fl" onclick="window.location.href='${path}/index.shtml'"></div>
	<div id="menu" class="menu fr">
		<ul>
			<li>
				关于我们
				<div class="menu_wrap">
					<div class="panel">
						<dl>
							<dd>公司地址</dd>
							<dd style="border:0;">成功案例</dd>
						</dl>
					</div>
				</div>
			</li>
			<c:if test="${productCategories != null}">
			<c:forEach var="row" items="${productCategories}" varStatus="index">
			<li <c:if test="${index.count==fn:length(productCategories)}">style="border:0;"</c:if>>
				${row.name }
				<div class="menu_wrap">
					<div class="panel panel_1">
						<dl>
							<c:if test="${row.children != null}">
							<c:forEach var="s" items="${row.children}" varStatus="si">
									<c:if test="${s.children != null}">
									<c:forEach var="t" items="${s.children}" varStatus="ti">
									<dd <c:if test="${si.count==fn:length(row.children) && ti.count==fn:length(s.children) }">style="border:0;"</c:if>>
										<a href="${path }/list.shtml?id=${t.id}">${t.name }</a>
										<c:if test="${t.children != null && fn:length(t.children) > 0}">
										<div class="panel panel_2">
											<dl>
											<c:forEach var="f" items="${t.children}" varStatus="fi">
												<dd <c:if test="${fi.count==fn:length(t.children) }">style="border:0;"</c:if>>${f.name }</dd>
											</c:forEach>
											</dl>
										</div>
										</c:if>
										
												
											
									</dd>
									</c:forEach>
									</c:if>
							</c:forEach>
							</c:if>
						</dl>
					</div>
				</div>
			</li>
			</c:forEach>
			</c:if>
		</ul>
	</div>
</div>