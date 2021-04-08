package com.tigerjoys.shark.supersign.dto.list;

/**
 * 安装日志展示对象
 *
 */
public class InstallLogDto {
	
	/**
	 * ID
	 */
	private long id;
	
	/**
	 * 开始时间
	 */
	private String createTime;
	
	/**
	 * 成功/失败时间
	 */
	private String finishTime;
	
	/**
	 * APP ID
	 */
	private long appId;
	
	/**
	 * APP名称
	 */
	private String appName;
	
	/**
	 * 包名
	 */
	private String bundleId;
	
	/**
	 * UDID
	 */
	private String udid;
	
	/**
	 * 开发者帐号ID
	 */
	private long developerId;
	
	/**
	 * 开发者帐号名称
	 */
	private String developerName;
	
	/**
	 * 状态，枚举InstallLogStatusEnums
	 */
	private int status;
	
	/**
	 * 状态描述
	 */
	private String statusStr;
	
	/**
	 * 安装的标识,1正在安装/已经安装,NULL安装结束
	 */
	private Integer ident;
	
	/**
	 * 耗时
	 */
	private String costTime;
	
	/**
	 * 错误原因
	 */
	private String error;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public long getAppId() {
		return appId;
	}

	public void setAppId(long appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public long getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(long developerId) {
		this.developerId = developerId;
	}

	public String getDeveloperName() {
		return developerName;
	}

	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
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

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getCostTime() {
		return costTime;
	}

	public void setCostTime(String costTime) {
		this.costTime = costTime;
	}

	public Integer getIdent() {
		return ident;
	}

	public void setIdent(Integer ident) {
		this.ident = ident;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

}
