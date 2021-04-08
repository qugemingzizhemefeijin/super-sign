package com.tigerjoys.shark.supersign.dto.list;

public class CerInfoDto {

	/**
	 * ID
	 */
	private long id;
	
	/**
	 * 账号昵称
	 */
	private String username;
	
	/**
	 * 绑定app
	 */
	private String app_info;
	
	/**
	 * 证书标识
	 */
	private String certificate_id;
	
	/**
	 * 公钥地址
	 */
	private String public_pem_path;
	
	/**
	 * 私钥地址
	 */
	private String private_pem_path;
	
	/**
	 * 过期时间
	 */
	private String expire_time;
	
	/**
	 * 状态信息   0禁用,1.正常
	 */
	private int status;
	
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

	public String getApp_info() {
		return app_info;
	}

	public void setApp_info(String app_info) {
		this.app_info = app_info;
	}

	public String getCertificate_id() {
		return certificate_id;
	}

	public void setCertificate_id(String certificate_id) {
		this.certificate_id = certificate_id;
	}

	public String getPublic_pem_path() {
		return public_pem_path;
	}

	public void setPublic_pem_path(String public_pem_path) {
		this.public_pem_path = public_pem_path;
	}

	public String getPrivate_pem_path() {
		return private_pem_path;
	}

	public void setPrivate_pem_path(String private_pem_path) {
		this.private_pem_path = private_pem_path;
	}

	public String getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
}
