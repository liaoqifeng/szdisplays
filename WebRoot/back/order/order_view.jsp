<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>查看订单</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $saveButton = $("#saveButton");
			var $tab = $("#tab");
			var $processButton = $("#processButton");
			var $paymentButton = $("#paymentButton");
			var $completedButton = $("#completedButton");
			var $deliverButton = $("#deliverButton");
			var $refundButton = $("#refundButton");
			var $returnsButton = $("#returnsButton");
			var $invalidButton = $("#invalidButton");
			
			var $paymentOrder = $("#paymentOrder");
			var $deliverOrder = $("#deliverOrder");
			var $returnsOrder = $("#returnsOrder");
			var $refundsOrder = $("#refundsOrder");
			
			var $processForm = $("#processForm");
			var $completeForm = $("#completeForm");
			var $invalidForm = $("#invalidForm");
			
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
			$processButton.click(function(){
				$.messager.confirm('提示', '订单确认后无法修改,是否确认订单?', function(r){
					if(r){
						$processForm.submit();
					}
				});
			});

			$completedButton.click(function(){
				$.messager.confirm('提示', '订单完成后无法退换货,是否确认完成订单?', function(r){
					if(r){
						$completeForm.submit();
					}
				});
			});

			$invalidButton.click(function(){
				$.messager.confirm('提示', '订单作废后订单失效,是否确认作废订单?', function(r){
					if(r){
						$invalidForm.submit();
					}
				});
			});
			$paymentButton.click(function(){
				$paymentOrder.window("open");
			});
			$deliverButton.click(function(){
				$deliverOrder.window("open");
			});
			$returnsButton.click(function(){
				$returnsOrder.window("open");
			});
			$refundButton.click(function(){
				$refundsOrder.window("open");
			});
		});
	</script>
