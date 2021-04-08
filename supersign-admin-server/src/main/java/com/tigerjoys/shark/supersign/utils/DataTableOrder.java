package com.tigerjoys.shark.supersign.utils;

/**
 * DataTable列的排序Bean
 * 
 *
 */
public class DataTableOrder {
	
	/**
	 * 列索引
	 */
	private int column;
	
	/**
	 * 列名称
	 */
	private String data;
	
	/**
	 * 排序方式,desc,asc
	 */
	private String dir;

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

}
