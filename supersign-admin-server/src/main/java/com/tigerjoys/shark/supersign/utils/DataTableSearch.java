package com.tigerjoys.shark.supersign.utils;

/**
 * DataTable Search Bean
 * 
 *
 */
public class DataTableSearch {

	/**
	 * 搜索内容
	 */
	private String value;

	/**
	 * 是否视为正则表达式
	 */
	private boolean regex;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isRegex() {
		return regex;
	}

	public void setRegex(boolean regex) {
		this.regex = regex;
	}

}