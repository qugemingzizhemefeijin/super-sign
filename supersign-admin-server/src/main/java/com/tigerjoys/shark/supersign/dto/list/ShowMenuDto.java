package com.tigerjoys.shark.supersign.dto.list;

import java.util.List;

/**
 * 显示的按钮Dto
 *
 */
public class ShowMenuDto {
	
	/**
	 * 菜单ID
	 */
	private Long id;
	
	/**
	 * 菜单名
	 */
	private String name;
	
	/**
	 * 别名
	 */
	private String ename;
	
	/**
	 * 显示的名称
	 */
	private String showName;
	
	/**
	 * 显示的ICON
	 */
	private String icon;
	
	/**
	 * 0默认，1上侧，2左侧父级，3左侧子级，4列表页上侧，5列表页操作区
	 */
	private Integer seat;
	
	/**
	 * 按钮类型seat=0，3，4，5时候，0调用JS，1打开新窗口,2直接跳转,3弹出窗体，4弹开新窗口window.open
	 */
	private Integer menuType;
	
	/**
	 * 是否循环显示，针对seat=4或者5，1不参与循环显示，0参与循环显示
	 */
	private Integer isshowlist;
	
	/**
	 * 链接
	 */
	private String position;
	
	/**
	 * 此按钮显示在哪个页面中
	 */
	private String showurl;
	
	/**
	 * 父按钮ID
	 */
	private Long parentId;
	
	/**
	 * 子按钮
	 */
	private List<ShowMenuDto> childrenList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public Integer getMenuType() {
		return menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public Integer getIsshowlist() {
		return isshowlist;
	}

	public void setIsshowlist(Integer isshowlist) {
		this.isshowlist = isshowlist;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getShowurl() {
		return showurl;
	}

	public void setShowurl(String showurl) {
		this.showurl = showurl;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public List<ShowMenuDto> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(List<ShowMenuDto> childrenList) {
		this.childrenList = childrenList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
