package com.tigerjoys.shark.supersign.inter.mapper;

import org.apache.ibatis.annotations.Producer;
import com.tigerjoys.nbs.mybatis.core.provider.DefaultSqlProvider;
import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;
import com.tigerjoys.nbs.mybatis.core.BaseMapper;
import com.tigerjoys.nbs.mybatis.core.annotation.Mapper;

/**
 * 数据库  安装日志[t_install_log]表 dao通用操作接口实现类
 * @Date 2020-03-26 00:08:53
 *
 */
@Producer(entityType=InstallLogEntity.class,providerType=DefaultSqlProvider.class)
@Mapper
public interface InstallLogMapper extends BaseMapper<InstallLogEntity> {
    
}