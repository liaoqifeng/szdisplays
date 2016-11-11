<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>新增资源</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $editForm = $("#editForm");
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
						required: true
					},
					"description": {
						maxlength: 100
					}
				},
				messages: {
					"name":{
						required: "请输入资源名称！"
					},
					"description":{
						maxlength:"资源描述不能超过100个字符！"
					}
				}
			});

			$("#saveButton").click(function(){
				$editForm.submit();
			});
		});
	</script>
</head>
<body>
	<div id="selectMenus" class="easyui-window" title="&nbsp;&nbsp;选择菜单" 
		data-options="iconCls:'icon-search',collapsible:false,minimizable:false,maximizable:false,closed:true" 
		style="width:400px;height:400px;padding:10px;">
		<div>
			<ul id="menuTree"></ul>
		</div>
		<div class="product-list-s-buttom">
			<a href="#" id="submitMenusButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确     定</a>
		    <a href="#" id="cancelMenusButton" class="easyui-linkbutton" data-options="iconCls:'icon-back'">取     消</a>
		</div></br>
	</div>
	<div id="mainBox" class="main-box">
		<div id="mainPanel" class="easyui-panel" iconCls="icon-save" title="新  增  资  源">
			<form id="editForm" action="${path }/back/resource/edit.shtml" method="post">
		    	<table class="table-edit" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			<tr>
		      				<td width="30%" align="right"><font color="red">*&nbsp;&nbsp;</font>资源名称：&nbsp;&nbsp;</td>
		      				<td width="70%" align="left"><input type="text" id="name" name="name" value="${valueMap.resource.name }" class="text middle" maxlength="30"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">资源路径：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" id="value" name="value" value="${valueMap.resource.value }" class="text long" maxlength="30"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">父级资源：&nbsp;&nbsp;</td>
		      				<td align="left">
								<select name="parentId">
		      						<option value="0" <c:if test="${valueMap.resource.parentId==0}">selected="selected"</c:if>>顶级资源</option>
		      						<c:if test="${valueMap.resources != null }">
		      							<c:forEach var="row" items="${valueMap.resources }" varStatus="status">
		      								<option value="${row.id }" <c:if test="${valueMap.resource.parentId==row.id}">selected="selected"</c:if>>${row.name }</option>
		      							</c:forEach>
		      						</c:if>
		      					</select>
							</td>
		      			</tr>
		      			<tr>
		      				<td align="right">排序：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" id="orderList" name="orderList" value="${valueMap.resource.orderList }" class="text middle" maxlength="30"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">角色描述：&nbsp;&nbsp;</td>
		      				<td align="left"><textarea id="description" rows="5" cols="40" name="description">${valueMap.resource.description }</textarea></td>
		      			</tr>
		      			<tr>
		      				<td height="60"></td>
		      				<td align="left">
		      					<a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保      存</a>&nbsp;&nbsp;
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