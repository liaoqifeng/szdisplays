package com.koch.bean;

import com.octo.captcha.component.image.backgroundgenerator.FileReaderRandomBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import java.awt.Color;
import java.awt.Font;
import org.springframework.core.io.ClassPathResource;

public class CaptchaEngine extends ListImageCaptchaEngine {

	private static final String codes = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final String path = "/com/koch/captcha/";
	private static final Font[] fonts = { new Font("nyala", 1, 16),
			new Font("Arial", 1, 16), new Font("nyala", 1, 16),
			new Font("Bell", 1, 16), new Font("Bell MT", 1, 16),
			new Font("Credit", 1, 16), new Font("valley", 1, 16),
			new Font("Impact", 1, 16) };
	private static final Color[] colors = { new Color(255, 255, 255),
			new Color(255, 220, 220), new Color(220, 255, 255),
			new Color(220, 220, 255), new Color(255, 255, 220),
			new Color(220, 255, 220) };

	protected void buildInitialFactories() {
		RandomFontGenerator fontGenerator = new RandomFontGenerator(12, 16, fonts);
		FileReaderRandomBackgroundGenerator backgroundGenerator = new FileReaderRandomBackgroundGenerator(80, 28, new ClassPathResource(path).getPath());
		DecoratedRandomTextPaster textPaster = new DecoratedRandomTextPaster(4,4, new RandomListColorGenerator(colors), new TextDecorator[0]);
		addFactory(new GimpyFactory(new RandomWordGenerator(codes), new ComposedWordToImage( fontGenerator,backgroundGenerator,textPaster)));
	}
}