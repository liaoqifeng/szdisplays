<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>促销信息列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		function doEdit(id){
			window.location.href='${path}/back/promotion/edit/'+id+'.shtml';
		}
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			var $name = $("#name");
			var $title = $("#title");
			var $searchButton = $("#searchButton");
			
			$mainTable.datagrid({
				url:'${path }/back/promotion/list/pager.shtml',
				toolbar:$toolBox,
				pagination:true,
				rownumbers: true,
				width:'auto',
				height:$mainBox.height(),
				columns:[[
				    {checkbox:true,field:'id',align:'center'},
					{title:'名称',field:'name',align:'center',width:'100'},
					{title:'标题',field:'title',align:'center',width:'100'},
					{title:'开始时间',field:'startDate',align:'center',width:'120'},
					{title:'结束时间',field:'endDate',align:'center',width:'120'},
					{title:'最小金额',field:'minPrice',align:'center',width:'100'},
					{title:'最大金额',field:'maxPrice',align:'center',width:'100'},
					{title:'是否免运费',field:'isFreeDeliver',align:'center',width:'100',
						formatter: function(value,row,index){
                   			return value ? "<font color='green'><fmt:message key='Common.yes'/></font>" : "<font color='red'><fmt:message key='Common.no'/></font>";
						}
					},
					{title:'是否允许使用优惠券  ',field:'isAllowedCoupon',align:'center',width:'100',
						formatter: function(value,row,index){
                   			return value ? "<font color='green'><fmt:message key='Common.yes'/></font>" : "<font color='red'><fmt:message key='Common.no'/></font>";
						}
					},
					{title:'操作',field:'operator',align:'center',width:'110',
						formatter: function(value,row,index){
							var html = '<a href="#" onclick="doEdit(' + row.id + ')" style=" text-decoration:none;">[<fmt:message key="Common.edit"/>]</a>';
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
		        pageSize: 20,
		        pageList: [10,20,30,50,100], 
		        beforePageText: '第',
		        afterPageText: '页    共 {pages} 页',  
		        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		    });  
			$(window).resize(function() { 
				var width= $mainBox.width(); 
				var height=  $mainBox.height();
				$mainTable.datagrid('resize', { width : width,height:height }); 
			});
			var reload = function(){
				var name = $name.val();
				var title = $title.val();
				$mainTable.datagrid('reload',{
					name:name,
					title:title,
					pageNumber:1
				});
			};
			$searchButton.click(function(){
				reload();
			});
			$("a[button='delete']").click(function(){
				var rows = $mainTable.datagrid('getSelections');
				if(rows != null && rows != ""){
					$.post("${path}/back/promotion/delete.shtml",$(":checkbox[name='id']").serialize(),function(data){
						$.message(data);
						reload();
					});
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
					<a href="${path }/back/promotion/view.shtml" class="easyui-linkbutton" iconCls="icon-add" plain="true">新    增</a>
					<a href="#" button="delete" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">删    除</a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷    新</a>
				</td>
				<td align="right">
					名称：<input type="text" id="name" class="text middle"/>
					标题：<input type="text" id="title" class="text middle"/>
					<a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查      询</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>