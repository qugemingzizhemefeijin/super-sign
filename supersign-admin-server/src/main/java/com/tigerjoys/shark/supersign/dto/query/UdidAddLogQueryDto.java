package com.tigerjoys.shark.supersign.dto.query;

import com.tigerjoys.shark.supersign.utils.DataTableQueryDto;

/**
 * UDID添加记录查询DTO
 *
 */
public class UdidAddLogQueryDto extends DataTableQueryDto {
	
	/**
	 * UDID
	 */
	private String udid;

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}
	
}
