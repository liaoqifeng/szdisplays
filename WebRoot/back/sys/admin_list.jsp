<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>管理员列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		function doEdit(id){
			window.location.href='${path}/back/admin/edit/'+id+'.shtml';
		}
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			var $enabledBox = $("#isAccountEnabled");
			var $lockedBox = $("#isAccountLocked");
			var $searchButton = $("#searchButton");
			var $username = $("#username");
			$mainTable.datagrid({
				url:'${path }/back/admin/list/pager.shtml',
				toolbar:$toolBox,
				pagination:true,
				width:'auto',
				height:$mainBox.height(),
				singleSelect:false,
				columns:[[
					{checkbox:true,field:'id',align:'center'},
					{title:'用户名',field:'username',align:'center',width:'150'},
					{title:'真实姓名',field:'realName',align:'center',width:'150'},
					{title:'创建时间',field:'createDate',align:'center',width:'150'},
					{title:'启用状态',field:'isAccountEnabled',align:'center',width:'100',
						formatter: function(value,row,index){
							if(value)
								return "<font color='green'>是</font>";
							else
								return "<font color='red'>否</font>";
						}
					},
					{title:'锁定状态',field:'isAccountLocked',align:'center',width:'100',
						formatter: function(value,row,index){
							if(value)
								return "<font color='red'>是</font>";
							else
								return "<font color='green'>否</font>";
						}
					},
					{title:'操作',field:'operator',align:'center',width:'100',
						formatter: function(value,row,index){
							var html = '<a href="#" onclick="doEdit(' + row.id + ')" style=" text-decoration:none;">[编辑]</a>';
	                   		return html;
						}
					}
				]],
				idField:"id",
				pageNumber:1,
				pageSize:20,
				loadMsg:"正在加载,请等待..."
			});
			$mainTable.datagrid('getPager').pagination({  
		        pageSize: 20,//每页显示的记录条数，默认为10  
		        pageList: [10,20,30,50,100],//可以设置每页记录条数的列表  
		        beforePageText: '第',//页数文本框前显示的汉字  
		        afterPageText: '页    共 {pages} 页',  
		        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		    });  
			$(window).resize(function() { 
				 var width= $mainBox.width(); 
				 $mainTable.datagrid('resize', { width : width }); 
			});

			var reload = function(){
				var _username = $username.val();
				$mainTable.datagrid('reload',{
					username:_username,
					pageNumber:1
				});
			};
			$searchButton.click(function(){
				reload();
			});

			$("a[button='delete']").click(function(){
				var rows = $mainTable.datagrid('getSelections');
				if(rows != null && rows != ""){
					$.post("${path}/back/admin/delete.shtml",$(":checkbox[name='id']").serialize(),function(data){
						$.message(data);
						if(data.type == "success"){
							reload();
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
					<a href="${path }/back/admin/view.shtml" class="easyui-linkbutton" iconCls="icon-add" plain="true">新    增</a>
					<a href="#" button="delete" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">删    除</a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷    新</a>
				</td>
				<td align="right">
					用户名：<input type="text" id="username" class="text middle"/>
					<a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查      询</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>