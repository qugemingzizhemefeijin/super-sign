package com.tigerjoys.shark.supersign.inter.contract.impl;

import org.springframework.stereotype.Repository;

import com.tigerjoys.nbs.mybatis.core.contract.LRUCacheContract;
import com.tigerjoys.shark.supersign.inter.contract.IAdminRoleContract;
import com.tigerjoys.shark.supersign.inter.entity.AdminRoleEntity;
import com.tigerjoys.shark.supersign.inter.mapper.AdminRoleMapper;

/**
 * 数据库中  后台角色表[t_admin_role]表 接口实现类
 * @Date 2020-04-01 14:31:07
 *
 */
@Repository
public class AdminRoleContractImpl extends LRUCacheContract<AdminRoleEntity , AdminRoleMapper> implements IAdminRoleContract {
	
}
