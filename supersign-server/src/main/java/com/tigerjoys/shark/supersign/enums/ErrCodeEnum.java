package com.tigerjoys.shark.supersign.enums;

import java.util.HashMap;
import java.util.Map;

import com.tigerjoys.nbs.common.IErrorCodeEnum;

/**
 * 系统错误码
 *
 */
public enum ErrCodeEnum implements IErrorCodeEnum {
	
	success(0,"成功"),
	error(1,"系统异常"),
	sign_error(2,"非法数据"),
	parameter_error(3,"参数校验失败"),
 	db_error(4,"数据异常"),
 	db_not_found(5,"没有找到数据，可能不存在或已被删除"),
 	have_no_permission(6,"你没有权限进行操作"),
 	state_error(7,"业务状态异常"),
 	login_error(8,"登录异常"),
 	user_isnull(9,"用户不存在"),
 	user_freeze(10,"用户已被冻结"),
 	validate_code_error(11,"验证码错误"),
 	password_against(12,"密码不符合规则"),
 	upload_photo_fail(13,"上传图片失败"),
 	upload_app_fail(14,"上传APP失败"),
 	upload_app_fileExt_error(15,"上传文件格式错误"),
	;
	
	private int code;
	private String desc;
	
	private static final Map<Integer , String> err_desc = new HashMap<Integer , String>();
	
	static {
		for(ErrCodeEnum refer : ErrCodeEnum.values()) {
			err_desc.put(refer.getCode(), refer.getDesc());
		}
	}
	
	private ErrCodeEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static String getDescByCode(int code) {
		return err_desc.get(code);
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
