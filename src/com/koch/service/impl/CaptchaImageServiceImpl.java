package com.koch.service.impl;

import java.awt.image.BufferedImage;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.koch.service.CaptchaImageService;
import com.octo.captcha.service.CaptchaService;

@Service
public class CaptchaImageServiceImpl implements CaptchaImageService {
	@Resource
	private CaptchaService captchaService;

	public BufferedImage build(String captchaId) {
		return (BufferedImage) this.captchaService.getChallengeForID(captchaId);
	}

	public boolean isValid(String captchaId, String captcha) {
		if (StringUtils.isNotEmpty(captchaId) && StringUtils.isNotEmpty(captcha))
			try {
				return this.captchaService.validateResponseForID(
						captchaId, captcha.toUpperCase()).booleanValue();
			} catch (Exception e) {
				return false;
			}
		return true;
	}
	
	
}
