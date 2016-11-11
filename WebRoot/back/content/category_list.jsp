<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>文章分类列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		function doEdit(id){
			window.location.href='${path}/back/articleCategory/edit/'+id+'.shtml';
		}
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			$mainTable.treegrid({
				url:'${path }/back/articleCategory/list/pager.shtml',
				idField:'id',
				treeField:'name',
				toolbar:$toolBox,
				rownumbers: true,
				width:'auto',
				height:$mainBox.height(),
				columns:[[
				    {checkbox:true,field:'id',align:'center'},
					{title:'${fn:call("Common.label.name")}',field:'name',align:'center',width:'150'},
					{title:'${fn:call("Common.label.seotitle")}',field:'title',align:'center',width:'150'},
					{title:'${fn:call("Common.label.seokeyword")}',field:'keywords',align:'center',width:'150'},
					{title:'${fn:call("Common.label.seodiscription")}',field:'describtion',align:'center',width:'150'},
					{title:'${fn:call("Common.label.orderList")}',field:'orderList',align:'center',width:'150'},
					{title:'${fn:call("Common.operator")}',field:'operator',align:'center',width:'100',
						formatter: function(value,row,index){
							var html = '<a href="#" onclick="doEdit(' + row.id + ')" style=" text-decoration:none;">[${fn:call("Common.edit")}]</a>';
	                   		return html;
						}
					}
				]],
				pageNumber:1,
				pageSize:20,
				loadMsg:"${fn:call('Common.message.loading')}",
				onBeforeLoad:function(row,param){ 
					if(row) 
						$(this).treegrid('options').url='${path }/back/articleCategory/list/pager.shtml?parentId='+row.id; 
				}
			});
			$(window).resize(function() { 
				 var width= $mainBox.width(); 
				 var height=  $mainBox.height();
				 $mainTable.datagrid('resize', { width : width,height:height }); 
			});
			$("a[button='delete']").click(function(){
				
				var rows = $mainTable.datagrid('getSelected');
				if(rows != null && rows != ""){
					$.post("${path}/back/articleCategory/delete.shtml",{id:rows.id},function(data){
						$.message(data);
						if(data.type == "success"){
							var parent = $mainTable.treegrid('getParent',rows.id);
							if(parent == null){
								$mainTable.treegrid('reload');
							}else{
								$mainTable.treegrid('reload',parent.id);
							}
						}
					});
				}else{
					$.messager.alert("${fn:call('Common.message.tips')}","${fn:call('Common.message.dataNotNull')}");
				}
			});
		});
	</script>
</head>
<body>
	<div id="mainBox" class="main-box" style="width: 100%;">
		<table title="" id="main-table"></table>
	</div>
	<div id="toolBox">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td height="50">
					<a href="${path }/back/articleCategory/view.shtml" class="easyui-linkbutton" iconCls="icon-add" plain="true"><fmt:message key="Common.button.add"/></a>
					<a href="#" button="delete" class="easyui-linkbutton" iconCls="icon-cancel" plain="true"><fmt:message key="Common.button.delete"/></a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true"><fmt:message key="Common.button.refresh"/></a>
				</td>
				<td align="right"></td>
			</tr>
		</table>
	</div>
</body>
</html>