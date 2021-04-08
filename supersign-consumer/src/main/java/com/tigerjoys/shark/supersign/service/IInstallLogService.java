package com.tigerjoys.shark.supersign.service;

import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;

/**
 * 安装日志相关的接口
 *
 */
public interface IInstallLogService {
	
	/**
	 * 安装失败
	 * @param id - ID
	 * @param error - 失败原因
	 * @throws Exception
	 */
	public void installFailure(long id, String error) throws Exception;
	
	/**
	 * 安装成功
	 * @param id - ID
	 * @throws Exception
	 */
	public void installSuccess(long id) throws Exception;
	
	/**
	 * 记录安装日志
	 * @param appId - APPID
	 * @param appInfoId - APP INFO ID
	 * @param developerId - 开发者ID
	 * @param cerId - 证书ID
	 * @param udid - UDID
	 * @return InstallLogEntity
	 * @throws Exception
	 */
	public InstallLogEntity createInstallLog(long appId, long appInfoId, long developerId, long cerId, String udid) throws Exception;

}
