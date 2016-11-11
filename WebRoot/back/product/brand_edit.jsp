<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>修改品牌</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<link rel="stylesheet" href="${path }/common/kindeditor/themes/default/default.css" />
	<script type="text/javascript" src="${path }/common/kindeditor/kindeditor-min.js" charset="utf-8"></script>
	<script type="text/javascript" src="${path }/common/kindeditor/lang/zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $image = $("#image");
			var $brandType = $("#brandType");
			var $editForm = $("#editForm");
			var $saveButton = $("#saveButton");
			var $browserButton = $("#browserButton");
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
			$browserButton.browser({callback:function(data){
				$image.val(data.content);
			}});
			
			$brandType.change(function(){
				var $this = $(this);
				$image.prop("disabled",($this.val() != "image"));
				$browserButton.prop("disabled",($this.val() != "image"));
			});
			
			$editForm.validate({
				rules: {
					"name": {
						required: true,
						maxlength: 100
					},
					"orderList": { integer:true }
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-edit" title="&nbsp;&nbsp;修  改  品  牌">
			<form id="editForm" action="${path }/back/brand/edit.shtml" method="post">
				<input type="hidden" name="id" value="${brand.id }"/>
		    	<table class="table-edit" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			<tr>
		      				<td width="30%" align="right"><font color="red">*&nbsp;&nbsp;</font>品牌：&nbsp;&nbsp;</td>
		      				<td width="70%" align="left"><input type="text" name="name" value="${brand.name }" class="text"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">图片：&nbsp;&nbsp;</td>
		      				<td align="left">
								<input type="text" id="image" name="image" class="text" value="${brand.image }" maxlength="255" ${brand.brandType=="literal"?"disabled='disabled'":"" }/>&nbsp;
								<input type="button" id="browserButton" class="button" value="选择文件.." ${brand.brandType=="literal"?"disabled='disabled'":"" }/>
							</td>
		      			</tr>
		      			<tr>
		      				<td align="right">类型：&nbsp;&nbsp;</td>
		      				<td align="left">
								<select id="brandType" name="brandType" class="select">
									<option value="literal" ${brand.brandType=="literal"?"selected='selected'":"" }>文字</option>
									<option value="image" ${brand.brandType=="image"?"selected='selected'":"" }>图片</option>
								</select>
							</td>
		      			</tr>
		      			<tr>
		      				<td align="right">网址：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="url" value="${brand.url }" class="text long" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">排序：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" value="${brand.orderList }" field="number" class="text short" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">描述：&nbsp;&nbsp;</td>
		      				<td align="left"><textarea name="describtion" style="width:700px;height:400px;">${brand.describtion }</textarea></td>
		      			</tr>
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
	<script type="text/javascript">
		KindEditor.ready(function(K) {
			var editor1 = K.create('textarea[name="describtion"]', {
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