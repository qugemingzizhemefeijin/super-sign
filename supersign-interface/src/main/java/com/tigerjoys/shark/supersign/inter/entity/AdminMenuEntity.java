package com.tigerjoys.shark.supersign.inter.entity;

import java.io.Serializable;
import java.util.Date;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.annotations.Column;
import org.apache.ibatis.annotations.Id;
import org.apache.ibatis.annotations.Table;
import com.tigerjoys.nbs.mybatis.core.BaseEntity;

/**
 * 数据库中  后台按钮表[t_admin_menu] 表对应的实体类
 * @Date 2020-04-01 14:31:06
 *
 */
@Table(name="t_admin_menu")
public class AdminMenuEntity extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID
	 */
	@Id
	@Column(name="id",nullable=false,jdbcType=JdbcType.BIGINT,comment="ID")
	private Long id;
	
	/**
	 * 创建时间
	 */
	@Column(name="createDate",nullable=true,jdbcType=JdbcType.TIMESTAMP,comment="创建时间")
	private Date createDate;
	
	/**
	 * 修改时间
	 */
	@Column(name="updateDate",nullable=true,jdbcType=JdbcType.TIMESTAMP,comment="修改时间")
	private Date updateDate;
	
	/**
	 * 录入员id
	 */
	@Column(name="createAdminId",nullable=false,jdbcType=JdbcType.BIGINT,comment="录入员id")
	private Long createAdminId;
	
	/**
	 * 修改员id
	 */
	@Column(name="updateAdminId",nullable=false,jdbcType=JdbcType.BIGINT,comment="修改员id")
	private Long updateAdminId;
	
	/**
	 * 菜单名
	 */
	@Column(name="name",nullable=false,jdbcType=JdbcType.VARCHAR,comment="菜单名")
	private String name;
	
	/**
	 * 别名
	 */
	@Column(name="ename",nullable=true,jdbcType=JdbcType.VARCHAR,comment="别名")
	private String ename;
	
	/**
	 * showName
	 */
	@Column(name="showName",nullable=false,jdbcType=JdbcType.VARCHAR,comment="showName")
	private String showName;
	
	/**
	 * 展示的按钮ICON
	 */
	@Column(name="icon",nullable=true,jdbcType=JdbcType.VARCHAR,comment="展示的按钮ICON")
	private String icon;
	
	/**
	 * 0默认，1上侧，2左侧父级，3左侧子级，4列表页上侧，5列表页操作区
	 */
	@Column(name="seat",nullable=false,jdbcType=JdbcType.TINYINT,comment="0默认，1上侧，2左侧父级，3左侧子级，4列表页上侧，5列表页操作区")
	private Integer seat;
	
	/**
	 * 按钮类型seat=0，3，4，5时候，0调用JS，1打开新窗口,2直接跳转,3弹出窗体，4弹开新窗口window.open
	 */
	@Column(name="menuType",nullable=false,jdbcType=JdbcType.TINYINT,comment="按钮类型seat=0，3，4，5时候，0调用JS，1打开新窗口,2直接跳转,3弹出窗体，4弹开新窗口window.open")
	private Integer menuType;
	
	/**
	 * 是否循环显示，针对seat=4或者5，1不参与循环显示，0参与循环显示
	 */
	@Column(name="isshowlist",nullable=false,jdbcType=JdbcType.INTEGER,comment="是否循环显示，针对seat=4或者5，1不参与循环显示，0参与循环显示")
	private Integer isshowlist;
	
	/**
	 * 链接
	 */
	@Column(name="position",nullable=true,jdbcType=JdbcType.VARCHAR,comment="链接")
	private String position;
	
	/**
	 * 此按钮显示在哪个页面中
	 */
	@Column(name="showurl",nullable=true,jdbcType=JdbcType.VARCHAR,comment="此按钮显示在哪个页面中")
	private String showurl;
	
	/**
	 * js函数名字
	 */
	@Column(name="jsfunName",nullable=true,jdbcType=JdbcType.VARCHAR,comment="js函数名字")
	private String jsfunName;
	
	/**
	 * js函数方法体
	 */
	@Column(name="jsfunction",nullable=true,jdbcType=JdbcType.LONGVARCHAR,comment="js函数方法体")
	private String jsfunction;
	
	/**
	 * 优先级
	 */
	@Column(name="priority",nullable=false,jdbcType=JdbcType.INTEGER,comment="优先级")
	private Integer priority;
	
	/**
	 * 父按钮ID
	 */
	@Column(name="parentId",nullable=false,jdbcType=JdbcType.BIGINT,comment="父按钮ID")
	private Long parentId;
	
	/**
	 * 状态0停用，1正常
	 */
	@Column(name="status",nullable=false,jdbcType=JdbcType.TINYINT,comment="状态0停用，1正常")
	private Integer status;
	
	/**
	 * 关联的url地址集合，回车换行符分割
	 */
	@Column(name="resource",nullable=true,jdbcType=JdbcType.LONGVARCHAR,comment="关联的url地址集合，回车换行符分割")
	private String resource;
	
	/**
	 * 备注
	 */
	@Column(name="remark",nullable=true,jdbcType=JdbcType.VARCHAR,comment="备注")
	private String remark;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public Long getCreateAdminId() {
		return createAdminId;
	}

	public void setCreateAdminId(Long createAdminId) {
		this.createAdminId = createAdminId;
	}
	
	public Long getUpdateAdminId() {
		return updateAdminId;
	}

	public void setUpdateAdminId(Long updateAdminId) {
		this.updateAdminId = updateAdminId;
	}
	
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
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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
	
	public String getJsfunName() {
		return jsfunName;
	}

	public void setJsfunName(String jsfunName) {
		this.jsfunName = jsfunName;
	}
	
	public String getJsfunction() {
		return jsfunction;
	}

	public void setJsfunction(String jsfunction) {
		this.jsfunction = jsfunction;
	}
	
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}