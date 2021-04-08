package com.tigerjoys.shark.supersign.dto.query;

import com.tigerjoys.shark.supersign.utils.DataTableQueryDto;

/**
 * APP INFO查询DTO
 *
 */
public class AppInfoQueryDto extends DataTableQueryDto {
	
	/**
	 * APP ID
	 */
	private Long appId;
	
	/**
	 * 状态,0下架,1上架
	 */
	private Integer status;

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
