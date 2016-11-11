<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>修改订单</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $editForm = $("#editForm");
			var $saveButton = $("#saveButton");
			var $province = $("#area_province");
			var $city = $("#area_city");
			var $county = $("#area_county");
			var $paymentwayId = $("#paymentwayId");
			var $deliverwayId = $("#deliverwayId");
			var $orderItemTable = $("#orderItemTable");
			var $productNumber = $(":text[name='productNumber']");
			var $addProductButton = $("#addProductButton");
			var $totalAmount = $("#totalAmount");
			var $totalWeight = $("#totalWeight");
			var $totalQuantity = $("#totalQuantity");
			var $isInvoice = $("#isInvoice");
			var $invoiceTitle = $("#invoiceTitle");
			var $areaId = $("#areaId");
			var $tab = $("#tab");
			var orderItemIndex = ${fn:length(order.orderItems)};
			var isCalculate = false;
			
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

			$areaId.lSelect({
				url: "${path}/back/common/area.shtml"
			});
			
			$isInvoice.click(function(){
				$invoiceTitle.prop("disabled",!$(this).prop("checked"));
			});
			$addProductButton.click(function(){
				var productNumber = $.trim($productNumber.val());
				if(productNumber == ""){
					$.messager.alert('提示','请填写商品编号!');
					return false;
				}
				var isExits = false;
				$orderItemTable.find(".productNumber").each(function(){
					if(productNumber == $(this).val()){
						isExits = true;
						return false;
					}
				});
				if(isExits){
					$.messager.alert('提示','商品已经存在!');
					return false;
				}
				$.ajax({
					url: "${path}/back/order/addOrderItem.shtml",
					type: "POST",
					data: {productNumber: productNumber},
					dataType: "json",
					success: function(data) {
						if(data.status=="success"){
							var html = '<tr class="item">'
							+ '<td align="left" >'+data.productNumber
							+ '	<input type="hidden" name="orderItems['+orderItemIndex+'].productNumber" class="productNumber" value="'+data.productNumber+'"/>'
							+ '	<input type="hidden" name="orderItems['+orderItemIndex+'].productWeight" class="productWeight" value="'+data.productWeight+'"/>'
							+ '</td>'
							+ '<td align="left">'+data.productName+'</td>'
							+ '<td align="left">'
							+ '	<input type="text" name="orderItems['+orderItemIndex+'].productPrice" data-value="'+$.currency(data.productPrice,false,false)+'" value="'+$.currency(data.productPrice,false,false)+'" class="text short productPrice" maxlength="10"/>'
							+ '</td>'
							+ '<td align="left">'
							+ '	<input type="text" name="orderItems['+orderItemIndex+'].productQuantity" data-value="1" value="1" class="text short productQuantity" maxlength="4"/>'
							+ '</td>'
							+ '<td><span class="totalAmount">'+$.currency(data.totalPrice,true,false)+'</span></td>'
							+ '<td><a href="#" class="delete">[删除]</a></td>'
							+ '</tr>';
							$(html).appendTo($orderItemTable);
							orderItemIndex++;
							isCalculate = true;
						}else{
							$.messager.alert('提示',data.message);
						}
					}
				});
			});
			$orderItemTable.on("click",".delete",function(){
				var $this = $(this);
				if($orderItemTable.find("tr.item").size() > 1){
					$this.closest("tr").remove();
					isCalculate = true;
				}else{
					$.messager.alert('提示',"删除失败，至少保留一个商品!");
				}
			});
			var calculateTimer = new (function() {
		        incrementTime = 500,
		        excute = function() {
		        	if(!$editForm.valid()){
						return false;	
			        }
			        var isAjax = false;
		        	$orderItemTable.find("tr[class='item']").each(function(){
						var $this = $(this);
						var $productPrice = $this.find(":text[class*='productPrice']");
						var $productQuantity = $this.find(":text[class*='productQuantity']");
						if(parseFloat($productPrice.val()) != parseFloat($productPrice.data("value"))
								|| parseInt($productQuantity.val()) != parseInt($productQuantity.data("value"))){
							isAjax = true;
							$productPrice.data("value",$productPrice.val());
							$productQuantity.data("value",$productQuantity.val());
							var total = parseFloat($productPrice.val()) * parseFloat($productQuantity.val());
							$this.find("span.totalAmount").text($.currency(total,true,false));
						}
			        });
			        if(isCalculate || isAjax){
			        	$.ajax({
							url: "${path}/back/order/calculate.shtml",
							type: "POST",
							data: $editForm.serialize(),
							dataType: "json",
							success: function(data) {
								if(data.status=="success"){
									$totalAmount.text(data.totalAmount);
									$totalWeight.text(data.totalWeight);
									$totalQuantity.text(data.totalQuantity);
								}else{
									$.messager.alert('提示',data.message);
								}
								isCalculate = false;
								isAjax = false;
							},
							error:function(){
								isCalculate = false;
								isAjax = false;
							}
						});
				    }
		        },
		        init = function() {
		        	calculateTimer.Timer = $.timer(excute, incrementTime, true);
		        };
			    this.resetStopwatch = function() {
			        this.Timer.stop().once();
			    };
		    	$(init);
			});
			
			$.validator.addClassRules({
				productPrice: {
					required: true,
					decimal:{
						length:18,
						scale:${fn:config("priceScale")}
					},
					min: 0
				},
				productQuantity: {
					required: true,
					integer: true,
					min: 1
				}
			});
			
			$editForm.validate({
				rules: {
					"paymentway.id": { required: true },
					"deliverway.id": { required: true },
					"receiver": { required: true },
					"deliveryAmount": { required: true },
					"adjustLimit": { 
						required: true, 
						decimal:{
							length:18,
							scale:${fn:config("priceScale")}
						}
					},
					"score": { required: true },
					"address": { required: true },
					"phone": { required: true, isMobileOrTel:true }
				}
			});
			$saveButton.click(function(){
				$(":hidden[name='areaVO.provinceName']").val($province.find("option:selected").text());
				$(":hidden[name='areaVO.cityName']").val($city.find("option:selected").text());
				$(":hidden[name='areaVO.countyName']").val($county.find("option:selected").text());
				$editForm.submit();
			});
			
		});
	</script>
