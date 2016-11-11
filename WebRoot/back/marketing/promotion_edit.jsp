<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>修改促销</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<link rel="stylesheet" href="${path }/common/kindeditor/themes/default/default.css" />
	<script type="text/javascript" src="${path }/common/kindeditor/kindeditor-min.js" charset="utf-8"></script>
	<script type="text/javascript" src="${path }/common/kindeditor/lang/zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="${path }/back/js/datePicker/WdatePicker.js" charset="utf-8"></script>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $editForm = $("#editForm");
			var $tab = $("#tab");
			var $categoryWindow = $("#categoryWindow");
			var $categorySelect = $("#categorySelect");
			var $categoryTd = $("#categoryTd");
			var $categoryTable = $("#categoryTable");
			var $selectSaveButton = $("#selectSaveButton");
			var $couponAll = $("#couponAll");
			var $couponTd = $("#couponTd");
			var $gradeAll = $("#gradeAll");
			var $gradeTd = $("#gradeTd");
			var $brandAll = $("#brandAll");
			var $brandTd = $("#brandTd");
			var $productWindow = $("#productWindow");
			var $productTable = $("#productTable");
			var $addProductButton = $("#addProductButton");
			var $addGiftButton = $("#addGiftButton");
			var $searchButton = $("#searchButton");
			var $productNumber = $("#productNumber");
			var $productName = $("#productName");
			var $productCategorySelect = $("#productCategorySelect");
			var $brandSelect = $("#brandSelect");
			var $addSureButton = $("#addSureButton");
			var $productPanel = $("#productPanel");
			var $giftPanel = $("#giftPanel");
			
			var $saveButton = $("#saveButton");
			$mainPanel.panel({
				width:$mainBox.width(),
				height:$mainBox.height(),
				tools:[{
					iconCls:'icon-reload',
					handler:function(){window.location.reload();}
				}]
			});
			$(window).resize(function() { 
				var width= $mainBox.width(); 
				var height=  $mainBox.height();
				$mainPanel.panel('resize', { width : width,height:height }); 
			});
			if ($.tools != null) {
				$tab.tabs("table.tabContent", {
					tabs: "input"
				});
			}
			var exchange = function($this,$td){
				var checked = $this.data("checked");
				if(checked == undefined || checked == null || checked == false){
					$td.find(":checkbox").prop("checked",true);
				}else{
					$td.find(":checkbox").prop("checked",false);
				}
				$this.data("checked",!checked);
			};
			$couponAll.click(function(){
				exchange($(this),$couponTd);
			});
			$gradeAll.click(function(){
				exchange($(this),$gradeTd);
			});
			$brandAll.click(function(){
				exchange($(this),$brandTd);
			});
			$categorySelect.click(function(){
				$categoryWindow.window('open');
			});

			var searchProduct = function(){
				var productNumber = $productNumber.val();
				var productName = $productName.val();
				var category = $productCategorySelect.val();
				var brand = $brandSelect.val();
				var isGift = $productTable.data("isGift");
				if(isGift == "false")
					isGift = null;
				$productTable.datagrid('reload',{
					number: productNumber,
					name: productName,
					productCategoryId: category,
					brandId: brand,
					isPublish:true,
					isGift:isGift,
					pageNumber:1
				});
			};

			$addProductButton.click(function(){
				$productWindow.window('open');
				$addSureButton.data("panel",$productPanel);
				$productTable.datagrid("clearSelections").data("isGift",false);
				searchProduct();
			});

			$addGiftButton.click(function(){
				$productWindow.window('open');
				$addSureButton.data("panel",$giftPanel);
				$productTable.datagrid("clearSelections").data("isGift",true);
				searchProduct();
			});

			$searchButton.click(function(){
				searchProduct();
			});

			var giftItemIndex = ${fn:length(promotion.products)};
			var appendProduct = function($this,$panel){
				var selected = $productTable.datagrid("getSelections");
				var tr = '';
				if(selected != null && selected.length > 0){
					for(var i=0;i<selected.length;i++){
						if($panel.find("tr.body"+selected[i].number).size() > 0) continue;

						var name = $panel.data("name");
						var quantity = name;
						var appendTd = "";
						if(name == "giftItems"){
							name += "["+giftItemIndex+"].product.id";
							quantity += "["+giftItemIndex+"].quantity";
							appendTd += '<td><input type="text" name="'+quantity+'" value="1" class="text short quantity"/></td>';
						}
						
						tr += '<tr class="body'+selected[i].number+'">'+
						'<td align="left"><input type="hidden" name="'+name+'" value="'+selected[i].id+'"/>'+selected[i].number+'</td>'+
						'<td align="left">'+selected[i].name+'</td>'+
						'<td>'+selected[i].productCategory.name+'</td>'+
						'<td>'+$.currency(selected[i].salePrice)+'</td>'+
						'<td>'+selected[i].stock+'</td>'+appendTd+
						'<td><a href="#" class="delete">[<fmt:message key="Common.delete"/>]</a></td></tr>';

						giftItemIndex++;
					}
					$panel.append(tr);
				}
			};

			$("table").on("click",".delete",function(){
				$(this).closest("tr").remove();
			});
			
			$addSureButton.click(function(){
				var $this = $(this);
				var $panel = $this.data("panel");
				appendProduct($this,$panel);
				$productWindow.window('close');
			});

			$selectSaveButton.click(function(){
				var selected = $categoryTable.datagrid("getSelections");
				if(selected != null && selected.length > 0){
					var options = [];;
					for(var i=0;i<selected.length;i++){
						if($categoryTd.find(".categoryId"+selected[i].id).size() > 0) continue;
						options.push('<label><input type="checkbox" name="categoryIds" value="'+selected[i].id+'" class="categoryId'+selected[i].id+' categoryIds" checked="checked"/>&nbsp;'+selected[i].name+'</label>');
					}
					$categoryTd.append(options.join(''));
				}
				$categoryWindow.window('close');
			});

			$categoryTd.on("click",".categoryIds",function(){
				$(this).closest("label").remove();
			});

			$categoryTable.treegrid({
				url:'${path }/back/productCategory/list/pager.shtml?parentId=0',
				idField:'id',
				treeField:'name',
				rownumbers: true,
				singleSelect:false,
				width:'784',
				height:'460',
				animate:true,
				columns:[[
				    {checkbox:true,field:'ck',align:'center'},
					{title:'ID',field:'id',align:'center',width:'80'},
					{title:'分类名称',field:'name',align:'center',width:'150'},
					{title:'标题',field:'title',align:'center',width:'150'},
					{title:'关键字',field:'keywords',align:'center',width:'150'},
					{title:'描述',field:'describtion',align:'center',width:'150'}
				]],
				pageNumber:1,
				pageSize:20,
				loadMsg:"正在加载,请等待...",
				onBeforeLoad:function(row,param){ 
					if(row) 
						$(this).treegrid('options').url='${path }/back/productCategory/list/pager.shtml?parentId='+row.id; 
				}
			});

			$productTable.datagrid({
				url:'${path }/back/product/list/pager.shtml',
				pagination:true,
				rownumbers: true,
				toolbar:$("#toolBox"),
				width:'auto',
				width:'884',
				height:'500',
				columns:[[
				    {checkbox:true,field:'ck',align:'center'},
					{title:'编号',field:'number',align:'center',width:'120'},
					{title:'商品名称',field:'name',align:'center',width:'120'},
					{title:'商品分类',field:'productCategory',align:'center',width:'130',
						formatter: function(value,row,index){
							return value.name;
						}
					},
					{title:'品牌',field:'brand',align:'center',width:'130',
						formatter: function(value,row,index){
							return value.name;
						}
					},
					{title:'库存',field:'stock',align:'center',width:'80',
						formatter: function(value,row,index){
							if(value>0)
								return "<font color='green'>"+value+"</font>";
							else
								return "<font color='red'>"+value+"</font>";
						}
					},
					{title:'市场价',field:'marketPrice',align:'center',width:'80'},
					{title:'成本价',field:'costPrice',align:'center',width:'80'},
					{title:'销售价',field:'salePrice',align:'center',width:'80'}
				]],
				idField:"id",
				pageNumber:1,
				pageSize:20,
				loadMsg:"正在加载,请等待..."
			});
			$productTable.datagrid('getPager').pagination({  
		        pageSize: 20,
		        pageList: [10,20,30,50,100], 
		        beforePageText: '第',
		        afterPageText: '页    共 {pages} 页',  
		        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
		    });

			$.validator.addClassRules({
				quantity: {
					required: true,
					integer: true,
					min: 1
				}
			});
					
			$editForm.validate({
				rules: {
					name: "required",
					title: "required",
					minQuantity: "digits",
					maxQuantity: {
						digits: true,
						compare: "#minQuantity"
					},

					minPrice: {
						min: 0,
						decimal: {
							length:18,
							scale:${fn:config("priceScale")}
						}
					},
					maxPrice: {
						min: 0,
						decimal: {
							length:18,
							scale:${fn:config("priceScale")}
						},
						compare: "#minPrice"
					},
					orderList:"digits"
				}
			});
			$saveButton.click(function(){
				$editForm.submit();
			});
		});
	</script>
