package com.koch.controller.back;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.koch.base.BaseController;
import com.koch.bean.Auth;
import com.koch.bean.JsonMessage;
import com.koch.entity.Admin;
import com.koch.entity.Role;
import com.koch.service.AdminService;
import com.koch.service.CaptchaImageService;
import com.koch.util.AuthUtils;
import com.koch.util.CloneUtil;
import com.koch.util.GlobalConstant;
import com.koch.util.JavaMD5;
/**
 * 登录控制器
 * @author koch
 * @date  2014-05-17
 */
@Controller
@RequestMapping(value="back")
public class BackLoginController extends BaseController{
	
	@Resource
	private AdminService adminService;
	@Resource
	private CaptchaImageService captchaImageService;

    @RequestMapping(value="login",method=RequestMethod.POST)
    @ResponseBody
	public JsonMessage login(HttpServletRequest request, Admin admin,String captchaId,String captcha){
    	if(StringUtils.isEmpty(captchaId)){
    		return JsonMessage.success("Common.login.captchaNotNull");
    	}
    	if(!captchaImageService.isValid(captchaId, captcha)){
    		return JsonMessage.error("Common.login.captchaError");
    	}
		Admin ad = adminService.get("username",admin.getUsername());
		if(ad != null){
			if(ad.getPassword().equals(JavaMD5.getMD5ofStr(admin.getPassword()))){
				
				Set<Role> roles = ad.getRoles();
				if(roles != null && roles.size() > 0){
					List<Auth> list = (List<Auth>)CloneUtil.cloneSerializable((Serializable) AuthUtils.get());
					Iterator<Auth> auths = list.iterator();
					while(auths.hasNext()){
						Auth auth = auths.next();
						Iterator<Auth> items = auth.getNodes().iterator();
						Boolean isExit = false;
						while(items.hasNext()){
							Auth item = items.next();
							Iterator<Role> iterator = roles.iterator();
							while(iterator.hasNext()){
								Role role = iterator.next();
								if(!role.hasPerms(item)){
									items.remove();
								}else{
									isExit = true;
								}
							}
						}
						if(!isExit){
							auths.remove();
						}
					}
					ad.getAuths().clear();
					ad.getAuths().addAll(list);
				}
				
				request.getSession().invalidate();
				request.getSession().setAttribute(GlobalConstant.BACK_SESSION_USER,ad);
				
				return JsonMessage.success("Common.login.success");
			}else{
				return JsonMessage.error("Common.login.userNameOrPasswordError");
			}
		}else{
			return JsonMessage.error("Common.login.error");
		}
    }
	
	@RequestMapping("logout")
	public String logout(HttpSession session){
		session.invalidate();
		return "redirect:/back/login.jsp";
	}
}
