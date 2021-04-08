package com.tigerjoys.shark.supersign.inter.contract;

import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.nbs.mybatis.core.BaseContract;

/**
 * 数据库中  APP子应用信息表[t_app_info]表 接口类
 * @Date 2020-03-26 00:08:52
 *
 */
public interface IAppInfoContract extends BaseContract<AppInfoEntity> {
	
	/**
	 * 根据bundleId获取对象
	 * @param bundleId - String
	 * @return AppInfoEntity
	 * @throws Exception
	 */
	public AppInfoEntity findByBundleId(String bundleId) throws Exception;
	
}
