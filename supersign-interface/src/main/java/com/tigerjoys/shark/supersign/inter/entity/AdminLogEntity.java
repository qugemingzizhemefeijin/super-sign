package com.tigerjoys.shark.supersign.inter.entity;

import java.io.Serializable;
import java.util.Date;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.annotations.Column;
import org.apache.ibatis.annotations.Id;
import org.apache.ibatis.annotations.Table;
import com.tigerjoys.nbs.mybatis.core.BaseEntity;

/**
 * 数据库中  后台管理员日志[t_admin_log] 表对应的实体类
 * @Date 2020-04-01 14:31:06
 *
 */
@Table(name="t_admin_log")
public class AdminLogEntity extends BaseEntity implements Serializable {

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
	 * 管理员ID
	 */
	@Column(name="adminId",nullable=false,jdbcType=JdbcType.BIGINT,comment="管理员ID")
	private Long adminId;
	
	/**
	 * 记录类型,参看枚举类
	 */
	@Column(name="type",nullable=false,jdbcType=JdbcType.SMALLINT,comment="记录类型,参看枚举类")
	private Integer type;
	
	/**
	 * 备注
	 */
	@Column(name="remark",nullable=true,jdbcType=JdbcType.VARCHAR,comment="备注")
	private String remark;
	
	/**
	 * IP地址
	 */
	@Column(name="ip",nullable=true,jdbcType=JdbcType.VARCHAR,comment="IP地址")
	private String ip;
	
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
	
	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}