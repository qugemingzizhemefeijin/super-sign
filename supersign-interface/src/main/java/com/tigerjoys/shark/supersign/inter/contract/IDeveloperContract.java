package com.tigerjoys.shark.supersign.inter.contract;

import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.nbs.mybatis.core.BaseContract;

/**
 * 数据库中  开发者信息[t_developer]表 接口类
 * @Date 2020-03-25 22:18:38
 *
 */
public interface IDeveloperContract extends BaseContract<DeveloperEntity> {
	
	/**
	 * 根据用户名获取对象
	 * @param username - 用户名
	 * @return DeveloperEntity
	 * @throws Exception
	 */
	public DeveloperEntity findByUsername(String username) throws Exception;
	
	/**
	 * 根据appInfoId获取对象
	 * @param appInfoId - APPINFOID
	 * @return DeveloperEntity
	 * @throws Exception
	 */
	public DeveloperEntity findByAppInfoId(long appInfoId) throws Exception;
	
	/**
	 * 减少指定数量的虚拟可用安装量
	 * @param id - ID
	 * @param num - 减少数量
	 * @throws Exception
	 */
	public void updateVirtualLimit(long id, int num) throws Exception;
	
}
