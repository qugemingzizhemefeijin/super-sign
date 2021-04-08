package com.tigerjoys.shark.supersign.inter.mapper;

import org.apache.ibatis.annotations.Producer;
import com.tigerjoys.nbs.mybatis.core.provider.DefaultSqlProvider;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;
import com.tigerjoys.nbs.mybatis.core.BaseMapper;
import com.tigerjoys.nbs.mybatis.core.annotation.Mapper;

/**
 * 数据库  UDID更新日志[t_udid_add_log]表 dao通用操作接口实现类
 * @Date 2020-03-26 15:39:15
 *
 */
@Producer(entityType=UdidAddLogEntity.class,providerType=DefaultSqlProvider.class)
@Mapper
public interface UdidAddLogMapper extends BaseMapper<UdidAddLogEntity> {
    
}