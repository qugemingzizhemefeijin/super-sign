package com.tigerjoys.shark.supersign.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tigerjoys.shark.supersign.dto.list.AdminDto;
import com.tigerjoys.shark.supersign.dto.list.AdminMenuDto;
import com.tigerjoys.shark.supersign.dto.list.AdminRoleDto;
import com.tigerjoys.shark.supersign.dto.list.ShowMenuDto;
import com.tigerjoys.shark.supersign.enums.LogTypeEnum;
import com.tigerjoys.shark.supersign.inter.entity.AdminEntity;
import com.tigerjoys.shark.supersign.inter.entity.AdminMenuEntity;
import com.tigerjoys.shark.supersign.inter.entity.AdminRoleEntity;

/**
 * 登录相关服务接口
 *
 */
public interface IAdminService {
	
	/**
	 * 登录操作
	 * @param username - 登录用户名
	 * @param password - 登录密码
	 * @return int 返回码
	 * @throws Exception
	 */
	public int login(String username , String password) throws Exception;
	
	/**
	 * 记录管理员日志
	 * @param adminId - 管理员ID
	 * @param type - @see com.tigerjoys.shark.miai.app.enums.LogTypeEnum
	 * @param remark - 备注
	 */
	public void addAdminLog(long adminId , LogTypeEnum type);
	
	/**
	 * 查封或者解封用户
	 * @param adminId - 管理员ID
	 * @param s - 1解封,0查封
	 * @throws Exception
	 */
	public void chageAdminStatus(long adminId , int s) throws Exception;
	
	/**
	 * 中转AdminEntity
	 * @param user - AdminEntity
	 * @return AdminDto
	 * @throws Exception
	 */
	public AdminDto parseUser(AdminEntity user) throws Exception;
	
	/**
	 * 中转AdminRoleEntity
	 * @param role - AdminRoleEntity
	 * @return AdminRoleDto
	 * @throws Exception
	 */
	public AdminRoleDto parseRole(AdminRoleEntity role) throws Exception;
	
	/**
	 * 将管理员角色按照Options的组织
	 * @param parentId - 父角色ID
	 * @param depth - 深度
	 * @param selectedRoleId - 选中的角色
	 * @param modifyRoleId - 修改的角色ID
	 * @return String
	 * @throws Exception
	 */
	public String toAdminRoleOptions(long parentId , int depth , long selectedRoleId , long modifyRoleId) throws Exception;
	
	/**
	 * 获得正常的角色列表
	 * @param parentId - 父角色ID
	 * @return List<AdminRoleEntity>
	 * @throws Exception
	 */
	public List<AdminRoleEntity> getAvailableRoleList(long parentId) throws Exception;
	
	/**
	 * 中转AdminMenuEntity
	 * @param menu - AdminMenuEntity
	 * @return AdminMenuDto
	 * @throws Exception
	 */
	public AdminMenuDto parseMenu(AdminMenuEntity menu) throws Exception;
	
	/**
	 * 根据父的Seat获得按钮
	 * @param parentMenu - 父按钮
	 * @param seat - 位置，只有父级为1,2,3的时候才可以指定此按钮
	 * @return int
	 * @throws Exception
	 */
	public int getMenuSeat(AdminMenuEntity parentMenu , int seat) throws Exception;
	
	/**
	 * 根据角色和父ID获得权限选择的按钮列表
	 * @param parentId - 按钮父ID
	 * @param roleId - 角色ID
	 * @param parentRoleId - 父角色ID
	 * @return List<AdminMenuEntity>
	 * @throws Exception
	 */
	public List<AdminMenuEntity> getMenuListPermissionByRoleId(long parentId , long roleId , long parentRoleId) throws Exception;
	
	/**
	 * 获得按钮的Tree数的json数据类型
	 * @param menuList - List<AdminMenuEntity>
	 * @param roleId - 角色ID
	 * @param parentRoleId - 父角色ID
	 * @return List<Map<String,Object>>
	 * @throws Exception
	 */
	public List<Map<String,Object>> getMenuTree(List<AdminMenuEntity> menuList , long roleId , long parentRoleId) throws Exception;
	
	/**
	 * 根据角色和父ID获得有权限的按钮列表
	 * @param parentId - 按钮父ID
	 * @param roleId - 角色ID
	 * @return List<AdminMenuEntity>
	 * @throws Exception
	 */
	public List<AdminMenuEntity> getMenuListPermissionByRoleId(long parentId , long roleId) throws Exception;
	
	/**
	 * 显示按钮树的结构，最多三级
	 * @param roleId - 角色ID
	 * @return List<ShowMenuDto>
	 * @throws Exception
	 */
	public List<ShowMenuDto> getShowMenuTreeData(long roleId) throws Exception;
	
	/**
	 * 根据显示的URL找到按钮实例
	 * @param url - String
	 * @return AdminMenuEntity
	 * @throws Exception
	 */
	public AdminMenuEntity getMenuByShowUrl(String url) throws Exception;
	
	/**
	 * 获得角色所有的权限资源Url集合
	 * @param roleId - 角色ID
	 * @return Set<String>
	 * @throws Exception
	 */
	public Set<String> getRolePermissionUrl(long roleId) throws Exception;
	
	/**
	 * 验证角色是否拥有对指定url的访问权限
	 * @param roleId - 角色ID
	 * @param url - URL地址
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validPermissionUrl(long roleId , String url) throws Exception;
	
	/**
	 * 根据管理员ID获得真实名称
	 * @param adminId - 管理员ID
	 * @return String
	 * @throws Exception
	 */
	public String getAdminUserRealName(long adminId) throws Exception;
	
	/**
	 * 获得所有的角色
	 * @param status - 状态
	 * @param currRoleId - 当前选择的角色ID
	 * @return List<AdminRoleEntity>
	 * @throws Exception
	 */
	public List<AdminRoleEntity> getAllRoles(Integer status , Long currRoleId) throws Exception;
	
	/**
	 * 转换成data value json
	 * @param list - List<AdminEntity>
	 * @return String
	 */
	public String transformerDataValueJSON(List<AdminEntity> list);
	
}
