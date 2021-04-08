package com.tigerjoys.shark.supersign.dto.list;

/**
 * UDID添加日志记录
 *
 */
public class UdidAddLogDto {
	
	/**
	 * ID
	 */
	private long id;
	
	/**
	 * 绑定时间
	 */
	private String createTime;
	
	/**
	 * 开发者ID
	 */
	private long developerId;
	
	/**
	 * 开发者名称
	 */
	private String developerName;
	
	/**
	 * udid
	 */
	private String udid;

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

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

}
