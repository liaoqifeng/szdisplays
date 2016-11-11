<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
    <title>修改支付方式</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<link rel="stylesheet" href="${path }/common/kindeditor/themes/default/default.css" />
	<script type="text/javascript" src="${path }/common/kindeditor/kindeditor-min.js" charset="utf-8"></script>
	<script type="text/javascript" src="${path }/common/kindeditor/lang/zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="${path }/back/js/datePicker/WdatePicker.js" charset="utf-8"></script>
</head>
<body>
	<div id="mainBox" class="main-box">
		<div id="mainPanel" class="easyui-panel" iconCls="icon-add" title="&nbsp;&nbsp;修  改  支  付  方  式">
			<form id="editForm" action="${path }/back/paymentway/edit.shtml" method="post">
			<input type="hidden" name="id" value="${paymentway.id}"/>
		    	<table class="table-edit" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			<tr>
		      				<td width="15%" align="right"><font color="red">*&nbsp;&nbsp;</font>名称：&nbsp;&nbsp;</td>
		      				<td width="85%" align="left"><input type="text" name="name" value="${paymentway.name}" class="text"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">类型：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<select name="type">
		      						<c:if test="${types != null}">
		      						<c:forEach var="row" items="${types}">
		      						<option value="${row }" ${paymentway.type == row?'selected="selected"':'' }><fmt:message key="PaymentwayType.${row }"/></option>
		      						</c:forEach>
		      						</c:if>
		      					</select>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right">超时时间：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<input type="text" name="timeout" value="${paymentway.timeout }" class="text short"/>&nbsp;
		      					<font color='#FFB042'>单位：分钟  留空表示永久有效</font>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right">支持配送方式：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<c:if test="${deliverways != null}">
		      					<c:forEach var="row" items="${deliverways}">
		      					<input type="checkbox" name="deliverwaysIds" ${fn:containsObject(paymentway.deliverways,row) ? 'checked="checked"' : ''}  value="${row.id }"/>${row.name }&nbsp;&nbsp;&nbsp;&nbsp;
		      					</c:forEach>
		      					</c:if>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right">图标：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<input type="text" id="image" name="image" value="${paymentway.image }" class="text long"/>
		      					<input type="button" id="browserButton" class="button" value="选择文件.." />&nbsp;
		      					<a href="${paymentway.image }" target="_blank">查看</a>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right">备注：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="remark" value="${paymentway.remark }" class="text long"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">排序：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" value="${paymentway.orderList }" class="text short"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right" valign="top">描述：&nbsp;&nbsp;</td>
		      				<td align="left">
		      				 	<textarea name="describtion" style="width:700px;height:300px;">${paymentway.describtion }</textarea>
		      				</td>
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
<script type="text/javascript">
	KindEditor.ready(function(K) {
		var editor1 = K.create('textarea[name="describtion"]', {
			uploadJson : '${path }/back/file/upload.shtml',
			fileManagerJson : '${path }/back/file/browser.shtml',
			allowFileManager : true,
			afterCreate : function() {},
			afterBlur: function(){this.sync();}
		});
	});
</script>
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
			var width= $mainBox.width(); 
			var height=  $mainBox.height();
			$mainPanel.panel('resize', { width : width,height:height }); 
		});
		$("#browserButton").browser({callback:function(data){
			$("#image").val(data.content);
		}});
		$editForm.validate({
			rules: {
				"name": {
					required: true,
					maxlength: 255
				},
				"timeout":{integer:true },
				"orderList": {integer:true }
			}
		});
		$saveButton.click(function(){
			$editForm.submit();
		});
	});
</script>
</html>