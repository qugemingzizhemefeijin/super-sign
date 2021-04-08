package com.tigerjoys.shark.supersign.inter.entity;

import java.io.Serializable;
import java.util.Date;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.annotations.Column;
import org.apache.ibatis.annotations.Id;
import org.apache.ibatis.annotations.Table;
import com.tigerjoys.nbs.mybatis.core.BaseEntity;

/**
 * 数据库中  管理员表[t_admin] 表对应的实体类
 * @Date 2020-04-01 14:31:05
 *
 */
@Table(name="t_admin")
public class AdminEntity extends BaseEntity implements Serializable {

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
	 * 用户
	 */
	@Column(name="username",nullable=false,jdbcType=JdbcType.VARCHAR,comment="用户")
	private String username;
	
	/**
	 * 密码
	 */
	@Column(name="password",nullable=false,jdbcType=JdbcType.CHAR,comment="密码")
	private String password;
	
	/**
	 * 权限ID
	 */
	@Column(name="roleId",nullable=false,jdbcType=JdbcType.BIGINT,comment="权限ID")
	private Long roleId;
	
	/**
	 * 真实姓名
	 */
	@Column(name="realname",nullable=false,jdbcType=JdbcType.VARCHAR,comment="真实姓名")
	private String realname;
	
	/**
	 * 邮箱
	 */
	@Column(name="email",nullable=false,jdbcType=JdbcType.VARCHAR,comment="邮箱")
	private String email;
	
	/**
	 * 联系方式
	 */
	@Column(name="mobile",nullable=false,jdbcType=JdbcType.VARCHAR,comment="联系方式")
	private String mobile;
	
	/**
	 * 头像
	 */
	@Column(name="avatar",nullable=true,jdbcType=JdbcType.VARCHAR,comment="头像")
	private String avatar;
	
	/**
	 * 最后登录时间
	 */
	@Column(name="lastDate",nullable=true,jdbcType=JdbcType.TIMESTAMP,comment="最后登录时间")
	private Date lastDate;
	
	/**
	 * 最后登录IP
	 */
	@Column(name="lastip",nullable=true,jdbcType=JdbcType.VARCHAR,comment="最后登录IP")
	private String lastip;
	
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
	
	/**
	 * 管理员扩展参数
	 */
	@Column(name="params",nullable=true,jdbcType=JdbcType.LONGVARCHAR,comment="管理员扩展参数")
	private String params;
	
	/**
	 * 关联的广告主ID
	 */
	@Column(name="advertiser_id",nullable=false,jdbcType=JdbcType.BIGINT,comment="关联的广告主ID")
	private Long advertiser_id;
	
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
	
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	
	public String getLastip() {
		return lastip;
	}

	public void setLastip(String lastip) {
		this.lastip = lastip;
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
	
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
	public Long getAdvertiser_id() {
		return advertiser_id;
	}

	public void setAdvertiser_id(Long advertiser_id) {
		this.advertiser_id = advertiser_id;
	}
	
}