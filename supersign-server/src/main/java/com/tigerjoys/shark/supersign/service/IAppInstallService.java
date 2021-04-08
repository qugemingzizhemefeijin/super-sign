package com.tigerjoys.shark.supersign.service;

import com.tigerjoys.shark.supersign.comm.sign.SignProcess;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;

/**
 * APP安装服务接口
 *
 */
public interface IAppInstallService {
	
	/**
	 * 是否在重签队列中 
	 * @param appId - APPID
	 * @param udid - UDID
	 * @return boolean
	 * @throws Exception
	 */
	public boolean inTranscocdQueue(long appId, String udid) throws Exception;
	
	/**
	 * 分配一个安装者
	 * @param appId - APPID
	 * @param udid - UDID
	 * @return DeveloperEntity
	 * @throws Exception
	 */
	public DeveloperEntity consume(long appId, String udid) throws Exception;
	
	/**
	 * 发送一个异步签名任务
	 * @param appId - APPID
	 * @param udid - UDID
	 * @throws Exception
	 */
	public void sendJob(long appId, String udid) throws Exception;
	
	/**
	 * 获取udid在安装队列中的进度信息
	 * @param appId - APPID
	 * @param udid - UDID
	 * @return SignProcess
	 * @throws Exception
	 */
	public SignProcess getSignProcess(long appId, String udid) throws Exception;

}
