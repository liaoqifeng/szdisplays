<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<title>商品详情</title>
		<%@ include file="/front/common/metainfo.jsp" %>
		<link rel="stylesheet" type="text/css" href="${path }/front/css/style.css"/>
		<style>
			.preview{width:400px; height:465px; margin:10px 0px 0px 0px;}
			/* smallImg */
			.smallImg{position:relative; height:52px; margin-top:1px; background-color:#F1F0F0; padding:6px 5px; width:390px; overflow:hidden;float:left;}
			.scrollbutton{width:14px; height:50px; overflow:hidden; position:relative; float:left; cursor:pointer; }
			.scrollbutton.smallImgUp , .scrollbutton.smallImgUp.disabled{background:url(images/d_08.png) no-repeat;}
			.scrollbutton.smallImgDown , .scrollbutton.smallImgDown.disabled{background:url(images/d_09.png) no-repeat; margin-left:375px; margin-top:-50px;}
			
			#imageMenu {height:50px; width:360px; overflow:hidden; margin-left:0; float:left;}
			#imageMenu li {height:50px; width:60px; overflow:hidden; float:left; text-align:center;}
			#imageMenu li img{width:50px; height:50px;cursor:pointer;}
			#imageMenu li#onlickImg img, #imageMenu li:hover img{ width:44px; height:44px; border:3px solid #959595;}
			/* bigImg */
			.bigImg{position:relative; float:left; width:400px; height:400px; overflow:hidden;}
			.bigImg #midimg{width:400px; height:400px;}
			.bigImg #winSelector{width:235px; height:210px;}
			#winSelector{position:absolute; cursor:crosshair; filter:alpha(opacity=15); -moz-opacity:0.15; opacity:0.15; background-color:#000; border:1px solid #fff;}
			/* bigView */
			#bigView{position:absolute;border: 1px solid #959595; overflow: hidden; z-index:999;}
			#bigView img{position:absolute;}
		</style>
	</head>
	<body>
		<div class="container">
			<jsp:include page="/menu.shtml" />
			<div class="banner"><%@ include file="/front/common/banner.jsp" %></div>
			<div class="main">
				<div class="title">
					<div class="label fl">${title }</div>
				</div>
				<div class="clearfix"></div>
				<div class="image">
					<div class="preview">
						<div id="vertical" class="bigImg">
							<c:if test="${product.productImages[0]!=null}">
							<img src="${product.productImages[0].medium }" data-large="${product.productImages[0].large }" width="400" height="400" alt="" id="midimg" />
							</c:if>
							<div style="display:none;" id="winSelector"></div>
						</div><!--bigImg end-->	
						<div class="smallImg">
							<div class="scrollbutton smallImgUp disabled"></div>
							<div id="imageMenu">
								<ul>
								<c:forEach var="row" items="${product.productImages}" varStatus="index">
								<li ${index.index==0?'id="onlickImg"':'' }><img src="${row.thumbnail }" data-medium="${row.medium }" data-large="${row.large }" width="68" height="68" alt="洋妞"/></li>
								</c:forEach>
								</ul>
							</div>
							<div class="scrollbutton smallImgDown"></div>
						</div><!--smallImg end-->	
						<div id="bigView" style="display:none;"><img width="800" height="800" alt="" src="" /></div>
					</div>
					<!--preview end-->
				</div>
				<div class="property">
					<ul>
						<li>品名：${product.name }</li>
						<c:if test="${propertys != null}">
						<c:forEach var="row" items="${propertys}">
						<li title="${fn:getPropertyValue(product,row)}">${row.name }：${fn:getPropertyValue(product,row)}</li>
						</c:forEach>
						</c:if>
					</ul>
				</div>
				<div class="clearfix"></div>
				<div class="discription">
					${product.introduction }
				</div>
				<div class="title">
					<div class="label fl">同类产品</div>
				</div>
				<ul class="same_list">
				<c:if test="${products != null}">
				<c:forEach var="row" items="${products}" varStatus="index">
				<li ${index.count==5?'style="margin-right: 0px;"':'' }>
					<img src="${row.showImg }" width="187" height="124"/>
					<h6 class="f13 tc">${row.name }</h6>
				</li>
				</c:forEach>
				</c:if>
				</ul>
			</div>
			<div class="clearfix"></div>
			<div class="footer mb50"></div>
		</div>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			// 图片上下滚动
			var count = $("#imageMenu li").length - 5; /* 显示 6 个 li标签内容 */
			var interval = $("#imageMenu li:first").width();
			var curIndex = 0;
			
			$('.scrollbutton').click(function(){
				if( $(this).hasClass('disabled') ) return false;
				
				if ($(this).hasClass('smallImgUp')) --curIndex;
				else ++curIndex;
				
				$('.scrollbutton').removeClass('disabled');
				if (curIndex == 0) $('.smallImgUp').addClass('disabled');
				if (curIndex == count-1) $('.smallImgDown').addClass('disabled');
				
				$("#imageMenu ul").stop(false, true).animate({"marginLeft" : -curIndex*interval + "px"}, 600);
			});	
			// 解决 ie6 select框 问题
			$.fn.decorateIframe = function(options) {
		        
		    }
		    $.fn.decorateIframe.defaults = {
		        iframeId: "decorateIframe1",
		        iframeZIndex: -1,
		        width: 0,
		        height: 0
		    }
		    //放大镜视窗
		    $("#bigView").decorateIframe();
		    //点击到中图
		    var midChangeHandler = null;
			
		    $("#imageMenu li img").bind("click", function(){
				if ($(this).attr("id") != "onlickImg") {
					midChange($(this).data().medium,$(this).data().large);
					$("#imageMenu li").removeAttr("id");
					$(this).parent().attr("id", "onlickImg");
				}
			}).bind("mouseover", function(){
				if ($(this).attr("id") != "onlickImg") {
					window.clearTimeout(midChangeHandler);
					midChange($(this).data().medium,$(this).data().large);
					$(this).css({ "border": "3px solid #959595" });
				}
			}).bind("mouseout", function(){
				if($(this).attr("id") != "onlickImg"){
					$(this).removeAttr("style");
					midChangeHandler = window.setTimeout(function(){
						midChange($("#onlickImg img").data().medium,$("#onlickImg img").data().large);
					}, 1000);
				}
			});
		    function midChange(src,large) {
		    	$("#midimg").data("large",large);
		        $("#midimg").attr("src", src).load(function() {
		            changeViewImg();
		        });
		    }
		    //大视窗看图
		    function mouseover(e) {
		        if ($("#winSelector").css("display") == "none") {
		            $("#winSelector,#bigView").show();
		        }
		        $("#winSelector").css(fixedPosition(e));
		        e.stopPropagation();
		    }
		    function mouseOut(e) {
		        if ($("#winSelector").css("display") != "none") {
		            $("#winSelector,#bigView").hide();
		        }
		        e.stopPropagation();
		    }
		    $("#midimg").mouseover(mouseover); //中图事件
		    $("#midimg,#winSelector").mousemove(mouseover).mouseout(mouseOut); //选择器事件
		
		    var $divWidth = $("#winSelector").width(); //选择器宽度
		    var $divHeight = $("#winSelector").height(); //选择器高度
		    var $imgWidth = $("#midimg").width(); //中图宽度
		    var $imgHeight = $("#midimg").height(); //中图高度
		    var $viewImgWidth = $viewImgHeight = $height = null; //IE加载后才能得到 大图宽度 大图高度 大图视窗高度
		
		    function changeViewImg() {
		        $("#bigView img").attr("src", $("#midimg").data().large);
		    }
		    changeViewImg();
		    $("#bigView").scrollLeft(0).scrollTop(0);
		    function fixedPosition(e) {
		        if (e == null) {
		            return;
		        }
		        var $imgLeft = $("#midimg").offset().left; //中图左边距
		        var $imgTop = $("#midimg").offset().top; //中图上边距
		        X = e.pageX - $imgLeft - $divWidth / 2; //selector顶点坐标 X
		        Y = e.pageY - $imgTop - $divHeight / 2; //selector顶点坐标 Y
		        X = X < 0 ? 0 : X;
		        Y = Y < 0 ? 0 : Y;
		        X = X + $divWidth > $imgWidth ? $imgWidth - $divWidth : X;
		        Y = Y + $divHeight > $imgHeight ? $imgHeight - $divHeight : Y;
		
		        if ($viewImgWidth == null) {
		            $viewImgWidth = $("#bigView img").outerWidth();
		            $viewImgHeight = $("#bigView img").height();
		            if ($viewImgWidth < 200 || $viewImgHeight < 200) {
		                $viewImgWidth = $viewImgHeight = 800;
		            }
		            $height = $divHeight * $viewImgHeight / $imgHeight;
		            $("#bigView").width($divWidth * $viewImgWidth / $imgWidth);
		            $("#bigView").height($height);
		        }
		        var scrollX = X * $viewImgWidth / $imgWidth;
		        var scrollY = Y * $viewImgHeight / $imgHeight;
		        $("#bigView img").css({ "left": scrollX * -1, "top": scrollY * -1 });
		        $("#bigView").css({ "top": $("#vertical").position().Y, "left": $(".preview").offset().left + $(".preview").width() + 15 });
		
		        return { left: X, top: Y };
		    }
		});
	</script>
</html>
