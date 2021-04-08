package com.tigerjoys.shark.supersign.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.page.PageBean;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.context.ActionResult;
import com.tigerjoys.shark.supersign.context.RequestUtils;
import com.tigerjoys.shark.supersign.dto.list.AdminRoleDto;
import com.tigerjoys.shark.supersign.enums.EStatus;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;
import com.tigerjoys.shark.supersign.inter.contract.IAdminMenuContract;
import com.tigerjoys.shark.supersign.inter.contract.IAdminRoleContract;
import com.tigerjoys.shark.supersign.inter.contract.IAdminRoleMenuContract;
import com.tigerjoys.shark.supersign.inter.entity.AdminEntity;
import com.tigerjoys.shark.supersign.inter.entity.AdminMenuEntity;
import com.tigerjoys.shark.supersign.inter.entity.AdminRoleEntity;
import com.tigerjoys.shark.supersign.inter.entity.AdminRoleMenuEntity;
import com.tigerjoys.shark.supersign.service.IAdminService;
import com.tigerjoys.shark.supersign.utils.DataTableOrder;
import com.tigerjoys.shark.supersign.utils.DataTableQueryDto;
import com.tigerjoys.shark.supersign.utils.DataTableResult;
import com.tigerjoys.shark.supersign.utils.DataTableSearch;
import com.tigerjoys.shark.supersign.utils.WebUtis;

/**
 * 管理员角色
 *
 */
@Controller
@RequestMapping(value="/conf/role")
public class AdminRoleController extends BaseController {
	
	@Autowired
	private IAdminRoleContract adminRoleContract;
	
	@Autowired
	private IAdminMenuContract adminMenuContract;
	
	@Autowired
	private IAdminRoleMenuContract adminRoleMenuContract;
	
	@Autowired
	private IAdminService adminService;
	
	/**
	 * 管理员角色页面
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public String roleList(HttpServletRequest request , Model model) throws Exception{
		return "conf/role/list";
	}
	
	/**
	 * Ajax加载角色数据
	 * @param searchDto - 查询对象
	 * @param request - HttpServletRequest
	 * @return DataTableResult<AdminRoleDto>
	 * @throws Exception
	 */
	@RequestMapping(value="/list/ajax" , method=RequestMethod.POST)
	public @ResponseBody DataTableResult<AdminRoleDto> ajaxList(@RequestBody DataTableQueryDto query , HttpServletRequest request) throws Exception{
		int pageSize = WebUtis.getPageSize(query);
		int currentPage =WebUtis.getCurrentPage(query);
		
		PageModel pageModel = PageModel.getPageModel();
		
		//搜索条件
		DataTableSearch search = query.getSearch();
		if(search != null) {
			if(Tools.isNotNull(search.getValue())) {
				pageModel.addQuery(Restrictions.like("username", search.getValue().trim()));
			}
		}
		
		//排序
		if(Tools.isNotNull(query.getOrder())) {
			for(DataTableOrder order : query.getOrder()) {
				pageModel.addSort(order.getData(), order.getDir());
			}
		}
		
		//总计
		long totalLength = adminRoleContract.count(pageModel);
		
		DataTableResult<AdminRoleDto> result = DataTableResult.getEmptyResult(query.getDraw() , totalLength , AdminRoleDto.class);
		
		PageBean pb = new PageBean(totalLength , pageSize , currentPage);
		if(totalLength > 0) {
			//分页查询
			pageModel.addPageField(pb.getCurrentPage(), pb.getPageSize());
			List<AdminRoleEntity> list = adminRoleContract.load(pageModel);
			if(Tools.isNotNull(list)) {
				List<AdminRoleDto> dtoList = new ArrayList<AdminRoleDto>();
				for(AdminRoleEntity role : list) {
					dtoList.add(adminService.parseRole(role));
				}
				result.setData(dtoList);
			}
		}
		
		return result;
	}
	
