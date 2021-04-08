package com.tigerjoys.shark.supersign.inter.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Producer;
import org.apache.ibatis.annotations.Update;

import com.tigerjoys.nbs.mybatis.core.BaseMapper;
import com.tigerjoys.nbs.mybatis.core.annotation.Mapper;
import com.tigerjoys.nbs.mybatis.core.provider.DefaultSqlProvider;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;

/**
 * 数据库  开发者信息[t_developer]表 dao通用操作接口实现类
 * @Date 2020-03-25 22:18:38
 *
 */
@Producer(entityType=DeveloperEntity.class,providerType=DefaultSqlProvider.class)
@Mapper
public interface DeveloperMapper extends BaseMapper<DeveloperEntity> {
	
	/**
	 * 原子减少虚拟的可用安装量
	 * @param id - 开发者帐号ID
	 * @param num - 减少的数量
	 */
	@Update("update t_developer set virtual_limit=virtual_limit - #{num} where id=#{id}")
	public void updateVirtualLimit(@Param("id")long id, @Param("num")int num);
    
}