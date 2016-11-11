<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>修改商品属性</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $propertyTable = $("#propertyTable");
			var $addProperty = $("#addProperty");
			var $deleteProperty = $("a.deleteProperty");
			var $editForm = $("#editForm");
			var $saveButton = $("#saveButton");

			var propertyIndex = ${property.options != null?fn:length(property.options):0};
			
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
			$addProperty.click(function(){
				var orderList = $propertyTable.find("tr").size();
				var trHtml = ''+
					'<tr height="30">'+
						'<td><input type="text" name="options" class="text propertyName" maxlength="255"/></td>'+
						'<td align="center"><a href="javascript:;" class="deleteProperty">[删除]</a></td>'+
					'</tr>';
				$propertyTable.append(trHtml);
				propertyIndex++;
			});
			$propertyTable.on("click",".deleteProperty",function(){
				$(this).closest("tr").remove();
			});
			$.validator.addClassRules({
				propertyName: {required: true}
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-edit" title="&nbsp;&nbsp;修  改  商  品  属  性">
			<form id="editForm" action="${path }/back/property/edit.shtml" method="post">
		    	<input type="hidden" name="id" value='${property.id }'/>
		    	<input type="hidden" name="productCategory.id" value="${property.productCategory.id}"/>
		    	<input type="hidden" name="propertyIndex" value='${property.propertyIndex }'/>
		    	<table class="table-edit" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			<tr>
		      				<td align="right"><font color="red">*&nbsp;&nbsp;</font>商品分类：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					${property.productCategory.name }
							</td>
		      			</tr>
		      			<tr>
		      				<td width="30%" align="right"><font color="red">*&nbsp;&nbsp;</font>属性名称：&nbsp;&nbsp;</td>
		      				<td width="70%" align="left"><input type="text" name="name" value="${property.name }" class="text"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">排序：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" value="${property.orderList }" field="number" class="text short" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="center" colspan="2" height="40"><a href="javascript:;" id="addProperty" class="easyui-linkbutton" data-options="iconCls:'icon-add'">增加参数值</a></td>
		      			</tr>
		      		</tbody>
		    	</table>
		    	<table id="propertyTable" width="800" align="center">
   					<tr bgcolor="#f2f2f2">
   						<td width="50%" height="30">名称</td>
   						<td width="20%" align="center">操作</td>
   					</tr>
   					<c:if test="${property.options != null}">
   						<c:forEach var="row" items="${property.options}" varStatus="index">
   							<tr height="30">
								<td><input type="text" name="options" value="${row }" class="text propertyName" maxlength="255"/></td>
								<td align="center"><a href="javascript:;" class="deleteProperty">[删除]</a></td>
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