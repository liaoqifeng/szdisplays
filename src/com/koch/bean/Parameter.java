package com.koch.bean;


/**
 * Bean类 - 参数
 */

public class Parameter {
	private String parameterName;
	
	private Object parameterValue;
	
	private String operator;
	
	public Parameter() {
		super();
	}
	public Parameter(String parameterName, Object parameterValue,
			String operator) {
		super();
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
		this.operator = operator;
	}



	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public Object getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(Object parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}