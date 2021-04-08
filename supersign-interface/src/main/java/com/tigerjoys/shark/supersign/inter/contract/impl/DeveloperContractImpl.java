package com.tigerjoys.shark.supersign.inter.contract.impl;

import org.springframework.stereotype.Repository;

import com.tigerjoys.shark.supersign.inter.contract.IDeveloperContract;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.nbs.mybatis.core.contract.AbstractBaseContract;
import com.tigerjoys.shark.supersign.inter.mapper.DeveloperMapper;

/**
 * 数据库中  开发者信息[t_developer]表 接口实现类
 * @Date 2020-03-25 22:18:38
 *
 */
@Repository
public class DeveloperContractImpl extends AbstractBaseContract<DeveloperEntity , DeveloperMapper> implements IDeveloperContract {

	@Override
	public DeveloperEntity findByUsername(String username) throws Exception {
		return mapper.findByProperty("username", username);
	}

	@Override
	public DeveloperEntity findByAppInfoId(long appInfoId) throws Exception {
		return mapper.findByProperty("appInfoId", appInfoId);
	}
	
	@Override
	public void updateVirtualLimit(long id, int num) throws Exception {
		mapper.updateVirtualLimit(id, num);
	}
	
}
