package com.tigerjoys.shark.supersign.utils;

/**
 * DataTable列的查询Bean
 * 
 *
 */
public class DataTableColumn {

	/**
	 * columns 绑定的数据源
	 */
	private String data;

	/**
	 * 列名称
	 */
	private String name;

	/**
	 * 标记列是否能被搜索 为 true代表可以，否则不可以
	 */
	private boolean searchable;

	/**
	 * 标记列是否能排序 为 true代表可以，否则不可以
	 */
	private String orderable;

	/**
	 * 特定列的搜索条件
	 */
	private DataTableSearch search;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public String getOrderable() {
		return orderable;
	}

	public void setOrderable(String orderable) {
		this.orderable = orderable;
	}

	public DataTableSearch getSearch() {
		return search;
	}

	public void setSearch(DataTableSearch search) {
		this.search = search;
	}

}