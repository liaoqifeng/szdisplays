<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>菜单列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		function doEdit(id){
			window.location.href='${path}/back/role/edit/'+id+'.shtml';
		}
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			$mainTable.treegrid({
				url:'${path }/back/nodes/list/pager.shtml',
				toolbar:$toolBox,
				width:'auto',
				height:$mainBox.height(),
				singleSelect:true,
				columns:[[
					{title:'ID',field:'id',align:'center',width:'80'},
					{title:'名称',field:'name',align:'center',width:'200'},
					{title:'URL',field:'url',align:'left',width:'200'},
					{title:'是否发布',field:'isPublish',align:'center',width:'200',
						formatter: function(value,row,index){
						if(value)
							return "<font color='green'>是</font>";
						else
							return "<font color='red'>否</font>";
					}},
					{title:'排序',field:'orderList',align:'center',width:'100'},
					{title:'操作',field:'operator',align:'center',width:'100',
						formatter: function(value,row,index){
							var html = '<a href="#" onclick="doEdit(' + row.id + ')" style=" text-decoration:none;">[编辑]</a>';
	                   		return html;
						}
					}
				]],
				idField:"id",
				treeField: 'name'
			});
			$(window).resize(function() { 
				 var width= $mainBox.width(); 
				 $mainTable.datagrid('resize', { width : width }); 
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
					<a href="${path }/back/role/view.shtml" class="easyui-linkbutton" iconCls="icon-add" plain="true">新    增</a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷    新</a>
				</td>
				<td align="right"></td>
			</tr>
		</table>
	</div>
</body>
</html>