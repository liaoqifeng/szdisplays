<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% request.setAttribute("path",request.getContextPath()); %>
<c:set var="captchaId" value="<%=UUID.randomUUID().toString() %>"/>
<!DOCTYPE html>
<html>
	<head>
		<META content="text/html; charset=utf-8" http-equiv="Content-Type">
		<title>三正管理登录</title>
		<link rel="stylesheet" type="text/css" href="${path }/back/css/easyui.css" />
		<link rel="stylesheet" type="text/css" href="${path }/back/css/login.css" />
		<script type="text/javascript" src="${path }/common/js/jquery-1.11.0.min.js"></script>
		<script type="text/javascript" src="${path }/back/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${path }/back/js/jquery.common.js"></script>
		<script type="text/javascript">
			$().ready(function(){
				var $username = $("#username");
				var $password = $("#password");
				var $captcha = $("#captcha");
				var $tip = $("#tip");
				var $word = $("#word");
				var $loadWord = $("#loadWord");
				var $loading = $("#loading");
				var $form = $("#form");
				var $captchaImage = $("#captchaImage");
				var $loginButton = $("#loginButton").click(function(){
					var username = $username.val();
					var password = $password.val();
					var captcha = $captcha.val();
					if($.trim(username) == ""){
						$tip.text("${fn:call('Common.login.userNameNotNull')}");
						return false;
					}
					if($.trim(password) == ""){
						$tip.text("${fn:call('Common.login.passwordNotNull')}");
						return false;
					}
					if($.trim(captcha) == ""){
						$tip.text("${fn:call('Common.login.captchaNotNull')}");
						return false;
					}
					$.ajax({
						url: "${path}/back/login.shtml",
						type: "POST",
						dataType: "json",
						data:{username:username,password:password,captchaId:"${captchaId}",captcha:captcha},
						cache: false,
						beforeSend:function(){
							$loginButton.addClass("loginLoading");
							$loadWord.show();
							$loading.show();
							$word.hide();
							$loginButton.prop("disabled",true);
						},
						success: function(data) {
							if(data.type == "success"){
								setTimeout(function(){
									$tip.text("${fn:call('Common.login.redirect')}");
									$loginButton.removeClass("loginLoading");
									$loadWord.hide();
									$loading.hide();
									$word.show();
									$loginButton.prop("disabled",false);
									setTimeout(function(){
										var redirectUrl = "${param.redirectUrl}";
										if(redirectUrl == ""){
											$form.submit();
										}else{
											window.location.href = redirectUrl;
										}
									}, 1000);
								}, 1000);
							}else{
								setTimeout(function(){
									$tip.text(data.content);
									$loginButton.removeClass("loginLoading");
									$loadWord.hide();
									$loading.hide();
									$word.show();
									$loginButton.prop("disabled",false);
									setTimeout(function(){
										$tip.empty();
									}, 1500);
								}, 1200);
							}
						},
						error:function(event, request, settings){
							$tip.text("${fn:call('Common.net.networkNotAllowed')}");
							setTimeout(function(){
								$tip.empty();
							}, 1800);
						}
					});
				});

				$captchaImage.click(function(){
					$captchaImage.attr("src", "${path }/back/common/captcha.shtml?captchaId=${captchaId}&timestamp=" + (new Date()).valueOf());
				});
			});
		</script>
	</head>
	<body>
		<div class="title">
			<div class="logo"></div>
		</div>
		<div class="main">
			<form id="form" action="${path}/back/common/main.shtml"></form>
			<table class="main-table">
				<tbody>
					<tr> 
						<th>用户名：</th>
						<td><input id="username" class="text" name="username" maxLength="20" value="admin" type="text"></td>
					</tr>
					<tr>
						<th>密&nbsp;&nbsp;&nbsp;码：</th>
						<td><input id="password" class="text" maxLength="20" value="000000" type="password" autocomplete="off"></td>
					</tr>
					<tr>
						<th>验证码：</th>
						<td>
							<input id="captcha" class="text captcha" name="captcha" maxLength="4" type="text" autocomplete="off">
							<img id="captchaImage" class="captchaImage" title="刷新验证码" src="${path }/back/common/captcha.shtml?captchaId=${captchaId}"/>
						</td>
					</tr>
					<tr>
						<th>&nbsp;</th>
						<td>
							<button id="loginButton" class="loginButton" type="submit">
								<div id="word" class="word">登录</div>
								<div id="loadWord" class="word hidden" style="display: none;">正在登录</div>
								<div id="loading" class="loading hidden" style="display: none;">&nbsp;</div>
							</button>
						</td>
					</tr>
				</tbody>
			</table>
			<div id="tip" class="tip"></div>
		</div>
		<div class="footer"></div>
	</body>
</html>
