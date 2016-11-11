<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>资源列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		function doEdit(id){
			window.location.href='${path}/back/resource/edit/'+id+'.shtml';
		}
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			$mainTable.datagrid({
				url:'${path }/back/resource/list/pager.shtml',
				toolbar:$toolBox,
				pagination:true,
				width:'auto',
				height:$mainBox.height(),
				singleSelect:true,
				columns:[[
					{title:'资源名称',field:'name',align:'center',width:'200',
						formatter: function(value,row,index){
							if(row.isSystem)
								return "<span style='float:left'>"+value+"</span>";
							else
								return "<span>"+value+"</span>";
						}
					},
					{title:'资源路径',field:'value',align:'center',width:'200'},
					{title:'资源描述',field:'description',align:'center',width:'200'},
					{title:'是否系统资源',field:'isSystem',align:'center',width:'200',
						formatter: function(value,row,index){
							if(value)
								return "<font color='green'>是</font>";
							else
								return "<font color='red'>否</font>";
						}
					},
					{title:'排序',field:'orderList',align:'center',width:'200'},
					{title:'操作',field:'operator',align:'center',width:'100',
						formatter: function(value,row,index){
							var html = '<a href="#" onclick="doEdit(' + row.id + ')" style=" text-decoration:none;">[编辑]</a>';
	                   		return html;
						}
					}
				]],
				idField:"id",
				pageNumber:1,
				pageSize:20
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
					<a href="${path }/back/resource/view.shtml" class="easyui-linkbutton" iconCls="icon-add" plain="true">新    增</a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷    新</a>
				</td>
				<td align="right"></td>
			</tr>
		</table>
	</div>
</body>
</html>