<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>文章列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		function doEdit(id){
			window.location.href='${path}/back/article/edit/'+id+'.shtml';
		}
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			var $title = $("#title");
			var $articleCategoryId = $("#articleCategoryId");
			var $isPublish = $("#isPublish");
			var $isTop = $("#isTop");
			var $searchButton = $("#searchButton");
			$mainTable.datagrid({
				url:'${path }/back/article/list/pager.shtml',
				idField:'id',
				toolbar:$toolBox,
				pagination:true,
				width:'auto',
				height:$mainBox.height(),
				columns:[[
				    {checkbox:true,field:'id',align:'center'},
					{title:'${fn:call("Common.label.title")}',field:'articleTitle',align:'center',width:'280'},
					{title:'${fn:call("Common.label.category")}',field:'category',align:'center',width:'150',
						formatter: function(value,row,index){return row.articleCategory.name;}
					},
					{title:'${fn:call("Common.label.isPublish")}',field:'isPublish',align:'center',width:'150',
						formatter: function(value,row,index){
							if(value){
								return "<font color='green'>${fn:call('Common.yes')}</color>";
							}else{
								return "<font color='red'>${fn:call('Common.no')}</color>";
							}
						}
					},
					{title:'${fn:call("Common.label.isTop")}',field:'isTop',align:'center',width:'150',
						formatter: function(value,row,index){
							if(value){
								return "<font color='green'>${fn:call('Common.yes')}</color>";
							}else{
								return "<font color='red'>${fn:call('Common.no')}</color>";
							}
						}
					},
					{title:'${fn:call("Common.label.createDate")}',field:'createDate',align:'center',width:'150'},
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
				var title = $title.val();
				var articleCategoryId = $articleCategoryId.val();
				var isPublish = $isPublish.val();
				var isTop = $isTop.val();
				$mainTable.datagrid('reload',{
					title: title,
					articleCategoryId:articleCategoryId,
					isPublish:isPublish,
					isTop:isTop,
					pageNumber:1
				});
			};
			
			$searchButton.click(function(){
				reload();
			});
		    
			$("a[button='delete']").click(function(){
				var rows = $mainTable.datagrid('getSelections');
				if(rows != null && rows != ""){
					$.post("${path}/back/article/delete.shtml",$(":checkbox[name='id']").serialize(),function(data){
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
					<a href="${path }/back/article/view.shtml" class="easyui-linkbutton" iconCls="icon-add" plain="true"><fmt:message key="Common.button.add"/></a>
					<a href="#" button="delete" class="easyui-linkbutton" iconCls="icon-cancel" plain="true"><fmt:message key="Common.button.delete"/></a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true"><fmt:message key="Common.button.refresh"/></a>
				</td>
				<td align="right">
					<fmt:message key="Common.label.title"/>：<input type="text" id="title" class="text"/>
					<fmt:message key="Common.label.category"/>：
					<select id="articleCategoryId" class="select">
						<option value="">--<fmt:message key="Common.message.select"/>--</option>
						<c:forEach var="row" items="${categorys}">
						<option value="${row.id }">${row.name }</option>
						</c:forEach>
					</select>
					<fmt:message key="Common.label.isPublish"/>：
					<select id="isPublish" class="select">
						<option value="">--<fmt:message key="Common.message.select"/>--</option>
						<option value="true"><fmt:message key="Common.yes"/></option>
						<option value="false"><fmt:message key="Common.no"/></option>
					</select>
					<fmt:message key="Common.label.isTop"/>：
					<select id="isTop" class="select">
						<option value="">--<fmt:message key="Common.message.select"/>--</option>
						<option value="true"><fmt:message key="Common.yes"/></option>
						<option value="false"><fmt:message key="Common.no"/></option>
					</select>
					<a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search"><fmt:message key="Common.button.search"/></a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>