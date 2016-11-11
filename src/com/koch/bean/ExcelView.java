package com.koch.bean;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ExcelView extends AbstractExcelView {
	private static final String format = "yyyy-MM-dd HH:mm:ss";
	private String filename;
	private String sheetName;
	private String[] properties;
	private String[] titles;
	private Integer[] widths;
	private Converter[] converters;
	private Collection<?> data;
	private String[] contents;

	static {
		DateConverter localDateConverter = new DateConverter();
		localDateConverter.setPattern("yyyy-MM-dd HH:mm:ss");
		ConvertUtils.register(localDateConverter, Date.class);
	}

	public ExcelView(String filename, String sheetName, String[] properties,
			String[] titles, Integer[] widths, Converter[] converters,
			Collection<?> data, String[] contents) {
		this.filename = filename;
		this.sheetName = sheetName;
		this.properties = properties;
		this.titles = titles;
		this.widths = widths;
		this.converters = converters;
		this.data = data;
		this.contents = contents;
	}

	public ExcelView(String[] properties, String[] titles, Collection<?> data,
			String[] contents) {
		this.properties = properties;
		this.titles = titles;
		this.data = data;
		this.contents = contents;
	}

	public ExcelView(String[] properties, String[] titles, Collection<?> data) {
		this.properties = properties;
		this.titles = titles;
		this.data = data;
	}

	public ExcelView(String[] properties, Collection<?> data) {
		this.properties = properties;
		this.data = data;
	}

	public void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) {
		Assert.notEmpty(this.properties);
		HSSFSheet sheet;
		if (StringUtils.isNotEmpty(this.sheetName))
			sheet = workbook.createSheet(this.sheetName);
		else
			sheet = workbook.createSheet();
		int i = 0;
		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFFont font = null;
		HSSFPatriarch patriarch = null;
		HSSFComment comment = null;
		if ((this.titles != null) && (this.titles.length > 0)) {
			row = sheet.createRow(i);
			row.setHeight((short) 400);
			for (int j = 0; j < this.properties.length; j++) {
				cell = row.createCell(j);
				HSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFillForegroundColor((short) 31);
				cellStyle.setFillPattern((short) 1);
				cellStyle.setAlignment((short) 2);
				cellStyle.setVerticalAlignment((short) 1);
				font = workbook.createFont();
				font.setFontHeightInPoints((short) 11);
				font.setBoldweight((short) 700);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);
				if (j == 0) {
					patriarch = sheet.createDrawingPatriarch();
					comment = patriarch.createComment(new HSSFClientAnchor(0,
							0, 0, 0, (short) 1, 1, (short) 4, 4));
					comment.setString(new HSSFRichTextString(""));
					cell.setCellComment(comment);
				}
				if ((this.titles.length > j) && (this.titles[j] != null))
					cell.setCellValue(this.titles[j]);
				else
					cell.setCellValue(this.properties[j]);

				if ((this.widths != null) && (this.widths.length > j)
						&& (this.widths[j] != null))
					sheet.setColumnWidth(j, this.widths[j].intValue());
				else
					sheet.autoSizeColumn(j);
			}
			i++;
		}
		if (this.data != null) {
			Iterator iterator = this.data.iterator();
			while (iterator.hasNext()) {
				Object object = iterator.next();
				row = sheet.createRow(i);
				for (int n = 0; n < this.properties.length; n++) {
					cell = row.createCell(n);
					if ((this.converters != null)
							&& (this.converters.length > n)
							&& (this.converters[n] != null)) {
						Class cls = null;
						try {
							cls = PropertyUtils.getPropertyType(object,
									this.properties[n]);
							ConvertUtils.register(this.converters[n], cls);
							cell.setCellValue(BeanUtils.getProperty(object,
									this.properties[n]));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
						ConvertUtils.deregister(cls);
						if (cls.equals(Date.class)) {
							DateConverter converter = new DateConverter();
							converter.setPattern("yyyy-MM-dd HH:mm:ss");
							ConvertUtils.register(converter, Date.class);
						}
					} else {
						try {
							cell.setCellValue(BeanUtils.getProperty(object,
									this.properties[n]));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}
					if ((i != 0) && (i != 1))
						continue;
					if ((this.widths != null) && (this.widths.length > n)
							&& (this.widths[n] != null))
						sheet.setColumnWidth(n, this.widths[n].intValue());
					else
						sheet.autoSizeColumn(n);
				}
				i++;
			}
		}
		if ((this.contents != null) && (this.contents.length > 0)) {
			i++;
			for (String content : this.contents) {
				row = sheet.createRow(i);
				cell = row.createCell(0);
				HSSFCellStyle cellStyle = workbook.createCellStyle();
				font = workbook.createFont();
				font.setColor((short) 23);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(content);
				i++;
			}
		}
		response.setContentType("application/force-download");
		if (StringUtils.isNotEmpty(this.filename))
			try {
				response.setHeader("Content-disposition",
						"attachment; filename="
								+ URLEncoder.encode(this.filename, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		else
			response.setHeader("Content-disposition", "attachment");
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String[] getProperties() {
		return properties;
	}

	public void setProperties(String[] properties) {
		this.properties = properties;
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public Integer[] getWidths() {
		return widths;
	}

	public void setWidths(Integer[] widths) {
		this.widths = widths;
	}

	public Converter[] getConverters() {
		return converters;
	}

	public void setConverters(Converter[] converters) {
		this.converters = converters;
	}

	public Collection<?> getData() {
		return data;
	}

	public void setData(Collection<?> data) {
		this.data = data;
	}

	public String[] getContents() {
		return contents;
	}

	public void setContents(String[] contents) {
		this.contents = contents;
	}
	
	

}