</head>
<body>
	<div id="mainBox" class="main-box">
		<div id="mainPanel" class="easyui-panel" iconCls="icon-edit" title="&nbsp;&nbsp;修  改  订  单  ">
			<form id="editForm" action="${path }/back/order/edit.shtml" method="post">
				<input type="hidden" name="id" value="${order.id }"/>
				<ul id="tab" class="tab">
					<li><input value="基本信息" type="button" /></li>
					<li><input value="商品信息" type="button" /></li>
				</ul>
				<table width="100%" class="input tabContent table">
                	<tr>
                		<td width="12%" align="right">订单编号：</td>
                		<td width="35%">${order.number }</td>
                		<td width="8%" align="right">会员：</td>
                		<td width="45%">${order.member.username }</td>
                	</tr>
                	<tr>
                		<td align="right">订单状态：</td>
                		<td><fmt:message key="OrderStatus.${order.orderStatus }"/></td>
                		<td align="right">支付状态：</td>
                		<td><fmt:message key="PaymentStatus.${order.payStatus }"/></td>
                	</tr>
                	<tr>
                		<td align="right">配送状态：</td>
                		<td><fmt:message key="DeliverStatus.${order.deliverStatus }"/></td>
                		<td align="right">创建时间：</td>
                		<td><fmt:formatDate  value="${order.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                	</tr>
                	<tr>
                		<td align="right">总金额：</td>
                		<td><span id="totalAmount">${fn:currency(order.totalAmount,true)}</span></td>
                		<td align="right">已付金额：</td>
                		<td>${fn:currency(order.paidAmount,true) }</td>
                	</tr>
                	<tr>
                		<td align="right">帐户付款金额：</td>
                		<td>${fn:currency(order.depositAmount,true) }</td>
                		<td align="right" >运费：</td>
                		<td><input type="text" name="deliveryAmount" value="${fn:currency(order.deliveryAmount,false) }" class="text short" maxlength="10"/></td>
                	</tr>
                	<tr>
                		<td align="right">是否使用优惠券：</td>
                		<td>${order.couponInfo!=null?"<font color='green'>是</font>":"<font color='red'>否</font>" }</td>
                		<td align="right">优惠券金额：</td>
                		<td>${fn:currency(order.voucherAmount,true) }</td>
                	</tr>
                	<tr>
                		<td align="right">促销方式：</td>
                		<td>${empty order.promotion ? "-" : order.promotion}</td>
                		<td align="right">促销金额：</td>
                		<td>${fn:currency(order.promAmount,true) }</td>
                	</tr>
                	<tr>
                		<td align="right">重量：</td>
                		<td><span id="totalWeight">${order.weight }</span></td>
                		<td align="right">数量：</td>
                		<td><span id="totalQuantity">${order.quantity }</span></td>
                	</tr>
                	<tr>
                		<td align="right"><font color="red">*</font>&nbsp;调整金额：</td>
                		<td><input type="text" name="adjustLimit" value="${fn:currency(order.adjustLimit,false) }" class="text short" maxlength="10"/></td>
                		<td align="right" ><font color="red">*</font>&nbsp;赠送积分：</td>
                		<td><input type="text" name="score" value="${order.score }" class="text short" maxlength="10"/></td>
                	</tr>
                	<tr>
                		<td align="right"><font color="red">*</font>&nbsp;支付方式：</td>
                		<td>
                			<select id="paymentwayId" name="paymentway.id" class="select">
								<option value="">--请选择--</option>
								<c:if test="${paymentways != null }">
									<c:forEach var="row" items="${paymentways}">
									<option value="${row.id }" ${order.paymentway.id==row.id?"selected='selected'":"" }>${row.name }</option>
									</c:forEach>
								</c:if>
							</select>
						</td>
                		<td align="right"><font color="red">*</font>&nbsp;配送方式：</td>
                		<td>
                			<select id="deliverwayId" name="deliverway.id" class="select">
                				<option value="">--请选择--</option>
                				<c:if test="${deliverways != null }">
	                				<c:forEach var="row" items="${deliverways }">
	                				<option value="${row.id }" ${order.deliverway.id==row.id?"selected='selected'":"" }>${row.name }</option>
	                				</c:forEach>
                				</c:if>
                			</select>
                		</td>
                	</tr>
                	<tr>
                		<td align="right">发票抬头：</td>
                		<td>
                			<input type="text" id="invoiceTitle" name="invoiceTitle" ${order.isInvoice?"":"disabled='disabled'" } value="${order.invoiceTitle }" class="text" maxlength="255"/>
                			<input type="checkbox" id="isInvoice" name="isInvoice" ${order.isInvoice?"checked='checked'":"" } value="true"/>是否开发票	
                		</td>
                		<td align="right">手续费：</td>
                		<td>${fn:currency(order.serviceAmount,true) }</td>
                	</tr>
                	<tr>
                		<td align="right"><font color="red">*</font>&nbsp;收货人：</td>
                		<td><input type="text" name="receiver" value="${order.receiver }" class="text" maxlength="50"/></td>
                		<td align="right"><font color="red">*</font>&nbsp;地区：</td>
                		<td>
                			<input type="hidden" id="areaId" name="areaId" value="${order.area.id }" treePath="${order.area.path }" />
                		</td>
                	</tr>
                	<tr>
                		<td align="right"><font color="red">*</font>&nbsp;详细地址：</td>
                		<td><input type="text" name="address" value="${order.address }" class="text long" maxlength="255"/></td>
                		<td align="right">邮编：</td>
                		<td><input type="text" name="zipCode" value="${order.zipCode }" class="text middle" maxlength="255"/></td>
                	</tr>
                	<tr>
                		<td align="right"><font color="red">*</font>&nbsp;联系电话：</td>
                		<td><input type="text" name="phone" value="${order.phone }" class="text" maxlength="255"/></td>
                		<td align="right">备注：</td>
                		<td><input type="text" name="remark" value="${order.remark }" class="text long" maxlength="255"/></td>
                	</tr>
                </table>
				<table id="orderItemTable" width="100%" class="input tabContent table">
					<tr>
						<td align="left" colspan="8" height="40">
							<span>商品编号：</span>
							<input type="text" name="productNumber" class="text"/>
							<a href="#" id="addProductButton" class="easyui-linkbutton" iconCls="icon-add">添加商品</a>
						</td>
					</tr>
					<tr class="header">
						<td align="left" width="15%">商品编号</td>
						<td align="left">商品名称</td>
						<td width="15%" align="left">商品价格</td>
						<td width="15%" align="left">商品数量</td>
						<td width="8%">小计</td>
						<td width="5%">操作</td>
					</tr>
					<c:if test="${order.orderItems != null}">
						<c:forEach var="row" items="${order.orderItems}" varStatus="i">
						<tr class="item">
							<td align="left" >
								${row.productNumber }
								<input type="hidden" name="orderItems[${i.index }].id" value="${row.id }"/>
								<input type="hidden" name="orderItems[${i.index }].productNumber" class="productNumber" value="${row.productNumber }"/>
								<input type="hidden" name="orderItems[${i.index }].productWeight" class="productWeight" value="${row.productWeight }"/>
							</td>
							<td align="left">${row.productName }</td>
							<td align="left">
								<input type="text" name="orderItems[${i.index }].productPrice" data-value="${fn:currency(row.productPrice,false)}" value="${fn:currency(row.productPrice,false)}" class="text short productPrice" maxlength="10"/>
							</td>
							<td align="left">
								<input type="text" name="orderItems[${i.index }].productQuantity" data-value="${row.productQuantity }" value="${row.productQuantity }" class="text short productQuantity" maxlength="4"/>
							</td>
							<td><span class="totalAmount">${fn:currency(row.totalAmount,true) }</span></td>
							<td><a href="#" class="delete">[删除]</a></td>
						</tr>
						</c:forEach>
					</c:if>
				</table>
				<div class="edit-tools">
		   			<a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		   			<a href="#" id="backButton" button="back" class="easyui-linkbutton" data-options="iconCls:'icon-back'">返回</a>
				</div>
		    </form>
		</div>
	</div>
</body>
</html>