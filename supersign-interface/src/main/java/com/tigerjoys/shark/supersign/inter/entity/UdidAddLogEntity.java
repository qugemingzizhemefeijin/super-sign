package com.tigerjoys.shark.supersign.inter.entity;

import java.io.Serializable;
import java.util.Date;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.annotations.Column;
import org.apache.ibatis.annotations.Id;
import org.apache.ibatis.annotations.Table;
import com.tigerjoys.nbs.mybatis.core.BaseEntity;

/**
 * 数据库中  UDID更新日志[t_udid_add_log] 表对应的实体类
 * @Date 2020-03-26 15:39:14
 *
 */
@Table(name="t_udid_add_log")
public class UdidAddLogEntity extends BaseEntity implements Serializable {

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
	 * 应用ID
	 */
	@Column(name="app_id",nullable=false,jdbcType=JdbcType.BIGINT,comment="应用ID")
	private Long app_id;
	
	/**
	 * app_info Id
	 */
	@Column(name="app_info_Id",nullable=false,jdbcType=JdbcType.BIGINT,comment="app_info Id")
	private Long app_info_Id;
	
	/**
	 * 开发者ID
	 */
	@Column(name="developer_id",nullable=false,jdbcType=JdbcType.BIGINT,comment="developer_id Id")
	private Long developer_id;
	
	/**
	 * udid
	 */
	@Column(name="udid",nullable=false,jdbcType=JdbcType.VARCHAR,comment="udid")
	private String udid;
	
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
	
	public Long getApp_id() {
		return app_id;
	}

	public void setApp_id(Long app_id) {
		this.app_id = app_id;
	}
	
	public Long getApp_info_Id() {
		return app_info_Id;
	}

	public void setApp_info_Id(Long app_info_Id) {
		this.app_info_Id = app_info_Id;
	}
	
	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public Long getDeveloper_id() {
		return developer_id;
	}

	public void setDeveloper_id(Long developer_id) {
		this.developer_id = developer_id;
	}
	
}