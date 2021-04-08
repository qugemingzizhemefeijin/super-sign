package com.tigerjoys.shark.supersign.inter.contract;

import com.tigerjoys.shark.supersign.inter.entity.AdminRoleMenuEntity;
import com.tigerjoys.nbs.mybatis.core.BaseContract;

/**
 * 数据库中  角色跟按钮的关联表[t_admin_role_menu]表 接口类
 * @Date 2020-04-01 14:31:07
 *
 */
public interface IAdminRoleMenuContract extends BaseContract<AdminRoleMenuEntity> {
	
	/**
	 * 根据角色ID和按钮ID获得实例
	 * @param roleId - 角色ID
	 * @param menuId - 按钮ID
	 * @return AdminRoleMenuEntity
	 * @throws Exception
	 */
	public AdminRoleMenuEntity getAdminRoleMenuEntity(long roleId , long menuId) throws Exception;
	
}
