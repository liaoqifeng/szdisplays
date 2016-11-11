<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<title></title>
		<%@ include file="/front/common/metainfo.jsp" %>
		<link rel="stylesheet" type="text/css" href="${path }/front/css/index.css"/>
	</head>
	<body>
		<div class="container">
			<div class="nav box_1">
				<h4 class="pt20"><b>About Us</b></h4>
				<h4>关于我们</h4>
				<div class="menu">
					<h5>&gt;公司地址</h5>
					<h5>&gt;成功案例</h5>
				</div>
			</div>
			<c:if test="${productCategories != null}">
			<c:forEach var="row" items="${productCategories}" varStatus="index">
			<div class="nav box_${index.count+1 }">
				<h4 class="pt20"><b>Displays</b></h4>
				<h4><a href="${path }/list.shtml?id=${row.id}">${row.name }</a></h4>
				<c:if test="${row.children != null}">
				<c:forEach var="s" items="${row.children}">
				<div class="menu">
					<h5><a href="${path }/list.shtml?id=${s.id}">&gt;${s.name }</a></h5>
					<ul>
						<c:if test="${s.children != null}">
						<c:forEach var="t" items="${s.children}">
						<li><a href="${path }/list.shtml?id=${t.id}">${t.name }</a></li>
						</c:forEach>
						</c:if>
					</ul>
				</div>
				<div class="clearfix"></div>
				</c:forEach>
				</c:if>
			</div>
			</c:forEach>
			</c:if>
			
			<!-- <div class="nav box_2">
				<h4 class="pt20"><b>Displays</b></h4>
				<h4><a href="list.html">展示道具</a></h4>
				<div class="menu">
					<h5>&gt;平柜道具</h5>
					<ul>
						<li><a href="list_2.html">珠宝柜台道具</a></li>
						<li>钟表柜台道具</li>
						<li>电子产品柜台道具</li>
						<li>其它柜台道具</li>
					</ul>
				</div>
				<div class="clearfix"></div>
				<div class="menu">
					<h5>&gt;橱窗展示</h5>
					<ul>
						<li>珠宝橱窗展示</li>
						<li>钟表橱窗展示</li>
						<li>电子产品橱窗展示</li>
						<li>其它产品橱窗展示</li>
					</ul>
				</div>
			</div>
			<div class="nav box_3">
				<h4 class="pt20"><b>Boxes</b></h4>
				<h4>包装盒</h4>
				<div class="menu">
					<h5>&gt;纸盒</h5>
					<ul>
						<li>珠宝纸盒</li>
						<li>钟表纸盒</li>
						<li>电子产品纸盒</li>
						<li>其它纸盒</li>
					</ul>
				</div>
				<div class="clearfix"></div>
				<div class="menu">
					<h5>&gt;木盒</h5>
					<ul>
						<li>珠宝木盒</li>
						<li>钟表木盒</li>
						<li>电子产品木盒</li>
						<li>其它产品木盒</li>
					</ul>
				</div>
				<div class="clearfix"></div>
				<div class="menu">
					<h5>&gt;胶胚盒</h5>
					<ul>
						<li>珠宝胶盒</li>
						<li>钟表胶盒</li>
						<li>电子产品胶盒</li>
						<li>其它产品胶盒</li>
					</ul>
				</div>
			</div>
			<div class="nav box_4">
				<h4 class="pt20"><b>Bags</b></h4>
				<h4>袋子及布袋</h4>
				<div class="menu">
					<h5>&gt;手提袋</h5>
					<ul>
						<li>纸袋</li>
						<li>无纺布袋</li>
						<li>胶袋</li>
					</ul>
				</div>
				<div class="clearfix"></div>
				<div class="menu">
					<h5>&gt;布袋</h5>
					<ul>
						<li>绒布袋</li>
						<li>皮料袋</li>
						<li>丝绸布袋</li>
						<li>普通布袋</li>
					</ul>
				</div>
			</div>
			<div class="nav box_5">
				<h4 class="pt20"><b>Accessorys</b></h4>
				<h4>配件及配饰</h4>
				<div class="menu">
					<h5>&gt;镜子</h5>
					<ul>
						<li>皮镜</li>
						<li>高档金属镜</li>
					</ul>
				</div>
				<div class="clearfix"></div>
				<div class="menu">
					<h5>&gt;标签类</h5>
					<ul>
						<li>标价签</li>
						<li>数码粒</li>
					</ul>
				</div>
			</div>
			 -->
		</div>
	</body>
</html>
