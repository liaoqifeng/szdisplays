<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>退款信息列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			var $number = $("#number");
			var $payee = $("#payee");
			var $searchButton = $("#searchButton");
			$mainTable.datagrid({
				url:'${path }/back/refunds/list/pager.shtml',
				idField:'id',
				treeField:'name',
				toolbar:$toolBox,
				pagination:true,
				singleSelect:true,
				width:'auto',
				height:$mainBox.height(),
				columns:[[
					{title:'编号',field:'number',align:'center',width:'100'},
					{title:'类型',field:'typeName',align:'center',width:'100'},
					{title:'支付方式',field:'paymentwayName',align:'center',width:'150'},
					{title:'会员',field:'memberName',align:'center',width:'120',
						formatter: function(value,row,index){ return row.member.username;}
					},
					{title:'付款金额',field:'paidAmount',align:'center',width:'100'},
					{title:'订单编号',field:'orderNumber',align:'center',width:'100',
						formatter: function(value,row,index){ return row.order.number;}
					},
					{title:'收款人',field:'payee',align:'center',width:'100'},
					{title:'创建日期',field:'createDate',align:'center',width:'100'},
					{title:'操作',field:'operator',align:'center',width:'100',
						formatter: function(value,row,index){
							var html = '<a href="${path}/back/refunds/view.shtml?id='+row.id+'" style=" text-decoration:none;">[查看]</a>';
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
				var payee = $payee.val();
				$mainTable.datagrid('reload',{
					number: number,
					payee: payee,
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
					收款人：<input type="text" id="payee" class="text middle"/>
					<a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">${fn:call("Common.button.search")}</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>