<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>修改商品分类</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $editForm = $("#editForm");
			var $brands = $("#brands");
			var $brandIds = $(":checkbox[name='brandIds']");
			var $saveButton = $("#saveButton");
			var brands = "${type.brands}";
			
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
			var initBrand = function(){
				if(brands != ""){
					$($.evalJSON(brands)).each(function(i,r){
						$(":checkbox[name='brandIds'][value='"+r+"']").prop("checked",true);
					});
				}
			}
			initBrand();
			$editForm.validate({
				rules: {
					"name": {
						required: true,
						maxlength: 100
					},
					"orderList": { integer:true },
					"title": { maxlength: 255 },
					"keywords": { maxlength: 255 },
					"describtion": { maxlength: 255 }
				}
			});
			$saveButton.click(function(){
				var ids = new Array();
				var $rows = $brandIds.filter(":checked");
				if($rows != null && $rows.size() > 0){
					$rows.each(function(){
						ids.push(parseInt($(this).val()));
					});
					$brands.val($.toJSON(ids));
				}
				$editForm.submit();
			});
		});
	</script>
</head>
<body>
	<div id="mainBox" class="main-box">
		<div id="mainPanel" class="easyui-panel" iconCls="icon-edit" title="&nbsp;&nbsp;修  改  商  品  分  类">
			<form id="editForm" action="${path }/back/productCategory/edit.shtml" method="post">
				<input type="hidden" name="id" value="${type.id }"/>
				<input type="hidden" id="brands" name="brands" value='' />
		    	<table class="table-edit sort" align="center">
		      		<tbody>
		      			<tr>
		      				<td width="20%" align="right"><font color="red">*&nbsp;&nbsp;</font>分类名称：&nbsp;&nbsp;</td>
		      				<td width="80%" align="left"><input type="text" name="name" value="${type.name }" class="text"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">上级分类：&nbsp;&nbsp;</td>
		      				<td align="left">
								<select id="productCategory" name="parentId" class="select">
									<option value="">--请选择--</option>
									<c:if test="${productCategorys != null}">
										<c:forEach var="row" items="${productCategorys}">
											<option value="${row.id }" ${row == type.parent?"selected='selected'":"" }>
												<c:forEach var="i" begin="0" end="${row.level}" step="1">
												&nbsp;&nbsp;
												</c:forEach>
												${row.name }
											</option>
										</c:forEach>
									</c:if>
								</select>
								&nbsp;&nbsp;
								(<span>不选择默认为顶级分类</span>)
							</td>
		      			</tr>
		      			<tr>
		      				<td align="right">关联品牌：&nbsp;&nbsp;</td>
		      				<td align="left">
								<c:if test="${brands != null}">
									<c:forEach var="row" items="${brands}" varStatus="index">
									<label><input type="checkbox" name="brandIds" value="${row.id }"/>${row.name }</label>
									<c:if test="${index.count%8==0 }"><br/></c:if>
									</c:forEach>
								</c:if>
							</td>
		      			</tr>
		      			<tr>
		      				<td align="right">排序：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="orderList" value="${type.orderList }" class="text short" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">页面标题：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="title" value="${type.title }" class="text long" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">页面关键字：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="keywords" value="${type.keywords }" class="text long" value=""/></td>
		      			</tr>
		      			<tr>
		      				<td align="right">页面描述：&nbsp;&nbsp;</td>
		      				<td align="left"><input type="text" name="describtion" value="${type.describtion }" class="text long" value=""/></td>
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
</body>
</html>