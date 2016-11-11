<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>发货信息列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			var $number = $("#number");
			var $deliverCode = $("#deliverCode");
			var $searchButton = $("#searchButton");
			$mainTable.datagrid({
				url:'${path }/back/returns/list/pager.shtml',
				idField:'id',
				treeField:'name',
				toolbar:$toolBox,
				pagination:true,
				singleSelect:true,
				width:'auto',
				height:$mainBox.height(),
				columns:[[
					{title:'编号',field:'number',align:'center',width:'200'},
					{title:'配送方式',field:'deliverwayName',align:'center',width:'150'},
					{title:'物流公司',field:'logisticsName',align:'center',width:'150'},
					{title:'配送单号',field:'deliverCode',align:'center',width:'150'},
					{title:'发货人',field:'shipper',align:'center',width:'150'},
					{title:'创建日期',field:'createDate',align:'center',width:'150'},
					{title:'操作',field:'operator',align:'center',width:'100',
						formatter: function(value,row,index){
							var html = '<a href="${path}/back/returns/view.shtml?id='+row.id+'" style=" text-decoration:none;">[查看]</a>';
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
				var height=  $mainBox.height();
				$mainTable.datagrid('resize', { width : width,height:height }); 
			});
			$mainTable.datagrid('getPager').pagination({  
		        pageSize: 20,
		        pageList: [10,20,30,50,100], 
		        beforePageText: '第',
		        afterPageText: '页    共 {pages} 页',  
		        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		    }); 
		    
			var reload = function(){
				var number = $number.val();
				var deliverCode = $deliverCode.val();
				$mainTable.datagrid('reload',{
					number: number,
					deliverCode:deliverCode,
					pageNumber:1
				});
			};
			
			$searchButton.click(function(){
				reload();
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
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷    新</a>
				</td>
				<td align="right">
					编号：<input type="text" id="number" class="text middle"/>
					配送单号：<input type="text" id="deliverCode" class="text middle"/>
					<a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">${fn:call("Common.button.search")}</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>