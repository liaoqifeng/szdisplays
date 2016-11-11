package com.koch.tag;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.koch.bean.Setting;
import com.koch.entity.Product;
import com.koch.entity.Property;
import com.koch.util.JsonUtil;
import com.koch.util.SettingUtils;
import com.koch.util.SpringUtil;

@Component("functionTag")
public class FunctionTag {
	
	@Value("${url.charset}")
	private static String charset;
	
	public static String currency(BigDecimal price,Boolean isCurrency){
		Setting setting = SettingUtils.get();
		if(price == null){
			if(isCurrency)
				return setting.getCurrencySign()+setting.setScale(new BigDecimal(0)).toString();
			else
				return setting.setScale(new BigDecimal(0)).toString();
		}else{
			if(isCurrency)
				return setting.getCurrencySign()+setting.setScale(price).toString();
			else
				return setting.setScale(price).toString();
		}
	}
	
	public static String config(String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		Setting setting = SettingUtils.get();
		Class cls = setting.getClass();
		Field field = cls.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(setting).toString();
	}
	
	public static String toJson(Object obj){
		return JsonUtil.toJson(obj);
	}
	
	public static String call(String code){
		return SpringUtil.getMessage(code, new Object[0]);
	}
	
	public static String message(String message){
		if(StringUtils.isNotEmpty(message)){
			StringBuffer html = new StringBuffer();
			html.append("$.messager.show({        ");
			html.append("	title:'提示',         ");
			html.append("	msg:'"+message+"',    ");
			html.append("	showType:'fade',      ");
			html.append("	timeout:1000,      ");
			html.append("	style:{               ");
			html.append("		right:'',         ");
			html.append("		bottom:''         ");
			html.append("	}                     ");
			html.append("});                      ");
			return html.toString();
		}
		return "";
	}
	
	public static  String substr(String value,Integer length,String fill){
		String result = "";
		if(StringUtils.isNotEmpty(value)){
			if(value.length() > length.intValue()){
				result = value.substring(0, length);
				if(StringUtils.isNotEmpty(fill)){
					result += fill;
				}
			}else{
				result = value;
			}
		}
		return result;
	}
	
	public static boolean containsObject(Object list,Object obj){
		if(list != null){
			if(list instanceof List){
				if(((List)list).size() <= 0) return false;
				return ((List)list).contains(obj);
			}else if(list instanceof Set){
				if(((Set)list).size() <= 0) return false;
				return ((Set)list).contains(obj);
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public static boolean containsKey(Map map,Object obj){
		if(map != null){
			Set keySet = map.keySet();
			return keySet.contains(obj);
		}else{
			return false;
		}
	}
	
	public static String decode(String value){
		if(StringUtils.isNotEmpty(value)){
			try {
				return URLDecoder.decode(value, charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public static String randomCode(){
		return  UUID.randomUUID().toString();
	}
	
	public static String getPropertyValue(Object product,Object property){
		if(property == null || product == null) return "";
		Property prop = (Property)property;
		Product prod = (Product)product;
		if ((prop != null) && (prop.getPropertyIndex() != null)){
			try {
				String str = "property" + prop.getPropertyIndex();
				return (String) PropertyUtils.getProperty(prod, str);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
