<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>地区列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			var $doAddArea = $("#doAddArea");
			var $addButton = $("a[button='add']");
			var $sureButton = $("#sureButton");
			var $cancelButton = $("#cancelButton");
			var $parentArea = $("#parentArea");
			var $addArea = $("#addArea");
			var $addForm = $("#addForm");
			var $parentId = $("#parentId");
			var $doEditArea = $("#doEditArea");
			var $name = $("#name");
			var $editSureButton = $("#editSureButton");
			var $editCancelButton = $("#editCancelButton");
			
			$mainTable.treegrid({
				url:'${path }/back/area/list/pager.shtml',
				idField:'id',
				treeField:'name',
				toolbar:$toolBox,
				rownumbers: true,
				width:'auto',
				checkOnSelect:true,
				singleSelect:true,
				height:$mainBox.height(),
				columns:[[
					{checkbox:true,field:'checkbox',align:'center'},
					{title:'编号',field:'id',align:'center',width:'150'},
					{title:'地区名称',field:'name',width:'400'},
					{title:'创建时间',field:'createDate',align:'center',width:'150'},
					{title:'修改时间',field:'modifyDate',align:'center',width:'150'},
					{title:'操作',field:'operator',align:'center',width:'100',
						formatter: function(value,row,index){
							var html = '<a href="#" class="edit" data-id="'+row.id+'" data-name="'+row.name+'" style=" text-decoration:none;">[编辑]</a>';
	                   		return html;
						}
					}
				]],
				loadMsg:"正在加载,请等待...",
				onBeforeLoad:function(row,param){ 
					if(row) 
						$(this).treegrid('options').url='${path }/back/area/list/pager.shtml?parentId='+row.id; 
				},
				onLoadSuccess:function(){
					$mainBox.find("a.edit").unbind("click").bind("click",function(){
						var $this = $(this);
						$doEditArea.window("open");
						$doEditArea.data("id",$this.data("id"));
						$doEditArea.data("name",$this.data("name"));
						$name.val($this.data("name"));
						return false;
					});
				}
			});
			$(window).resize(function() { 
				 var width= $mainBox.width(); 
				 $mainTable.datagrid('resize', { width : width }); 
			});
			$doAddArea.window({
				title:'&nbsp;&nbsp;添加地区',
				iconCls:'icon-save',
				modal:true,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				closed:true
			});
			$doEditArea.window({
				title:'&nbsp;&nbsp;修改地区',
				iconCls:'icon-save',
				modal:true,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				closed:true
			});
			$editCancelButton.click(function(){
				$doEditArea.window("close");
			});
			$editSureButton.click(function(){
				var id = $doEditArea.data("id");
				var name = $doEditArea.data("name");
				if($name.val() != "" && $name.val() != name){
					$.post("${path}/back/area/edit.shtml",{id:id,name:$name.val()},function(data){
						$.message(data);
						if(data.type == "success"){
							var parent = $mainTable.treegrid('getParent', id);
							if(parent == null || parent == undefined){
								$mainTable.treegrid('reload');
							}else{
								$mainTable.treegrid('reload',parent.id);
							}
						}
						$doEditArea.window("close");
					});
				}else{
					$doEditArea.window("close");
				}
				
			});
			$("a[button='add']").click(function(){
				var row = $mainTable.treegrid('getSelected');
				if(row == null){
					$parentArea.text("顶级地区");
				}else{
					$parentArea.text(row.name);
					$parentId.val(row.id);
				}
				
				$doAddArea.show().window("open");
			});
			$addArea.click(function(){
				var $areaRow = $("#areaRow").clone();
				$doAddArea.find("table").append($areaRow.show());
			});
			$doAddArea.on("click",".delete",function(){
				$(this).closest("tr").remove();
			});
			$cancelButton.click(function(){
				$doAddArea.find(":text[name='name']").val("");
				$parentArea.text("");
				parentId.val("");
				$doAddArea.window("close");
			});
			$sureButton.click(function(){
				$.post("${path}/back/area/add.shtml",$addForm.serialize(),function(data){
					$.message(data);
					if(data.type == "success"){
						var row = $mainTable.treegrid('getSelected');
						if(row == null || row == undefined){
							$mainTable.treegrid('reload');
						}else{
							$mainTable.treegrid('reload',row.id);
						}
					}
					$doAddArea.window("close");
				});
			});
			$("a[button='delete']").click(function(){
				var row = $mainTable.treegrid('getSelected');
				if(row != null){
					$.post("${path}/back/area/delete.shtml",{id:row.id},function(data){
						$.message(data);
						if(data.type == "success"){
							var parent = $mainTable.treegrid('getParent', row.id);
							if(parent == null || parent == undefined){
								$mainTable.treegrid('reload');
							}else{
								$mainTable.treegrid('reload',parent.id);
							}
						}
						$doAddArea.window("close");
					});
				}else{
					$.messager.alert('提示','请选择数据');
				}
			});
		});
	</script>
</head>
<body>
	
	<div id="doAddArea" style="width:350px;height:250px;padding:10px;display: none;">
		<form id="addForm" action="${path}/back/area/add.shtml" method="post">
		<input type="hidden" id="parentId" name="parentId" value=""/>
		<div>
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td width="90" height="40" align="right">父级地区:</td>
					<td width="150">
						&nbsp;&nbsp;<font id="parentArea" color="green"></font>&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>&nbsp;&nbsp;<a href="#" id="addArea"><b>添加</b></a></td>
				</tr>
				<tr>
					<td width="90" height="30" align="right">地区名称:</td>
					<td>&nbsp;&nbsp;<input type="text" name="names" class="text middle"/></td>
					<td></td>
				</tr>
				<tr id="areaRow" style="display: none;">
					<td width="90" height="30" align="right">地区名称:</td>
					<td>&nbsp;&nbsp;<input type="text" name="names" class="text middle"/></td>
					<td>&nbsp;&nbsp;<a href="#" class="delete">删除</a></td>
				</tr>
			</table>
		</div>
		<div style="padding-top: 30px;clear: both;text-align: center;">
			<a href="#" id="sureButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确     定</a>
		    &nbsp;&nbsp;<a href="#" id="cancelButton" class="easyui-linkbutton" data-options="iconCls:'icon-back'">取     消</a>
		</div>
		</form>
	</div>
	<div id="doEditArea" style="width:350px;height:150px;padding:10px;">
		<div>
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td width="90" height="30" align="right">地区名称:</td>
					<td>&nbsp;&nbsp;<input type="text" id="name" class="text middle"/></td>
				</tr>
			</table>
		</div>
		<div style="padding-top: 30px;clear: both;text-align: center;">
			<a href="#" id="editSureButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确     定</a>
		    &nbsp;&nbsp;<a href="#" id="editCancelButton" class="easyui-linkbutton" data-options="iconCls:'icon-back'">取     消</a>
		</div>
	</div>
	<div id="mainBox" class="main-box" style="width: 100%;">
		<table title="" id="main-table"></table>
	</div>
	<div id="toolBox">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td height="50">
					<a href="#" button="add" class="easyui-linkbutton" iconCls="icon-add" plain="true">新    增</a>
					<a href="#" button="delete" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">删    除</a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷    新</a>
				</td>
				<td align="right"></td>
			</tr>
		</table>
	</div>
</body>
</html>