<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>新增优惠券</title>
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
			var $isExchange = $("#isExchange");
			var $score = $("#score");
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
			$isExchange.click(function(){
				$score.prop("disabled",!$(this).prop("checked"));
			});
			$("#browserButton").browser({callback:function(data){
				$("#image").val(data.content);
			}})
			$.validator.addMethod("compare", 
				function(value, element, param) {
					var parameterValue = $(param).val();
					if ($.trim(parameterValue) == "" || $.trim(value) == "") {
						return true;
					}
					try {
						return parseFloat(parameterValue) <= parseFloat(value);
					} catch(e) {
						return false;
					}
				},
				"不允许小于最小值"
			);
			$editForm.validate({
				rules: {
					name: "required",
					prefix: "required",
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
					expression: { required: true },
					score:{
						required: true,
						digits: true
					}
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-add" title="&nbsp;&nbsp;新  增  优  惠  券">
			<form id="editForm" action="${path }/back/coupon/add.shtml" method="post">
		    	<ul id="tab" class="tab">
					<li><input value="基本信息" type="button" /></li>
					<li><input value="简介" type="button" /></li>
				</ul>
				<table width="100%" class="input tabContent table">
					<tr>
		    			<td width="30%" height="30" align="right"><font color="red">*</font>&nbsp;名称：&nbsp;&nbsp;</td>
		    			<td width="70%" align="left"><input type="text" name="name" class="text"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right"><font color="red">*</font>&nbsp;前缀：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="prefix" class="text"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;图标：&nbsp;&nbsp;</td>
		    			<td align="left">
		    				<input type="text" id="image" name="image" class="text"/>
		    				<input type="button" id="browserButton" class="button" value="选择文件.." />
		    			</td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;开始时间：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" id="startDate" name="startDate" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;结束时间：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" id="endDate" name="endDate" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'startDate\')}'});"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;最小商品价格：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" id="minPrice" name="minPrice" class="text" maxlength="20"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;最大商品价格：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="maxPrice" class="text" maxlength="20"/></td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right"><font color="red">*</font>&nbsp;价格运算表达式：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" name="expression" class="text"/>&nbsp;表达式可用参数:(数量、价格) [+ - * /]&nbsp;&nbsp;&nbsp;示例:数量*价格*0.8</td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right">&nbsp;设置：&nbsp;&nbsp;</td>
		    			<td align="left">
		    				<input type="checkbox" name="isEnabled" value="true"/>&nbsp;是否启用
		    				<input type="checkbox" id="isExchange" name="isExchange" value="true"/>&nbsp;是否允许积分兑换
		    			</td>
		    		</tr>
		    		<tr>
		    			<td height="30" align="right"><font color="red">*</font>&nbsp;积分数：&nbsp;&nbsp;</td>
		    			<td align="left"><input type="text" id="score" name="score" disabled="disabled" class="text" maxlength="20"/></td>
		    		</tr>
				</table>
	   			<table width="100%" class="input tabContent table">
	   				<tr>
		    			<td><textarea name="introduction" style="width:1000px;height:350px;"></textarea></td>
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