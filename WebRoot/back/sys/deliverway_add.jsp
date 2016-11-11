<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>新增配送方式</title>
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
				 var width= $("html").width(); 
				 $mainPanel.panel('resize', { width : width }); 
			});
			$editForm.validate({
				rules: {
					"name": {
						required: true,
						maxlength: 100
					},
					"logistics.id": {
						required: true
					},
					"firstWeight": {
						required: true,
						decimal:{
							length:10,
							scale:2
						}
					},
					"firstPrice": {
						required: true,
						decimal:{
							length:18,
							scale:${fn:config("priceScale")}
						}
					},
					"conWeight": {
						required: true,
						decimal:{
							length:10,
							scale:2
						}
					},
					"conPrice": {
						required: true,
						decimal:{
							length:18,
							scale:${fn:config("priceScale")}
						}
					},
					"orderList": {integer:true}
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-add" title="&nbsp;&nbsp;新  增  配  送  方  式">
			<form id="editForm" action="${path }/back/deliverway/add.shtml" method="post">
		    	<table class="table-edit" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			<tr>
		      				<td width="30%" align="right"><font color="red">*&nbsp;&nbsp;</font>名称：&nbsp;&nbsp;</td>
		      				<td width="70%" align="left"><input type="text" name="name" class="text"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><font color='#ff0000'>*</font>&nbsp;物流公司：&nbsp;&nbsp;</td>
		      				<td align="left">
								<select name="logistics.id" class="select">
									<option value="">--请选择--</option>
									<c:if test="${logistics != null }">
										<c:forEach var="row" items="${logistics }">
											<option value="${row.id }">${row.name }</option>
										</c:forEach>
									</c:if>
								</select>
							</td>
		      			</tr>
		      			<tr>
		      				<td align="right"><font color='#ff0000'>*</font>&nbsp;首重：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="firstWeight" class="text short"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><font color='#ff0000'>*</font>&nbsp;首重费用：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="firstPrice" class="text short"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><font color='#ff0000'>*</font>&nbsp;续重：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="conWeight" class="text short"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><font color='#ff0000'>*</font>&nbsp;续重费用：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="conPrice" class="text short"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">排序：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" class="text short"/></td>
		      			</tr>
		      			<tr>
		      				<td height="60"></td>
		      				<td align="left">
		      					<a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保      存</a>
		      					<a href="#" button="back" class="easyui-linkbutton" data-options="iconCls:'icon-back'">返      回</a>
		      				</td>
		      			</tr>
		      		</tbody>
		    	</table>
		    </form>
		</div>
	</div>
</body>
</html>