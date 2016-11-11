package com.koch.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

/**
 * @Title: JsonUtil
 * @Description: Json处理工具类
 */
public class JsonUtil {
	private static ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * Object集合转Json字符串
	 * @Title: toJson
	 * @Description: 
	 * @param 对象集合
	 * @return
	 */
	public static String toJson(Object obj) throws RuntimeException{
		return toJson(obj, null);
	}
	
	public static String toJson(Object obj,String datePattern) throws RuntimeException{
		if (obj == null) {
			return "";
		}
		if(StringUtils.isNotEmpty(datePattern)){
			objectMapper.setDateFormat(new SimpleDateFormat(datePattern));
		}
		StringWriter sw = new StringWriter();
		try {
			objectMapper.writeValue(sw, obj);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return ObjectUtils.toString(sw);
	}

	@SuppressWarnings("unchecked")
	public static String toJsonIgnoreProperties(Object beans,String [] properties) throws RuntimeException{
		String filterName = "";
		if(beans instanceof Collection){
			filterName = AnnotationUtils.getValue(AnnotationUtils.findAnnotation(CollectionUtils.get(beans, 0).getClass(), JsonFilter.class)).toString();
		}else{
			filterName = AnnotationUtils.getValue(AnnotationUtils.findAnnotation(beans.getClass(), JsonFilter.class)).toString();
		}
		return toJsonIgnoreProperties(beans,filterName,properties);
	}
	
	public static String toJsonIgnoreProperties(Object beans,String filterName,String [] properties) throws RuntimeException{
		if (beans == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			SimpleFilterProvider filter = new SimpleFilterProvider();
			filter.addFilter(filterName,SimpleBeanPropertyFilter.serializeAllExcept(properties));
			mapper.setFilters(filter);
			mapper.writeValue(sw, beans);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return ObjectUtils.toString(sw);
	}
	
	public static String toJsonIgnoreProperties(Object beans,Map<String,String[]> filterMap) throws RuntimeException{
		if (beans == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			if(filterMap != null && filterMap.size()>0){
				SimpleFilterProvider filter = new SimpleFilterProvider();
				for(Map.Entry<String, String[]> entry : filterMap.entrySet()){
					String filterName = entry.getKey();
					String[] properties = entry.getValue();
					filter.addFilter(filterName,SimpleBeanPropertyFilter.serializeAllExcept(properties));
				}
				mapper.setFilters(filter);
			}
			mapper.writeValue(sw, beans);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return ObjectUtils.toString(sw);
	}
	
	@SuppressWarnings("unchecked")
	public static String toJsonIncludeProperties(Object beans,String [] properties) throws RuntimeException{
		String filterName = "";
		if(beans instanceof Collection){
			filterName = AnnotationUtils.getValue(AnnotationUtils.findAnnotation(CollectionUtils.get(beans, 0).getClass(), JsonFilter.class)).toString();
		}else{
			filterName = AnnotationUtils.getValue(AnnotationUtils.findAnnotation(beans.getClass(), JsonFilter.class)).toString();
		}
		return toJsonIncludeProperties(beans,filterName,properties);
	}
	
	public static String toJsonIncludeProperties(Object beans,String filterName,String [] properties) throws RuntimeException{
		if (beans == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			SimpleFilterProvider fileter = new SimpleFilterProvider();
			fileter.addFilter(filterName,SimpleBeanPropertyFilter.filterOutAllExcept(properties));
			mapper.setFilters(fileter);
			mapper.writeValue(sw, beans);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return ObjectUtils.toString(sw);
	}
	
	public static String toJsonIncludeProperties(Object beans,Map<String,String[]> filterMap) throws RuntimeException{
		if (beans == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
			if(filterMap != null && filterMap.size()>0){
				SimpleFilterProvider filter = new SimpleFilterProvider();
				for(Map.Entry<String, String[]> entry : filterMap.entrySet()){
					String filterName = entry.getKey();
					String[] properties = entry.getValue();
					filter.addFilter(filterName,SimpleBeanPropertyFilter.filterOutAllExcept(properties));
				}
				mapper.setFilters(filter);
			}
			mapper.writeValue(sw, beans);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return ObjectUtils.toString(sw);
	}
	
	public static void toJson(HttpServletResponse response, String contentType, Object value) {
		Assert.notNull(response);
		Assert.hasText(contentType);
		try {
			response.setContentType(contentType);
			objectMapper.writeValue(response.getWriter(), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void toJson(HttpServletResponse response, Object value) {
		Assert.notNull(response);
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			objectMapper.writeValue(pw, value);
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.close();
		}
	}

	public static <T> ArrayList<T> toList(String json, Class<T> cls){
		if(StringUtils.isBlank(json)){
			return new ArrayList<T>();
		}
		try {
			return objectMapper.readValue(json, getCollectionType(objectMapper, ArrayList.class, cls));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> T toObject(String json, Class<T> valueType) {
		Assert.hasText(json);
		Assert.notNull(valueType);
		try {
			return objectMapper.readValue(json, valueType);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	public static <T> T toObject(String json, TypeReference<?> typeReference) {
		Assert.hasText(json);
		Assert.notNull(typeReference);
		try {
			return objectMapper.readValue(json, typeReference);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	public static <T> T toObject(String json, JavaType javaType) {
		Assert.hasText(json);
		Assert.notNull(javaType);
		try {
			return objectMapper.readValue(json, javaType);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}
	 /**   
     * 获取泛型的Collection Type  
	 * @param objectMapper Jackson对象
     * @param collectionCls 泛型的Collection   
     * @param beanCls 元素类   
     * @return JavaType Java类型   
     */   
	 public static JavaType getCollectionType(ObjectMapper objectMapper, Class<?> collectionCls, Class<?>... beanCls)throws RuntimeException{   
	     return objectMapper.getTypeFactory().constructParametricType(collectionCls, beanCls);   
	 }
}
