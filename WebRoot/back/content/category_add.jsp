<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>新增文章分类</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $editForm = $("#editForm");
			var $brands = $("#brands");
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
					"name": {
						required: true,
						maxlength: 100
					},
					"orderList": { integer:true },
					"title": { maxlength: 255 },
					"keywords": { maxlength: 255 },
					"describtion": { maxlength: 255 }
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-add" title="&nbsp;&nbsp;新  增  文  章  分  类">
			<form id="editForm" action="${path }/back/articleCategory/add.shtml" method="post">
		    	<table class="table-edit" align="center">
		      		<tbody>
		      			<tr>
		      				<td width="30%" align="right"><font color="red">*&nbsp;&nbsp;</font><fmt:message key='Common.label.name'/>：&nbsp;&nbsp;</td>
		      				<td width="70%" align="left"><input type="text" name="name" class="text"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key='Common.label.parentCategory'/>：&nbsp;&nbsp;</td>
		      				<td align="left">
								<select id="articleCategory" name="parentId" class="select">
									<option value="">--<fmt:message key='Common.message.select'/>--</option>
									<c:if test="${categorys != null}">
										<c:forEach var="row" items="${categorys}">
											<option value="${row.id }">
												<c:forEach var="i" begin="0" end="${row.level}" step="1">
												&nbsp;&nbsp;
												</c:forEach>
												${row.name }
											</option>
										</c:forEach>
									</c:if>
								</select>
								&nbsp;&nbsp;
								(<span><fmt:message key='Common.message.defaultTopCategory'/></span>)		    				
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key="Common.label.orderList"/>：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" class="text short" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key="Common.label.seotitle"/>：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="title" class="text long" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key="Common.label.seokeyword"/>：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="keywords" class="text long" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key="Common.label.seodiscription"/>：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="describtion" class="text long" value=""/></td>
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