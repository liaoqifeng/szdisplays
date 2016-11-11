<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="com.koch.util.SpringUtil"%>
<%@page import="com.koch.service.CaptchaImageService"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	String captchaId = request.getParameter("captchaId");
	if (StringUtils.isEmpty(captchaId))
		captchaId = request.getSession().getId();
	String str1 = new StringBuffer().append("yB").append("-").append("der").append("ewoP").reverse().toString();
	String str2 = new StringBuffer().append("ten").append(".").append("xxp").append("ohs").reverse().toString();
	response.addHeader(str1, str2);
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0L);
	response.setContentType("image/jpeg");
	ServletOutputStream servletOutputStream = null;
	CaptchaImageService captchaImageService = (CaptchaImageService)SpringUtil.getBean("captchaImageServiceImpl");
	try {
		servletOutputStream = response.getOutputStream();
		BufferedImage localBufferedImage = captchaImageService.build(captchaId);
		ImageIO.write(localBufferedImage, "jpg", servletOutputStream);
		servletOutputStream.flush();
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		IOUtils.closeQuietly(servletOutputStream);
		out.clear();
		out = pageContext.pushBody();
	}
%>