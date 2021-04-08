package com.tigerjoys.shark.supersign.dto.list;

/**
 * 按钮Dto
 *
 */
public class AdminMenuDto {
	
	/**
	 * ID
	 */
	private long id;
	
	/**
	 * 用户名
	 */
	private String name;
	
	/**
	 * 英文名称
	 */
	private String ename;
	
	/**
	 * 显示的名称
	 */
	private String showName;
	
	/**
	 * 位置0默认，1左侧顶级，2左侧二级，3左侧三级，4列表页上侧，5列表页操作区
	 */
	private int seat;
	
	/**
	 * 位置的文字描述
	 */
	private String seatStr;
	
	/**
	 * 按钮url
	 */
	private String position;
	
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

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSeatStr() {
		return seatStr;
	}

	public void setSeatStr(String seatStr) {
		this.seatStr = seatStr;
	}

}
