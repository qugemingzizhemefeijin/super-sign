package com.tigerjoys.shark.supersign.inter.contract.impl;

import org.springframework.stereotype.Repository;

import com.tigerjoys.nbs.mybatis.core.contract.LRUCacheContract;
import com.tigerjoys.shark.supersign.inter.contract.IAdminMenuContract;
import com.tigerjoys.shark.supersign.inter.entity.AdminMenuEntity;
import com.tigerjoys.shark.supersign.inter.mapper.AdminMenuMapper;

/**
 * 数据库中  后台按钮表[t_admin_menu]表 接口实现类
 * @Date 2020-04-01 14:31:06
 *
 */
@Repository
public class AdminMenuContractImpl extends LRUCacheContract<AdminMenuEntity , AdminMenuMapper> implements IAdminMenuContract {
	
}
