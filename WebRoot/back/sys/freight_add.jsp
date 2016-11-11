<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>新增运费模板</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<style type="text/css">
		.list-area li{
			float: left;
			width:150px;
			margin-top:3px;
		}
		.list-area-c li{
			float: left;
			width:125px;
			margin-top:3px;
			margin-left:5px;
		}
		.list-city-d{
			width: 400px;
			background-color: #ececec;
			display: none;
			position: absolute;
			border:1px solid #e6e6e6;
		}
		.style-btn-o {
			  color: #444;
			  width:100px;
			  background-repeat: no-repeat;
			  background: #f5f5f5;
			  background-repeat: repeat-x;
			  border: 1px solid #bbb;
			  background: -webkit-linear-gradient(top,#ffffff 0,#e6e6e6 100%);
			  background: -moz-linear-gradient(top,#ffffff 0,#e6e6e6 100%);
			  background: -o-linear-gradient(top,#ffffff 0,#e6e6e6 100%);
			  background: linear-gradient(to bottom,#ffffff 0,#e6e6e6 100%);
			  background-repeat: repeat-x;
			  filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#ffffff,endColorstr=#e6e6e6,GradientType=0);
			  -moz-border-radius: 5px 5px 5px 5px;
			  -webkit-border-radius: 5px 5px 5px 5px;
			  border-radius: 5px 5px 5px 5px;
			  height:25px;
			  cursor: pointer;
		}
	</style>
</head>
<body>
	<div id="mainBox" class="main-box">
		<div id="mainPanel" class="easyui-panel" iconCls="icon-add" title="&nbsp;&nbsp;新  增  运  费  模  板">
			<form id="editForm" action="${path }/back/freightTemplet/view/add.shtml" method="post">
				<input type="hidden" name="items" value=''/>
				<input type="hidden" name="attr" value=''/>
		    	<table class="table-edit" align="center" cellpadding="0" cellspacing="0" width="100%">
		      		<tbody>
		      			<tr>
		      				<td width="30%" align="right"><font color="red">*&nbsp;&nbsp;</font>模板名称：&nbsp;&nbsp;</td>
		      				<td width="70%" align="left"><input type="text" name="name" class="text-three"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">备注：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="remark" class="text-three"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">排序：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" field="number" class="text-num"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><b>请选择并添加运费方式：</b>&nbsp;&nbsp;</td>
		      				<td align="left"></td>
		      			</tr>
		      			<c:if test="${valueMap.deliverways != null}">
		      				<c:forEach var="row" items="${valueMap.deliverways}">
		      					<tr name="deliverway" rowId="${row.id }">
				      				<td width="30%" align="right"><input type="checkbox" name="deliverway.id" value="${row.id }"/></td>
				      				<td width="70%" align="left">${row.name }</td>
				      			</tr>
				      			<tr name="content" rowId="${row.id }" valign="top" style="display: none;">
				      				<td colspan="2" align="right" valign="top">
				      					<table cellpadding="0" cellspacing="0" width="92%" style="border:1px solid #d4d4d4;text-align: left;">
				      						<tr>
				      							<td bgcolor="#ececec">
				      								&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" name="add" class="easyui-linkbutton" data-options="iconCls:'icon-add'">为指定地区添加运费</a>
				      							</td>
				      						</tr>
				      						<tr>
				      							<td>
				      								<table name="config" align="center" cellpadding="0" cellspacing="0" width="100%">
							      						<tr name="" style="display: none;">
							      							<td>
							      								送至<input type="text" name="area" area="" class="text-four" onkeydown="return false;" readonly="readonly"/>
							      								首重<input type="text" name="firstWeight" field="price" value="0" class="text-num"/>&nbsp;kg
							      								首重费用<input type="text" name="firstPrice" field="price" value="0" class="text-one"/>元
							      								续重<input type="text" name="conWeight" field="price" value="0" class="text-num"/>&nbsp;kg
							      								续重费用<input type="text" name="conPrice" field="price" value="0" class="text-num"/>元
							      								&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" name="delete">[删除]</a>
							      							</td>
							      						</tr>
							      					</table>
				      							</td>
				      						</tr>
				      					</table>
				      				</td>
				      			</tr>
		      				</c:forEach>
		      			</c:if>
		      			<tr>
		      				<td height="60"></td>
		      				<td align="left">
		      					<a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保      存</a>
		      					&nbsp;&nbsp;<a href="#" button="back" class="easyui-linkbutton" data-options="iconCls:'icon-back'">返      回</a>
		      				</td>
		      			</tr>
		      		</tbody>
		    	</table>
		    </form>
		</div>
	</div>
</body>
<div id="doAddArea" name="" class="easyui-dialog" data-options="title:'&nbsp;&nbsp;地区',closable:false,closed:true" style="width:750px;height:380px;padding:10px;position: relative;">
	<div>
		<ul class="list-area">
		<c:if test="${valueMap.areas != null}">
		<c:forEach var="row" items="${valueMap.areas}">
			<li name="province">
				<span><input type="checkbox" name="province" value="${row.id }" label="${row.name }"/>${row.name }</span>
				<div class="list-city-d">
					<ul class="list-area-c">
					<c:if test="${row.children != null}">
					<c:forEach var="item" items="${row.children}">
						<li name="city"><input type="checkbox" name="city" province="${row.id }" value="${item.id }" label="${item.name }"/>${item.name }</li>
					</c:forEach>
					</c:if>
					</ul>
				</div>
			</li>
		</c:forEach>
		</c:if>
		</ul>
		<div style="padding-top: 40px;clear:both;text-align: center;">
			<button id="sureButton" class="style-btn-o" >确     定</button>&nbsp;&nbsp;
		    <button id="cancelButton" class="style-btn-o">取     消</button>
		</div>
	</div>
	
</div>
</html>
<script type="text/javascript">
	$().ready(function(){
		var $mainBox = $("#mainBox");
		var $mainPanel = $("#mainPanel");
		var $editForm = $("#editForm");
		var $saveButton = $("#saveButton");
		var $deliverway = $("tr[name='deliverway']");
		var $content = $("tr[name='content']");
		var $doAddArea = $("#doAddArea");
		var $sureButton = $("#sureButton");
		var $cancelButton = $("#cancelButton");
		
		$mainPanel.panel({
			width:$mainBox.width(),
			height:$mainBox.height(),
			tools:[{
				iconCls:'icon-reload',
				handler:function(){window.location.reload();}
			}]
		});
		$(window).resize(function() { 
			 var width= $("html").width(); 
			 $mainPanel.panel('resize', { width : width }); 
		});
		$editForm.validate({
			rules: {
				"name": {
					required: true,
					maxlength: 100
				}
			},
			messages: {
				"name":{
					required: $.message("请输入名称！"),
					maxlength: $.message("名称不能超过100个字符！")
				}
			}
		});
		$deliverway.find(":checkbox").click(function(){
			$("tr[name='content']").hide();
			var rowId = $(this).val();
			$deliverway.find(":checkbox[value!='"+rowId+"']").prop("checked",false);
			if($(this).prop("checked"))
				$("tr[name='content'][rowId='"+rowId+"']").show();
			else
				$("tr[name='content'][rowId='"+rowId+"']").hide();
		});
		var initArea = function(area,rowId){
			$("table tr[name='content'][rowId='"+rowId+"']").find("table[name='config'] :text[name='area']").each(function(i){
				var $this = $(this);
				var otherArea = $this.attr("area");
				if(otherArea != null && otherArea != "" && $.toJSON(otherArea) != $.toJSON(area)){
					$($.evalJSON(otherArea)).each(function(i,rs){
						var provinceId = rs.provinceId;
						var citys = rs.cityItem;
						if(citys != null && citys != ""){
							var len = $doAddArea.find(":checkbox[name='city'][province='"+provinceId+"']").length;
							if($.evalJSON(citys).length == len){
								$doAddArea.find(":checkbox[name='province'][value='"+provinceId+"']").prop("disabled",true);
							}
							$($.evalJSON(citys)).each(function(j,city){
								$doAddArea.find(":checkbox[name='city'][value='"+city+"']").prop("disabled",true);
							});
						}else{
							$doAddArea.find(":checkbox[name='province'][value='"+provinceId+"']").prop("disabled",true);
							$doAddArea.find(":checkbox[name='city'][province='"+provinceId+"']").prop("disabled",true);
						}
					});
				}
			});
			$doAddArea.find("li[name='province']").each(function(){
				var $province = $(this).find(":checkbox[name='province']");
				var len = $(this).find(":checkbox[name='city']").length;
				if(len > 0){
					var disabledLen = $(this).find(":checkbox[name='city']:disabled").length;
					if(len == disabledLen){
						$province.prop("disabled",true);
					}
				}
			});
			if(area != null && area != ""){
				$($.evalJSON(area)).each(function(i,rs){
					var provinceId = rs.provinceId;
					var citys = rs.cityItem;
					$doAddArea.find(":checkbox[name='province'][value='"+provinceId+"']").prop("checked",true);
					if(citys != null && citys != ""){
						$($.evalJSON(citys)).each(function(j,city){
							$doAddArea.find(":checkbox[name='city'][value='"+city+"']").prop("checked",true);
						});
					}
				});
			}
		}
		$content.each(function(i){
			var $this = $(this);
			var rowId = $this.attr("rowId");
			$this.find("a[name='add']").click(function(){
				var clone = $this.find("table[name='config'] tr:hidden").clone().show();
				var random = $.encryption();
				clone.attr("name",random);
				clone.find("a[name='delete']").click(function(){
					clone.remove();
				});
				clone.find(":text[name='area']").click(function(){
					$doAddArea.attr("name",random);
					var area = $(this).attr("area");
					initArea(area,rowId);
					$doAddArea.dialog("open");
				});
				clone.find(":text[field='price']").onlypressPrice();
				clone.find(":text[field='number']").onlypressnum();
				$this.find("table[name='config']").append(clone);
			});
		});
		var clearArea = function(){
			$doAddArea.find(":checkbox:checked").prop("checked",false);
			$doAddArea.find(":checkbox").prop("disabled",false);
			$doAddArea.dialog("close");
		}
		$doAddArea.find("li[name='province'] span").mousemove(function(){
			var $this = $(this);
			var len = $this.parent().find(":checkbox[name='city']").length;
			if(len == 0)
				return false;
			var left = $this.position().left;
			var height = $this.height();
			var $city = $this.parent().find("div");
			var top = $this.position().top+height;
			if(left+$city.width()>$doAddArea.width()){
				left = left+$this.width()-$city.width();
			}
			$city.css({"left":left,"top":top}).show();
			$city.bind("mousemove",function(){
				$city.css({"left":left,"top":top}).show();
			}).bind("mouseout",function(){
				$this.parent().find("div").hide();
			});
		}).mouseout(function(){
			var $this = $(this);
			$this.parent().find("div").hide();
		});
		
		$doAddArea.find(":checkbox[name='city']").click(function(){
			var $this = $(this);
			var provinceId = $this.attr("province");
			var isChecked = $this.prop("checked");
			var province = $doAddArea.find(":checkbox[name='province'][value='"+provinceId+"']");
			if(!isChecked){
				var len = $this.parent().parent().find(":checkbox[name='city']:checked").length;
				if(len <= 0)
					province.prop("checked",false);
			}else{
				if(!province.prop("checked"))
					province.prop("checked",true);
			}
		});
		$sureButton.click(function(){
			var random = $doAddArea.attr("name");
			var label = "";
			var value = "[";
			var index = 0;
			$doAddArea.find("li[name='province']").each(function(i){
				var $this = $(this);
				var $province = $this.find(":checkbox[name='province']");
				var $city = $this.find(":checkbox[name='city']:checked");
				var isExitCity = ($city.length>0);
				var citys = new Array();
				if($province.prop("checked") || isExitCity){
					var p = $province.val();
					var c = "";
					var l = "";
					if(isExitCity){
						$city.each(function(j){
							citys.push(parseInt($(this).val()));
								l += ","+$(this).attr("label");
						});
					}
					if(citys != null && citys.length>0){
						c = $.toJSON(citys);
					}
					if(index == 0){
						value += "{\"provinceId\":"+p+",\"cityItem\":\""+c+"\"}";
						label += $province.attr("label")+l;
					}else{
						value += ",{\"provinceId\":"+p+",\"cityItem\":\""+c+"\"}";
						label += ","+$province.attr("label")+l;
					}
					index++;
				}
			});
			value += "]";
			$content.find("table[name='config'] tr[name='"+random+"'] :text[name='area']").val(label);
			$content.find("table[name='config'] tr[name='"+random+"'] :text[name='area']").attr("area",value);
			clearArea();
		});
		$cancelButton.click(function(){
			clearArea();
		});
		
		$saveButton.click(function(){
			if($deliverway.find(":checkbox:checked").length<=0){
				$.messager.alert('提示','请选择配送方式!');
				return false;
			}
			var result = "[";
			var deliverway = $deliverway.find(":checkbox:checked");
			var index = 0;
			var attr = "[";
			$("tr[name='content'][rowId='"+deliverway.val()+"'").find("table[name='config'] tr:visible").each(function(i){
				var firstWeight = $(this).find(":text[name='firstWeight']").val();
				var conWeight = $(this).find(":text[name='conWeight']").val();
				var firstPrice = $(this).find(":text[name='firstPrice']").val();
				var conPrice = $(this).find(":text[name='conPrice']").val();
				var value = $(this).find(":text[name='area']").val();
				var $area = $(this).find(":text[name='area']");
				var area = $area.attr("area");
				if(i == 0){
					attr += "{\"items\":"+$.toJSON(area)+",\"value\":\""+value+"\",\"firstWeight\":"+firstWeight+",\"conWeight\":"+conWeight+",\"firstPrice\":"+firstPrice+",\"conPrice\":"+conPrice+"}";
				}else{
					attr += ",{\"items\":"+$.toJSON(area)+",\"value\":\""+value+"\",\"firstWeight\":"+firstWeight+",\"conWeight\":"+conWeight+",\"firstPrice\":"+firstPrice+",\"conPrice\":"+conPrice+"}";
				}
				var item = "";
				if(area != null && area != ""){
					$($.evalJSON(area)).each(function(j,rs){
						var provinceId = rs.provinceId;
						var cityItem = rs.cityItem;
						if(index == 0){
							item += "{\"provinceId\":"+provinceId+",\"cityItem\":"+$.toJSON(cityItem)+",\"firstWeight\":"+firstWeight+",\"conWeight\":"+conWeight+",\"firstPrice\":"+firstPrice+",\"conPrice\":"+conPrice+"}";
						}else{
							item += ",{\"provinceId\":"+provinceId+",\"cityItem\":"+$.toJSON(cityItem)+",\"firstWeight\":"+firstWeight+",\"conWeight\":"+conWeight+",\"firstPrice\":"+firstPrice+",\"conPrice\":"+conPrice+"}";
						}
						index++;
					});
				}
				result += item;
			});
			result += "]";
			attr += "]";
			if(result == "[]" || attr == "[]"){
				$.messager.alert('提示','请添加运费方式!');
				return false;
			}
			$(":hidden[name='items']").val(result);
			$(":hidden[name='attr']").val(attr);
			$editForm.submit();
		});
	});
</script>
