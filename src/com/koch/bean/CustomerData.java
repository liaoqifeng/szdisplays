package com.koch.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean类 - 分页
 */

@SuppressWarnings("unchecked")
public class CustomerData {
	private List rows;
	private Integer total;

	public CustomerData(List rows, Integer total) {
		super();
		this.rows = rows;
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
}