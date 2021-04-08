package com.tigerjoys.shark.supersign.dto.list;

/**
 * APP INFO DTO
 *
 */
public class AppInfoDto {
	
	/**
	 * APP INFO ID
	 */
	private long id;
	
	/**
	 * APP ID
	 */
	private long appId;
	
	/**
	 * APP名称
	 */
	private String appName;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * APP INFO名称
	 */
	private String appInfoName;
	
	/**
	 * Bundle ID
	 */
	private String bundleId;
	
	/**
	 * APP版本
	 */
	private String version;
	
	/**
	 * APP版本编号
	 */
	private String versionCode;
	
	/**
	 * IPA源文件路径
	 */
	private String path;
	
	/**
	 * 96x96 icon地址
	 */
	private String iconPath;
	
	/**
	 * 192x192 icon地址
	 */
	private String fullIconPath;
	
	/**
	 * 最新的重签名后的IPA地址
	 */
	private String resignIpaPath;
	
	/**
	 * 最新的重签名后的plist地址
	 */
	private String resignPlistPath;
	
	/**
	 * 最新的重签名后的mp地址
	 */
	private String resignMpPath;
	
	/**
	 * 状态,0：下架； 1：上架
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

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getFullIconPath() {
		return fullIconPath;
	}

	public void setFullIconPath(String fullIconPath) {
		this.fullIconPath = fullIconPath;
	}

	public String getResignIpaPath() {
		return resignIpaPath;
	}

	public void setResignIpaPath(String resignIpaPath) {
		this.resignIpaPath = resignIpaPath;
	}

	public String getResignPlistPath() {
		return resignPlistPath;
	}

	public void setResignPlistPath(String resignPlistPath) {
		this.resignPlistPath = resignPlistPath;
	}

	public String getResignMpPath() {
		return resignMpPath;
	}

	public void setResignMpPath(String resignMpPath) {
		this.resignMpPath = resignMpPath;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getAppInfoName() {
		return appInfoName;
	}

	public void setAppInfoName(String appInfoName) {
		this.appInfoName = appInfoName;
	}

}
