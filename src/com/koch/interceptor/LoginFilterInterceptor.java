package com.koch.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.koch.entity.Member;
import com.koch.service.MemberService;

@Repository
public class LoginFilterInterceptor implements HandlerInterceptor {
	
	private static final String loginUrl = "/login.shtml";
	private static final String doLoginUrl = "/doLogin.shtml";
	
	@Resource
	private MemberService memberService;
	
	@Value("${url.charset}")
	private String charset;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Member member = this.memberService.getCurrent();
		String uri = request.getRequestURI();
		if(uri.indexOf("/back/") > 0 || uri.indexOf(loginUrl) > 0 || uri.indexOf(doLoginUrl) > 0){
			return true;
		}
		
		if(member != null){
			return true;
		}
		response.sendRedirect(request.getContextPath() + this.loginUrl);
		
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
		
	}
}
