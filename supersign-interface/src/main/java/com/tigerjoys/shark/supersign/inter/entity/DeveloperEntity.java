package com.tigerjoys.shark.supersign.inter.entity;

import java.io.Serializable;
import java.util.Date;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.annotations.Column;
import org.apache.ibatis.annotations.Id;
import org.apache.ibatis.annotations.Table;
import com.tigerjoys.nbs.mybatis.core.BaseEntity;

/**
 * 数据库中  开发者信息[t_developer] 表对应的实体类
 * @Date 2020-03-26 00:41:01
 *
 */
@Table(name="t_developer")
public class DeveloperEntity extends BaseEntity implements Serializable {

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
	 * 绑定应用ID
	 */
	@Column(name="app_info_id",nullable=false,jdbcType=JdbcType.BIGINT,comment="绑定应用ID")
	private Long app_info_id;
	
	/**
	 * 帐号
	 */
	@Column(name="username",nullable=false,jdbcType=JdbcType.VARCHAR,comment="帐号")
	private String username;
	
	/**
	 * 密码
	 */
	@Column(name="password",nullable=false,jdbcType=JdbcType.VARCHAR,comment="密码")
	private String password;
	
	/**
	 * 装机数量上限
	 */
	@Column(name="real_limit",nullable=false,jdbcType=JdbcType.INTEGER,comment="装机数量上限")
	private Integer real_limit;
	
	/**
	 * 虚拟可用数量
	 */
	@Column(name="virtual_limit",nullable=false,jdbcType=JdbcType.INTEGER,comment="虚拟可用数量")
	private Integer virtual_limit;
	
	/**
	 * 实际已用数量
	 */
	@Column(name="install_limit",nullable=false,jdbcType=JdbcType.INTEGER,comment="实际已用数量")
	private Integer install_limit;
	
	/**
	 * 登录状态：0未登录,1登录成功,2失败
	 */
	@Column(name="checked",nullable=false,jdbcType=JdbcType.TINYINT,comment="登录状态")
	private Integer checked;
	
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
	
	public Long getApp_info_id() {
		return app_info_id;
	}

	public void setApp_info_id(Long app_info_id) {
		this.app_info_id = app_info_id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Integer getReal_limit() {
		return real_limit;
	}

	public void setReal_limit(Integer real_limit) {
		this.real_limit = real_limit;
	}
	
	public Integer getVirtual_limit() {
		return virtual_limit;
	}

	public void setVirtual_limit(Integer virtual_limit) {
		this.virtual_limit = virtual_limit;
	}
	
	public Integer getInstall_limit() {
		return install_limit;
	}

	public void setInstall_limit(Integer install_limit) {
		this.install_limit = install_limit;
	}

	public Integer getChecked() {
		return checked;
	}

	public void setChecked(Integer checked) {
		this.checked = checked;
	}

}