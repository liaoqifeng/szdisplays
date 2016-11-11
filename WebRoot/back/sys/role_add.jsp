<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>新增角色</title>
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
				}
			});

			$("#saveButton").click(function(){
				$editForm.submit();
			});
		});
	</script>
</head>
<body>
	<div id="mainBox" class="main-box">
		<div id="mainPanel" class="easyui-panel" iconCls="icon-save" title="新  增  角  色">
			<form id="editForm" action="${path }/back/role/add.shtml" method="post">
		    	<table class="table-edit" width="100%" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			<tr>
		      				<td width="20%" align="right"><font color="red">*&nbsp;&nbsp;</font>角色名称：&nbsp;&nbsp;</td>
		      				<td width="80%" align="left"><input type="text" id="name" name="name" class="text middle" maxlength="30"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">角色标识：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" id="value" name="value" class="text middle" maxlength="30"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">角色描述：&nbsp;&nbsp;</td>
		      				<td align="left" height="100"><textarea id="description" rows="5" cols="40" name="description"></textarea></td>
		      			</tr>
		      			<tr>
		      				<td align="right">启用权限：&nbsp;&nbsp;</td>
		      				<td align="left">
								<fieldset>
									<table>
										<c:if test="${auths != null }">
										<c:forEach var="row" items="${auths }" varStatus="status">
				      						<tr>
												<td width="60" valign="middle">${row.name }:</td>
												<td valign="middle">
													<c:if test="${row.nodes != null }">
					      								<c:forEach var="item" items="${row.nodes }" varStatus="index">
					      									<input type="checkbox" name="auths"  value="${item.perms }"/>${item.name }
					      								</c:forEach>
					      							</c:if>
												</td>
											</tr>
			      						</c:forEach>
			      						</c:if>
									</table>
		      					</fieldset>
							</td>
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