package com.tigerjoys.shark.supersign.dto.list;

/**
 * APP列表展示信息
 *
 */
public class AppDto {
	
	/**
	 * APP ID
	 */
	private long id;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * APP名称
	 */
	private String appName;
	
	/**
	 * ICON地址
	 */
	private String icon;
	
	/**
	 * mobileConfig文件地址
	 */
	private String mb;
	
	/**
	 * APP INFO的数量
	 */
	private Integer appInfoCount;
	
	/**
	 * 状态0下架,1上架
	 */
	private int status;
	
	/**
	 * 状态描述
	 */
	private String statusStr;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMb() {
		return mb;
	}

	public void setMb(String mb) {
		this.mb = mb;
	}

	public Integer getAppInfoCount() {
		return appInfoCount;
	}

	public void setAppInfoCount(Integer appInfoCount) {
		this.appInfoCount = appInfoCount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

}
