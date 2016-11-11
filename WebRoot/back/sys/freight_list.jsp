<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>运费模板列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
</head>
<body>
	<div id="mainBox" class="main-box" style="width: 100%;">
		<table title="" id="main-table"></table>
	</div>
	<div id="toolBox">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td height="50">
					<a href="${path }/back/freightTemplet/view.shtml" class="easyui-linkbutton" iconCls="icon-add" plain="true">新    增</a>
					<a href="#" button="edit" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修    改</a>
					<a href="#" button="delete" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">删    除</a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷    新</a>
					<a href="#" button="print" class="easyui-linkbutton" iconCls="icon-print" plain="true">打    印</a>
				</td>
				<td align="right">
					模板名称：<input type="text" id="name" class="text-two"/>
					配送方式：<select id="deliverway" class="easyui-combobox" data-options="panelHeight:'auto'">
								<option value="">--请选择--</option>
								<c:if test="${deliverways != null}">
									<c:forEach var="row" items="${deliverways}">
									<option value="${row.id }">${row.name }</option>
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
<script type="text/javascript">
	function doEdit(id){
		window.location.href='${path}/back/freightTemplet/edit/'+id+'.shtml';
	}
	$().ready(function(){
		var $mainTable = $("#main-table");
		var $toolBox = $("#toolBox");
		var $mainBox = $("#mainBox");
		var $deliverway = $("#deliverway");
		var $name = $("#name");
		var $searchButton = $("#searchButton");
		$mainTable.datagrid({
			url:'${path }/back/freightTemplet/list/pager.shtml',
			idField:'id',
			treeField:'name',
			toolbar:$toolBox,
			rownumbers: true,
			pagination:true,
			width:'auto',
			height:$mainBox.height(),
			columns:[[
			    {checkbox:true,field:'ck',align:'center'},
				{title:'编号',field:'id',align:'center',width:'80'},
				{title:'模板名称',field:'name',align:'center',width:'250'},
				{title:'配送方式',field:'deliverway',align:'center',width:'150',
					formatter: function(value,row,index){return value.name;}
				},
				{title:'备注',field:'remark',align:'center',width:'250'},
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
			loadMsg:"正在加载,请等待..."
		});
		$(window).resize(function() { 
			 var width= $mainBox.width(); 
			 $mainTable.datagrid('resize', { width : width }); 
		});
		$mainTable.datagrid('getPager').pagination({  
	        pageSize: 20,
	        pageList: [10,20,30,50,100], 
	        beforePageText: '第',
	        afterPageText: '页    共 {pages} 页',  
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	    }); 
		$("a[button='delete']").click(function(){
			var rows = $mainTable.datagrid('getSelections');
			if(rows != null && rows != ""){
				var ids = new Array();
				for(var i=0;i<rows.length;i++){
					ids.push(rows[i].id);
				}
				window.location.href="${path}/back/freightTemplet/delete.shtml?ids="+$.toJSON(ids);
			}else{
				$.messager.alert('提示','请选择运费模板!');
			}
		});
		$("a[button='edit']").click(function(){
			var rows = $mainTable.datagrid('getSelections');
			if(rows.length > 1){
				$.messager.alert('提示','只能选择一条数据进行修改!');
				return false;
			}
			if(rows != null && rows != ""){
				doEdit(rows[0].id);
			}else{
				$.messager.alert('提示','请选择运费模板!');
			}
		});
		$searchButton.click(function(){
			var deliverwayId = $deliverway.combobox('getValue');
			var name = $name.val();
			$mainTable.datagrid('reload',{
				name: name,
				deliverwayId: deliverwayId,
				pageNumber:1
			});
		});
	});
</script>