package com.koch.service;

import java.awt.image.BufferedImage;

public interface CaptchaImageService{
	public BufferedImage build(String captchaId);
	public boolean isValid(String captchaId,String captcha);
}
