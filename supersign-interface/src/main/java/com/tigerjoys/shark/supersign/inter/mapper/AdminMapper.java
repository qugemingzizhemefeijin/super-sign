package com.tigerjoys.shark.supersign.inter.mapper;

import org.apache.ibatis.annotations.Producer;
import com.tigerjoys.nbs.mybatis.core.provider.DefaultSqlProvider;
import com.tigerjoys.shark.supersign.inter.entity.AdminEntity;
import com.tigerjoys.nbs.mybatis.core.BaseMapper;
import com.tigerjoys.nbs.mybatis.core.annotation.Mapper;

/**
 * 数据库  管理员表[t_admin]表 dao通用操作接口实现类
 * @Date 2020-04-01 14:31:05
 *
 */
@Producer(entityType=AdminEntity.class,providerType=DefaultSqlProvider.class)
@Mapper
public interface AdminMapper extends BaseMapper<AdminEntity> {
    
}