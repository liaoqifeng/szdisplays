<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>${fn:call("call.product.list")}</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		function doEdit(id){
			window.location.href='${path}/back/product/edit/'+id+'.shtml';
		}
		$().ready(function(){
			var $mainTable = $("#main-table");
			var $toolBox = $("#toolBox");
			var $mainBox = $("#mainBox");
			var $isPublish = $("#isPublish");
			var $productNumber = $("#productNumber");
			var $productName = $("#productName");
			var $searchButton = $("#searchButton");
			var $searchMore = $("#searchMore");
			var $searchMoreButton = $("#searchMoreButton");
			var $cancelButton = $("#cancelButton");
			var $productCategory = $("#productCategory");
			var $brand = $("#brand");
			
			$mainTable.datagrid({
				url:'${path }/back/product/list/pager.shtml',
				toolbar:$toolBox,
				pagination:true,
				rownumbers: true,
				width:'auto',
				height:$mainBox.height(),
				columns:[[
				    {checkbox:true,field:'id',align:'center'},
					{title:'${fn:call("call.product.number")}',field:'number',align:'center',width:'120'},
					{title:'${fn:call("call.product.name")}',field:'name',align:'center',width:'120'},
					{title:'${fn:call("call.product.category")}',field:'productCategory',align:'center',width:'130',
						formatter: function(value,row,index){
							return value.name;
						}
					},
					{title:'${fn:call("call.brand")}',field:'brand',align:'center',width:'130',
						formatter: function(value,row,index){
							return value.name;
						}
					},
					{title:'${fn:call("call.product.stock")}',field:'stock',align:'center',width:'100',
						formatter: function(value,row,index){
							if(value>0)
								return "<font color='green'>"+value+"</font>";
							else
								return "<font color='red'>"+value+"</font>";
						}
					},
					{title:'${fn:call("call.product.marketPrice")}',field:'marketPrice',align:'center',width:'80'},
					{title:'${fn:call("call.product.costPrice")}',field:'costPrice',align:'center',width:'80'},
					{title:'${fn:call("call.product.salePrice")}',field:'salePrice',align:'center',width:'80'},
					{title:'${fn:call("call.product.isPublish")}',field:'isPublish',align:'center',width:'80',
						formatter: function(value,row,index){
							if(value)
								return "<font color='green'>${fn:call('Common.yes')}</font>";
							else
								return "<font color='red'>${fn:call('Common.no')}</font>";
						}
					},
					{title:'${fn:call("Common.operator")}',field:'operator',align:'center',width:'100',
						formatter: function(value,row,index){
							var html = '<a href="#" onclick="doEdit(' + row.id + ')" style=" text-decoration:none;">[${fn:call("Common.edit")}]</a>';
	                   		return html;
						}
					}
				]],
				idField:"id",
				pageNumber:1,
				pageSize:20,
				loadMsg:"${fn:call('Common.message.loading')}"
			});
			$mainTable.datagrid('getPager').pagination({  
		        pageSize: 20,
		        pageList: [10,20,30,50,100], 
		        beforePageText: '${fn:call("Common.page.beforePageText")}',
		        afterPageText: '${fn:call("Common.page.afterPageText")}',  
		        displayMsg: '${fn:call("Common.page.displayMsg")}'
		    });  
			$(window).resize(function() { 
				 var width= $mainBox.width();
				 var height=  $mainBox.height();
				 $mainTable.datagrid('resize', { width : width,height:height }); 
			});
			$searchButton.click(function(){
				reload();
			});
			var reload = function(){
				var _isPublish = $isPublish.val();
				var _productNumber = $productNumber.val();
				var _productName = $productName.val();
				var _productCategoryId = $productCategory.val();
				var _brandId = $brand.val();
				$mainTable.datagrid('reload',{
					number: _productNumber,
					name: _productName,
					isPublish:_isPublish,
					productCategoryId: _productCategoryId,
					brandId: _brandId,
					pageNumber:1
				});
			};
			$("a[button='searchMore']").click(function(){
				$searchMore.window("open");
			});
			$cancelButton.click(function(){
				$searchMore.window("close");
			});
			$searchMoreButton.click(function(){
				reload();
				$searchMore.window("close");
			});
			$("a[button='delete']").click(function(){
				var rows = $mainTable.datagrid('getSelections');
				if(rows != null && rows != ""){
					$.post("${path}/back/product/delete.shtml",$(":checkbox[name='id']").serialize(),function(data){
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
					<a href="${path }/back/product/view.shtml" class="easyui-linkbutton" iconCls="icon-add" plain="true">${fn:call("Common.button.add")}</a>
					<a href="#" button="delete" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">${fn:call("Common.button.delete")}</a>
					<a href="#" button="reload" class="easyui-linkbutton" iconCls="icon-reload" plain="true">${fn:call("Common.button.refresh")}</a>
					<a href="#" button="searchMore" class="easyui-linkbutton" iconCls="icon-search" plain="true">${fn:call("Common.button.more")}</a>
				</td>
				<td align="right">
					${fn:call("call.product.number")}：<input type="text" id="productNumber" class="text middle"/>
					${fn:call("call.product.name")}：<input type="text" id="productName" class="text middle"/>
					${fn:call("call.product.isPublish")}：<select id="isPublish" name="isPublish" class="select">
								<option value="">--${fn:call("Common.message.select")}--</option>
								<option value="true">${fn:call("Common.yes")}</option>
								<option value="false">${fn:call("Common.no")}</option>
							  </select>
					<a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">${fn:call("Common.button.search")}</a>
				</td>
			</tr>
		</table>
	</div>
	<div id="searchMore" class="easyui-window" title="&nbsp;&nbsp;${fn:call('Common.button.more')}" 
		data-options="iconCls:'icon-search',collapsible:false,minimizable:false,maximizable:false,closed:true" 
		style="width:300px;height:250px;padding:10px;">
		<div>
			<table class="product-list-s-top">
				<tr>
					<td width="40%" align="right">${fn:call("call.product.category")}：</td>
					<td>
						<select id="productCategory" class="select">
							<option value="">--${fn:call("Common.message.select")}--</option>
							<c:if test="${productCategorys != null}">
								<c:forEach var="row" items="${productCategorys}">
									<option value="${row.id }">
										<c:forEach var="i" begin="0" end="${row.level}" step="1">
										&nbsp;&nbsp;
										</c:forEach>
										${row.name }
									</option>
								</c:forEach>
							</c:if>
						</select>
					</td>
				</tr>
				<tr>
					<td width="40%" align="right">${fn:call("call.brand")}：</td>
					<td>
						<select id="brand" class="select">
							<option value="">--${fn:call("Common.message.select")}--</option>
							<c:if test="${brands != null}">
								<c:forEach var="row" items="${brands}">
								<option value="${row.id }">${row.name }</option>
								</c:forEach>
							</c:if>
						</select>
					</td>
				</tr>
			</table>
		</div>
		<div class="product-list-s-buttom">
			<a href="#" id="searchMoreButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">${fn:call("Common.button.search")}</a>
		    <a href="#" id="cancelButton" class="easyui-linkbutton" data-options="iconCls:'icon-back'">${fn:call("Common.button.cancel")}</a>
		</div>
	</div>
</body>
</html>