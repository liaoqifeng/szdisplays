<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>修改商品规格</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $specTable = $("#specTable");
			var $specType = $("#specType");
			var $addSpec = $("#addSpec");
			var $selectImage = $(":button.select");
			var $editForm = $("#editForm");
			var $saveButton = $("#saveButton");

			var productSpecIndex = ${spec.specAttributes != null?fn:length(spec.specAttributes):0};
			
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
			
			$specType.change(function(){
				var $this = $(this);
				$specTable.find(":text.specUrl").prop("disabled",($this.val()!="image"));
				$specTable.find(":button.select").prop("disabled",($this.val()!="image"));
			});
			
			$addSpec.click(function(){
				var orderList = $specTable.find("tr").length;
				var disabled = ($specType.val()!="image")?'disabled="disabled"':'';
				var trHtml = ''+
					'<tr height="30">'+
						'<td>'+
							'<input type="text" name="specAttributes['+productSpecIndex+'].name" class="text middle specName" maxlength="255"/>'+
						'</td>'+
						'<td>'+
							'<input type="text" name="specAttributes['+productSpecIndex+'].url" '+disabled+' class="text specUrl" maxlength="255"/>&nbsp;'+
							'<input type="button" name="selectImage" '+disabled+' class="button select" value="选择文件.." />'	+
						'</td>'+
						'<td><input type="text" name="specAttributes['+productSpecIndex+'].orderList" value="'+orderList+'" class="text short orderList" maxlength="10"/></td>'+
						'<td><a href="javascript:;" class="delete">[删除]</a></td>'+
					'</tr>';
				var $trHtml = $(trHtml);
				$specTable.append($trHtml);
				productSpecIndex++;
				var $select = $trHtml.find(".select");
				$select.browser({callback:function(data){
					$select.prev().val(data.content);
				}});
			});

			$selectImage.each(function(){
				var $this = $(this);
				$this.browser({callback:function(data){
					$this.prev().val(data.content);
				}});
			});
			
			$specTable.on("click",".delete",function(){
				$(this).closest("tr").remove();
			});
			$.validator.addClassRules({
				specName: {required: true},
				orderList:{integer:true}
			});
			$editForm.validate({
				rules: {
					"name": {
						required: true,
						maxlength: 100
					},
					"orderList": { integer:true },
					"remark": { maxlength: 255 }
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-edit" title="&nbsp;&nbsp;修  改  规  格">
			<form id="editForm" action="${path }/back/spec/edit.shtml" method="post" enctype="multipart/form-data">
		    	<input type="hidden" name="id" value='${spec.id }'/>
		    	<input type="hidden" name="value" value=''/>
		    	<table class="table-edit" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			<tr>
		      				<td width="30%" align="right"><font color="red">*&nbsp;&nbsp;</font>规格：&nbsp;&nbsp;</td>
		      				<td width="70%" align="left">
		      					<input type="text" name="name" value="${spec.name }" class="text"/>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right">类型：&nbsp;&nbsp;</td>
		      				<td align="left">
								<select id="specType" name="specType" class="select">
									<option value="literal" ${spec.specType=="literal"?"selected='selected'":"" }>文字</option>
									<option value="image" ${spec.specType=="image"?"selected='selected'":"" }>图片</option>
								</select>
							</td>
		      			</tr>
		      			<tr>
		      				<td align="right">备注：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="remark" value="${spec.remark }" class="text long" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">排序：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" value="${spec.orderList }" class="text short" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="right" height="40"></td>
		      				<td align="left"><a href="#" id="addSpec" class="easyui-linkbutton" data-options="iconCls:'icon-add'">增加规格值</a></td>
		      			</tr>
		      		</tbody>
		    	</table>
		    	<table id="specTable" width="800" align="center">
   					<tr name="head" bgcolor="#f2f2f2">
   						<td width="25%" height="30">规格名称</td>
   						<td width="45%">规格值图片</td>
   						<td width="20%">排序</td>
   						<td width="10%">操作</td>
   					</tr>
   					<c:if test="${spec.specAttributes != null}">
   						<c:forEach var="row" items="${spec.specAttributes}" varStatus="index">
   							<tr height="30">
								<td>
									<input type="hidden" name="specAttributes[${index.index }].id" value="${row.id }"/>
									<input type="text" name="specAttributes[${index.index }].name" value="${row.name }" class="text middle specName" maxlength="255"/>
								</td>
								<td>
									<input type="text" name="specAttributes[${index.index }].url" value="${row.url }" ${spec.specType=="literal"?"disabled='disabled'":"" } class="text specUrl" maxlength="255"/>
									<input type="button" class="button select" value="选择文件.."  ${spec.specType=="literal"?"disabled='disabled'":"" }/>
								</td>
								<td><input type="text" name="specAttributes[${index.index }].orderList" value="${row.orderList }" class="text short orderList" maxlength="10"/></td>
								<td><a href="javascript:;" class="delete">[删除]</a></td>
							</tr>
   						</c:forEach>
   					</c:if>
   				</table>
   				<table align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		   				<tr>
		      				<td height="60" width="30%"></td>
		      				<td align="left" width="70%">
		      					<a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保      存</a>
		      					<a href="#" button="back" class="easyui-linkbutton" data-options="iconCls:'icon-back'">返      回</a>
		      				</td>
		      			</tr>
      				</tbody>
      			</table>
		    </form>
		</div>
	</div>
	<div id="selectFile" class="easyui-window" title="&nbsp;&nbsp;选择文件" data-options="iconCls:'icon-save',collapsible:false,minimizable:false,maximizable:false,closed:true" style="width:300px;height:250px;padding:10px;">
		<div id="colorImg">
			<ul class="list-img">
				<li><a><img src="${path }/common/images/color/1.gif" title="无" values="无" lsrc="/common/images/color/1.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/2.gif" title="黄色" values="黄色" lsrc="/common/images/color/2.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/3.gif" title="酒红色" values="酒红色" lsrc="/common/images/color/3.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/4.gif" title="金色" values="金色" lsrc="/common/images/color/4.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/5.gif" title="橙色" values="橙色" lsrc="/common/images/color/5.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/6.gif" title="灰色" values="灰色" lsrc="/common/images/color/6.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/7.gif" title="蓝色" values="蓝色" lsrc="/common/images/color/7.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/8.gif" title="黑色" values="黑色" lsrc="/common/images/color/8.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/9.gif" title="卡其色" values="卡其色" lsrc="/common/images/color/9.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/10.gif" title="混色" values="混色" lsrc="/common/images/color/10.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/11.gif" title="紫色" values="紫色" lsrc="/common/images/color/11.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/12.gif" title="粉红色" values="粉红色" lsrc="/common/images/color/12.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/13.gif" title="银色" values="银色" lsrc="/common/images/color/13.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/14.gif" title="红色" values="红色" lsrc="/common/images/color/14.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/15.gif" title="白色" values="白色" lsrc="/common/images/color/15.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/16.gif" title="浅蓝色" values="浅蓝色" lsrc="/common/images/color/16.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/17.gif" title="深蓝色" values="深蓝色" lsrc="/common/images/color/17.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/18.gif" title="绿色" values="绿色" lsrc="/common/images/color/18.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/19.gif" title="米黄色" values="米黄色" lsrc="/common/images/color/19.gif"/></a></li>
				<li><a><img src="${path }/common/images/color/20.gif" title="咖啡色" values="咖啡色" lsrc="/common/images/color/20.gif"/></a></li>
			</ul>
		</div>
		<div style="padding-top: 15px;clear: both;text-align: center;">
			<a href="#" id="sureButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确     定</a>
		    <a href="#" id="cancelButton" class="easyui-linkbutton" data-options="iconCls:'icon-back'">取     消</a>
		</div>
	</div>
</body>
</html>