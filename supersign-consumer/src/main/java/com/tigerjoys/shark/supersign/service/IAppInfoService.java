package com.tigerjoys.shark.supersign.service;

/**
 * APP INFO相关的安装接口
 *
 */
public interface IAppInfoService {
	
	/**
	 * 消费指定APP的安装数量
	 * @param appId - APPID
	 * @param udid - UDID
	 * @throws Exception
	 */
	public void consume(long appId, String udid) throws Exception;

}
