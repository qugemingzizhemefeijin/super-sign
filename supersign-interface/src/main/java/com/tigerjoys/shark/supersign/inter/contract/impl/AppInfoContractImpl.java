package com.tigerjoys.shark.supersign.inter.contract.impl;

import org.springframework.stereotype.Repository;

import com.tigerjoys.shark.supersign.inter.contract.IAppInfoContract;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.nbs.mybatis.core.contract.AbstractBaseContract;
import com.tigerjoys.shark.supersign.inter.mapper.AppInfoMapper;

/**
 * 数据库中  APP子应用信息表[t_app_info]表 接口实现类
 * @Date 2020-03-26 00:08:52
 *
 */
@Repository
public class AppInfoContractImpl extends AbstractBaseContract<AppInfoEntity , AppInfoMapper> implements IAppInfoContract {
	
	@Override
	public AppInfoEntity findByBundleId(String bundleId) throws Exception {
		return mapper.findByProperty("bundle_id", bundleId);
	}
	
}
