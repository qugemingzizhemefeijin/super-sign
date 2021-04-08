package com.tigerjoys.shark.supersign.inter.mapper;

import org.apache.ibatis.annotations.Producer;
import com.tigerjoys.nbs.mybatis.core.provider.DefaultSqlProvider;
import com.tigerjoys.shark.supersign.inter.entity.UdidSuccessEntity;
import com.tigerjoys.nbs.mybatis.core.BaseMapper;
import com.tigerjoys.nbs.mybatis.core.annotation.Mapper;

/**
 * 数据库  UDID安装成功信息[t_udid_success]表 dao通用操作接口实现类
 * @Date 2020-03-26 00:08:54
 *
 */
@Producer(entityType=UdidSuccessEntity.class,providerType=DefaultSqlProvider.class)
@Mapper
public interface UdidSuccessMapper extends BaseMapper<UdidSuccessEntity> {
    
}