	/**
	 * 添加角色页面
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/add")
	public String ajaxAdd(HttpServletRequest request , Model model) throws Exception{
		model.addAttribute("roleList", adminService.toAdminRoleOptions(0 , 0, 0  ,0));
		return "conf/role/ajax/add";
	}
	
	/**
	 * 修改角色页面
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/modify")
	public String ajaxModify(HttpServletRequest request , Model model , @RequestParam("id") int id) throws Exception{
		if(id <= 0) {
			return errorPage("ID=" + id +"无法找到", model);
		}
		if(id == Const.SUPER_ROLE) {
			return errorPage("ID=" + id +"管理员不允许修改", model);
		}
		
		AdminRoleEntity role = adminRoleContract.findById(id);
		if(role == null) {
			return errorPage("ID=" + id +"的信息无法找到", model);
		}
		
		model.addAttribute("role", role);
		model.addAttribute("roleList", adminService.toAdminRoleOptions(0 , 0, role.getParentId() , id));
		return "conf/role/ajax/add";
	}
	
	/**
	 * 保存角色信息
	 * @param request - HttpServletRequest
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/addsave" , method=RequestMethod.POST)
	public @ResponseBody ActionResult ajaxAddSave(AdminRoleEntity role , HttpServletRequest request) throws Exception{
		Tools.trimStringField(role);
		
		if(role.getParentId() != null && role.getParentId() != 0) {
			AdminRoleEntity parentRole = adminRoleContract.findById(role.getParentId());
			if(parentRole == null) {
				return ActionResult.fail(ErrCodeEnum.error.getCode(), "找不到指定的上级职能");
			}
		}
		
		if(role.getId() != null) {
			if(role.getId() == Const.SUPER_ROLE) {
				return ActionResult.fail(ErrCodeEnum.error.getCode(), "管理员是不允许修改的");
			}
			if(role.getId() == role.getParentId()) {
				return ActionResult.fail(ErrCodeEnum.error.getCode(), "自己的上级不能指向自己或者自己分支下的");
			}
		}
		
		long adminId = RequestUtils.getCurrent().getAdminId();
		Date currDate = new Date();
		
		role.setUpdateAdminId(adminId);
		role.setUpdateDate(currDate);
		
		if(role.getId() == null) {//新增
			role.setCreateDate(currDate);
			role.setCreateAdminId(adminId);
			adminRoleContract.insert(role);
		} else {
			adminRoleContract.update(role);
		}
		
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_BOX_FUNCTION);
	}

	/**
	 * 修改角色状态
	 * @param request - HttpServletRequest
	 * @param status - 状态1正常，14停用
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/changeStatus" , method=RequestMethod.POST)
	public @ResponseBody ActionResult ajaxChangeStatus(HttpServletRequest request , @RequestParam("status") int status) throws Exception {
		String ids = request.getParameter("id");
		if(Tools.isNull(ids)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode());
		}
		String[] idList = Tools.split(ids);
		if(idList == null || idList.length == 0) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode());
		}
		
		long adminId = RequestUtils.getCurrent().getAdminId();
		
		for(String id : idList) {
			AdminRoleEntity role = adminRoleContract.findById(Tools.parseInt(id));
			if(role != null) {
				if((status == 1 && role.getStatus() != EStatus.AVAILABLE.getCode())//恢复
					|| (status == 0 && role.getStatus() != EStatus.UNAVAILABLE.getCode())) {//停用
					if(role.getId() != Const.SUPER_ROLE) {
						AdminRoleEntity temp = new AdminRoleEntity();
						temp.setId(role.getId());
						temp.setUpdateDate(new Date());
						temp.setUpdateAdminId(adminId);
						temp.setStatus(status==1?EStatus.AVAILABLE.getCode():EStatus.UNAVAILABLE.getCode());
						adminRoleContract.update(temp);
					}
				}
			}
		}
		
		return ActionResult.success(ActionResult.RESULT_TYPE_REFRESH_TABLE);
	}
	
	/**
	 * 角色授于权限页面
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @param roleId - 角色ID
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/list/permission")
	public String permissionModel(HttpServletRequest request , Model model , @RequestParam("id") int roleId) throws Exception{
		//原则上下级权限不能超过上级权限，除非是中途修改上下级关系的角色。
		AdminRoleEntity role = adminRoleContract.findById(roleId);
		if(role == null) {
			return errorPage("ID=" + roleId +"的管理员没有找到", model);
		}
		
		model.addAttribute("role", role);
		
		List<AdminMenuEntity> menuList = adminService.getMenuListPermissionByRoleId(0, roleId , role.getParentId());
		if(Tools.isNotNull(menuList)) {
			model.addAttribute("zNodes", JSON.toJSONString(adminService.getMenuTree(menuList , role.getId() , role.getParentId())));
		} else {
			model.addAttribute("zNodes", "{}");
		}
		
		return "conf/role/permission";
	}
	
	/**
	 * 角色的权限保存
	 * @param request - HttpServletRequest
	 * @param roleId - 角色ID
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/list/permissionsave",method=RequestMethod.POST)
	public @ResponseBody ActionResult permissionSave(HttpServletRequest request , @RequestParam Long roleId) throws Exception {
		AdminRoleEntity role = adminRoleContract.findById(roleId);
		if(role == null) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode() , "ID=" + roleId +"的管理员没有找到");
		}
		if(role.getId() == Const.SUPER_ROLE){
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode() , "抱歉，超级管理员是不允许设置权限的.");
		}
		
		String authIds = request.getParameter("authIds");
		if(authIds == null) authIds = "";
		
		//首先判断提交的数据完整性
		Set<Long> authIdSet = new HashSet<Long>();
		String[] ids = StringUtils.split(authIds,",");
		for(String authId : ids){
			if(Tools.isPositiveNumber(authId)){
				long id = Tools.parseLong(authId);
				AdminMenuEntity menu = adminMenuContract.findById(id);
				if(menu == null || menu.getStatus() != EStatus.AVAILABLE.getCode()) continue;
				
				authIdSet.add(id);
			}
		}
		
		//获取角色的现在拥有的权限
		PageModel pageModel = PageModel.getPageModel();
		pageModel.addQuery(Restrictions.eq("roleId", roleId));
		List<AdminRoleMenuEntity> list = adminRoleMenuContract.load(pageModel);
		if(Tools.isNotNull(list)){
			for(AdminRoleMenuEntity srar : list){
				long authId = srar.getMenuId();
				if(authIdSet.contains(authId)){
					authIdSet.remove(authId);
				}else{
					adminRoleMenuContract.delete(srar);//删除
				}
			}
		}
		
		AdminEntity admin = RequestUtils.getCurrent().getAdmin();
		
		Date currDate = new Date();
		//剩余的将是需要新增的
		for(Long authId : authIdSet){
			AdminRoleMenuEntity srar = new AdminRoleMenuEntity();
			srar.setCreateDate(currDate);
			srar.setHandle(1);
			srar.setMenuId(authId);
			srar.setUpdateAdminId(admin.getId());
			srar.setCreateAdminId(admin.getId());
			srar.setRoleId(roleId);
			srar.setUpdateDate(currDate);
			adminRoleMenuContract.insert(srar);
		}
		
		return ActionResult.success(ActionResult.RESULT_TYPE_SELFPAGE);
	}

}
