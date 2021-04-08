package com.tigerjoys.shark.supersign.service;

import java.io.FileNotFoundException;

/**
 * APP模版文件服务接口
 *
 */
public interface IAppModelFileService {
	
	/**
	 * 读取模版文件内容
	 * @param appInfoId - APP INFO ID
	 * @return String
	 * @throws FileNotFoundException
	 */
	public String getPListModelFile(long appInfoId) throws FileNotFoundException;

}
