<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>修改文章</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<link rel="stylesheet" href="${path }/common/kindeditor/themes/default/default.css" />
	<script type="text/javascript" src="${path }/common/kindeditor/kindeditor-min.js" charset="utf-8"></script>
	<script type="text/javascript" src="${path }/common/kindeditor/lang/zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $editForm = $("#editForm");
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
			
			$editForm.validate({
				rules: {
					"articleTitle": {
						required: true,
						maxlength: 200
					},
					"articleCategoryId": {
						required: true
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
		<div id="mainPanel" class="easyui-panel" iconCls="icon-add" title="&nbsp;&nbsp;修  改  文  章">
			<form id="editForm" action="${path }/back/article/edit.shtml" method="post">
				<input type="hidden" name="id" value="${article.id }"/>
		    	<table class="table-edit" width="100%" align="center" cellpadding="0" cellspacing="0">
		      		<tbody>
		      			<tr>
		      				<td width="20%" align="right"><font color="red">*&nbsp;&nbsp;</font><fmt:message key='Common.label.title'/>：&nbsp;&nbsp;</td>
		      				<td width="80%" align="left"><input type="text" name="articleTitle" value="${article.articleTitle }" class="text"/></td>
		      			</tr>
		      			<tr>
		      				<td align="right"><font color="red">*&nbsp;&nbsp;</font><fmt:message key='Common.label.category'/>：&nbsp;&nbsp;</td>
		      				<td align="left">
								<select name="articleCategoryId" class="select">
									<c:if test="${categorys != null}">
										<c:forEach var="row" items="${categorys}">
										<option value="${row.id }" ${row == article.articleCategory ? 'selected="selected"':'' }>${row.name}</option>
										</c:forEach>
									</c:if>
								</select>
							</td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key='Common.label.author'/>：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<input type="text" name="author" value="${article.author }" class="text"/>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key='Common.label.setting'/>：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<input type="checkbox" name="isPublish" value="true" ${article.isPublish?'checked="checked"':'' }/>&nbsp;<fmt:message key='Common.label.isPublish'/>
		      					<input type="checkbox" name="isTop" value="true" ${article.isTop?'checked="checked"':'' }/>&nbsp;<fmt:message key='Common.label.isTop'/>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key='Common.label.tag'/>：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<c:if test="${tags != null}">
		      						<c:forEach var="row" items="${tags}">
		      						<label><input type="checkbox" name="tagIds" value="${row.id }" ${fn:containsObject(article.tags,row)?'checked="checked"':'' }/>&nbsp;${row.name }</label>
		      						</c:forEach>
		      					</c:if>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key='Common.label.tag'/>：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<textarea name="content" style="width:90%;height:400px;">${article.content }</textarea>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key='Common.label.seotitle'/>：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<input type="text" name="title" value="${article.title }" class="text long"/>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key='Common.label.seokeyword'/>：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<input type="text" name="keywords" value="${article.keywords }" class="text long"/>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td align="right"><fmt:message key='Common.label.seodiscription'/>：&nbsp;&nbsp;</td>
		      				<td align="left">
		      					<input type="text" name="describtion" value="${article.describtion }" class="text long"/>
		      				</td>
		      			</tr>
		      			<tr>
		      				<td height="60"></td>
		      				<td align="left">
		      					<a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'"><fmt:message key='Common.button.save'/></a>
		      					&nbsp;&nbsp;<a href="#" button="back" class="easyui-linkbutton" data-options="iconCls:'icon-back'"><fmt:message key='Common.button.return'/></a>
		      				</td>
		      			</tr>
		      		</tbody>
		    	</table>
		    </form>
		</div>
	</div>
</body>
<script type="text/javascript">
	KindEditor.ready(function(K) {
		var editor1 = K.create('textarea[name="content"]', {
			uploadJson : '${path }/back/file/upload.shtml',
			fileManagerJson : '${path }/back/file/browser.shtml',
			allowFileManager : true,
			afterCreate : function() {},
			afterBlur: function(){this.sync();}
		});
	});
</script>
</html>