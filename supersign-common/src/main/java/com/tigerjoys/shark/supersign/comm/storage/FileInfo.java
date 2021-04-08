package com.tigerjoys.shark.supersign.comm.storage;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件信息
 *
 */
public class FileInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6735082160649993282L;
	
	/**
	 * 文件路径
	 */
	private String path;
	
	/**
	 * 文件名
	 */
    private String name;
    
    /**
     * 扩展名
     */
    private String ext;

    /**
     * 文件类型 {file, folder}
     */
    private String type;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件创建日期
     */
    private Date createDate;
	
	/**
	 * 文件内容类型
	 */
	private String contentType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FileInfo [path=");
		builder.append(path);
		builder.append(", name=");
		builder.append(name);
		builder.append(", ext=");
		builder.append(ext);
		builder.append(", type=");
		builder.append(type);
		builder.append(", size=");
		builder.append(size);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", contentType=");
		builder.append(contentType);
		builder.append("]");
		return builder.toString();
	}

}
