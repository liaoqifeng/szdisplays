package com.koch.base;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koch.bean.DateEditor;
import com.koch.bean.JsonMessage;
import com.koch.bean.Message;
import com.koch.bean.Pager;
import com.koch.bean.Parameter;
import com.koch.bean.Setting;
import com.koch.util.GlobalConstant;
import com.koch.util.SettingUtils;
import com.koch.util.SpringUtil;

/**
 * 控制器基类
 * @author koch
 * @date  2013-06-04
 */
public class BaseController {
	public static final String COMMON_FAIL_PAGE = "fail";
	public static final String COMMON_FAIL_ALERT_KEY = "fail_key";
	public static final String USER_SESSION_KEY = "_user_session_key_";
	public static final String HTML_PATH_BACK = "/back/";
	public static final String HTML_PATH_FRONT = "/front/";
	public static final String REDIRECT_MESSAGE_URL = "/back/common/message";
	
	protected static final JsonMessage save_success = JsonMessage.success("Common.save.success", new Object[0]);
	protected static final JsonMessage save_error = JsonMessage.error("Common.save.error", new Object[0]);
	protected static final JsonMessage update_success = JsonMessage.success("Common.update.success", new Object[0]);
	protected static final JsonMessage update_error = JsonMessage.error("Common.update.error", new Object[0]);
	protected static final JsonMessage delete_success = JsonMessage.success("Common.delete.success", new Object[0]);
	protected static final JsonMessage delete_error = JsonMessage.error("Common.delete.error", new Object[0]);
	
	@InitBinder
	protected void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		webDataBinder.registerCustomEditor(Date.class, new DateEditor(true));
	}
	
	protected String currency(BigDecimal paramBigDecimal, boolean isSign, boolean isUnit) {
		Setting setting = SettingUtils.get();
		String str = setting.setScale(paramBigDecimal).toString();
		if (isSign) {
			str = setting.getCurrencySign() + str;
		}
		if (isUnit) {
			str = str + setting.getCurrencyUnit();
		}
		return str;
	}
	
	// AJAX输出，返回null
	public String ajax(String content, String type, HttpServletResponse response) {
		try {
			response.setContentType(type + ";charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// AJAX输出文本，返回null
	public String ajaxText(String text,HttpServletResponse response) {
		return ajax(text, "text/plain", response);
	}

	// AJAX输出HTML，返回null
	public String ajaxHtml(String html,HttpServletResponse response) {
		return ajax(html, "text/html", response);
	}

	// AJAX输出XML，返回null
	public String ajaxXml(String xml,HttpServletResponse response) {
		return ajax(xml, "text/xml", response);
	}

	// 根据字符串输出JSON，返回null
	public String ajaxJson(String jsonString,HttpServletResponse response) {
		return ajax(jsonString, "text/html",response);
	}
	
	// 根据字符串输出JSON，返回null
	public String ajaxJsonString(String jsonString,HttpServletResponse response) {
		return ajax(jsonString, "text/json",response);
	}
	
	// 根据Map输出JSON，返回null
	public String ajaxJson(Map<String, String> jsonMap,HttpServletResponse response) {
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		return ajax(jsonObject.toString(), "text/html",response);
	}
	
	// 根据List输出JSON，返回null
	public String ajaxJson(List list,HttpServletResponse response) {
		JSONObject jsonObject = JSONObject.fromObject(list);
		return ajax(jsonObject.toString(), "text/html",response);
	}
	
	// 根据String []输出JSON，返回null
	public String ajaxJson(String [] list,HttpServletResponse response) {
		JSONObject jsonObject = JSONObject.fromObject(list);
		return ajax(jsonObject.toString(), "text/html",response);
	}
	
	public ModelAndView redirect(Message message){
		return new ModelAndView(REDIRECT_MESSAGE_URL,"message",message);
	}
	
	public String redirect(Message message,RedirectAttributes attrs){
		attrs.addFlashAttribute("message", message);
		return "redirect:/back/common/message.jsp";
	}
	
	protected void setModelMap(ModelMap modelMap,String code){
		setModelMap(modelMap, code, new Object[0]);
	}
	
	protected void setModelMap(ModelMap modelMap,String code,Object...args){
		modelMap.addAttribute("message", SpringUtil.getMessage(code, args));
	}
	
	protected void setRedirectAttributes(RedirectAttributes attrs,String code){
		if ((attrs != null) && (code != null))
			setRedirectAttributes(attrs, code, new Object[0]);
	}
	
	protected void setRedirectAttributes(RedirectAttributes attrs,String code,Object...args){
		if ((attrs != null) && (code != null))
			attrs.addFlashAttribute("message", SpringUtil.getMessage(code, args));
	}
	
	protected void setRedirectAttributes(RedirectAttributes attrs,JsonMessage message){
		if ((attrs != null) && (message != null))
			attrs.addFlashAttribute("message", message);
	}
}
