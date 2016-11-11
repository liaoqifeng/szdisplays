<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("path",request.getContextPath()); %>
<!DOCTYPE html>
<HTML>
<HEAD>
<TITLE></TITLE>
<META content="text/html; charset=utf-8" http-equiv="Content-Type">
<link rel="stylesheet" type="text/css" href="${path }/back/css/layoutStyle.css" />
<link rel="stylesheet" type="text/css" href="${path }/back/css/zTreeStyle.css" />
<SCRIPT type="text/javascript" src="${path }/common/js/jquery-1.11.0.min.js"></SCRIPT>
<SCRIPT type="text/javascript" src="${path }/back/js/jquery.layout-latest.js"></SCRIPT>
<script type="text/javascript" src="${path }/back/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${path }/back/js/jquery.ztree.core-3.5.js"></script>
<SCRIPT type="text/javascript" src="${path }/common/js/jquery.ui.min.js"></SCRIPT>
<SCRIPT type="text/javascript" src="${path }/common/js/jquery.select.min.js"></SCRIPT>
<SCRIPT type="text/javascript">
$(document).ready(function () {
	$('body').layout({
		applyDefaultStyles: false,
		north: {
			size:97,
			spacing_open:5,
			spacing_closed:5
		},
		west: {
			size:200,
			spacing_open:5,
			spacing_closed:5
		},
		south: {
			size:0,
			closable:false,
			spacing_open:0,
			spacing_closed:0
		}
	});
	var $logoutBtn = $("#logoutBtn");
	var $iframe = $("#iframe");
	var $nodes = $("#nodes .node");
	var $menus = $("#header-menu li").click(function(){
		$menus.removeClass("menu-selected");
		var $this = $(this).addClass("menu-selected");
		var index = $this.data("index");
		$nodes.hide();
		$nodes.eq(index).show();
	});
	$nodes.find("li").click(function(){
		if(window.currentNode){
			window.currentNode.removeClass("selected");
		}
		var $this = $(this);
		var $div = window.currentNode = $this.find("div").addClass("selected");
		var url = $this.data("url");
		$iframe.attr("src",url);
	});
	$logoutBtn.click(function(){
		if(confirm("是否确定退出?")){
			window.location.href = "${path}/back/logout.shtml";
		}
	});
});

</SCRIPT>

</HEAD>
<BODY>
<div class="ui-layout-north">
	<div class="header-t">
        <div class="header-t-box">
            <div class="header-t-tool">
                <ul>
                    <li><a href="#"><img src="${path }/back/images/home-i.gif"/></a></li>
                    <li><a href="#"><img src="${path }/back/images/mes-i.gif"/></a></li>
                    <li><a href="#"><img src="${path }/back/images/tool-i.gif"/></a></li>
                    <li><a id="logoutBtn" href="javascript:void(0);"><img src="${path }/back/images/shutdown-i.gif"/></a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="header-b">
    	<div class="header-b-info"><font color="#92ccfc">欢迎您!</font><font color="#dfda04"><b>${backSessionUser.username }</b></font></div>
    	<div id="header-menu" class="header-b-menu">
        	<ul>
            	<c:if test="${backSessionUser.auths != null}">
            		<c:forEach var="row" items="${backSessionUser.auths}" varStatus="index">
            		<li data-index="${index.index }"}>${row.name }</li>
            		</c:forEach>
            	</c:if>
            </ul>
        </div>
    </div>
</div>
<div class="ui-layout-west">
	<div class="nav-t">
            <ul>
            <li></li>
            <li></li>
        </ul>
    </div>
    <div class="split-line"></div>
    <div id="nodes" class="nav-menu">
    	<c:if test="${backSessionUser.auths != null}">
       		<c:forEach var="row" items="${backSessionUser.auths}" varStatus="index">
       		<ul id="node_${index.index }" class="node" style="display: ${index.index==0?'block':'none' };">
       			<c:if test="${row.nodes != null}">
       				<c:forEach var="node" items="${row.nodes}">
       					<li data-url="${path }${node.url }"><div>${node.name }</div></li>
       				</c:forEach>
       			</c:if>
       		</ul>
       		</c:forEach>
       	</c:if>
    </div>
</div>
<div class="ui-layout-center">
	<iframe id="iframe" width="99%" height="99%" frameborder="0" border=0 scrolling="auto" marginwidth="0" marginheight="0" src="${path }/back/admin/list.shtml" allowTransparency="true"></iframe>
</div>
<div class="ui-layout-south"></div>

</BODY>
</HTML>
