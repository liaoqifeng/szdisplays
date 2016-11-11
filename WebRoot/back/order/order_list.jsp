<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>订单列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		function doEdit(id){
			window.location.href='${path}/back/order/edit/'+id+'.shtml';
		}
		function doView(id){
			window.location.href='${path}/back/order/view/'+id+'.shtml';
		}
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			$mainTable.datagrid({
				url:'${path }/back/order/list/pager.shtml',
				idField:'id',
				treeField:'name',
				toolbar:$toolBox,
				rownumbers: true,
				pagination:true,
				width:'auto',
				height:$mainBox.height(),
				columns:[[
				    {checkbox:true,field:'id',align:'center'},
					{title:'订单编号',field:'number',align:'center',width:'80'},
					{title:'会员',field:'member',align:'center',width:'100',
						formatter: function(value,row,index){return value.username;}
					},
					{title:'总金额',field:'productAmount',align:'center',width:'100'},
					{title:'订单状态',field:'orderStatusName',align:'center',width:'100'},
					{title:'支付状态',field:'payStatusName',align:'center',width:'100'},
					{title:'配送状态',field:'deliverStatusName',align:'center',width:'100'},
					{title:'支付方式',field:'paymentwayName',align:'center',width:'100'},
					{title:'配送方式',field:'deliverwayName',align:'center',width:'100'},
					{title:'收货人',field:'receiver',align:'center',width:'100'},
					{title:'创建时间',field:'createDate',align:'center',width:'100'},
					{title:'打印',field:'deliverOrder',align:'center',width:'100',
						formatter: function(value,row,index){
							var html = '<select><option>快递单</option></select>';
	                   		return html;
						}
					},
					{title:'操作',field:'operator',align:'center',width:'100',
						formatter: function(value,row,index){
							var html = '<a href="#" onclick="doView(' + row.id + ')" style=" text-decoration:none;">[查看]</a>';
							if(row.orderStatus == 'unprocessed'){
								html += '<a href="#" onclick="doEdit(' + row.id + ')" style=" text-decoration:none;">[编辑]</a>';
							}else{
								html += '<span title="不允许编辑">[编辑]</span>';
							}
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
			$("a[button='delete']").click(function(){
				var rows = $mainTable.datagrid('getSelections');
				if(rows != null && rows != ""){
					$.post("${path}/back/order/delete.shtml",$(":checkbox[name='id']").serialize(),function(data){
						$.message(data);
						if(data.type == "success"){
							alert(data.type);
							$mainTable.datagrid('reload')
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
					<a href="#" button="delete" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">删    除</a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷    新</a>
				</td>
				<td align="right"></td>
			</tr>
		</table>
	</div>
</body>
</html>