package com.tigerjoys.shark.supersign.dto.list;

import java.io.Serializable;

/**
 * autocomplete lookup 用的data value bean
 *
 */
public class DataValueBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8671147998311854163L;
	
	private String data;
	
	private String value;
	
	public DataValueBean(){
		
	}
	
	public DataValueBean(String data , String value) {
		this.data = data;
		this.value = value;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "DataValueBean [data=" + data + ", value=" + value + "]";
	}

}
