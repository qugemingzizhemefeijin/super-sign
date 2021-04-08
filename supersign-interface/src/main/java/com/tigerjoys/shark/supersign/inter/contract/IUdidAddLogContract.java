package com.tigerjoys.shark.supersign.inter.contract;

import com.tigerjoys.nbs.mybatis.core.BaseContract;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;

/**
 * 数据库中  UDID更新日志[t_udid_add_log]表 接口类
 * @Date 2020-03-26 15:39:15
 *
 */
public interface IUdidAddLogContract extends BaseContract<UdidAddLogEntity> {
	
	/**
	 * 获得此UDID安装在哪个APP上
	 * @param udid - UDID
	 * @param appId - APPID
	 * @return UdidSuccessEntity
	 * @throws Exception
	 */
	public UdidAddLogEntity findByUdidAndAppId(String udid, long appId) throws Exception;
	
}
