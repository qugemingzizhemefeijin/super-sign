package com.tigerjoys.shark.supersign.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DataTable Ajax 数据返回对象
 *
 */
public class DataTableResult<T> {
	
	/**
	 * DataTable请求次数计数器
	 */
	private int draw;
	
	/**
	 * 总记录数
	 */
	private long recordsTotal;
	
	/**
	 * 过滤后的记录数，一般等于recordsTotal
	 */
	private long recordsFiltered;
	
	/**
	 * 表中中需要显示的数据
	 */
	private List<T> data = new ArrayList<T>();
	
	/**
	 * 如果错误了，提示错误
	 */
	private String error;
	
	/**
	 * 自定义属性
	 */
	private Map<String , Object> customData;
	
	/**
	 * 获得一个错误的对象
	 * @param draw - int
	 * @param error - String
	 * @param clazz - Class<E>
	 * @return DataTableResult<E>
	 */
	public static <E> DataTableResult<E> getErrorResult(int draw , String error , Class<E> clazz) {
		DataTableResult<E> result = new DataTableResult<E>();
		result.error = error;
		result.draw = draw;
		
		return result;
	}
	
	/**
	 * 初始化一个对象
	 * @param draw - int
	 * @param total - 数据量
	 * @param clazz - Data的Class
	 * @return DataTableResult<E>
	 */
	public static <E> DataTableResult<E> getEmptyResult(int draw , long total , Class<E> clazz){
		DataTableResult<E> result = new DataTableResult<E>();
		result.recordsFiltered = total;
		result.recordsTotal = total;
		result.draw = draw;
		
		return result;
	}
	
	public static DataTableResult<Map<String,Object>> getEmptyResult(int draw , long total){
		DataTableResult<Map<String,Object>> result = new DataTableResult<Map<String,Object>>();
		result.recordsFiltered = total;
		result.recordsTotal = total;
		result.draw = draw;
		return result;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Map<String, Object> getCustomData() {
		return customData;
	}

	public void setCustomData(Map<String, Object> customData) {
		this.customData = customData;
	}

}
