package com.koch.controller.front;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koch.base.BaseController;
import com.koch.bean.JsonMessage;
import com.koch.entity.Admin;
import com.koch.entity.Member;
import com.koch.service.CaptchaImageService;
import com.koch.service.MemberService;
import com.koch.util.GlobalConstant;
import com.koch.util.JavaMD5;

@Controller("frontLoginController")
@RequestMapping
public class LoginController extends BaseController{
	@Resource
	private MemberService memberService;
	@Resource
	private CaptchaImageService captchaImageService;
	
	@RequestMapping(value="login")
	public String login(ModelMap modelMap){
		
		return "/front/login";
    }
	
	@RequestMapping(value="doLogin")
	@ResponseBody
	public JsonMessage doLogin(HttpServletRequest request,Member member,String captchaId,String captcha){
		if(StringUtils.isEmpty(captchaId)){
    		return JsonMessage.success("Common.login.captchaNotNull");
    	}
    	if(!captchaImageService.isValid(captchaId, captcha)){
    		return JsonMessage.error("Common.login.captchaError");
    	}
    	Member m = memberService.get("username",member.getUsername());
    	if(m == null){
    		return JsonMessage.error("Common.login.userNameOrPasswordError");
    	}
    	if(!m.getPassword().equals(JavaMD5.getMD5ofStr(member.getPassword()))){
    		return JsonMessage.error("Common.login.userNameOrPasswordError");
    	}
    	request.getSession().invalidate();
		request.getSession().setAttribute(GlobalConstant.MEMBER_SESSION_USER,m);
		return JsonMessage.success("Common.login.success");
    }
	
}
