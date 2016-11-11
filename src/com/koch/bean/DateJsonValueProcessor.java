package com.koch.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {

	private String format ="yyyy-MM-dd";
	
	public DateJsonValueProcessor(){}
	public DateJsonValueProcessor(String format) {
		this.format = format;
	}
	
	public Object processArrayValue(Object value, JsonConfig config) {
		return process(value);
	}

	public Object processObjectValue(String key, Object value, JsonConfig config) {
		return process(value);
	}
	
	private Object process(Object value){
		
		if(value instanceof Date){
			SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.UK);
            return sdf.format(value);
		}
		return value == null ? "" : value.toString();
	}
}