package com.tigerjoys.shark.supersign.comm.storage;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import com.tigerjoys.nbs.common.enums.ECharset;

/**
 * 抽象的云存储
 *
 */
public abstract class AbstractCloudStorage implements ICloudStorage , DisposableBean {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 空间名称
	 */
	protected final String bucketName;
	
	/**
	 * 操作员名称
	 */
	protected final String userName;
	
	/**
	 * 密码
	 */
	protected final String password;
	
	/**
	 * 传入参数构造云存储对象
	 * @param bucketName - 空间名称
	 * @param userName - 操作员名称
	 * @param password - 密码
	 */
	public AbstractCloudStorage(String bucketName, String userName, String password) {
		this.bucketName = bucketName;
		this.userName = userName;
		this.password = password;
	}
	
	/**
	 * 将字符串转换为输入流
	 * @param datas - String
	 * @return ByteArrayInputStream
	 */
	protected ByteArrayInputStream toInputStream(String datas) {
		return new ByteArrayInputStream(datas.getBytes(ECharset.UTF_8.getCharset()));
	}
	
	/**
	 * 将byte数组转换为输入流
	 * @param datas - byte[]
	 * @return ByteArrayInputStream
	 */
	protected ByteArrayInputStream toInputStream(byte[] datas) {
		return new ByteArrayInputStream(datas);
	}
	
	@Override
	public boolean writeDir(String rootFilePath , String dirPath) throws Exception {
		return this.writeDir(rootFilePath, new File(dirPath));
	}
	
	/**
	 * 递归上传文件
	 * @param rootFilePath - 远程根路径
	 * @param file - 文件或目录
	 * @param rootPath - 本地根路径
	 * @return boolean
	 * @throws Exception
	 */
	protected boolean writeDir(String rootFilePath , File file , String rootPath) throws Exception {
		if(rootFilePath.charAt(rootFilePath.length()-1) == '/') {
			rootFilePath = rootFilePath.substring(0, rootFilePath.length() - 1);
		}
		
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			if(files != null && files.length > 0) {
				for(int i=0,size=files.length;i<size;i++) {
					this.writeDir(rootFilePath, files[i] , rootPath);
				}
			}
		} else {
			String relativePath = rootFilePath+file.getAbsolutePath().replace(rootPath, "").replace("\\", "/");
			this.writeFile(relativePath, file, true);
		}
		return true;
	}
	
	/**
	 * 获得文件的扩展名，顺便也检查了上传路径是否为空
	 * @param filePath - 上传的文件路径
	 * @return String
	 * @throws NullPointerException
	 */
	protected String getExt(String filePath) {
		if(filePath == null || filePath.length() == 0) {
			throw new NullPointerException("filePath is null");
		}
		
		int index = filePath.lastIndexOf(".");
		if(index == -1) {
			return null;
		} else {
			return filePath.substring(index+1);
		}
	}

}
