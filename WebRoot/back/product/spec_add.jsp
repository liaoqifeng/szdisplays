<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>新增商品规格</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $specTable = $("#specTable");
			var $specType = $("#specType");
			var $addSpec = $("#addSpec");
			var $deleteSepc = $("a.deleteSepc");
			var $selectImage = $(":button[name='selectImage']");
			var $editForm = $("#editForm");
			var $saveButton = $("#saveButton");

			var productSpecIndex = 0;
			
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
			$specType.change(function(){
				var $this = $(this);
				$specTable.find(":text.specUrl").prop("disabled",($this.val()!="image"));
				$specTable.find(":button.select").prop("disabled",($this.val()!="image"));
			});
			$addSpec.click(function(){
				var orderList = $specTable.find("tr").length;
				var disabled = ($specType.val()!="image")?'disabled="disabled"':'';
				var trHtml = ''+
					'<tr height="30">'+
						'<td>'+
							'<input type="text" name="specAttributes['+productSpecIndex+'].name" class="text middle specName" maxlength="255"/>'+
						'</td>'+
						'<td>'+
							'<input type="text" name="specAttributes['+productSpecIndex+'].url" '+disabled+' class="text specUrl" maxlength="255"/>&nbsp;'+
							'<input type="button" '+disabled+' class="button select" value="选择文件.." />'	+
						'</td>'+
						'<td><input type="text" name="specAttributes['+productSpecIndex+'].orderList" value="'+orderList+'" class="text short orderList" maxlength="10"/></td>'+
						'<td><a href="javascript:;" class="delete">[删除]</a></td>'+
					'</tr>';
				var $trHtml = $(trHtml);
				$specTable.append($trHtml);
				productSpecIndex++;
				var $select = $trHtml.find(".select");
				$select.browser({callback:function(data){
					$select.prev().val(data.content);
				}});
			});
			
			$specTable.on("click",".delete",function(){
				$(this).closest("tr").remove();
			});
			
			$.validator.addClassRules({
				specName: {required: true},
				orderList:{integer:true}
			});
			$editForm.validate({
				rules: {
					"name": {
						required: true,
						maxlength: 100
					},
					"orderList": { integer:true },
					"remark": { maxlength: 255 }
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-add" title="&nbsp;&nbsp;新  增  规  格">
			<form id="editForm" action="${path }/back/spec/add.shtml" method="post" enctype="multipart/form-data">
		    	<table class="table-edit" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			<tr>
		      				<td width="30%" align="right"><font color="red">*&nbsp;&nbsp;</font>规格：&nbsp;&nbsp;</td>
		      				<td width="70%" align="left"><input type="text" name="name" class="text"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">类型：&nbsp;&nbsp;</td>
		      				<td align="left">
								<select id="specType" name="specType" class="select">
									<option value="literal">文字</option>
									<option value="image">图片</option>
								</select>
							</td>
		      			</tr>
		      			<tr>
		      				<td align="right">备注：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="remark" class="text long" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">排序：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" class="text short" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="right" height="40"></td>
		      				<td align="left"><a href="#" id="addSpec" class="easyui-linkbutton" data-options="iconCls:'icon-add'">增加规格值</a></td>
		      			</tr>
		      		</tbody>
		    	</table>
		    	<table id="specTable" width="800" align="center" cellpadding="0" cellspacing="0">
   					<tr bgcolor="#f2f2f2">
   						<td width="25%" height="30">规格名称</td>
   						<td width="45%">规格值图片</td>
   						<td width="20%">排序</td>
   						<td width="10%">操作</td>
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