<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>员工列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		function doEdit(id){
			window.location.href='${path}/back/member/edit/'+id+'.shtml';
		}
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			var $username = $("#username");
			var $status = $("#status");
			var $memberGrade = $("#memberGrade");
			var $searchButton = $("#searchButton");
			
			$mainTable.datagrid({
				url:'${path }/back/member/list/pager.shtml',
				toolbar:$toolBox,
				pagination:true,
				rownumbers: true,
				width:'auto',
				height:$mainBox.height(),
				columns:[[
				    {checkbox:true,field:'id',align:'center'},
					{title:'帐户名称',field:'username',align:'center',width:'100'},
					{title:'真实姓名',field:'realname',align:'center',width:'100'},
					{title:'手机号',field:'phone',align:'center',width:'100'},
					{title:'邮箱',field:'email',align:'center',width:'150'},
					{title:'注册IP',field:'registerIp',align:'center',width:'100'},
					{title:'创建时间',field:'createDate',align:'center',width:'100'},
					{title:'用户状态',field:'statusName',align:'center',width:'80'},
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
				var status = $status.val();
				var username = $username.val();
				var gradeId = $memberGrade.val();
				$mainTable.datagrid('reload',{
					gradeId:gradeId,
					username:username,
					status:status,
					pageNumber:1
				});
			};
			$searchButton.click(function(){
				reload();
			});
			$("a[button='delete']").click(function(){
				var rows = $mainTable.datagrid('getSelections');
				if(rows != null && rows != ""){
					$.post("${path}/back/member/delete.shtml",$(":checkbox[name='id']").serialize(),function(data){
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
					<a href="${path }/back/member/view.shtml" class="easyui-linkbutton" iconCls="icon-add" plain="true">新    增</a>
					<a href="#" button="delete" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">删    除</a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷    新</a>
				</td>
				<td align="right">
					帐户名称：<input type="text" id="username" class="text middle"/>
					<select id="memberGrade" class="select hidden">
						<option value="">--请选择--</option>
					</select>
					是否启用：
					<select id="status" class="select">
						<option value="">--请选择--</option>
						<c:if test="${statusArray != null}">
						<c:forEach var="row" items="${statusArray}">
						<option value="${row }"><fmt:message key="MemberStatus.${row}"/></option>
						</c:forEach>
						</c:if>
					</select>
					<a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查      询</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>