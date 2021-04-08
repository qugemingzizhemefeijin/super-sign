package com.tigerjoys.shark.supersign.inter.entity;

import java.io.Serializable;
import java.util.Date;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.annotations.Column;
import org.apache.ibatis.annotations.Id;
import org.apache.ibatis.annotations.Table;
import com.tigerjoys.nbs.mybatis.core.BaseEntity;

/**
 * 数据库中  应用信息表[t_app] 表对应的实体类
 * @Date 2020-03-26 01:53:38
 *
 */
@Table(name="t_app")
public class AppEntity extends BaseEntity implements Serializable {

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
	@Column(name="create_time",nullable=false,jdbcType=JdbcType.TIMESTAMP,comment="创建时间")
	private Date create_time;
	
	/**
	 * 更新时间
	 */
	@Column(name="update_time",nullable=false,jdbcType=JdbcType.TIMESTAMP,comment="更新时间")
	private Date update_time;
	
	/**
	 * 应用名称
	 */
	@Column(name="app_name",nullable=false,jdbcType=JdbcType.VARCHAR,comment="应用名称")
	private String app_name;
	
	/**
	 * icon路径
	 */
	@Column(name="icon_path",nullable=false,jdbcType=JdbcType.VARCHAR,comment="icon路径")
	private String icon_path;
	
	/**
	 * fullIcon路径
	 */
	@Column(name="full_icon_path",nullable=false,jdbcType=JdbcType.VARCHAR,comment="fullIcon路径")
	private String full_icon_path;
	
	/**
	 * mobileconfig路径
	 */
	@Column(name="mbconfig",nullable=false,jdbcType=JdbcType.VARCHAR,comment="mobileconfig路径")
	private String mbconfig;
	
	/**
	 * 状态 0：禁用； 1：正常 
	 */
	@Column(name="status",nullable=false,jdbcType=JdbcType.TINYINT,comment="状态 0：禁用； 1：正常 ")
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
	
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	
	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	
	public String getIcon_path() {
		return icon_path;
	}

	public void setIcon_path(String icon_path) {
		this.icon_path = icon_path;
	}
	
	public String getFull_icon_path() {
		return full_icon_path;
	}

	public void setFull_icon_path(String full_icon_path) {
		this.full_icon_path = full_icon_path;
	}
	
	public String getMbconfig() {
		return mbconfig;
	}

	public void setMbconfig(String mbconfig) {
		this.mbconfig = mbconfig;
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