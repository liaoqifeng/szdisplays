<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>商品分类列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		function doEdit(id){
			window.location.href='${path}/back/productCategory/edit/'+id+'.shtml';
		}
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			$mainTable.treegrid({
				url:'${path }/back/productCategory/list/pager.shtml',
				idField:'id',
				treeField:'name',
				toolbar:$toolBox,
				rownumbers: true,
				width:'auto',
				height:$mainBox.height(),
				columns:[[
				    {checkbox:true,field:'id',align:'center'},
					{title:'名称',field:'name',align:'left',width:'150'},
					{title:'标题',field:'title',align:'center',width:'150'},
					{title:'关键字',field:'keywords',align:'center',width:'150'},
					{title:'描述',field:'describtion',align:'center',width:'150'},
					{title:'排序',field:'orderList',align:'center',width:'150'},
					{title:'操作',field:'operator',align:'center',width:'100',
						formatter: function(value,row,index){
							var html = '<a href="#" onclick="doEdit(' + row.id + ')" style=" text-decoration:none;">[编辑]</a>';
	                   		return html;
						}
					}
				]],
				pageNumber:1,
				pageSize:20,
				loadMsg:"正在加载,请等待...",
				onBeforeLoad:function(row,param){ 
					if(row) 
						$(this).treegrid('options').url='${path }/back/productCategory/list/pager.shtml?parentId='+row.id; 
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
					$.post("${path}/back/productCategory/delete.shtml",{id:rows.id},function(data){
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
					$.messager.alert('提示','请选择数据!');
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
					<a href="${path }/back/productCategory/view.shtml" class="easyui-linkbutton" iconCls="icon-add" plain="true">新    增</a>
					<a href="#" button="delete" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">删    除</a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷    新</a>
				</td>
				<td align="right"></td>
			</tr>
		</table>
	</div>
</body>
</html>