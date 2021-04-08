package com.tigerjoys.shark.supersign.inter.mapper;

import org.apache.ibatis.annotations.Producer;
import com.tigerjoys.nbs.mybatis.core.provider.DefaultSqlProvider;
import com.tigerjoys.shark.supersign.inter.entity.AdminMenuEntity;
import com.tigerjoys.nbs.mybatis.core.BaseMapper;
import com.tigerjoys.nbs.mybatis.core.annotation.Mapper;

/**
 * 数据库  后台按钮表[t_admin_menu]表 dao通用操作接口实现类
 * @Date 2020-04-01 14:31:06
 *
 */
@Producer(entityType=AdminMenuEntity.class,providerType=DefaultSqlProvider.class)
@Mapper
public interface AdminMenuMapper extends BaseMapper<AdminMenuEntity> {
    
}