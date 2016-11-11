<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>新增标签</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $editForm = $("#editForm");
			var $saveButton = $("#saveButton");
			var $browserButton = $("#browserButton");
			var $image = $("#image");
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
			$browserButton.browser({callback:function(data){
				$image.val(data.content);
			}});
			$editForm.validate({
				rules: {
					"name": {
						required: true,
						maxlength: 100
					},
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-add" title="&nbsp;&nbsp;新  增  标  签">
			<form id="editForm" action="${path }/back/tag/add.shtml" method="post">
		    	<table class="table-edit" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			<tr>
		      				<td width="30%" align="right"><font color="red">*&nbsp;&nbsp;</font><fmt:message key='Common.label.name'/>：&nbsp;&nbsp;</td>
		      				<td width="70%" align="left"><input type="text" name="name" class="text middle"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key='Common.label.type'/>：&nbsp;&nbsp;</td>
		      				<td align="left">
								<select name="type" class="select">
									<c:forEach var="row" items="${types}">
									<option value="${row }"><fmt:message key="TagType.${row}"/></option>
									</c:forEach>
								</select>
							</td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key='Common.label.icon'/>：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<input type="text" id="image" name="image" class="text"/>
		      					<input type="button" id="browserButton" class="button" value="<fmt:message key='Common.choose'/>" />
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key='Common.label.remark'/>：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="remark" class="text"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key='Common.label.orderList'/>：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" class="text short" /></td>
		      			</tr>
		      			<tr>
		      				<td height="60"></td>
		      				<td align="left">
		      					<a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'"><fmt:message key='Common.button.save'/></a>
		      					&nbsp;&nbsp;<a href="#" button="back" class="easyui-linkbutton" data-options="iconCls:'icon-back'"><fmt:message key='Common.button.return'/></a>
		      				</td>
		      			</tr>
		      		</tbody>
		    	</table>
		    </form>
		</div>
	</div>
</body>
</html>