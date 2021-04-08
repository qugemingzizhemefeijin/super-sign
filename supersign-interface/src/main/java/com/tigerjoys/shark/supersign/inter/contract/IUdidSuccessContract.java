package com.tigerjoys.shark.supersign.inter.contract;

import com.tigerjoys.shark.supersign.inter.entity.UdidSuccessEntity;
import com.tigerjoys.nbs.mybatis.core.BaseContract;

/**
 * 数据库中  UDID安装成功信息[t_udid_success]表 接口类
 * @Date 2020-03-26 00:08:54
 *
 */
public interface IUdidSuccessContract extends BaseContract<UdidSuccessEntity> {
	
	/**
	 * 获得安装成功的信息
	 * @param udid - UDID
	 * @param appId - APPID
	 * @return UdidSuccessEntity
	 * @throws Exception
	 */
	public UdidSuccessEntity findByUdidAndAppId(String udid, long appId) throws Exception;
	
}
