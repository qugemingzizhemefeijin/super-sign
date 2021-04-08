package com.tigerjoys.shark.supersign.inter.entity;

import java.io.Serializable;
import java.util.Date;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.annotations.Column;
import org.apache.ibatis.annotations.Id;
import org.apache.ibatis.annotations.Table;
import com.tigerjoys.nbs.mybatis.core.BaseEntity;

/**
 * 数据库中  开发者证书[t_developer_cer] 表对应的实体类
 * @Date 2020-03-27 21:33:22
 *
 */
@Table(name="t_developer_cer")
public class DeveloperCerEntity extends BaseEntity implements Serializable {

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
	 * 开发者ID
	 */
	@Column(name="developer_id",nullable=false,jdbcType=JdbcType.BIGINT,comment="开发者ID")
	private Long developer_id;
	
	/**
	 * 苹果证书ID
	 */
	@Column(name="certificate_id",nullable=false,jdbcType=JdbcType.VARCHAR,comment="苹果证书ID")
	private String certificate_id;
	
	/**
	 * 公钥文件地址
	 */
	@Column(name="public_pem_path",nullable=false,jdbcType=JdbcType.VARCHAR,comment="公钥文件地址")
	private String public_pem_path;
	
	/**
	 * 私钥文件地址
	 */
	@Column(name="private_pem_path",nullable=false,jdbcType=JdbcType.VARCHAR,comment="私钥文件地址")
	private String private_pem_path;
	
	/**
	 * 证书结束时间
	 */
	@Column(name="expire_time",nullable=true,jdbcType=JdbcType.TIMESTAMP,comment="证书结束时间")
	private Date expire_time;
	
	/**
	 * 0禁用,1.正常
	 */
	@Column(name="status",nullable=false,jdbcType=JdbcType.TINYINT,comment="0禁用,1.正常")
	private Integer status;
	
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
	
	public Long getDeveloper_id() {
		return developer_id;
	}

	public void setDeveloper_id(Long developer_id) {
		this.developer_id = developer_id;
	}
	
	public String getCertificate_id() {
		return certificate_id;
	}

	public void setCertificate_id(String certificate_id) {
		this.certificate_id = certificate_id;
	}

	public String getPublic_pem_path() {
		return public_pem_path;
	}

	public void setPublic_pem_path(String public_pem_path) {
		this.public_pem_path = public_pem_path;
	}
	
	public String getPrivate_pem_path() {
		return private_pem_path;
	}

	public void setPrivate_pem_path(String private_pem_path) {
		this.private_pem_path = private_pem_path;
	}
	
	public Date getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(Date expire_time) {
		this.expire_time = expire_time;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}