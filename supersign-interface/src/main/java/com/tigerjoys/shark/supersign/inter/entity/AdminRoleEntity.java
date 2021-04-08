package com.tigerjoys.shark.supersign.inter.entity;

import java.io.Serializable;
import java.util.Date;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.annotations.Column;
import org.apache.ibatis.annotations.Id;
import org.apache.ibatis.annotations.Table;
import com.tigerjoys.nbs.mybatis.core.BaseEntity;

/**
 * 数据库中  后台角色表[t_admin_role] 表对应的实体类
 * @Date 2020-04-01 14:31:07
 *
 */
@Table(name="t_admin_role")
public class AdminRoleEntity extends BaseEntity implements Serializable {

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
	 * 名称
	 */
	@Column(name="name",nullable=false,jdbcType=JdbcType.VARCHAR,comment="名称")
	private String name;
	
	/**
	 * 父角色ID
	 */
	@Column(name="parentId",nullable=false,jdbcType=JdbcType.BIGINT,comment="父角色ID")
	private Long parentId;
	
	/**
	 * 排序优先级
	 */
	@Column(name="priority",nullable=false,jdbcType=JdbcType.INTEGER,comment="排序优先级")
	private Integer priority;
	
	/**
	 * 是否有效
	 */
	@Column(name="status",nullable=false,jdbcType=JdbcType.TINYINT,comment="是否有效")
	private Integer status;
	
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
	
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}