package com.koch.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


public class CommonExceptionHandler implements HandlerExceptionResolver{
	private static Logger logger = Logger.getLogger(CommonExceptionHandler.class);
	
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception exception) {
		ModelAndView failModelAndView = new ModelAndView(BaseController.COMMON_FAIL_PAGE);
		if(exception instanceof MessageAlertable){
			failModelAndView.addObject(BaseController.COMMON_FAIL_ALERT_KEY, exception.getMessage());
		}else{
			failModelAndView.addObject(BaseController.COMMON_FAIL_ALERT_KEY, "");
		}
		if(exception instanceof Logable){
			logger.error(exception.getMessage(), exception);
		}
		return failModelAndView;
	}
	
}
