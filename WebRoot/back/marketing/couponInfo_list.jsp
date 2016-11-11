<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>优惠码列表</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			var $isUsed = $("#isUsed");
			var $coupon = $("#coupon");
			var $username = $("#username");
			var $searchButton = $("#searchButton");

			$mainTable.datagrid({
				url:'${path }/back/couponInfo/list/pager.shtml',
				toolbar:$toolBox,
				pagination:true,
				rownumbers: true,
				singleSelect:true,
				width:'auto',
				height:$mainBox.height(),
				columns:[[
					{title:'优惠券名称',field:'name',align:'center',width:'120',
						formatter: function(value,row,index){return row.coupon.name;}
					},
					{title:'优惠码',field:'code',align:'center',width:'140'},
					{title:'开始时间',field:'startDate',align:'center',width:'120',
						formatter: function(value,row,index){return row.coupon.startDate;}
					},
					{title:'结束时间',field:'endDate',align:'center',width:'120',
						formatter: function(value,row,index){return row.coupon.endDate;}
					},
					{title:'是否启用',field:'isEnabled',align:'center',width:'100',
						formatter: function(value,row,index){
                   			return row.coupon.isEnabled ? "<font color='green'>是</font>" : "<font color='red'>否</font>";
						}
					},
					{title:'是否使用',field:'isUsed',align:'center',width:'100',
						formatter: function(value,row,index){
               				return value ? "<font color='green'>是</font>" : "<font color='red'>否</font>";
						}
					},
					{title:'使用人',field:'member',align:'center',width:'100',
						formatter: function(value,row,index){
							if(value != null && value != undefined){
								return value.username;
							}else{
								return "";
							}
						}
					},
					{title:'使用时间',field:'usedDate',align:'center',width:'100'},
					{title:'操作',field:'operator',align:'center',width:'110',
						formatter: function(value,row,index){
							var html = '';
							if(row.isUsed){
								html = '<a href="#" data-id="'+row.id+'" class="onChange">[释放]</a>';
							}
	                   		return html;
						}
					}
				]],
				idField:"id",
				pageNumber:1,
				pageSize:20,
				loadMsg:"正在加载,请等待...",
				onLoadSuccess:function(){
					$mainBox.find("a.onChange").unbind("click").bind("click",function(){
						var $this = $(this);
						$.ajax({
						    type: "POST",                                       
			                url: "${path}/back/couponInfo/change.shtml",          
			                dataType: "json",
			                data: { id: $this.data("id")},  
			                success: function (data) {
			             	   if(data.type == "success"){ $this.remove(); }
			             	  $.messager.alert('提示',data.content);
			                }        
						});
					});
				}
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
				var isUsed = $isUsed.val();
				var couponId = $coupon.val();
				var username = $username.val();
				$mainTable.datagrid('reload',{
					"isUsed":isUsed,
					"couponId":couponId,
					"username":username,
					pageNumber:1
				});
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
					使用人：<input type="text" id="username" name="username" class="text middle"/>
					优惠券：
					<select id="coupon" class="select">
						<option value="">--请选择--</option>
						<c:if test="${coupons != null}">
						<c:forEach var="row" items="${coupons}">
						<option value="${row.id }">${row.name }</option>
						</c:forEach>
						</c:if>
					</select>
					是否使用：
					<select id="isUsed" class="select">
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