package com.tigerjoys.shark.supersign.dto.query;

import com.tigerjoys.shark.supersign.utils.DataTableQueryDto;

/**
 * 按钮列表查询Dto
 *
 */
public class MenuQueryDto extends DataTableQueryDto {
	
	/**
	 * 父ID
	 */
	private Integer parentId;

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

}
