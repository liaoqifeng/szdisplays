<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
String htmlData = request.getParameter("content1") != null ? request.getParameter("content1") : "";
%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8" />
	<title>KindEditor JSP</title>
	<link rel="stylesheet" href="${path }/common/kindeditor/themes/default/default.css" />
	<link rel="stylesheet" href="${path }/common/kindeditor/plugins/code/prettify.css" />
	<script charset="utf-8" src="${path }/common/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="${path }/common/kindeditor/lang/zh_CN.js"></script>
	<script charset="utf-8" src="${path }/common/kindeditor/plugins/code/prettify.js"></script>
	<script>
		KindEditor.ready(function(K) {
			var editor1 = K.create('textarea[name="introduction"]', {
				cssPath : '${path }/common/kindeditor/plugins/code/prettify.css',
				uploadJson : '${path }/common/kindeditor/jsp/upload_json.jsp',
				fileManagerJson : '${path }/common/kindeditor/jsp/file_manager_json.jsp',
				allowFileManager : true,
				afterCreate : function() {}
			});
			prettyPrint();
		});
	</script>
</head>
<body>
	<%=htmlData%>
	<form name="example" method="post" action="demo.jsp">
		<textarea name="content1" cols="100" rows="8" style="width:700px;height:200px;visibility:hidden;"><%=htmlspecialchars(htmlData)%></textarea>
		<br />
		<input type="submit" name="button" value="提交内容" /> (提交快捷键: Ctrl + Enter)
	</form>
</body>
</html>
<%!
private String htmlspecialchars(String str) {
	str = str.replaceAll("&", "&amp;");
	str = str.replaceAll("<", "&lt;");
	str = str.replaceAll(">", "&gt;");
	str = str.replaceAll("\"", "&quot;");
	return str;
}
%>