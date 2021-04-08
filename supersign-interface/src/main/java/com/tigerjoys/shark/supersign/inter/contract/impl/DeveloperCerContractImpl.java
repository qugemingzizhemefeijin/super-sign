package com.tigerjoys.shark.supersign.inter.contract.impl;

import org.springframework.stereotype.Repository;

import com.tigerjoys.shark.supersign.inter.contract.IDeveloperCerContract;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperCerEntity;
import com.tigerjoys.nbs.mybatis.core.contract.AbstractBaseContract;
import com.tigerjoys.shark.supersign.inter.mapper.DeveloperCerMapper;

/**
 * 数据库中  开发者证书[t_developer_cer]表 接口实现类
 * @Date 2020-03-27 21:33:22
 *
 */
@Repository
public class DeveloperCerContractImpl extends AbstractBaseContract<DeveloperCerEntity , DeveloperCerMapper> implements IDeveloperCerContract {
	
}
