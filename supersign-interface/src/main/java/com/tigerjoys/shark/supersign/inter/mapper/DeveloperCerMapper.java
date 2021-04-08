package com.tigerjoys.shark.supersign.inter.mapper;

import org.apache.ibatis.annotations.Producer;
import com.tigerjoys.nbs.mybatis.core.provider.DefaultSqlProvider;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperCerEntity;
import com.tigerjoys.nbs.mybatis.core.BaseMapper;
import com.tigerjoys.nbs.mybatis.core.annotation.Mapper;

/**
 * 数据库  开发者证书[t_developer_cer]表 dao通用操作接口实现类
 * @Date 2020-03-27 21:33:22
 *
 */
@Producer(entityType=DeveloperCerEntity.class,providerType=DefaultSqlProvider.class)
@Mapper
public interface DeveloperCerMapper extends BaseMapper<DeveloperCerEntity> {
    
}