<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>修改会员等级</title>
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
					"discount": {
						required: true,
						decimal:{
							length:3,
							scale:2
						}
					},
					"expValue": { 
						required: true,
						decimal:{
							length:18,
							scale:${fn:config("priceScale")}
						}
					},
					"gradeType": { required: true },
					"orderList": { integer:true }
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-edit" title="&nbsp;&nbsp;修  改  等  级">
			<form id="editForm" action="${path }/back/grade/edit.shtml" method="post">
		    	<input type="hidden" name="id" value="${grade.id }"/>
		    	<table class="table-edit" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			<tr>
		      				<td width="30%" align="right"><font color="red">*&nbsp;&nbsp;</font>等级名称：&nbsp;&nbsp;</td>
		      				<td width="70%" align="left"><input type="text" name="name" value="${grade.name }" class="text middle"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><font color="red">*&nbsp;&nbsp;</font>类型：&nbsp;&nbsp;</td>
		      				<td align="left">
								<select name="gradeType" class="select">
									<option value="">--请选择--</option>
									<option value="common" ${grade.gradeType=="common"?"selected='selected'":"" }>普通</option>
									<option value="system" ${grade.gradeType=="system"?"selected='selected'":"" }>系统</option>
								</select>
							</td>
		      			</tr>
		      			<tr>
		      				<td align="right"><font color="red">*&nbsp;&nbsp;</font>优惠比例：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="discount" value="${grade.discount }" class="text short"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><font color="red">*&nbsp;&nbsp;</font>消费金额：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="expValue" value="${fn:currency(grade.expValue,false) }" class="text short"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">排序：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" value="${grade.orderList }" class="text short"/></td>
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