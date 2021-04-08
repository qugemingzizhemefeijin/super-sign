package com.tigerjoys.shark.supersign.dto.list;

/**
 * 管理员角色Dto
 *
 */
public class AdminRoleDto {
	
	/**
	 * ID
	 */
	private long id;
	
	/**
	 * 用户名
	 */
	private String name;
	
	/**
	 * 父角色ID
	 */
	private long parentId;
	
	/**
	 * 父角色名称
	 */
	private String parentName;
	
	/**
	 * 权限名称
	 */
	private int priority;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
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
