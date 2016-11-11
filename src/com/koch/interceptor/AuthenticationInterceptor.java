package com.koch.interceptor;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.koch.bean.Auth;
import com.koch.entity.Admin;
import com.koch.service.AdminService;
import com.koch.util.AuthUtils;
import com.koch.util.SpringUtil;

@Repository
public class AuthenticationInterceptor implements HandlerInterceptor {
	
	private static final String loginPage = "/back/login.jsp";
	private static final String messagePage = "/back/common/message.jsp";
	private static final String loginUrl = "/back/login.shtml";
	private static final String logoutUrl = "/back/logout.shtml";
	private static final String captchaUrl = "/back/common/captcha.shtml";
	private static final String mainUrl = "/back/common/main.shtml";
	
	@Resource
	private AdminService adminService;
	
	@Value("${url.charset}")
	private String charset;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Admin admin = this.adminService.getCurrent();
		String uri = request.getRequestURI();
		if(uri.indexOf(loginUrl) > 0 || uri.indexOf(logoutUrl) > 0 || uri.indexOf(captchaUrl) > 0){
			return true;
		}
		
		if(admin != null && admin.getAuths() != null && admin.getAuths().size() > 0){
			List<String> values = AuthUtils.getValues();
			String [] items = uri.split("/");
			String url = "/" + items[2] + "/" + items[3] + "/**";
			if(!values.contains(url)){
				return true;
			}
			return premisAudit(admin, request, response, handler);
		}
		
		String header = request.getHeader("X-Requested-With");
		if (header != null && (header.equalsIgnoreCase("XMLHttpRequest"))) {
			response.addHeader("loginStatus", "accessDenied");
			response.sendError(403);
			return false;
		}
		if (request.getMethod().equalsIgnoreCase("GET")) {
			String redirectUrl = request.getQueryString() != null ? request .getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
			if(redirectUrl.indexOf(mainUrl) >= 0){
				redirectUrl = "";
			}else{
				redirectUrl = "?" + "redirectUrl" + "=" + URLEncoder.encode(redirectUrl, this.charset);
			}
			response.sendRedirect(request.getContextPath() + this.loginPage + redirectUrl);
		} else {
			response.sendRedirect(request.getContextPath() + this.loginPage);
		}
	    return false;
	}
	
	private boolean premisAudit(Admin admin, HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
		List<Auth> auths = admin.getAuths();
		List<String> perms = new ArrayList<String>();
		for (Auth auth : auths) {
			List<Auth> nodes = auth.getNodes();
			for(Auth node : nodes){
				perms.add(node.getValue());
			}
		}
		String [] items = request.getRequestURI().split("/");
		String uri = "/" + items[2] + "/" + items[3] + "/**"; 
		if(perms.contains(uri)){
			return true;
		}
		
		String header = request.getHeader("X-Requested-With");
		if (header != null && (header.equalsIgnoreCase("XMLHttpRequest"))) {
			response.addHeader("auditStatus", "accessDenied");
			response.sendError(403);
			return false;
		}
		
		response.sendRedirect(request.getContextPath() + this.messagePage + "?" + "message=" + URLEncoder.encode(SpringUtil.getMessage("Common.perms.notAllowed"), this.charset));
		return false;
	}
	

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
	
	public static void main(String[] args) {
		//String uri = "/cms/back/order/list.shtml";
		//String [] items = uri.split("/");
	}
}
