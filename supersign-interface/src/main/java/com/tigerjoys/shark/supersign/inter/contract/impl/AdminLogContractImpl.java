package com.tigerjoys.shark.supersign.inter.contract.impl;

import org.springframework.stereotype.Repository;

import com.tigerjoys.shark.supersign.inter.contract.IAdminLogContract;
import com.tigerjoys.shark.supersign.inter.entity.AdminLogEntity;
import com.tigerjoys.nbs.mybatis.core.contract.AbstractBaseContract;
import com.tigerjoys.shark.supersign.inter.mapper.AdminLogMapper;

/**
 * 数据库中  后台管理员日志[t_admin_log]表 接口实现类
 * @Date 2020-04-01 14:31:06
 *
 */
@Repository
public class AdminLogContractImpl extends AbstractBaseContract<AdminLogEntity , AdminLogMapper> implements IAdminLogContract {
	
}
