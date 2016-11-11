<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>新增员工</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $editForm = $("#editForm");
			var $tab = $("#tab");
			var $saveButton = $("#saveButton");
			var $areaId = $("#areaId");
			var $questionId = $("#questionId");
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
			$areaId.lSelect({
				url: "${path}/back/common/area.shtml"
			});
			$editForm.validate({
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
					"confirmPassword": { equalTo:"#password" },
					"email": {
						required: true,
						email: true ,
						remote: {
							url: "${path}/back/member/checkEmail.shtml",
							cache: false
						}
					},
					"deposit":{
						min: 0,
						decimal: {
							length:18,
							scale:${fn:config("priceScale")}
						}
					},
					"score":{
						digits: true
					},
					"phone":{
						required: true,
						isMobileOrTel:true
					},
					"qq":{ integer: true },
					"answer":{ isNullByTarget: "#questionId" },
					"areaId":{ required: true }
				},
				messages: {
					username: {
						pattern: "非法字符",
						remote: "用户名已存在"
					},
					password: { pattern: "非法字符" },
					email: { remote: "邮箱已存在" }
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-edit" title="&nbsp;&nbsp;新  增  员  工">
			<form id="editForm" action="${path }/back/member/add.shtml" method="post">
		    	<ul id="tab" class="tab">
					<li><input value="基本信息" type="button" /></li>
					<li><input value="个人资料" type="button" /></li>
				</ul>
				<table width="100%" class="input tabContent table">
					<tr>
		    			<td width="30%" height="30" align="right"><font color="red">*</font>&nbsp;用户名：&nbsp;&nbsp;</td>
		    			<td width="70%" align="left"><input type="text" name="username" value="${username }" class="text" maxlength="50"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right"><font color="red">*</font>&nbsp;密&nbsp;&nbsp;码：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="password" id="password" name="password" class="text" maxlength="50"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">重复密码：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="password" name="confirmPassword" class="text" maxlength="50"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right"><font color="red">*</font>&nbsp;E-Mail：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" id="email" name="email" value="${member.email }" class="text" maxlength="255"/></td>
		    		</tr>
		    		<tr class="hidden">
		    			<td height="30" align="right">&nbsp;会员等级：&nbsp;&nbsp;</td>
		    			<td align="left">
		    				<select name="grade.id">
		    					<c:if test="${grades != null}">
		    						<c:forEach var="row" items="${grades}">
		    						<option value="${row.id }" ${member.grade == row ? 'selected="selected"' : '' }>${row.name }</option>
		    						</c:forEach>
		    					</c:if>
		    				</select>
		    			</td>
		    		</tr>
		    		<tr class="hidden">
		    			<td height="30" align="right"><font color="red">*</font>&nbsp;余额：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="deposit" value="0" class="text middle" maxlength="20"/></td>
		    		</tr>
		    		<tr class="hidden">
		    			<td height="30" align="right"><font color="red">*</font>&nbsp;积分：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="score" value="0" class="text middle" maxlength="20"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;用户状态：&nbsp;&nbsp;</td>
		    			<td align="left">
		    				<select name="status">
		    					<c:if test="${statusArray != null}">
		    						<c:forEach var="row" items="${statusArray}">
		    						<option value="${row }" ${member.status == row ? 'selected="selected"' : '' }><fmt:message key="MemberStatus.${row }"/></option>
		    						</c:forEach>
		    					</c:if>
		    				</select>
		    			</td>
		    		</tr>
				</table>
	   			<table width="100%" class="input tabContent table">
	   				<tr>
		    			<td width="30%" align="right">&nbsp;真实姓名：&nbsp;&nbsp;</td>
		    			<td width="70%" align="left"><input type="text" name="realname" value="${member.realname }" class="text middle" maxlength="255"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;性别：&nbsp;&nbsp;</td>
		    			<td align="left">
		    				<input type="radio" name="gender" value="male" ${member==null?"checked='checked'":""} ${member.gender=='male'?"checked='checked'":"" }/>&nbsp;&nbsp;男
		    				<input type="radio" name="gender" value="female" ${member.gender=='female'?"checked='checked'":"" }/>&nbsp;&nbsp;女
		    			</td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right"><font color="red">*</font>&nbsp;联系电话：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="phone" value="${member.phone }" class="text" maxlength="255"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;QQ：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="qq" value="${member.qq }" field="number" class="text" maxlength="255"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;邮编：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="zipCode" value="${member.zipCode }" field="number" class="text" maxlength="255"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right"><font color="red">*</font>&nbsp;地区：&nbsp;&nbsp;</td>
		    			<td align="left">
							<input type="hidden" id="areaId" name="areaId" value="${member.area==null?'':member.area.id }" treePath="${member.area==null?'':member.area.path }" />
		    			</td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">详细地址：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="address" value="${member.address }" class="text long" maxlength="255"/></td>
		    		</tr>
		    		<tr class="hidden">
		    			<td height="30" align="right">安全问题：&nbsp;&nbsp;</td>
		    			<td align="left">
		    				<select id="questionId" name="question.id" class="select">
								<option value="">--请选择--</option>
		    					<c:if test="${questions != null }">
		    						<c:forEach var="row" items="${questions }">
		    							<option value="${row.id }" <c:if test="${member.question.id==row.id }">selected="selected"</c:if>>${row.value }</option>
		    						</c:forEach>
		    					</c:if>
							</select>
		    			</td>
		    		</tr>
		    		<tr class="hidden">
		    			<td height="30" align="right">&nbsp;问题回答：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="answer" value="${member.answer }" class="text long" maxlength="255"/></td>
		    		</tr>
	    		</table>
		    	<table width="100%" align="center" cellpadding="0" cellspacing="0">
		      		<tr>
	      				<td width="60%" align="center" height="50">
	      					<a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保      存</a>
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