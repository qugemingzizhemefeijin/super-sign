package com.tigerjoys.shark.supersign.dto.list;

/**
 * 管理员Dto
 *
 */
public class AdminDto {
	
	/**
	 * ID
	 */
	private long id;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 真实姓名
	 */
	private String realname;
	
	/**
	 * 权限名称
	 */
	private String rolename;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 手机号码
	 */
	private String mobile;
	
	/**
	 * 状态
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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
