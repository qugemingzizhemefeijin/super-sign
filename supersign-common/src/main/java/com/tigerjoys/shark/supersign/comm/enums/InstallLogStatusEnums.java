package com.tigerjoys.shark.supersign.comm.enums;

import java.util.Map;

import com.google.api.client.util.Maps;

/**
 * 安装日志状态枚举
 *
 */
public enum InstallLogStatusEnums {
	
	INSTALLING(0 , "正在安装"),
	SUCCESS(10 , "安装成功"),
	FAILURE(50 , "安装失败"),
	;
	
	private int code;
	private String desc;
	
	private static final Map<Integer , InstallLogStatusEnums> err_desc = Maps.newHashMap();
	
	static {
		for(InstallLogStatusEnums refer : InstallLogStatusEnums.values()) {
			err_desc.put(refer.getCode(), refer);
		}
	}
	
	private InstallLogStatusEnums(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static InstallLogStatusEnums getByCode(int code) {
		return err_desc.get(code);
	}
	
	public static String getDescByCode(int code) {
		return err_desc.get(code).desc;
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
