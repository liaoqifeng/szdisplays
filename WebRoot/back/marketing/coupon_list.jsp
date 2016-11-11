<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>优惠券列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		function doEdit(id){
			window.location.href='${path}/back/coupon/edit/'+id+'.shtml';
		}
		function doBuild(id){
			window.location.href='${path}/back/coupon/build/'+id+'.shtml';
		}
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			var $name = $("#name");
			var $isExchange = $("#isExchange");
			var $isEnabled = $("#isEnabled");
			var $searchButton = $("#searchButton");
			
			$mainTable.datagrid({
				url:'${path }/back/coupon/list/pager.shtml',
				toolbar:$toolBox,
				pagination:true,
				rownumbers: true,
				width:'auto',
				height:$mainBox.height(),
				columns:[[
				    {checkbox:true,field:'ck',align:'center'},
					{title:'编号',field:'id',align:'center',width:'50'},
					{title:'名称',field:'name',align:'center',width:'100'},
					{title:'前缀',field:'prefix',align:'center',width:'100'},
					{title:'开始时间',field:'startDate',align:'center',width:'120'},
					{title:'结束时间',field:'endDate',align:'center',width:'120'},
					{title:'最小金额',field:'minPrice',align:'center',width:'100'},
					{title:'最大金额',field:'maxPrice',align:'center',width:'100'},
					{title:'是否启用',field:'isEnabled',align:'center',width:'100',
						formatter: function(value,row,index){
                   			return value ? "<font color='green'>是</font>" : "<font color='red'>否</font>";
						}
					},
					{title:'积分兑换',field:'isExchange',align:'center',width:'100',
						formatter: function(value,row,index){
               				return value ? "<font color='green'>是</font>" : "<font color='red'>否</font>";
						}
					},
					{title:'积分',field:'score',align:'center',width:'80'},
					{title:'操作',field:'operator',align:'center',width:'110',
						formatter: function(value,row,index){
							var html = '<a href="#" onclick="doBuild(' + row.id + ')" style=" text-decoration:none;">[生成优惠码]</a><a href="#" onclick="doEdit(' + row.id + ')" style=" text-decoration:none;">[编辑]</a>';
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
			$searchButton.click(function(){
				var isExchange = $isExchange.val();
				var name = $name.val();
				var isEnabled = $isEnabled.val();
				$mainTable.datagrid('reload',{
					name:name,
					isExchange:isExchange,
					isEnabled:isEnabled,
					pageNumber:1
				});
			});
			$("a[button='delete']").click(function(){
				var rows = $mainTable.datagrid('getSelections');
				if(rows != null && rows != ""){
					var ids = new Array();
					for(var i=0;i<rows.length;i++){
						ids.push(rows[i].id);
					}
					window.location.href="${path}/back/coupon/delete.shtml?ids="+$.toJSON(ids);
				}else{
					$.messager.alert('提示','请选择会员!');
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
					<a href="${path }/back/coupon/view.shtml" class="easyui-linkbutton" iconCls="icon-add" plain="true">新    增</a>
					<a href="#" button="delete" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">删    除</a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷    新</a>
				</td>
				<td align="right">
					名称：<input type="text" id="name" class="text middle"/>
					是否积分兑换：
					<select id="isExchange" class="select">
						<option value="">--请选择--</option>
						<option value="true">是</option>
						<option value="false">否</option>
					</select>
					是否启用：
					<select id="isEnabled" class="select">
						<option value="">--请选择--</option>
						<option value="true">是</option>
						<option value="false">否</option>
					</select>
					<a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查      询</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>