package com.tigerjoys.shark.supersign.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.tigerjoys.nbs.common.utils.BeanUtils;
import com.tigerjoys.nbs.common.utils.JsonHelper;
import com.tigerjoys.nbs.common.utils.MD5;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.mybatis.core.sql.Criterion;
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.constant.Param;
import com.tigerjoys.shark.supersign.context.RequestUtils;
import com.tigerjoys.shark.supersign.dto.list.AdminDto;
import com.tigerjoys.shark.supersign.dto.list.AdminMenuDto;
import com.tigerjoys.shark.supersign.dto.list.AdminRoleDto;
import com.tigerjoys.shark.supersign.dto.list.DataValueBean;
import com.tigerjoys.shark.supersign.dto.list.ShowMenuDto;
import com.tigerjoys.shark.supersign.enums.AdminMenuSeatEnum;
import com.tigerjoys.shark.supersign.enums.EStatus;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;
import com.tigerjoys.shark.supersign.enums.LogTypeEnum;
import com.tigerjoys.shark.supersign.inter.contract.IAdminContract;
import com.tigerjoys.shark.supersign.inter.contract.IAdminLogContract;
import com.tigerjoys.shark.supersign.inter.contract.IAdminMenuContract;
import com.tigerjoys.shark.supersign.inter.contract.IAdminRoleContract;
import com.tigerjoys.shark.supersign.inter.contract.IAdminRoleMenuContract;
import com.tigerjoys.shark.supersign.inter.entity.AdminEntity;
import com.tigerjoys.shark.supersign.inter.entity.AdminLogEntity;
import com.tigerjoys.shark.supersign.inter.entity.AdminMenuEntity;
import com.tigerjoys.shark.supersign.inter.entity.AdminRoleEntity;
import com.tigerjoys.shark.supersign.inter.entity.AdminRoleMenuEntity;
import com.tigerjoys.shark.supersign.service.IAdminService;
import com.tigerjoys.shark.supersign.utils.WebUtis;

/**
 * 登录相关服务实现类
 *
 */
