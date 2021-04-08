package com.tigerjoys.shark.supersign.utils;

import java.util.List;

/**
 * DataTable基础的列表查询Dto
 *
 */
public class DataTableQueryDto {
	
	/**
	 * DataTable请求次数计数器
	 */
	private int draw;
	
	/**
	 * 第一条数据的起始位置
	 */
	private int start;
	
	/**
	 * 告诉服务器每页显示的条数
	 */
	private int length;
	
	/**
	 * 总显示的数量
	 */
	private int totalLength;
	
	/**
	 * 全局的搜索条件
	 */
	private DataTableSearch search;
	
	/**
	 * 请求的列信息
	 */
	private List<DataTableColumn> columns;
	
	/**
	 * 排序方式
	 */
	private List<DataTableOrder> order;
	
	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public DataTableSearch getSearch() {
		return search;
	}

	public void setSearch(DataTableSearch search) {
		this.search = search;
	}

	public List<DataTableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<DataTableColumn> columns) {
		this.columns = columns;
	}

	public List<DataTableOrder> getOrder() {
		return order;
	}

	public void setOrder(List<DataTableOrder> order) {
		this.order = order;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}

}
