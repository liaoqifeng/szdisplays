package com.koch.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.koch.bean.Setting;

public final class CookieUtils {
	public static void addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer maxAge, String path, String domain, Boolean secure) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(name);
		try {
			name = URLEncoder.encode(name, "UTF-8");
			value = URLEncoder.encode(value, "UTF-8");
			Cookie cookie = new Cookie(name, value);
			if (maxAge != null) {
				cookie.setMaxAge(maxAge.intValue());
			}
			if (StringUtils.isNotEmpty(path)) {
				cookie.setPath(path);
			}
			if (StringUtils.isNotEmpty(domain)) {
				cookie.setDomain(domain);
			}
			if (secure != null) {
				cookie.setSecure(secure.booleanValue());
			}
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer maxAge) {
		Setting setting = SettingUtils.get();
		addCookie(request, response, name, value, maxAge, setting
				.getCookiePath(), setting.getCookieDomain(), null);
	}

	public static void addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value) {
		Setting localSetting = SettingUtils.get();
		addCookie(request, response, name, value, null, localSetting
				.getCookiePath(), localSetting.getCookieDomain(), null);
	}

	public static String getCookie(HttpServletRequest request, String name) {
		Assert.notNull(request);
		Assert.hasText(name);
		Cookie[] arrayOfCookie1 = request.getCookies();
		if (arrayOfCookie1 != null) {
			try {
				name = URLEncoder.encode(name, "UTF-8");
				for (Cookie localCookie : arrayOfCookie1) {
					if (name.equals(localCookie.getName())) {
						return URLDecoder.decode(localCookie.getValue(),
								"UTF-8");
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void removeCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String path,
			String domain) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(name);
		try {
			name = URLEncoder.encode(name, "UTF-8");
			Cookie localCookie = new Cookie(name, null);
			localCookie.setMaxAge(0);
			if (StringUtils.isNotEmpty(path)) {
				localCookie.setPath(path);
			}
			if (StringUtils.isNotEmpty(domain)) {
				localCookie.setDomain(domain);
			}
			response.addCookie(localCookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void removeCookie(HttpServletRequest request,
			HttpServletResponse response, String name) {
		Setting localSetting = SettingUtils.get();
		removeCookie(request, response, name, localSetting.getCookiePath(),
				localSetting.getCookieDomain());
	}
}
