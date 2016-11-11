<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>新增管理员</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $menuTree = $("#menuTree");
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
			
			$("#editForm").validate({
				rules: {
					"username":{
						required: true,
						pattern: /^[0-9a-z_A-Z\u4e00-\u9fa5]+$/,
						minlength: ${fn:config("usernameMinLength")},
						maxlength: ${fn:config("usernameMaxLength")},
						remote: {
							url: "${path}/back/member/checkUserName.shtml",
							cache: false
						}
					},
					"password": {
						required: true,
						pattern: /^[^\s&\"<>]+$/,
						maxlength: ${fn:config("passwordMaxLength")},
						minlength: ${fn:config("passwordMinLength")}
					},
					"confirmPassword": { equalTo:"#password" }
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-add" title="新  增  管  理  员">
			<form id="editForm" action="${path }/back/admin/add.shtml" method="post">
		    	<table class="table-edit" width="100%" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			<tr>
		      				<td width="30%" align="right"><font color="red">*&nbsp;&nbsp;</font>用户名：&nbsp;&nbsp;</td>
		      				<td width="70%" align="left"><input type="text" name="username" class="text middle"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><font color="red">*&nbsp;&nbsp;</font>密码：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="password" id="password" name="password" class="text middle"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><font color="red">*&nbsp;&nbsp;</font>确认密码：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="password" name="confirmPassword" class="text middle"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">真实姓名：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="realName" class="text middle" /></td>
		      			</tr>
		      			<tr>
		      				<td align="right">角色管理：&nbsp;&nbsp;</td>
		      				<td align="left">
	      						<c:if test="${roles != null }">
		      						<c:forEach var="row" items="${roles }" varStatus="status">
		      							<label><input type="checkbox" name="roleIds" value="${row.id }" />${row.name }</label>
		      						</c:forEach>
	      						</c:if>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right">是否启用：&nbsp;&nbsp;</td>
		      				<td align="left">
								<input type="radio" name="isAccountEnabled" value="true" checked/>是
								<input type="radio" name="isAccountEnabled" value="false" />否
							</td>
		      			</tr>
		      			<tr>
		      				<td align="right">是否锁定：&nbsp;&nbsp;</td>
		      				<td align="left">
								<input type="radio" name="isAccountLocked" value="true"/>是
								<input type="radio" name="isAccountLocked" value="false" checked/>否
							</td>
		      			</tr>
		      			<tr>
		      				<td height="60"></td>
		      				<td align="left">
		      					<a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保      存</a>
		      					&nbsp;&nbsp;<a href="#" button="back" class="easyui-linkbutton" data-options="iconCls:'icon-back'">返      回</a>
		      				</td>
		      			</tr>
		      		</tbody>
		    	</table>
		    </form>
		</div>
	</div>
</body>
</html>