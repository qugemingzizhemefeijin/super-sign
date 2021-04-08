package com.tigerjoys.shark.supersign.dto.list;

public class AccountInfoDto {

	/**
	 * ID
	 */
	private long id;
	
	/**
	 * 账号昵称
	 */
	private String username;
	
	/**
	 * 所属app
	 */
	private String app;
	
	/**
	 * 绑定app
	 */
	private String app_info;
	
	/**
	 * 剩余可用数量
	 */
	private int virtual_limit;
	
	/**
	 * 实际按照数量
	 */
	private int install_limit;
	
	/**
	 * 账号创建时间
	 */
	private String create_time;

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

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getApp_info() {
		return app_info;
	}

	public void setApp_info(String app_info) {
		this.app_info = app_info;
	}

	public int getVirtual_limit() {
		return virtual_limit;
	}

	public void setVirtual_limit(int virtual_limit) {
		this.virtual_limit = virtual_limit;
	}

	public int getInstall_limit() {
		return install_limit;
	}

	public void setInstall_limit(int install_limit) {
		this.install_limit = install_limit;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
}
