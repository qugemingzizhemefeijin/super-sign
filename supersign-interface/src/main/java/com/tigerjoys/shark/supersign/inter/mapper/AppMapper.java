package com.tigerjoys.shark.supersign.inter.mapper;

import org.apache.ibatis.annotations.Producer;
import com.tigerjoys.nbs.mybatis.core.provider.DefaultSqlProvider;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.nbs.mybatis.core.BaseMapper;
import com.tigerjoys.nbs.mybatis.core.annotation.Mapper;

/**
 * 数据库  应用信息表[t_app]表 dao通用操作接口实现类
 * @Date 2020-03-25 22:18:36
 *
 */
@Producer(entityType=AppEntity.class,providerType=DefaultSqlProvider.class)
@Mapper
public interface AppMapper extends BaseMapper<AppEntity> {
    
}