</head>
<body>
	<div id="mainBox" class="main-box">
		<div id="mainPanel" class="easyui-panel" iconCls="icon-edit" title="&nbsp;&nbsp;查  看  订  单  ">
			<form id="processForm" action="${path}/back/order/process.shtml" method="post">
				<input type="hidden" name="orderId" value="${order.id }"/>
			</form>
			<form id="completeForm" action="${path}/back/order/complete.shtml" method="post">
				<input type="hidden" name="id" value="${order.id}" />
			</form>
			<form id="invalidForm" action="${path}/back/order/invalid.shtml" method="post">
				<input type="hidden" name="id" value="${order.id}" />
			</form>
			<div class="order-view-tools">
	   			<input type="button" id="processButton" value="确认" class="button" ${(order.isExpired || order.orderStatus != "unprocessed")?"disabled='disabled'":"" } />
	   			<input type="button" id="paymentButton" value="支付" class="button" ${(order.isExpired || order.orderStatus != "processed" || (order.payStatus != "unpaid" && order.payStatus != "partPaid"))?"disabled='disabled'":"" } />
	   			<input type="button" id="deliverButton" value="发货" class="button" ${(order.isExpired || order.orderStatus != "processed" || (order.deliverStatus != "unDeliver" && order.deliverStatus != "partDeliver"))?"disabled='disabled'":"" } />
	   			<input type="button" id="completedButton" value="完成" class="button" ${(order.isExpired || order.orderStatus != "processed")?"disabled='disabled'":"" } />
	   			
	   			<input type="button" id="refundButton" value="退款" class="button" ${(order.isExpired || order.orderStatus != "processed" || (order.payStatus != "paid" && order.payStatus != "partPaid" && order.payStatus != "partRefund"))?"disabled='disabled'":"" } style="margin-left:50px;" />
	   			<input type="button" id="returnsButton" value="退货" class="button" ${(order.isExpired || order.orderStatus != "processed" || (order.deliverStatus != "deliver" && order.deliverStatus != "partDeliver" && order.deliverStatus != "partReturns"))?"disabled='disabled'":"" }/>
	   			
	   			<input type="button" id="invalidButton" value="作废" class="button" ${(order.isExpired || order.orderStatus != "unprocessed")?"disabled='disabled'":"" }/>
			</div>
			<ul id="tab" class="tab">
				<li><input value="基本信息" type="button" /></li>
				<li><input value="商品信息" type="button" /></li>
				<li><input value="支付信息" type="button" /></li>
				<li><input value="发货信息" type="button" /></li>
				<li><input value="退款信息" type="button" /></li>
				<li><input value="退货信息" type="button" /></li>
				<li><input value="订单日志" type="button" /></li>
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
               		<td>
               			<fmt:message key="OrderStatus.${order.orderStatus }"/>
               			<c:if test="${order.isExpired}">
               				<span title="${order.expire}">[已过期]</span>
               			</c:if>
               		</td>
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
               		<td>${fn:currency(order.deliveryAmount,false) }</td>
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
               		<td align="right">调整金额：</td>
               		<td>${fn:currency(order.adjustLimit,false) }</td>
               		<td align="right" >赠送积分：</td>
               		<td>${order.score }</td>
               	</tr>
               	<tr>
               		<td align="right">支付方式：</td>
               		<td>${order.paymentway.name}</td>
               		<td align="right">配送方式：</td>
               		<td>${order.deliverway.name}</td>
               	</tr>
               	<tr>
               		<td align="right">发票抬头：</td>
               		<td>${order.invoiceTitle }</td>
               		<td align="right">手续费：</td>
               		<td>${fn:currency(order.serviceAmount,true) }</td>
               	</tr>
               	<tr>
               		<td align="right">收货人：</td>
               		<td>${order.receiver }</td>
               		<td align="right">地区：</td>
               		<td>${order.areaName }</td>
               	</tr>
               	<tr>
               		<td align="right">详细地址：</td>
               		<td>${order.address }</td>
               		<td align="right">邮编：</td>
               		<td>${order.zipCode }</td>
               	</tr>
               	<tr>
               		<td align="right">联系电话：</td>
               		<td>${order.phone }</td>
               		<td align="right">备注：</td>
               		<td>${order.remark }</td>
               	</tr>
            </table>
			<table width="100%" class="input tabContent table">
				<tr class="header">
					<td align="left" width="15%">商品编号</td>
					<td align="left">商品名称</td>
					<td width="15%" align="left">商品价格</td>
					<td width="15%" align="left">商品数量</td>
					<td width="8%">小计</td>
				</tr>
				<c:if test="${order.orderItems != null}">
					<c:forEach var="row" items="${order.orderItems}" varStatus="i">
					<tr class="item">
						<td align="left">${row.productNumber }</td>
						<td align="left">${row.productName }</td>
						<td align="left">${fn:currency(row.productPrice,false)}</td>
						<td align="left">${row.productQuantity }</td>
						<td>${fn:currency(row.totalAmount,true) }</td>
					</tr>
					</c:forEach>
				</c:if>
			</table>
			<table width="100%" class="input tabContent table">
				<tr class="header">
					<th>支付编号</th>
					<th>支付方式</th>
					<th>类型</th>
					<th>支付金额</th>
					<th>支付状态</th>
					<th>支付日期</th>
				</tr>
				<c:if test="${order.paymentInfos != null}">
					<c:forEach var="row" items="${order.paymentInfos}">
					<tr align="center">
						<td>${row.number }</td>
						<td>${row.paymentwayName }</td>
						<td><fmt:message key="PaymentInfoType.${row.type }"/></td>
						<td>${fn:currency(row.paidAmount,false)}</td>
						<td><fmt:message key="PaymentInfoStatus.${row.status }"/></td>
						<td><fmt:formatDate  value="${row.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					</c:forEach>
				</c:if>
			</table>
			<table width="100%" class="input tabContent table">
				<tr class="header">
					<th>发货编号</th>
					<th>配送方式</th>
					<th>物流单号</th>
					<th>收货人</th>
					<th>联系电话</th>
					<th>发货日期</th>
				</tr>
				<c:if test="${order.deliverInfos != null}">
					<c:forEach var="row" items="${order.deliverInfos}">
					<tr align="center">
						<td>${row.number }</td>
						<td>${row.deliverwayName }</td>
						<td>${row.deliverCode }</td>
						<td>${row.receiver }</td>
						<td>${row.phone }</td>
						<td><fmt:formatDate  value="${row.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					</c:forEach>
				</c:if>
			</table>
			<table width="100%" class="input tabContent table">
				<tr class="header">
					<th>退款编号</th>
					<th>退款方式</th>
					<th>类型</th>
					<th>退款金额</th>
					<th>退款日期</th>
				</tr>
				<c:if test="${order.refunds != null}">
					<c:forEach var="row" items="${order.refunds}">
					<tr align="center">
						<td>${row.number }</td>
						<td>${row.paymentwayName }</td>
						<td><fmt:message key="RefundsType.${row.type }"/></td>
						<td>${fn:currency(row.paidAmount,false)}</td>
						<td><fmt:formatDate value="${row.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					</c:forEach>
				</c:if>
			</table>
			<table width="100%" class="input tabContent table">
				<tr class="header">
					<th>退货编号</th>
					<th>配送方式</th>
					<th>物流单号</th>
					<th>发货人</th>
					<th>联系电话</th>
					<th>发货日期</th>
				</tr>
				<c:if test="${order.returns != null}">
					<c:forEach var="row" items="${order.returns}">
					<tr align="center">
						<td>${row.number }</td>
						<td>${row.deliverwayName }</td>
						<td>${row.deliverCode }</td>
						<td>${row.shipper }</td>
						<td>${row.phone }</td>
						<td><fmt:formatDate  value="${row.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					</c:forEach>
				</c:if>
			</table>
			<table width="100%" class="input tabContent table">
				<tr class="header">
					<th>订单编号</th>
					<th>类型</th>
					<th>操作员</th>
					<th>内容</th>
					<th>创建日期</th>
				</tr>
				<c:if test="${order.logs != null}">
					<c:forEach var="row" items="${order.logs}">
					<tr align="center">
						<td>${row.orderNumber }</td>
						<td><fmt:message key="OrderLogType.${row.type }"/></td>
						<td>${row.operator }</td>
						<td>${row.info }</td>
						<td><fmt:formatDate  value="${row.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					</c:forEach>
				</c:if>
			</table>
		</div>
	</div>
	<c:if test="${(!order.isExpired && order.orderStatus == 'processed' && (order.payStatus == 'unpaid' || order.payStatus == 'partPaid'))}">
	<div id="paymentOrder" class="easyui-window" title="支付" data-options="modal:true,minimizable:false,maximizable:false,closed:true" style="width:800px;height:350px;padding:10px;">
		<form id="paymentForm" action="${path}/back/order/payment.shtml" method="post">
			<input type="hidden" name="orderId" value="${order.id }"/>
			<table width="100%" class="table table-tr-large">
				<tr>
					<td width="15%" align="right">订单编号：</td>
					<td width="35%">${order.number }</td>
					<td width="15%" align="right">下单时间：</td>
					<td width="35%"><fmt:formatDate  value="${order.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
				<tr>
					<td width="15%" align="right"><font color="red">*</font>&nbsp;支付类型：</td>
					<td width="35%">
						<select id="paymentInfoType" name="type" class="select">
							<option value="">--请选择--</option>
							<c:if test="${paymentInfoTypes != null}">
								<c:forEach var="row" items="${paymentInfoTypes}">
									<option value="${row }"><fmt:message key="PaymentInfoType.${row }"/></option>
								</c:forEach>
							</c:if>
						</select>
					</td>
					<td width="15%" align="right"><font color="red">*</font>&nbsp;支付方式：</td>
					<td width="35%">
						<select name="paymentwayId" class="select">
							<option value="">--请选择--</option>
							<c:if test="${paymentways != null}">
								<c:forEach var="row" items="${paymentways}">
									<option value="${row.id }">${row.name }</option>
								</c:forEach>
							</c:if>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">订单金额：</td>
					<td>${fn:currency(order.totalAmount,true)}</td>
					<td align="right">已付金额：</td>
					<td>${fn:currency(order.paidAmount,true) }</td>
				</tr>
				<tr>
					<td align="right"><font color="red">*</font>&nbsp;支付金额：</td>
					<td><input type="text" name="paidAmount" class="text"/></td>
					<td align="right">支付人：</td>
					<td><input type="text" name="payer" class="text"/></td>
				</tr>
				<tr>
					<td align="right">收款银行：</td>
					<td><input type="text" name="bank" class="text"/></td>
					<td align="right">收款帐号：</td>
					<td><input type="text" name="account" class="text"/></td>
				</tr>
				<tr>
					<td align="right">备注：</td>
					<td colspan="3"><input type="text" name="remark" class="text long"/></td>
				</tr>
				<tr class="footer">
					<td colspan="4" align="center">
						<input type="button" id="paymentSureButton" value="支付" class="button" />
	   					<input type="button" id="paymentCancelButton" value="取消" class="button" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		$().ready(function(){
			var $paymentOrder = $("#paymentOrder");
			var $paymentForm = $("#paymentForm");
			var $paymentInfoType = $("#paymentInfoType");
			var $paymentSureButton = $("#paymentSureButton");
			var $paymentCancelButton = $("#paymentCancelButton");
			$.validator.addMethod("deposit", 
				function(value, element, param) {
					return this.optional(element) || $paymentInfoType.val() != "deposit" || parseFloat(value) <= parseFloat(param);
				},
				"帐户余额不足"
			);
			$paymentForm.validate({
				rules: {
					paidAmount: {
						required: true,
						decimal: {
							length:18,
							scale:${fn:config("priceScale")}
						},
						deposit: ${order.member.deposit}
					},
					type:{required: true},
					paymentwayId:{required: true},
					account:{integer: true}
				}
			});
			$paymentSureButton.click(function(){
				$paymentForm.submit();
			});
			$paymentCancelButton.click(function(){
				$paymentOrder.window("close");
			});
		});
	</script>
	</c:if>
	<c:if test="${(!order.isExpired && order.orderStatus == 'processed' && (order.deliverStatus == 'unDeliver' || order.deliverStatus == 'partDeliver'))}">
	<div id="deliverOrder" class="easyui-window" title="发货" data-options="modal:true,minimizable:false,maximizable:false,closed:true" style="width:850px;height:470px;padding:10px;">
		<form id="deliverForm" action="${path}/back/order/deliver.shtml" method="post">
			<input type="hidden" name="orderId" value="${order.id }"/>
			<table width="100%" class="table table-tr-large">
				<tr>
					<td width="15%" align="right">订单编号：</td>
					<td width="40%">${order.number }</td>
					<td width="10%" align="right">下单时间：</td>
					<td width="35%"><fmt:formatDate  value="${order.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
				<tr>
					<td align="right"><font color="red">*</font>&nbsp;配送方式：</td>
					<td>
						<select name="deliverwayId" class="select">
							<option value="">--请选择--</option>
							<c:if test="${deliverways != null}">
								<c:forEach var="row" items="${deliverways}">
									<option value="${row.id }" ${row == order.deliverway ? 'selected="selected"' : '' }>${row.name }</option>
								</c:forEach>
							</c:if>
						</select>
					</td>
					<td align="right">物流公司：</td>
					<td>
						<select name="logisticsId" class="select">
							<option value="">--请选择--</option>
							<c:if test="${logisticses != null}">
								<c:forEach var="row" items="${logisticses}">
									<option value="${row.id }" ${row == order.deliverway.logistics ? 'selected="selected"' : '' }>${row.name }</option>
								</c:forEach>
							</c:if>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">物流单号：</td>
					<td><input type="text" name="deliverCode" class="text" maxlength="200"/></td>
					<td align="right"><font color="red">*</font>&nbsp;物流费用：</td>
					<td><input type="text" name="deliverAmount" value="0" class="text short" maxlength="10"/></td>
				</tr>
				<tr>
					<td align="right"><font color="red">*</font>&nbsp;收货人：</td>
					<td><input type="text" name="receiver" value="${order.receiver }" class="text"/></td>
					<td align="right">邮编：</td>
					<td><input type="text" name="zipCode" value="${order.zipCode }" class="text" maxlength="200"/></td>
				</tr>
				<tr>
					<td align="right"><font color="red">*</font>&nbsp;地区：</td>
					<td><input type="hidden" id="areaId" name="areaId" value="${order.area.id }" treePath="${order.area.path }" /></td>
					<td align="right"><font color="red">*</font>&nbsp;地址：</td>
					<td><input type="text" name="address" value="${order.address }" class="text" maxlength="200"/></td>
				</tr>
				<tr>
					<td align="right"><font color="red">*</font>&nbsp;电话：</td>
					<td><input type="text" name="phone" value="${order.phone }" class="text" maxlength="200"/></td>
					<td align="right">备注：</td>
					<td colspan="3"><input type="text" name="remark" value="${order.remark }" class="text" maxlength="200"/></td>
				</tr>
				<tr>
					<td colspan="4">
						<table id="orderItemTable" width="100%" class="input tabContent table">
							<tr class="header">
								<td width="15%">商品编号</td>
								<td>商品名称</td>
								<td width="10%" align="center">购买数量</td>
								<td width="10%" align="center">已发货数量</td>
								<td width="10%" align="center">已退货数量</td>
								<td width="28%" align="center">发货数量</td>
							</tr>
							<c:if test="${order.orderItems != null}">
								<c:forEach var="row" items="${order.orderItems}" varStatus="i">
								<tr class="item">
									<td align="left" >
										${row.productNumber }
										<input type="hidden" name="deliverInfoItems[${i.index }].productNumber" class="productNumber" value="${row.productNumber }"/>
										<input type="hidden" name="deliverInfoItems[${i.index }].productName" class="productName" value="${row.productName }"/>
									</td>
									<td align="left"><span title="${row.productName }">${fn:substr(row.productName,18,"......") }</span></td>
									<td align="center">${row.productQuantity }</td>
									<td align="center">${row.deliveryQuantity }</td>
									<td align="center">${row.returnQuantity }</td>
									<td align="center"><input type="text" name="deliverInfoItems[${i.index }].productQuantity" data-max="${row.productQuantity-row.deliveryQuantity }" value="${row.productQuantity-row.deliveryQuantity }" class="text short deliverInfoItemsQuantity"/></td>
								</tr>
								</c:forEach>
							</c:if>
						</table>
					</td>
				</tr>
				<tr class="footer">
					<td colspan="4" align="center">
						<input type="button" id="deliverSureButton" value="发货" class="button" />
	   					<input type="button" id="deliverCancelButton" value="取消" class="button" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		var $deliverOrder = $("#deliverOrder");
		var $deliverForm = $("#deliverForm");
		var $deliverSureButton = $("#deliverSureButton");
		var $deliverCancelButton = $("#deliverCancelButton");
		var $areaId = $("#areaId");

		$areaId.lSelect({
			url: "${path}/back/common/area.shtml"
		});
		
		$deliverForm.validate({
			rules: {
				"deliverAmount": {
					decimal: {
						required: true,
						length:18,
						scale:${fn:config("priceScale")}
					}
				},
				"areaId":{required: true},
				"deliverwayId":{required: true},
				"receiver":{required: true},
				"address":{required: true},
				"phone":{required: true,isMobileOrTel:true}
			}
		});
		$.validator.addClassRules({
			deliverInfoItemsQuantity: {
				required: true,
				integer: true,
				maximum:true
			}
		});
		$deliverSureButton.click(function(){
			$deliverForm.submit();
		});
		$deliverCancelButton.click(function(){
			$deliverOrder.window("close");
		});
	</script>
	</c:if>
	
	<c:if test="${(!order.isExpired && order.orderStatus == 'processed' && (order.deliverStatus == 'partDeliver' || order.deliverStatus == 'deliver' || order.deliverStatus == 'partReturns'))}">
	<div id="returnsOrder" class="easyui-window" title="退货" data-options="modal:true,minimizable:false,maximizable:false,closed:true" style="width:850px;height:470px;padding:10px;">
		<form id="returnsForm" action="${path}/back/order/returns.shtml" method="post">
			<input type="hidden" name="orderId" value="${order.id }"/>
			<table width="100%" class="table table-tr-large">
				<tr>
					<td width="15%" align="right">订单编号：</td>
					<td width="40%">${order.number }</td>
					<td width="10%" align="right">下单时间：</td>
					<td width="35%"><fmt:formatDate  value="${order.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
				<tr>
					<td align="right"><font color="red">*</font>&nbsp;配送方式：</td>
					<td>
						<select name="deliverwayId" class="select">
							<option value="">--请选择--</option>
							<c:if test="${deliverways != null}">
								<c:forEach var="row" items="${deliverways}">
									<option value="${row.id }" ${row == order.deliverway ? 'selected="selected"' : '' }>${row.name }</option>
								</c:forEach>
							</c:if>
						</select>
					</td>
					<td align="right">物流公司：</td>
					<td>
						<select name="logisticsId" class="select">
							<option value="">--请选择--</option>
							<c:if test="${logisticses != null}">
								<c:forEach var="row" items="${logisticses}">
									<option value="${row.id }" ${row == order.deliverway.logistics ? 'selected="selected"' : '' }>${row.name }</option>
								</c:forEach>
							</c:if>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">物流单号：</td>
					<td><input type="text" name="deliverCode" class="text" maxlength="200"/></td>
					<td align="right"><font color="red">*</font>&nbsp;物流费用：</td>
					<td><input type="text" name="deliverAmount" value="0" class="text short" maxlength="10"/></td>
				</tr>
				<tr>
					<td align="right"><font color="red">*</font>&nbsp;发货人：</td>
					<td><input type="text" name="receiver" value="${order.receiver }" class="text"/></td>
					<td align="right">邮编：</td>
					<td><input type="text" name="zipCode" value="${order.zipCode }" class="text" maxlength="200"/></td>
				</tr>
				<tr>
					<td align="right"><font color="red">*</font>&nbsp;地区：</td>
					<td><input type="hidden" id="returnsAreaId" name="areaId" value="${order.area.id }" treePath="${order.area.path }" /></td>
					<td align="right"><font color="red">*</font>&nbsp;地址：</td>
					<td><input type="text" name="address" value="${order.address }" class="text" maxlength="200"/></td>
				</tr>
				<tr>
					<td align="right"><font color="red">*</font>&nbsp;电话：</td>
					<td><input type="text" name="phone" value="${order.phone }" class="text" maxlength="200"/></td>
					<td align="right">备注：</td>
					<td colspan="3"><input type="text" name="remark" value="${order.remark }" class="text" maxlength="200"/></td>
				</tr>
				<tr>
					<td colspan="4">
						<table id="orderItemTable" width="100%" class="input tabContent table">
							<tr class="header">
								<td width="15%">商品编号</td>
								<td>商品名称</td>
								<td width="10%" align="center">购买数量</td>
								<td width="10%" align="center">已发货数量</td>
								<td width="10%" align="center">已退货数量</td>
								<td width="28%" align="center">退货数量</td>
							</tr>
							<c:if test="${order.orderItems != null}">
								<c:forEach var="row" items="${order.orderItems}" varStatus="i">
								<tr class="item">
									<td align="left" >
										${row.productNumber }
										<input type="hidden" name="returnsItems[${i.index }].productNumber" class="productNumber" value="${row.productNumber }"/>
										<input type="hidden" name="returnsItems[${i.index }].productName" class="productName" value="${row.productName }"/>
									</td>
									<td align="left"><span title="${row.productName }">${fn:substr(row.productName,18,"......") }</span></td>
									<td align="center">${row.productQuantity }</td>
									<td align="center">${row.deliveryQuantity }</td>
									<td align="center">${row.returnQuantity }</td>
									<td align="center"><input type="text" name="returnsItems[${i.index }].productQuantity" data-max="${row.deliveryQuantity-row.returnQuantity }" value="${row.deliveryQuantity-row.returnQuantity }" class="text short returnsItemsQuantity"/></td>
								</tr>
								</c:forEach>
							</c:if>
						</table>
					</td>
				</tr>
				<tr class="footer">
					<td colspan="4" align="center">
						<input type="button" id="returnsSureButton" value="退货" class="button" />
	   					<input type="button" id="returnsCancelButton" value="取消" class="button" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		var $returnsOrder = $("#returnsOrder");
		var $returnsForm = $("#returnsForm");
		var $returnsSureButton = $("#returnsSureButton");
		var $returnsOrderCancelButton = $("#deliverCancelButton");
		var $returnsAreaId = $("#returnsAreaId");

		$returnsAreaId.lSelect({
			url: "${path}/back/common/area.shtml"
		});
		
		$returnsForm.validate({
			rules: {
				"deliverAmount": {
					decimal: {
						required: true,
						length:18,
						scale:${fn:config("priceScale")}
					}
				},
				"areaId":{required: true},
				"deliverwayId":{required: true},
				"receiver":{required: true},
				"address":{required: true},
				"phone":{required: true,isMobileOrTel:true}
			}
		});
		
		$.validator.addClassRules({
			returnsItemsQuantity: {
				required: true,
				integer: true,
				maximum:true
			}
		});
		
		$returnsSureButton.click(function(){
			$returnsForm.submit();
		});
		$returnsOrderCancelButton.click(function(){
			$returnsOrder.window("close");
		});
	</script>
	</c:if>
	
	<c:if test="${(!order.isExpired && order.orderStatus == 'processed' && (order.payStatus == 'paid' || order.payStatus == 'partPaid' || order.payStatus == 'partRefund'))}">
	<div id="refundsOrder" class="easyui-window" title="退款" data-options="modal:true,minimizable:false,maximizable:false,closed:true" style="width:800px;height:350px;padding:10px;">
		<form id="refundsForm" action="${path}/back/order/refunds.shtml" method="post">
			<input type="hidden" name="orderId" value="${order.id }"/>
			<table width="100%" class="table table-tr-large">
				<tr>
					<td width="15%" align="right">订单编号：</td>
					<td width="35%">${order.number }</td>
					<td width="15%" align="right">下单时间：</td>
					<td width="35%"><fmt:formatDate  value="${order.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
				<tr>
					<td width="15%" align="right"><font color="red">*</font>&nbsp;支付类型：</td>
					<td width="35%">
						<select name="type" class="select">
							<option value="">--请选择--</option>
							<c:if test="${refundsTypes != null}">
								<c:forEach var="row" items="${refundsTypes}">
									<option value="${row }"><fmt:message key="RefundsType.${row }"/></option>
								</c:forEach>
							</c:if>
						</select>
					</td>
					<td width="15%" align="right"><font color="red">*</font>&nbsp;支付方式：</td>
					<td width="35%">
						<select name="paymentwayId" class="select">
							<option value="">--请选择--</option>
							<c:if test="${paymentways != null}">
								<c:forEach var="row" items="${paymentways}">
									<option value="${row.id }">${row.name }</option>
								</c:forEach>
							</c:if>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">订单金额：</td>
					<td>${fn:currency(order.totalAmount,true)}</td>
					<td align="right">已付金额：</td>
					<td>${fn:currency(order.paidAmount,true) }</td>
				</tr>
				<tr>
					<td align="right"><font color="red">*</font>&nbsp;支付金额：</td>
					<td><input type="text" name="paidAmount" class="text"/></td>
					<td align="right">退款人：</td>
					<td><input type="text" name="payee" class="text"/></td>
				</tr>
				<tr>
					<td align="right">退款银行：</td>
					<td><input type="text" name="bank" class="text"/></td>
					<td align="right">退款帐号：</td>
					<td><input type="text" name="account" class="text"/></td>
				</tr>
				<tr>
					<td align="right">备注：</td>
					<td colspan="3"><input type="text" name="remark" class="text long"/></td>
				</tr>
				<tr class="footer">
					<td colspan="4" align="center">
						<input type="button" id="refundsSureButton" value="退款" class="button" />
	   					<input type="button" id="refundsCancelButton" value="取消" class="button" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		$().ready(function(){
			var $refundsOrder = $("#refundsOrder");
			var $refundsForm = $("#refundsForm");
			var $refundsType = $("#refundsType");
			var $refundsSureButton = $("#refundsSureButton");
			var $refundsCancelButton = $("#refundsCancelButton");
			$refundsForm.validate({
				rules: {
					paidAmount: {
						required: true,
						decimal: {
							length:18,
							scale:${fn:config("priceScale")}
						}
					},
					type:{required: true},
					paymentwayId:{required: true},
					account:{integer: true}
				}
			});
			$refundsSureButton.click(function(){
				$refundsForm.submit();
			});
			$refundsCancelButton.click(function(){
				$refundsOrder.window("close");
			});
		});
	</script>
	</c:if>
</body>
</html>