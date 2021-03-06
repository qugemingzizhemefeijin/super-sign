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
 * ???????????????
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
	 * ?????????????????????
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
	 * Ajax??????????????????
	 * @param searchDto - ????????????
	 * @param request - HttpServletRequest
	 * @return DataTableResult<AdminRoleDto>
	 * @throws Exception
	 */
	@RequestMapping(value="/list/ajax" , method=RequestMethod.POST)
	public @ResponseBody DataTableResult<AdminRoleDto> ajaxList(@RequestBody DataTableQueryDto query , HttpServletRequest request) throws Exception{
		int pageSize = WebUtis.getPageSize(query);
		int currentPage =WebUtis.getCurrentPage(query);
		
		PageModel pageModel = PageModel.getPageModel();
		
		//????????????
		DataTableSearch search = query.getSearch();
		if(search != null) {
			if(Tools.isNotNull(search.getValue())) {
				pageModel.addQuery(Restrictions.like("username", search.getValue().trim()));
			}
		}
		
		//??????
		if(Tools.isNotNull(query.getOrder())) {
			for(DataTableOrder order : query.getOrder()) {
				pageModel.addSort(order.getData(), order.getDir());
			}
		}
		
		//??????
		long totalLength = adminRoleContract.count(pageModel);
		
		DataTableResult<AdminRoleDto> result = DataTableResult.getEmptyResult(query.getDraw() , totalLength , AdminRoleDto.class);
		
		PageBean pb = new PageBean(totalLength , pageSize , currentPage);
		if(totalLength > 0) {
			//????????????
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
	 * ??????????????????
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
	 * ??????????????????
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/modify")
	public String ajaxModify(HttpServletRequest request , Model model , @RequestParam("id") int id) throws Exception{
		if(id <= 0) {
			return errorPage("ID=" + id +"????????????", model);
		}
		if(id == Const.SUPER_ROLE) {
			return errorPage("ID=" + id +"????????????????????????", model);
		}
		
		AdminRoleEntity role = adminRoleContract.findById(id);
		if(role == null) {
			return errorPage("ID=" + id +"?????????????????????", model);
		}
		
		model.addAttribute("role", role);
		model.addAttribute("roleList", adminService.toAdminRoleOptions(0 , 0, role.getParentId() , id));
		return "conf/role/ajax/add";
	}
	
	/**
	 * ??????????????????
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
				return ActionResult.fail(ErrCodeEnum.error.getCode(), "??????????????????????????????");
			}
		}
		
		if(role.getId() != null) {
			if(role.getId() == Const.SUPER_ROLE) {
				return ActionResult.fail(ErrCodeEnum.error.getCode(), "??????????????????????????????");
			}
			if(role.getId() == role.getParentId()) {
				return ActionResult.fail(ErrCodeEnum.error.getCode(), "?????????????????????????????????????????????????????????");
			}
		}
		
		long adminId = RequestUtils.getCurrent().getAdminId();
		Date currDate = new Date();
		
		role.setUpdateAdminId(adminId);
		role.setUpdateDate(currDate);
		
		if(role.getId() == null) {//??????
			role.setCreateDate(currDate);
			role.setCreateAdminId(adminId);
			adminRoleContract.insert(role);
		} else {
			adminRoleContract.update(role);
		}
		
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_BOX_FUNCTION);
	}

	/**
	 * ??????????????????
	 * @param request - HttpServletRequest
	 * @param status - ??????1?????????14??????
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
				if((status == 1 && role.getStatus() != EStatus.AVAILABLE.getCode())//??????
					|| (status == 0 && role.getStatus() != EStatus.UNAVAILABLE.getCode())) {//??????
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
	 * ????????????????????????
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @param roleId - ??????ID
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/list/permission")
	public String permissionModel(HttpServletRequest request , Model model , @RequestParam("id") int roleId) throws Exception{
		//????????????????????????????????????????????????????????????????????????????????????????????????
		AdminRoleEntity role = adminRoleContract.findById(roleId);
		if(role == null) {
			return errorPage("ID=" + roleId +"????????????????????????", model);
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
	 * ?????????????????????
	 * @param request - HttpServletRequest
	 * @param roleId - ??????ID
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/list/permissionsave",method=RequestMethod.POST)
	public @ResponseBody ActionResult permissionSave(HttpServletRequest request , @RequestParam Long roleId) throws Exception {
		AdminRoleEntity role = adminRoleContract.findById(roleId);
		if(role == null) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode() , "ID=" + roleId +"????????????????????????");
		}
		if(role.getId() == Const.SUPER_ROLE){
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode() , "???????????????????????????????????????????????????.");
		}
		
		String authIds = request.getParameter("authIds");
		if(authIds == null) authIds = "";
		
		//????????????????????????????????????
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
		
		//????????????????????????????????????
		PageModel pageModel = PageModel.getPageModel();
		pageModel.addQuery(Restrictions.eq("roleId", roleId));
		List<AdminRoleMenuEntity> list = adminRoleMenuContract.load(pageModel);
		if(Tools.isNotNull(list)){
			for(AdminRoleMenuEntity srar : list){
				long authId = srar.getMenuId();
				if(authIdSet.contains(authId)){
					authIdSet.remove(authId);
				}else{
					adminRoleMenuContract.delete(srar);//??????
				}
			}
		}
		
		AdminEntity admin = RequestUtils.getCurrent().getAdmin();
		
		Date currDate = new Date();
		//??????????????????????????????
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
