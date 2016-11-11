<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>新增商品参数</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $paramTable = $("#paramTable");
			var $addParameter = $("#addParameter");
			var $deleteParameter = $("a.deleteParameter");
			var $editForm = $("#editForm");
			var $saveButton = $("#saveButton");

			var parameterIndex = 0;
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
				 $mainPanel.datagrid('resize', { width : width,height:height }); 
			});
			$addParameter.click(function(){
				var orderList = $paramTable.find("tr").size();
				var trHtml = ''+
					'<tr height="30">'+
						'<td><input type="text" name="parameterItems['+parameterIndex+'].name" class="text paramName" maxlength="255"/></td>'+
						'<td><input type="text" name="parameterItems['+parameterIndex+'].orderList" value="'+orderList+'" class="text short orderList" maxlength="10"/></td>'+
						'<td align="center"><a href="javascript:;" class="deleteParameter">[删除]</a></td>'+
					'</tr>';
				$paramTable.append(trHtml);
				parameterIndex++;
			});
			$paramTable.on("click",".deleteParameter",function(){
				$(this).closest("tr").remove();
			});
			$.validator.addClassRules({
				paramName: {required: true},
				orderList:{integer:true}
			});
			$editForm.validate({
				rules: {
					"productCategory.id": { required: true },
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-add" title="&nbsp;&nbsp;新  增  商  品  参  数">
			<form id="editForm" action="${path }/back/parameter/add.shtml" method="post">
		    	<table class="table-edit" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			
		      			<tr>
		      				<td align="right"><font color="red">*&nbsp;&nbsp;</font>商品分类：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<select id="productCategory" name="productCategory.id" class="select">
									<option value="">--请选择--</option>
									<c:if test="${productCategorys != null}">
										<c:forEach var="row" items="${productCategorys}">
											<option value="${row.id }">
												<c:forEach var="i" begin="0" end="${row.level}" step="1">
												&nbsp;&nbsp;
												</c:forEach>
												${row.name }
											</option>
										</c:forEach>
									</c:if>
								</select>
							</td>
		      			</tr>
		      			<tr>
		      				<td width="30%" align="right"><font color="red">*&nbsp;&nbsp;</font>参数名称：&nbsp;&nbsp;</td>
		      				<td width="70%" align="left"><input type="text" name="name" class="text"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">排序：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" class="text short"/></td>
		      			</tr>
		      			<tr>
		      				<td align="center" colspan="2" height="40"><a href="#" id="addParameter" class="easyui-linkbutton" data-options="iconCls:'icon-add'">增加参数值</a></td>
		      			</tr>
		      		</tbody>
		    	</table>
		    	<table id="paramTable" width="800" align="center" cellpadding="0" cellspacing="0">
   					<tr bgcolor="#f2f2f2">
   						<td width="50%" height="30">名称</td>
   						<td width="30%">排序</td>
   						<td width="20%" align="center">操作</td>
   					</tr>
   				</table>
   				<table align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		   				<tr>
		      				<td height="60" width="30%"></td>
		      				<td align="left" width="70%">
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