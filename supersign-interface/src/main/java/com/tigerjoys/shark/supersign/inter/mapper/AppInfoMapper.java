package com.tigerjoys.shark.supersign.inter.mapper;

import org.apache.ibatis.annotations.Producer;
import com.tigerjoys.nbs.mybatis.core.provider.DefaultSqlProvider;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.nbs.mybatis.core.BaseMapper;
import com.tigerjoys.nbs.mybatis.core.annotation.Mapper;

/**
 * 数据库  APP子应用信息表[t_app_info]表 dao通用操作接口实现类
 * @Date 2020-03-26 00:08:52
 *
 */
@Producer(entityType=AppInfoEntity.class,providerType=DefaultSqlProvider.class)
@Mapper
public interface AppInfoMapper extends BaseMapper<AppInfoEntity> {
    
}