package com.tigerjoys.shark.supersign.inter.contract.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tigerjoys.nbs.mybatis.core.contract.LRUCacheContract;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.shark.supersign.inter.contract.IAdminRoleMenuContract;
import com.tigerjoys.shark.supersign.inter.entity.AdminRoleMenuEntity;
import com.tigerjoys.shark.supersign.inter.mapper.AdminRoleMenuMapper;

/**
 * 数据库中  角色跟按钮的关联表[t_admin_role_menu]表 接口实现类
 * @Date 2020-04-01 14:31:07
 *
 */
@Repository
public class AdminRoleMenuContractImpl extends LRUCacheContract<AdminRoleMenuEntity , AdminRoleMenuMapper> implements IAdminRoleMenuContract {
	
	@Override
	public AdminRoleMenuEntity getAdminRoleMenuEntity(long roleId, long menuId) throws Exception {
		PageModel pageModel = PageModel.getPageModel();
		pageModel.addQuery(Restrictions.eq("roleId", roleId));
		pageModel.addQuery(Restrictions.eq("menuId", menuId));
		
		List<AdminRoleMenuEntity> list = load(pageModel);
		
		if(list == null || list.isEmpty()) return null;
		return list.get(0);
	}
	
}
