<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>生成优惠码</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $editForm = $("#editForm");
			var $saveButton = $("#saveButton");
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
			$editForm.validate({
				rules: {
					count:{
						digits: true
					}
				}
			});
			$saveButton.click(function(){
				$editForm.submit();
			});
		});
	</script>
</head>
<body>
	<div id="mainBox" class="main-box">
		<div id="mainPanel" class="easyui-panel" iconCls="icon-add" title="&nbsp;&nbsp;生 成  优  惠  码">
			<form id="editForm" action="${path }/back/coupon/export.shtml" method="post">
				<input type="hidden" name="id" value="${coupon.id }"/>
				<table width="100%" class="input tabContent table">
					<tr>
		    			<td width="30%" height="30" align="right">&nbsp;名称：&nbsp;&nbsp;</td>
		    			<td width="70%" align="left">${coupon.name }</td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;前缀：&nbsp;&nbsp;</td>
		    			<td align="left">${coupon.prefix }</td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;开始时间：&nbsp;&nbsp;</td>
		    			<td align="left"><fmt:formatDate value="${coupon.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;结束时间：&nbsp;&nbsp;</td>
		    			<td align="left"><fmt:formatDate value="${coupon.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;已生成数：&nbsp;&nbsp;</td>
		    			<td align="left">${totalCount }</td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;已使用数：&nbsp;&nbsp;</td>
		    			<td align="left">${usedCount }</td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;生成数：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="count" class="text"/>&nbsp;不填默认生成50条</td>
		    		</tr>
				</table>
		    	<table width="100%" align="center" cellpadding="0" cellspacing="0">
		      		<tr>
	      				<td width="60%" align="center" height="50">
	      					<a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">生      成</a>
	      					<a href="#" button="back" class="easyui-linkbutton" data-options="iconCls:'icon-back'">返      回</a>
	      				</td>
	      				<td width="40%"></td>
	      			</tr>
		    	</table>
		    </form>
		</div>
	</div>
</body>
</html>