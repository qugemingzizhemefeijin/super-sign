package com.tigerjoys.shark.supersign.dto.query;

import com.tigerjoys.shark.supersign.utils.DataTableQueryDto;

/**
 * 安装日志查询DTO
 *
 */
public class InstallLogQueryDto extends DataTableQueryDto {
	
	/**
	 * UDID
	 */
	private String udid;
	
	/**
	 * 状态
	 */
	private Integer status;
	
	/**
	 * APP ID
	 */
	private Long appId;

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
	
}
