package com.tigerjoys.shark.supersign.inter.entity;

import java.io.Serializable;
import java.util.Date;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.annotations.Column;
import org.apache.ibatis.annotations.Id;
import org.apache.ibatis.annotations.Table;
import com.tigerjoys.nbs.mybatis.core.BaseEntity;

/**
 * 数据库中  APP子应用信息表[t_app_info] 表对应的实体类
 * @Date 2020-03-26 01:53:39
 *
 */
@Table(name="t_app_info")
public class AppInfoEntity extends BaseEntity implements Serializable {

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
	 * 应用名称
	 */
	@Column(name="app_name",nullable=false,jdbcType=JdbcType.VARCHAR,comment="应用名称")
	private String app_name;
	
	/**
	 * 应用bundle Id
	 */
	@Column(name="bundle_id",nullable=false,jdbcType=JdbcType.VARCHAR,comment="应用bundle Id")
	private String bundle_id;
	
	/**
	 * 版本号
	 */
	@Column(name="version",nullable=false,jdbcType=JdbcType.VARCHAR,comment="版本号")
	private String version;
	
	/**
	 * 版本编码
	 */
	@Column(name="version_code",nullable=false,jdbcType=JdbcType.VARCHAR,comment="版本编码")
	private String version_code;
	
	/**
	 * IPA路径
	 */
	@Column(name="path",nullable=false,jdbcType=JdbcType.VARCHAR,comment="IPA路径")
	private String path;
	
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
	 * 重签名后的ipa地址
	 */
	@Column(name="resign_ipa_path",nullable=true,jdbcType=JdbcType.VARCHAR,comment="重签名后的ipa地址")
	private String resign_ipa_path;
	
	/**
	 * 重签名后的plist地址
	 */
	@Column(name="resign_plist_path",nullable=true,jdbcType=JdbcType.VARCHAR,comment="重签名后的plist地址")
	private String resign_plist_path;
	
	/**
	 * 重签名后的mobileprovision地址
	 */
	@Column(name="resign_mp_path",nullable=true,jdbcType=JdbcType.VARCHAR,comment="重签名后的mobileprovision地址")
	private String resign_mp_path;
	
	/**
	 * 用户状态 0：禁用； 1：正常 
	 */
	@Column(name="status",nullable=false,jdbcType=JdbcType.TINYINT,comment="用户状态 0：禁用； 1：正常 ")
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
	
	public Long getApp_id() {
		return app_id;
	}

	public void setApp_id(Long app_id) {
		this.app_id = app_id;
	}
	
	public String getBundle_id() {
		return bundle_id;
	}

	public void setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getVersion_code() {
		return version_code;
	}

	public void setVersion_code(String version_code) {
		this.version_code = version_code;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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
	
	public String getResign_ipa_path() {
		return resign_ipa_path;
	}

	public void setResign_ipa_path(String resign_ipa_path) {
		this.resign_ipa_path = resign_ipa_path;
	}
	
	public String getResign_plist_path() {
		return resign_plist_path;
	}

	public void setResign_plist_path(String resign_plist_path) {
		this.resign_plist_path = resign_plist_path;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getResign_mp_path() {
		return resign_mp_path;
	}

	public void setResign_mp_path(String resign_mp_path) {
		this.resign_mp_path = resign_mp_path;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	
}