package com.tigerjoys.shark.supersign.inter.contract;

import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;
import com.tigerjoys.nbs.mybatis.core.BaseContract;

/**
 * 数据库中  安装日志[t_install_log]表 接口类
 * @Date 2020-03-26 00:08:53
 *
 */
public interface IInstallLogContract extends BaseContract<InstallLogEntity> {
	
	/**
	 * 获取当前udid正在安装的对象
	 * @param udid - UDID
	 * @return InstallLogEntity
	 * @throws Exception
	 */
	public InstallLogEntity findByInstalling(String udid) throws Exception;
	
}
