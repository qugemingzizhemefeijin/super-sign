package com.tigerjoys.shark.supersign.comm.storage;

import java.io.Serializable;
import java.util.Date;

/**
 * Bucket的信息
 *
 */
public class BucketInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3832903903871894662L;
	
	/**
	 * Bucket的名称
	 */
	private final String name;
	
	/**
	 * 创建日期
	 */
	private final Date createDate;
	
	public BucketInfo(String name , Date createDate) {
		this.name = name;
		this.createDate = createDate;
	}

	public String getName() {
		return name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BucketInfo [name=");
		builder.append(name);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append("]");
		return builder.toString();
	}

}
