package com.tigerjoys.shark.supersign.enums;

/**
 * 后台按钮位置枚举
 *
 */
public enum AdminMenuSeatEnum {
	
	_default(0 , "自由按钮"),
	_left_top(1 , "左侧顶级按钮"),
	_left_parent(2 , "左侧父级按钮"),
	_left_children(3 , "左侧子级按钮"),
	_list_top(4 , "列表页上侧按钮"),
	_list_each(5 , "列表页循环操作按钮"),
	;
	
	private int code;
	private String desc;
	
	private AdminMenuSeatEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static String getDescByCode(int code) {
		for (AdminMenuSeatEnum refer : AdminMenuSeatEnum.values())
			if (code == refer.getCode())
				return refer.getDesc();
		return null;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
