<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>标签列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		function doEdit(id){
			window.location.href='${path}/back/tag/edit/'+id+'.shtml';
		}
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			var $name = $("#name");
			var $searchButton = $("#searchButton");
			$mainTable.datagrid({
				url:'${path }/back/tag/list/pager.shtml',
				idField:'id',
				treeField:'name',
				toolbar:$toolBox,
				pagination:true,
				width:'auto',
				height:$mainBox.height(),
				columns:[[
				    {checkbox:true,field:'id',align:'center'},
					{title:'${fn:call("Common.label.name")}',field:'name',align:'center',width:'200'},
					{title:'${fn:call("Common.label.type")}',field:'typeName',align:'center',width:'150'},
					{title:'${fn:call("Common.label.icon")}',field:'image',align:'center',width:'150',
						formatter: function(value,row,index){
							if(value != "" && value != null){
								return '<a href="'+value+'" target="_blank" style=" text-decoration:none;">[${fn:call("Common.look")}]</a>';
							}else{
								return "";
							}
						}
					},
					{title:'${fn:call("Common.label.remark")}',field:'remark',align:'center',width:'150'},
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
				loadMsg:"${fn:call('Common.message.loading')}"
			});
			$(window).resize(function() { 
				var width= $mainBox.width(); 
				var height=  $mainBox.height();
				$mainTable.datagrid('resize', { width : width,height:height }); 
			});
			$mainTable.datagrid('getPager').pagination({  
		        pageSize: 20,
		        pageList: [10,20,30,50,100], 
		        beforePageText: "${fn:call('Common.page.beforePageText')}",
		        afterPageText: "${fn:call('Common.page.afterPageText')}",  
		        displayMsg: "${fn:call('Common.page.displayMsg')}"
		    }); 
			var reload = function(){
				var name = $name.val();
				$mainTable.datagrid('reload',{
					name: name,
					pageNumber:1
				});
			};
			
			$searchButton.click(function(){
				reload();
			});
		    
			$("a[button='delete']").click(function(){
				var rows = $mainTable.datagrid('getSelections');
				if(rows != null && rows != ""){
					$.post("${path}/back/tag/delete.shtml",$(":checkbox[name='id']").serialize(),function(data){
						$.message(data);
						if(data.type == "success"){
							reload();
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
					<a href="${path }/back/tag/view.shtml" class="easyui-linkbutton" iconCls="icon-add" plain="true"><fmt:message key="Common.button.add"/></a>
					<a href="#" button="delete" class="easyui-linkbutton" iconCls="icon-cancel" plain="true"><fmt:message key="Common.button.delete"/></a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true"><fmt:message key="Common.button.refresh"/></a>
				</td>
				<td align="right">
					<fmt:message key="Common.label.name"/>：<input type="text" id="name" class="text middle"/>
					<a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search"><fmt:message key="Common.button.search"/></a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>