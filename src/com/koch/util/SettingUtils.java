package com.koch.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.io.IOUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.ClassPathResource;

import com.koch.bean.EnumConverter;
import com.koch.bean.Setting;

public final class SettingUtils {
	
	private static final CacheManager cacheManager = CacheManager.create();
	private static final BeanUtilsBean beanUtilsBean;

	static {
		SettingConvert convert = new SettingConvert();
		DateConverter localDateConverter = new DateConverter();
		localDateConverter.setPatterns(GlobalConstant.DATE_PATTERNS);
		convert.register(localDateConverter, Date.class);
		beanUtilsBean = new BeanUtilsBean(convert);
	}

	public static Setting get() {
		Ehcache ehcache = cacheManager.getEhcache("setting");
		net.sf.ehcache.Element localElement = ehcache.get(Setting.CACHE_KEY);
		Setting setting;
		if (localElement != null) {
			setting = (Setting) localElement.getObjectValue();
		} else {
			setting = new Setting();
			try {
				File localFile = new ClassPathResource(GlobalConstant.SHOP_XML_PATH).getFile();
				Document localDocument = new SAXReader().read(localFile);
				List list = localDocument.selectNodes("/shop/setting");
				Iterator iterator = list.iterator();
				while (iterator.hasNext()) {
					Element element = (org.dom4j.Element) iterator.next();
					String name = element.attributeValue("name");
					String value = element.attributeValue("value");
					try {
						beanUtilsBean.setProperty(setting, name, value);
					} catch (IllegalAccessException exception) {
						exception.printStackTrace();
					} catch (InvocationTargetException exception) {
						exception.printStackTrace();
					}
				}
			} catch (Exception localException) {
				localException.printStackTrace();
			}
			ehcache.put(new net.sf.ehcache.Element(Setting.CACHE_KEY, setting));
		}
		return setting;
	}

	public static void set(Setting setting) {
		try {
			File localFile = new ClassPathResource(GlobalConstant.SHOP_XML_PATH).getFile();
			Document localDocument = new SAXReader().read(localFile);
			List list = localDocument.selectNodes("/shop/setting");
			Iterator itor = list.iterator();
			while (itor.hasNext()) {
				Element element = (Element) itor.next();
				try {
					String str1 = element.attributeValue("name");
					String str2 = beanUtilsBean.getProperty(setting, str1);
					Attribute attr = element.attribute("value");
					attr.setValue(str2);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
			OutputStream outputStream = null;
			XMLWriter writer = null;
			try {
				OutputFormat outputFormat = OutputFormat.createPrettyPrint();
				outputFormat.setEncoding("UTF-8");
				outputFormat.setIndent(true);
				outputFormat.setIndent("\t");
				outputFormat.setNewlines(true);
				outputStream = new FileOutputStream(localFile);
				writer = new XMLWriter(outputStream,outputFormat);
				writer.write(localDocument);
			} catch (Exception localException3) {
				localException3.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
					}
				}
				IOUtils.closeQuietly(outputStream);
			}
			Ehcache ehcache = cacheManager.getEhcache("setting");
			ehcache.put(new net.sf.ehcache.Element(Setting.CACHE_KEY,setting));
		} catch (Exception localException2) {
			localException2.printStackTrace();
		}
	}

	static class SettingConvert extends ConvertUtilsBean {
		public String convert(Object value) {
			if (value != null) {
				Class cls = value.getClass();
				if (cls.isEnum() && super.lookup(cls) == null) {
					super.register(new EnumConverter(cls), cls);
				} else if (cls.isArray() && cls.getComponentType().isEnum()) {
					if (super.lookup(cls) == null) {
						ArrayConverter arrayConverter = new ArrayConverter(cls, new EnumConverter(cls.getComponentType()), 0);
						arrayConverter.setOnlyFirstToString(false);
						super.register((Converter) arrayConverter, cls);
					}
					Object localObject = super.lookup(cls);
					return (String) ((Converter) localObject).convert(String.class, value);
				}
			}
			return super.convert(value);
		}

		public Object convert(String value, Class clazz) {
			if ((clazz.isEnum()) && (super.lookup(clazz) == null)) {
				super.register(new EnumConverter(clazz), clazz);
			}
			return super.convert(value, clazz);
		}

		public Object convert(String[] values, Class clazz) {
			if ((clazz.isArray()) && (clazz.getComponentType().isEnum())
					&& (super.lookup(clazz.getComponentType()) == null)) {
				super.register(new EnumConverter(clazz.getComponentType()),
						clazz.getComponentType());
			}
			return super.convert(values, clazz);
		}

		public Object convert(Object value, Class targetType) {
			if (super.lookup(targetType) == null) {
				if (targetType.isEnum()) {
					super.register(new EnumConverter(targetType), targetType);
				} else if ((targetType.isArray())
						&& (targetType.getComponentType().isEnum())) {
					ArrayConverter localArrayConverter = new ArrayConverter(
							targetType, new EnumConverter(targetType
									.getComponentType()), 0);
					localArrayConverter.setOnlyFirstToString(false);
					super.register(localArrayConverter, targetType);
				}
			}
			return super.convert(value, targetType);
		}
	}

}
