<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<title>登录</title>
		<%@ include file="/front/common/metainfo.jsp" %>
		<c:set var="captchaId" value="<%=UUID.randomUUID().toString() %>"/>
		<link rel="stylesheet" type="text/css" href="${path }/front/css/login.css"/>
		<script type="text/javascript">
			$().ready(function(){
				var $username = $("#username");
				var $password = $("#password");
				var $captcha = $("#captcha");
				var $form = $("#form");
				var $captchaImage = $("#captchaImage");
				var $loginButton = $("#loginButton").click(function(){
					var username = $username.val();
					var password = $password.val();
					var captcha = $captcha.val();
					if($.trim(username) == ""){
						alert("${fn:call('Common.login.userNameNotNull')}");
						return false;
					}
					if($.trim(password) == ""){
						alert("${fn:call('Common.login.passwordNotNull')}");
						return false;
					}
					if($.trim(captcha) == ""){
						alert("${fn:call('Common.login.captchaNotNull')}");
						return false;
					}
					$.ajax({
						url: "${path}/doLogin.shtml",
						type: "POST",
						dataType: "json",
						data:{username:username,password:password,captchaId:"${captchaId}",captcha:captcha},
						cache: false,
						beforeSend:function(){
							$loginButton.prop("disabled",true);
						},
						success: function(data) {
							$loginButton.prop("disabled",false);
							if(data.type == "success"){
								window.location.href="${path}/index.shtml";
							}else{
								alert(data.content);
								$captchaImage.attr("src", "${path }/back/common/captcha.shtml?captchaId=${captchaId}&timestamp=" + (new Date()).valueOf());
							}
						},
						error:function(event, request, settings){
							alert("${fn:call('Common.net.networkNotAllowed')}");
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
		<div class="container">
			<div class="logo"></div>
			<div class="main">
				<ul class="form">
					<li><input type="text" id="username" name="username"/></li>
					<li><input type="password" id="password" name="password"/></li>
					<li>
						<input type="text" id="captcha" class="captcha" name="captcha"/>
						<img id="captchaImage" class="captchaImage" title="刷新验证码" src="${path }/back/common/captcha.shtml?captchaId=${captchaId}"/>
					</li>
					<li class="pt10"><button class="submit" id="loginButton"></button></li>
				</ul>
			</div>
		</div>
	</body>
</html>
