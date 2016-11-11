<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>修改商品参数</title>
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

			var parameterIndex = ${parameter.parameterItems != null?fn:length(parameter.parameterItems):0};
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-edit" title="&nbsp;&nbsp;修  改  商  品  参  数">
			<form id="editForm" action="${path }/back/parameter/edit.shtml" method="post">
		    	<input type="hidden" name="id" value='${parameter.id }'/>
		    	<table class="table-edit" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			
		      			<tr>
		      				<td align="right"><font color="red">*&nbsp;&nbsp;</font>商品分类：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<select id="productCategory" name="productCategory.id" class="select">
									<option value="">--请选择--</option>
									<c:if test="${productCategorys != null}">
										<c:forEach var="row" items="${productCategorys}">
											<option value="${row.id }" ${row == parameter.productCategory ? 'selected="selected"' : '' }>
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
		      				<td width="70%" align="left"><input type="text" name="name" value="${parameter.name }" class="text-two"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">排序：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" value="${parameter.orderList }" field="number" class="text-num" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="center" colspan="2" height="40"><a href="#" id="addParameter" class="easyui-linkbutton" data-options="iconCls:'icon-add'">增加参数值</a></td>
		      			</tr>
		      		</tbody>
		    	</table>
		    	<table id="paramTable" width="700" align="center" cellpadding="0" cellspacing="0">
   					<tr name="head" bgcolor="#f2f2f2">
   						<td width="50%" height="30">名称</td>
   						<td width="30%">排序</td>
   						<td width="20%" align="center">操作</td>
   					</tr>
   					<c:if test="${parameter.parameterItems != null}">
   						<c:forEach var="row" items="${parameter.parameterItems}" varStatus="index">
   							<tr height="30">
								<td>
									<input type="hidden" name="parameterItems[${index.index }].id" value="${row.id }"/>
									<input type="text" name="parameterItems[${index.index }].name" value="${row.name }" class="text paramName" maxlength="255"/>
								</td>
								<td><input type="text" name="parameterItems[${index.index }].orderList" value="${row.orderList }" class="text short orderList" maxlength="10"/></td>
								<td align="center"><a href="javascript:;" class="deleteParameter">[删除]</a></td>
							</tr>
   						</c:forEach>
   					</c:if>
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