package com.koch.bean;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.time.DateUtils;

import com.koch.util.GlobalConstant;

public class DateEditor extends PropertyEditorSupport {
	private boolean emptyAsNull;
	private String dateFormat = "yyyy-MM-dd HH:mm:ss";

	public DateEditor(boolean emptyAsNull) {
		this.emptyAsNull = emptyAsNull;
	}

	public DateEditor(boolean emptyAsNull, String dateFormat) {
		this.emptyAsNull = emptyAsNull;
		this.dateFormat = dateFormat;
	}

	public String getAsText() {
		Date localDate = (Date) getValue();
		return localDate != null ? new SimpleDateFormat(this.dateFormat)
				.format(localDate) : "";
	}

	public void setAsText(String text) {
		if (text == null) {
			setValue(null);
		} else {
			String str = text.trim();
			if ((this.emptyAsNull) && ("".equals(str)))
				setValue(null);
			else
				try {
					setValue(DateUtils.parseDate(str,
							GlobalConstant.DATE_PATTERNS));
				} catch (ParseException localParseException) {
					setValue(null);
				}
		}
	}
}