</head>
<body>
	<div id="mainBox" class="main-box">
		<div id="mainPanel" class="easyui-panel" iconCls="icon-add" title="&nbsp;&nbsp;修  改  促 销 ">
			<form id="editForm" action="${path }/back/promotion/edit.shtml" method="post">
				<input type="hidden" name="id" value="${promotion.id }"/>
		    	<ul id="tab" class="tab">
					<li><input value="基本信息" type="button" /></li>
					<li><input value="设置促销对象" type="button" /></li>
					<li><input value="设置促销商品" type="button" /></li>
					<li><input value="设置赠品" type="button" /></li>
					<li><input value="简介" type="button" /></li>
				</ul>
				<table width="100%" class="input tabContent table">
					<tr>
		    			<td width="30%" height="30" align="right"><font color="red">*</font>&nbsp;名称：&nbsp;&nbsp;</td>
		    			<td width="70%" align="left"><input type="text" name="name" value="${promotion.name }" class="text"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right"><font color="red">*</font>&nbsp;标题：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="title" value="${promotion.title }" class="text"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;开始时间：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" id="startDate" value='<fmt:formatDate value="${promotion.startDate }" pattern="yyyy-MM-dd HH:mm:ss"/>' name="startDate" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;结束时间：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" id="endDate" name="endDate" value='<fmt:formatDate value="${promotion.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>' class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'startDate\')}'});"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;最小商品数量：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" id="minQuantity" name="minQuantity" value="${promotion.minQuantity }" class="text" maxlength="20"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;最大商品数量：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="maxQuantity" class="text" value="${promotion.maxQuantity }" maxlength="20"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;最小商品价格：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" id="minPrice" name="minPrice" value="${fn:currency(promotion.minPrice,false) }" class="text" maxlength="20"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;最大商品价格：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="maxPrice" value="${fn:currency(promotion.maxPrice,false) }" class="text" maxlength="20"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;价格运算表达式：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="priceExpression" value="${promotion.priceExpression }" class="text"/>&nbsp;表达式可用参数:(数量、价格) [+ - * /]&nbsp;&nbsp;&nbsp;示例:数量*价格*0.8</td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;积分运算表达式：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="scoreExpression" value="${promotion.scoreExpression }" class="text"/>&nbsp;表达式可用参数:(积分、数量) [+ - * /]&nbsp;&nbsp;&nbsp;示例:数量*积分*0.8</td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;排序：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="orderList" value="${promotion.orderList }" class="text" maxlength="10"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;设置：&nbsp;&nbsp;</td>
		    			<td align="left">
		    				<input type="checkbox" name="isFreeDeliver" value="true" ${promotion.isFreeDeliver?'checked="checked"':'' }/>&nbsp;是否免运费
		    				<input type="checkbox" name="isAllowedCoupon" value="true" ${promotion.isAllowedCoupon?'checked="checked"':'' }/>&nbsp;是否允许使用优惠券  
		    			</td>
		    		</tr>
				</table>
				<table width="100%" class="input tabContent table sort">
					<tr>
		    			<td width="20%" align="right" valign="top"><a href="#" id="couponAll" title="点击我全选" class="green">[全选]</a>&nbsp;&nbsp;赠送优惠券:&nbsp;&nbsp;</td>
		    			<td id="couponTd" width="80%">
		    				<c:if test="${coupons != null}">
		    				<c:forEach var="row" items="${coupons}">
		    				<label><input type="checkbox" name="couponIds" value="${row.id }" ${fn:containsObject(promotion.coupons,row)?'checked="checked"':'' } class="coupon"/>&nbsp;${row.name }</label>
		    				</c:forEach>
		    				</c:if>
		    			</td>
		    		</tr>
	   				<tr>
		    			<td align="right" valign="top"><a href="#" id="gradeAll" title="点击我全选" class="green">[全选]</a>&nbsp;&nbsp;允许参与会员等级:&nbsp;&nbsp;</td>
		    			<td id="gradeTd">
		    				<c:if test="${grades != null}">
		    				<c:forEach var="row" items="${grades}">
		    				<label><input type="checkbox" name="gradeIds" value="${row.id }" ${fn:containsObject(promotion.grades,row)?'checked="checked"':'' } class="grade"/>&nbsp;${row.name }</label>
		    				</c:forEach>
		    				</c:if>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td align="right" valign="top"><a href="#" id="brandAll" title="点击我全选" class="green">[全选]</a>&nbsp;&nbsp;允许参与品牌:&nbsp;&nbsp;</td>
		    			<td id="brandTd">
		    				<c:if test="${brands != null}">
		    				<c:forEach var="row" items="${brands}">
		    				<label><input type="checkbox" name="brandIds" value="${row.id }" ${fn:containsObject(promotion.brands,row)?'checked="checked"':'' } class="brand"/>&nbsp;${row.name }</label>
		    				</c:forEach>
		    				</c:if>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td width="20%" align="right" valign="top"><a href="#" id="categorySelect" title="点击选择商品分类" class="green">[请选择商品分类]</a>&nbsp;&nbsp;允许商品分类:&nbsp;&nbsp;</td>
		    			<td id="categoryTd">
		    				<c:if test="${promotion.productCategorys != null}">
		    				<c:forEach var="row" items="${promotion.productCategorys}">
		    				<label><input type="checkbox" name="categoryIds" value="${row.id }" class="categoryId${row.id } categoryIds" checked="checked"/>&nbsp;${row.name }</label>
		    				</c:forEach>
		    				</c:if>
		    			</td>
		    		</tr>
	    		</table>
	    		<table id="productPanel" data-name="productIds" width="100%" class="input tabContent table">
	   				<tr>
						<td align="left" colspan="6" height="40">
							<a href="#" id="addProductButton" class="easyui-linkbutton" iconCls="icon-add">添加允许参与商品</a>
						</td>
					</tr>
					<tr class="header">
						<td align="left" width="15%">商品编号</td>
						<td align="left">商品名称</td>
						<td width="10%">商品分类</td>
						<td width="10%" align="left">商品价格</td>
						<td width="10%" align="left">商品库存</td>
						<td width="5%">操作</td>
					</tr>
					<c:if test="${promotion.products != null}">
					<c:forEach var="row" items="${promotion.products}">
					<tr class="body${row.number }">
						<td align="left">
							${row.number }<input type="hidden" name="productIds" value="${row.id }"/>
						</td>
						<td>${row.name }</td>
						<td>${row.productCategory.name }</td>
						<td>${fn:currency(row.salePrice,false) }</td>
						<td>${row.stock }</td>
						<td><a href="#" class="delete">[删除]</a></td>
					</tr>
					</c:forEach>
					</c:if>
	    		</table>
	    		<table id="giftPanel" data-name="giftItems" width="100%" class="input tabContent table">
	   				<tr>
						<td align="left" colspan="7" height="40">
							<a href="#" id="addGiftButton" class="easyui-linkbutton" iconCls="icon-add">添加赠品</a>
						</td>
					</tr>
					<tr class="header">
						<td align="left" width="15%">商品编号</td>
						<td align="left">商品名称</td>
						<td width="10%">商品分类</td>
						<td width="10%">商品价格</td>
						<td width="10%">商品库存</td>
						<td width="10%">赠送数量</td>
						<td width="5%">操作</td>
					</tr>
					<c:if test="${promotion.giftItems != null}">
					<c:forEach var="row" items="${promotion.giftItems}" varStatus="i">
					<tr class="body${row.product.number }">
						<td align="left">
							${row.product.number }<input type="hidden" name="giftItems[${i.index }].product.id" value="${row.product.id }"/>
						</td>
						<td align="left">${row.product.name }</td>
						<td>${row.product.productCategory.name }</td>
						<td >${fn:currency(row.product.salePrice,false) }</td>
						<td>${row.product.stock }</td>
						<td><input type="text" name="giftItems[${i.index }].quantity" value="${row.quantity }" class="text short quantity"/></td>
						<td><a href="#" class="delete">[删除]</a></td>
					</tr>
					</c:forEach>
					</c:if>
	    		</table>
	   			<table width="100%" class="input tabContent table">
	   				<tr>
		    			<td><textarea name="introduction" style="width:90%;height:350px;"></textarea></td>
		    		</tr>
	    		</table>
		    	<table width="100%" align="center" cellpadding="0" cellspacing="0">
		      		<tr>
	      				<td width="60%" align="center" height="50">
	      					<a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保      存</a>
	      					<a href="#" button="back" class="easyui-linkbutton" data-options="iconCls:'icon-back'">返      回</a>
	      				</td>
	      				<td width="40%"></td>
	      			</tr>
		    	</table>
		    </form>
		</div>
	</div>
	<div id="categoryWindow" class="easyui-window" title="选择商品分类" data-options="modal:true,closed:true,minimizable:false,maximizable:false,tools:'#tools'" style="width:800px;height:500px;padding:1px;">
		<table id="categoryTable"></table>
	</div>
	<div id="productWindow" class="easyui-window" title="选择商品" data-options="modal:true,closed:true,minimizable:false,maximizable:false,tools:'#tools'" style="width:900px;height:550px;padding:1px;">
		<table id="productTable"></table>
	</div>
	<div id="tools"><a id="selectSaveButton" href="javascript:void(0)" class="icon-save"></a></div>
	<div id="toolBox">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td height="50">
					商品编号：<input type="text" id="productNumber" class="text middle"/>
					商品名称：<input type="text" id="productName" class="text middle"/>
					商品分类：<select id="productCategorySelect" class="select">
								<option value="">--请选择--</option>
								<c:if test="${categorys != null}">
									<c:forEach var="row" items="${categorys}">
										<option value="${row.id }">
											<c:forEach var="i" begin="0" end="${row.level}" step="1">
											&nbsp;&nbsp;
											</c:forEach>
											${row.name }
										</option>
									</c:forEach>
								</c:if>
							</select>
					商品品牌：<select id="brandSelect" name="isPublish" class="select">
								<option value="">--请选择--</option>
								<c:if test="${brands != null}">
			    				<c:forEach var="row" items="${brands}">
			    				<option value="${row.id }">${row.name }</option>
			    				</c:forEach>
			    				</c:if>
							  </select>
					<a href="#" id="searchButton" class="easyui-linkbutton" iconCls="icon-search">查      询</a>
					<a href="#" id="addSureButton" class="easyui-linkbutton" iconCls="icon-add">添      加</a>
				</td>
			</tr>
		</table>
	</div>
	<script type="text/javascript">
		KindEditor.ready(function(K) {
			var editor1 = K.create('textarea[name="introduction"]', {
				uploadJson : '${path }/back/file/upload.shtml',
				fileManagerJson : '${path }/back/file/browser.shtml',
				allowFileManager : true,
				afterCreate : function() {},
				afterBlur: function(){this.sync();}
			});
		});
	</script>
</body>
</html>