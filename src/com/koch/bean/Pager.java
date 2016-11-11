package com.koch.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean类 - 分页
 */

@SuppressWarnings("unchecked")
public class Pager<T> {
	
	// 排序方式
	public enum OrderType{
		asc, desc
	}
	
	public static final Integer MAX_PAGE_SIZE = 500;// 每页最大记录数限制

	private Integer pageNumber = this.page;// 当前页码
	private Integer page = 1;//easyui当前页
	private Integer pageSize = this.rows;// 每页记录数
	private Integer rows = 20;//easyui每页记录数
	private Integer totalCount = 0;// 总记录数
	private Integer pageCount = 0;// 总页数
	private String property;// 查找属性名称
	private String keyword;// 查找关键字
	private String orderBy = "id";// 排序字段
	private OrderType orderType = OrderType.desc;// 排序方式
	private List<T> list = new ArrayList<T>();// 数据List
	private Map<String,Object> parameters = new HashMap<String, Object>();
	private List<Filter> filters = new ArrayList<Filter>();

	public Integer getPageNumber() {
		return this.page;
	}

	public void setPageNumber(Integer pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		this.pageNumber = pageNumber;
	}
	
	public Integer getPage() {
		return this.pageNumber;
	}

	public void setPage(Integer page) {
		this.page = page;
		this.pageNumber = page;
	}

	public Integer getRows() {
		return this.pageSize;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
		this.pageSize = rows;
	}

	public Integer getPageSize() {
		return this.rows;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		} else if(pageSize > MAX_PAGE_SIZE) {
			pageSize = MAX_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}
	
	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageCount() {
		pageCount = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			pageCount ++;
		}
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	public void put(String parameterName,Object parameterValue){
		this.parameters.put(parameterName, parameterValue);
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
	
}