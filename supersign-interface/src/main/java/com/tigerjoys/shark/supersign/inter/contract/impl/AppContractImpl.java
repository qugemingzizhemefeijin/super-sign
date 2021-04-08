package com.tigerjoys.shark.supersign.inter.contract.impl;

import org.springframework.stereotype.Repository;

import com.tigerjoys.shark.supersign.inter.contract.IAppContract;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.nbs.mybatis.core.contract.AbstractBaseContract;
import com.tigerjoys.shark.supersign.inter.mapper.AppMapper;

/**
 * 数据库中  应用信息表[t_app]表 接口实现类
 * @Date 2020-03-25 22:18:36
 *
 */
@Repository
public class AppContractImpl extends AbstractBaseContract<AppEntity , AppMapper> implements IAppContract {
	
}
