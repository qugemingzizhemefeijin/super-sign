package com.tigerjoys.shark.supersign.inter.contract.impl;

import org.springframework.stereotype.Repository;

import com.tigerjoys.nbs.mybatis.core.contract.LRUCacheContract;
import com.tigerjoys.shark.supersign.inter.contract.IAdminContract;
import com.tigerjoys.shark.supersign.inter.entity.AdminEntity;
import com.tigerjoys.shark.supersign.inter.mapper.AdminMapper;

/**
 * 数据库中  管理员表[t_admin]表 接口实现类
 * @Date 2020-04-01 14:31:05
 *
 */
@Repository
public class AdminContractImpl extends LRUCacheContract<AdminEntity , AdminMapper> implements IAdminContract {
	
}
