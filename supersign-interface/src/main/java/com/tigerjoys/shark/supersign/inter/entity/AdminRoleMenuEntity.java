package com.tigerjoys.shark.supersign.inter.entity;

import java.io.Serializable;
import java.util.Date;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.annotations.Column;
import org.apache.ibatis.annotations.Id;
import org.apache.ibatis.annotations.Table;
import com.tigerjoys.nbs.mybatis.core.BaseEntity;

/**
 * 数据库中  角色跟按钮的关联表[t_admin_role_menu] 表对应的实体类
 * @Date 2020-04-01 14:31:07
 *
 */
@Table(name="t_admin_role_menu")
public class AdminRoleMenuEntity extends BaseEntity implements Serializable {

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
	 * 角色ID
	 */
	@Column(name="roleId",nullable=false,jdbcType=JdbcType.BIGINT,comment="角色ID")
	private Long roleId;
	
	/**
	 * 按钮ID
	 */
	@Column(name="menuId",nullable=false,jdbcType=JdbcType.BIGINT,comment="按钮ID")
	private Long menuId;
	
	/**
	 * 权限 0=隐藏，1=可见，2=可见并可操作
	 */
	@Column(name="handle",nullable=false,jdbcType=JdbcType.TINYINT,comment="权限 0=隐藏，1=可见，2=可见并可操作")
	private Integer handle;
	
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
	
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	
	public Integer getHandle() {
		return handle;
	}

	public void setHandle(Integer handle) {
		this.handle = handle;
	}
	
}