@Service
public class AdminServiceImpl implements IAdminService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IAdminContract adminContract;
	
	@Autowired
	private IAdminLogContract adminLogContract;
	
	@Autowired
	private IAdminRoleContract adminRoleContract;
	
	@Autowired
	private IAdminMenuContract adminMenuContract;
	
	@Autowired
	private IAdminRoleMenuContract adminRoleMenuContract;
	
	//AntPath路径匹配
    private PathMatcher pathMatcher = new AntPathMatcher();
	
	@Override
	public int login(String username, String password) throws Exception {
		AdminEntity admin = adminContract.findByProperty("username", username);
		if(admin == null) {
			return ErrCodeEnum.user_isnull.getCode();
		}
		if(admin.getStatus() != EStatus.AVAILABLE.getCode()) {
			return ErrCodeEnum.user_freeze.getCode();
		}
		if(!admin.getPassword().equals(MD5.encode(password))) {
			return ErrCodeEnum.password_against.getCode();
		}
		
		HttpServletRequest request = RequestUtils.getCurrent().getRequest();
		
		//此处记录登录信息以及log
		AdminEntity temp = new AdminEntity();
		temp.setId(admin.getId());
		temp.setLastDate(new Date());
		temp.setLastip(Tools.getIP(request));
		adminContract.update(temp);
		
		//记录session
		request.getSession().setAttribute(Param.ADMID_LOGIN_SESSION, admin.getId());
		//初始化此登录用户的所有可访问的资源信息
		request.getSession().setAttribute(Param.ADMIN_PERMISSION_URL_SESSION, getRolePermissionUrl(admin.getRoleId()));
		
		//记录login
		addAdminLog(admin.getId() , LogTypeEnum.admin_login);
		
		return 0;
	}

	@Override
	public void addAdminLog(long adminId, LogTypeEnum type) {
		HttpServletRequest request = RequestUtils.getCurrent().getRequest();
		
		try {
			AdminLogEntity log = new AdminLogEntity();
			log.setCreateDate(new Date());
			log.setIp(Tools.getIP(request));
			log.setRemark(type.getDesc());
			log.setType(type.getCode());
			log.setUpdateDate(new Date());
			log.setAdminId(adminId);
			adminLogContract.insert(log);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void chageAdminStatus(long adminId, int s) throws Exception {
		AdminEntity temp = new AdminEntity();
		temp.setId(adminId);
		temp.setUpdateDate(new Date());
		temp.setUpdateAdminId(RequestUtils.getCurrent().getAdminId());
		temp.setStatus(s==1?EStatus.AVAILABLE.getCode():EStatus.UNAVAILABLE.getCode());
		adminContract.update(temp);
	}

	@Override
	public AdminDto parseUser(AdminEntity user) throws Exception {
		AdminDto dto = BeanUtils.copyBean(user, AdminDto.class);
		dto.setStatusStr(EStatus.getDescByCode(dto.getStatus()));
		AdminRoleEntity parentRole = null;
		if(user.getRoleId() != 0) {
			parentRole = adminRoleContract.findById(user.getRoleId());
		}
		dto.setRolename(parentRole!=null?parentRole.getName():"");
		return dto;
	}

	@Override
	public AdminRoleDto parseRole(AdminRoleEntity role) throws Exception {
		AdminRoleEntity parentRole = null;
		if(role.getParentId() != 0) {
			parentRole = adminRoleContract.findById(role.getParentId());
		}
		
		AdminRoleDto dto = BeanUtils.copyBean(role, AdminRoleDto.class);
		dto.setStatusStr(EStatus.getDescByCode(role.getStatus()));
		dto.setParentName(parentRole!=null?parentRole.getName():"");
		return dto;
	}

	@Override
	public String toAdminRoleOptions(long parentId , int depth , long selectedRoleId , long modifyRoleId) throws Exception {
		StringBuilder buf = new StringBuilder();
		String split = System.getProperty("line.separator");
		
		List<AdminRoleEntity> list = getAvailableRoleList(parentId);
		if(Tools.isNotNull(list)) {
			for(AdminRoleEntity role : list) {
				if(modifyRoleId > 0 && role.getId() == modifyRoleId) break;
				buf.append("<option value='"+role.getId()+"' ");
				if(selectedRoleId == role.getId())buf.append("selected=\"selected\" ");
				buf.append(">");
				if(depth > 0) {
					for(int i=0;i<depth;i++) buf.append("&nbsp;");
					buf.append("|-");
				}
				buf.append(role.getName());
				buf.append("</option>").append(split);
				buf.append(toAdminRoleOptions(role.getId() , depth+1, selectedRoleId , modifyRoleId));
			}
		}
		
		return buf.toString();
	}

	@Override
	public List<AdminRoleEntity> getAvailableRoleList(long parentId) throws Exception {
		PageModel pageModel = PageModel.getPageModel();
		pageModel.addQuery(Restrictions.eq("status", EStatus.AVAILABLE.getCode()));
		pageModel.addQuery(Restrictions.eq("parentId", parentId));
		
		pageModel.asc("priority");
		pageModel.asc("id");
		
		return adminRoleContract.load(pageModel);
	}

	@Override
	public AdminMenuDto parseMenu(AdminMenuEntity menu) throws Exception {
		AdminMenuEntity parentMenu = null;
		if(menu.getParentId() != 0) {
			parentMenu = adminMenuContract.findById(menu.getParentId());
		}
		
		AdminMenuDto dto = BeanUtils.copyBean(menu, AdminMenuDto.class);
		dto.setStatusStr(EStatus.getDescByCode(menu.getStatus()));
		dto.setParentName(parentMenu!=null?parentMenu.getName():"");
		dto.setSeatStr(AdminMenuSeatEnum.getDescByCode(menu.getSeat()));
		return dto;
	}

	@Override
	public int getMenuSeat(AdminMenuEntity parentMenu , int seat) throws Exception {
		long parentId = parentMenu != null ? parentMenu.getId() : 0;
		int parentSeat = parentMenu != null ? parentMenu.getSeat() : 0;
		
		if(parentId == 0) {
			return AdminMenuSeatEnum._left_top.getCode();
		} else {
			if(seat == 0) {
				if(parentSeat == AdminMenuSeatEnum._left_top.getCode()) {
					return AdminMenuSeatEnum._left_parent.getCode();
				} else if(parentSeat == AdminMenuSeatEnum._left_parent.getCode()) {
					return AdminMenuSeatEnum._left_children.getCode();
				}
			} else {
				return seat;
			}
		}
		return AdminMenuSeatEnum._default.getCode();
	}

	@Override
	public List<AdminMenuEntity> getMenuListPermissionByRoleId(long parentId, long roleId , long parentRoleId) throws Exception {
		PageModel pageModel = PageModel.getPageModel();
		pageModel.addQuery(Restrictions.eq("parentId", parentId));
		pageModel.addQuery(Restrictions.eq("status", EStatus.AVAILABLE.getCode()));
		
		pageModel.asc("priority");
		pageModel.asc("id");
		
		List<AdminMenuEntity> menuList = adminMenuContract.load(pageModel);
		
		//父角色为0，或者是超级管理员，或者本身自己是超级管理员则返回所有按钮
		if(parentRoleId == 0 || parentRoleId == Const.SUPER_ROLE || roleId == Const.SUPER_ROLE) {
			return menuList;
		} else {
			if(Tools.isNotNull(menuList)) {
				List<AdminMenuEntity> filterList = new ArrayList<AdminMenuEntity>();
				for(AdminMenuEntity menu : menuList) {
					//查看父角色是否有此按钮权限
					if(adminRoleMenuContract.getAdminRoleMenuEntity(parentRoleId, menu.getId()) != null) {
						filterList.add(menu);
					}
				}
				return filterList;
			}
		}
		return null;
	}

	@Override
	public List<Map<String,Object>> getMenuTree(List<AdminMenuEntity> menuList, long roleId , long parentRoleId) throws Exception {
		if(Tools.isNotNull(menuList)) {
			List<Map<String,Object>> jsArray = new ArrayList<Map<String,Object>>();
			for(AdminMenuEntity menu : menuList){
				Map<String,Object> jsonMap = new HashMap<String,Object>();
				jsonMap.put("id", menu.getId());
				jsonMap.put("name", menu.getName());
				//查看是否有子按钮
				List<AdminMenuEntity> childrenList = getMenuListPermissionByRoleId(menu.getId(), roleId , parentRoleId);
				if(Tools.isNotNull(childrenList)) {
					jsonMap.put("isParent", true);
					jsonMap.put("nodes", getMenuTree(childrenList , roleId , parentRoleId));
				} else {
					jsonMap.put("isParent", false);
				}
				
				//此处查看是否有选中
				if(adminRoleMenuContract.getAdminRoleMenuEntity(roleId, menu.getId()) != null) {
					jsonMap.put("checked", true);
				} else {
					jsonMap.put("checked", false);
				}
				
				jsArray.add(jsonMap);
			}
			
			return jsArray;
		}
		return null;
	}

	@Override
	public List<AdminMenuEntity> getMenuListPermissionByRoleId(long parentId, long roleId) throws Exception {
		PageModel pageModel = PageModel.getPageModel();
		pageModel.addQuery(Restrictions.eq("parentId", parentId));
		pageModel.addQuery(Restrictions.eq("status", EStatus.AVAILABLE.getCode()));
		pageModel.addQuery(Restrictions.ne("seat", AdminMenuSeatEnum._default.getCode()));
		
		pageModel.asc("priority");
		pageModel.asc("id");
		
		List<AdminMenuEntity> menuList = adminMenuContract.load(pageModel);
		
		if(Const.SUPER_ROLE == roleId) {
			return menuList;
		} else {
			if(Tools.isNotNull(menuList)) {
				List<AdminMenuEntity> filterList = new ArrayList<AdminMenuEntity>();
				for(AdminMenuEntity menu : menuList) {
					//查看父角色是否有此按钮权限
					if(adminRoleMenuContract.getAdminRoleMenuEntity(roleId, menu.getId()) != null) {
						filterList.add(menu);
					}
				}
				return filterList;
			}
		}
		return null;
	}

	@Override
	public List<ShowMenuDto> getShowMenuTreeData(long roleId) throws Exception {
		List<ShowMenuDto> top_list = new ArrayList<ShowMenuDto>();
		//首先获取第一层按钮
		List<AdminMenuEntity>  top_oneList = getMenuListPermissionByRoleId(0 , roleId);
		if(Tools.isNotNull(top_oneList)) {
			for(AdminMenuEntity top_menu : top_oneList) {
				ShowMenuDto top_dto = new ShowMenuDto();
				BeanUtils.copyBean(top_menu, top_dto);
				
				//此处加载第二层
				List<ShowMenuDto> sec_list = new ArrayList<ShowMenuDto>();
				top_dto.setChildrenList(sec_list);
				
				List<AdminMenuEntity>  sec_oneList = getMenuListPermissionByRoleId(top_menu.getId() , roleId);
				if(Tools.isNotNull(sec_oneList)) {
					for(AdminMenuEntity sec_menu : sec_oneList) {
						ShowMenuDto sec_dto = new ShowMenuDto();
						BeanUtils.copyBean(sec_menu, sec_dto);
						
						//此处加载第三层
						List<ShowMenuDto> thd_list = new ArrayList<ShowMenuDto>();
						sec_dto.setChildrenList(thd_list);
						
						List<AdminMenuEntity> thd_oneList = getMenuListPermissionByRoleId(sec_menu.getId() , roleId);
						if(Tools.isNotNull(thd_oneList)) {
							for(AdminMenuEntity thd_menu : thd_oneList) {
								ShowMenuDto thd_dto = new ShowMenuDto();
								BeanUtils.copyBean(thd_menu, thd_dto);
								
								thd_list.add(thd_dto);
							}
						}
						
						sec_list.add(sec_dto);
					}
				}
				
				top_list.add(top_dto);
			}
		}
		
		return top_list;
	}

	@Override
	public AdminMenuEntity getMenuByShowUrl(String url) throws Exception {
		PageModel pageModel = PageModel.getPageModel();
		pageModel.addQuery(Restrictions.eq("showurl", url));
		pageModel.addQuery(Restrictions.in("seat", AdminMenuSeatEnum._left_parent.getCode(), AdminMenuSeatEnum._left_children.getCode()));
		
		List<AdminMenuEntity> list = adminMenuContract.load(pageModel);
		
		if(Tools.isNotNull(list)) return list.get(0);
		return null;
	}

	@Override
	public Set<String> getRolePermissionUrl(long roleId) throws Exception {
		PageModel pageModel = PageModel.getPageModel();
		pageModel.addQuery(Restrictions.eq("roleId", roleId));
		
		Set<String> hashSet = new HashSet<String>();
		String contextPath = WebUtis.getContextPath(RequestUtils.getCurrent().getRequest());
		
		List<AdminRoleMenuEntity> allReation = adminRoleMenuContract.load(pageModel);
		if(Tools.isNotNull(allReation)) {
			for(AdminRoleMenuEntity arm : allReation) {
				AdminMenuEntity menu = adminMenuContract.findById(arm.getMenuId());
				if(menu != null && Tools.isNotNull(menu.getResource())) {
					String resources[] = menu.getResource().split("\n");
					if(resources != null && resources.length > 0) {
						for(String url : resources) {
							if(contextPath.length() == 0) {
								hashSet.add(url);
							} else {
								hashSet.add(contextPath+url);
							}
						}
					}
				}
			}
		}
		
		return hashSet;
	}

	@Override
	public String getAdminUserRealName(long adminId) throws Exception {
		AdminEntity admin = adminContract.findById(adminId);
		return admin!=null?admin.getRealname():"";
	}

	@Override
	public List<AdminRoleEntity> getAllRoles(Integer status, Long currRoleId) throws Exception {
		PageModel pageModel = PageModel.getPageModel();
		
		List<Criterion> criterList = new ArrayList<Criterion>();
		if(status != null) {
			criterList.add(Restrictions.eq("status", status));
		}
		if(currRoleId != null && currRoleId != 0) {
			criterList.add(Restrictions.eq("id", currRoleId));
		}
		
		if(Tools.isNotNull(criterList)) {
			pageModel.addQuery(Restrictions.or(criterList.toArray(new Criterion[0])));
		}
		
		return adminRoleContract.load(pageModel);
	}
	
	@Override
	public String transformerDataValueJSON(List<AdminEntity> list) {
		List<DataValueBean> dataList = transformerDataValue(list);
		return JsonHelper.toJson(dataList);
	}

	/**
	 * 将管理员列表转换成DataValueBean的形式
	 * @param list - List<AdminUserEntity>
	 * @return List<DataValueBean>
	 */
	private List<DataValueBean> transformerDataValue(List<AdminEntity> list) {
		List<DataValueBean> dataList = new ArrayList<DataValueBean>();
		if(Tools.isNotNull(list)) {
			for(AdminEntity user : list) {
				dataList.add(new DataValueBean(user.getId().toString(), user.getRealname()));
			}
		}
		return dataList;
	}

    @SuppressWarnings("unchecked")
    @Override
    public boolean validPermissionUrl(long roleId, String url) throws Exception {
        if(roleId == Const.SUPER_ROLE) return true;
        
        HttpSession session = RequestUtils.getCurrent().getRequest().getSession();
        Map<String , Boolean> permissionURLMap = (Map<String , Boolean>)session.getAttribute(Param.ADMIN_PERMISSION_URL_VALID);
        if(permissionURLMap == null) {
            permissionURLMap = new HashMap<String , Boolean>();
            session.setAttribute(Param.ADMIN_PERMISSION_URL_VALID, permissionURLMap);
        }
        
        Boolean v = permissionURLMap.get(url);
        if(v != null) return v.booleanValue();
        
        boolean valid = false;
        Set<String> permissionHashSet = (Set<String>)session.getAttribute(Param.ADMIN_PERMISSION_URL_SESSION);
        if(permissionHashSet == null || permissionHashSet.isEmpty()) {
            valid = true;
        } else {
            //直接查看是否有权限
            if(permissionHashSet.contains(url)) {
                valid = true;
            } else {
              //如果没有匹配则，则需要走Ant循环匹配，效率地点，不过没关系，匹配速度也是毫秒级别的
                for(String path : permissionHashSet) {
                    if(pathMatcher.match(path, url)) {
                        valid = true;
                        break;
                    }
                }
            }
        }
        
        permissionURLMap.put(url, valid);
        return valid;
    }

}
