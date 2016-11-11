<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>退货信息</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $editForm = $("#editForm");
			var $tab = $("#tab");
			$mainPanel.panel({
				width:$mainBox.width(),
				height:$mainBox.height(),
				tools:[{
					iconCls:'icon-reload',
					handler:function(){window.location.reload();}
				}]
			});
			$(window).resize(function() { 
				var width= $mainBox.width(); 
				var height=  $mainBox.height();
				$mainPanel.panel('resize', { width : width,height:height }); 
			});
			if ($.tools != null) {
				$tab.tabs("table.tabContent", {
					tabs: "input"
				});
			}
		});
	</script>
</head>
<body>
	<div id="mainBox" class="main-box">
		<div id="mainPanel" class="easyui-panel" iconCls="icon-search" title="&nbsp;&nbsp;查  看  退  货  信  息">
	    	<ul id="tab" class="tab">
				<li><input value="基本信息" type="button" /></li>
				<li><input value="商品信息" type="button" /></li>
			</ul>
			<table width="100%" class="input tabContent table">
				<tr>
	    			<td width="30%" height="30" align="right">&nbsp;编号：&nbsp;&nbsp;</td>
	    			<td width="70%" align="left">${returns.number }</td>
	    		</tr>
	    		<tr>
	    			<td height="30" align="right">&nbsp;配送方式：&nbsp;&nbsp;</td>
	    			<td align="left">${returns.deliverwayName }</td>
	    		</tr>
	    		<tr>
	    			<td height="30" align="right">&nbsp;物流公司：&nbsp;&nbsp;</td>
	    			<td align="left">${returns.logisticsName }</td>
	    		</tr>
	    		<tr>
	    			<td height="30" align="right">&nbsp;配送单号：&nbsp;&nbsp;</td>
	    			<td align="left">${returns.deliverCode }</td>
	    		</tr>
	    		<tr>
	    			<td height="30" align="right">&nbsp;配送费用：&nbsp;&nbsp;</td>
	    			<td align="left">${fn:currency(returns.deliverAmount,true) }</td>
	    		</tr>
	    		<tr>
	    			<td height="30" align="right">&nbsp;发货人：&nbsp;&nbsp;</td>
	    			<td align="left">${returns.shipper }</td>
	    		</tr>
	    		<tr>
	    			<td height="30" align="right">&nbsp;电话：&nbsp;&nbsp;</td>
	    			<td align="left">${returns.phone }</td>
	    		</tr>
	    		<tr>
	    			<td height="30" align="right">&nbsp;邮编：&nbsp;&nbsp;</td>
	    			<td align="left">${returns.zipCode }</td>
	    		</tr>
	    		<tr>
	    			<td height="30" align="right">&nbsp;地区：&nbsp;&nbsp;</td>
	    			<td align="left">${returns.area }</td>
	    		</tr>
	    		<tr>
	    			<td height="30" align="right">&nbsp;详细地址：&nbsp;&nbsp;</td>
	    			<td align="left">${returns.address }</td>
	    		</tr>
	    		<tr>
	    			<td height="30" align="right">&nbsp;备注：&nbsp;&nbsp;</td>
	    			<td align="left">${returns.remark }</td>
	    		</tr>
	    		<tr>
	    			<td height="30" align="right">&nbsp;操作人：&nbsp;&nbsp;</td>
	    			<td align="left">${returns.operator }</td>
	    		</tr>
			</table>
   			<table width="100%" class="input tabContent table" style="text-align: center;">
   				<tr class="header">
					<th>商品编号</th>
					<th>商品名称</th>
					<th>退货数量</th>
					<th>退货日期</th>
				</tr>
				<c:if test="${returns.returnsItems != null}">
					<c:forEach var="row" items="${returns.returnsItems}">
					<tr>
						<td>${row.productNumber }</td>
						<td>${row.productName }</td>
						<td>${row.productQuantity }</td>
						<td><fmt:formatDate value="${row.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					</tr>
					</c:forEach>
				</c:if>
    		</table>
	    	<table width="100%" align="center" cellpadding="0" cellspacing="0">
	      		<tr>
      				<td width="60%" align="center" height="50">
      					<a href="#" button="back" class="easyui-linkbutton" data-options="iconCls:'icon-back'">返      回</a>
      				</td>
      				<td width="40%"></td>
      			</tr>
	    	</table>
		</div>
	</div>
</body>
</html>