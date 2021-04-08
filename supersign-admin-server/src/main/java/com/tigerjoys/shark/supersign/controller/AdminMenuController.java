package com.tigerjoys.shark.supersign.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.page.PageBean;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.shark.supersign.context.ActionResult;
import com.tigerjoys.shark.supersign.context.RequestUtils;
import com.tigerjoys.shark.supersign.dto.list.AdminMenuDto;
import com.tigerjoys.shark.supersign.dto.query.MenuQueryDto;
import com.tigerjoys.shark.supersign.enums.AdminMenuSeatEnum;
import com.tigerjoys.shark.supersign.enums.EStatus;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;
import com.tigerjoys.shark.supersign.inter.contract.IAdminMenuContract;
import com.tigerjoys.shark.supersign.inter.entity.AdminMenuEntity;
import com.tigerjoys.shark.supersign.service.IAdminService;
import com.tigerjoys.shark.supersign.utils.DataTableOrder;
import com.tigerjoys.shark.supersign.utils.DataTableResult;
import com.tigerjoys.shark.supersign.utils.DataTableSearch;
import com.tigerjoys.shark.supersign.utils.WebUtis;

/**
 * 管理员角色
 *
 */
@Controller
@RequestMapping(value="/conf/menu")
public class AdminMenuController extends BaseController {
	
	@Autowired
	private IAdminMenuContract adminMenuContract;
	
	@Autowired
	private IAdminService adminService;
	
	/**
	 * 按钮页面
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public String adminList(HttpServletRequest request , Model model) throws Exception{
		return "conf/menu/list";
	}
	
	/**
	 * Ajax加载角色数据
	 * @param searchDto - 查询对象
	 * @param request - HttpServletRequest
	 * @return DataTableResult<AdminMenuDto>
	 * @throws Exception
	 */
	@RequestMapping(value="/list/ajax" , method=RequestMethod.POST)
	public @ResponseBody DataTableResult<AdminMenuDto> ajaxList(@RequestBody MenuQueryDto query , HttpServletRequest request) throws Exception{
		if(query.getParentId() == null) {
			return DataTableResult.getErrorResult(query.getDraw(), "必须传递parentId", AdminMenuDto.class);
		}
		
		int pageSize = WebUtis.getPageSize(query);
		int currentPage =WebUtis.getCurrentPage(query);
		
		PageModel pageModel = PageModel.getPageModel();
		
		//搜索条件
		DataTableSearch search = query.getSearch();
		if(search != null) {
			if(Tools.isNotNull(search.getValue())) {
				pageModel.addQuery(Restrictions.like("name", search.getValue().trim()));
			}
		}
		
		pageModel.addQuery(Restrictions.eq("parentId", query.getParentId()));
		
		//排序
		if(Tools.isNotNull(query.getOrder())) {
			for(DataTableOrder order : query.getOrder()) {
				pageModel.addSort(order.getData(), order.getDir());
			}
		}
		
		pageModel.asc("priority");
		pageModel.desc("id");
		
		//总计
		long totalLength = adminMenuContract.count(pageModel);
		
		DataTableResult<AdminMenuDto> result = DataTableResult.getEmptyResult(query.getDraw(),totalLength , AdminMenuDto.class);
		
		PageBean pb = new PageBean(totalLength , pageSize , currentPage);
		if(totalLength > 0) {
			//分页查询
			pageModel.addPageField(pb.getCurrentPage(), pb.getPageSize());
			List<AdminMenuEntity> list = adminMenuContract.load(pageModel);
			if(Tools.isNotNull(list)) {
				List<AdminMenuDto> dtoList = new ArrayList<AdminMenuDto>();
				for(AdminMenuEntity role : list) {
					dtoList.add(adminService.parseMenu(role));
				}
				result.setData(dtoList);
			}
		}
		
		Map<String , Object> customData = new HashMap<String , Object>();
		customData.put("parentId", query.getParentId());
		//查看本地分页的上一级parentId是多少
		if(query.getParentId() != 0) {
			AdminMenuEntity parentMenu = adminMenuContract.findById(query.getParentId());
			if(parentMenu != null) {
				customData.put("preParentId", parentMenu.getParentId());
			} else {
				customData.put("preParentId", 0);
			}
		} else {
			customData.put("preParentId", 0);
		}
		result.setCustomData(customData);
		
		return result;
	}
	
	/**
	 * 添加按钮页面
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/add")
	public String ajaxAdd(HttpServletRequest request , Model model , @RequestParam("parentId") int parentId) throws Exception{
		if(parentId != 0) {
			AdminMenuEntity parentMenu = adminMenuContract.findById(parentId);
			if(parentMenu == null) {
				return errorPage("找不到指定的父级按钮", model);
			}
			
			model.addAttribute("parentId", parentMenu.getId());
			model.addAttribute("parentName", parentMenu.getName());
		} else {
			model.addAttribute("parentId", 0);
			model.addAttribute("parentName", "顶级按钮");
		}
		return "conf/menu/ajax/add";
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
		
		AdminMenuEntity menu = adminMenuContract.findById(id);
		if(menu == null) {
			return errorPage("ID=" + id +"的信息无法找到", model);
		}
		
		if(menu.getParentId() == 0) {
			model.addAttribute("parentId", 0);
			model.addAttribute("parentName", "顶级按钮");
		} else {
			AdminMenuEntity parentMenu = adminMenuContract.findById(menu.getParentId());
			if(parentMenu == null) {
				return errorPage("ID=" + id +"的父级按钮无法找到", model);
			}
			model.addAttribute("parentId", parentMenu.getId());
			model.addAttribute("parentName", parentMenu.getName());
		}
		
		
		model.addAttribute("menu", menu);
		return "conf/menu/ajax/add";
	}
	
	/**
	 * 保存按钮信息
	 * @param request - HttpServletRequest
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/addsave" , method=RequestMethod.POST)
	public @ResponseBody ActionResult ajaxAddSave(AdminMenuEntity menu , HttpServletRequest request) throws Exception{
		Tools.trimStringField(menu);
		
		AdminMenuEntity parentMenu = null;
		if(menu.getParentId() != 0) {
			parentMenu = adminMenuContract.findById(menu.getParentId());
			if(parentMenu == null) {
				return ActionResult.fail(ErrCodeEnum.error.getCode(), "找不到指定的上级按钮");
			}
		}
		
		String menuType = request.getParameter("menuType");
		if(!"0".equals(menuType)) menuType = "1";
		
		Date currDate = new Date();
		
		menu.setUpdateAdminId(RequestUtils.getCurrent().getAdminId());
		menu.setUpdateDate(currDate);
		menu.setMenuType(0);
		menu.setIsshowlist(0);
		
		if(menu.getId() == null) {//新增
			menu.setCreateDate(currDate);
			menu.setCreateAdminId(RequestUtils.getCurrent().getAdminId());
			if("0".equals(menuType)) {//自由按钮
				menu.setSeat(AdminMenuSeatEnum._default.getCode());
			} else {
				menu.setSeat(adminService.getMenuSeat(parentMenu, 0));
			}
			adminMenuContract.insert(menu);
		} else {
			//不允许修改父ID
			menu.setParentId(null);
			adminMenuContract.update(menu);
		}
		
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_BOX_FUNCTION);
	}
	
	/**
	 * 修改按钮状态
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
		
		for(String id : idList) {
			AdminMenuEntity menu = adminMenuContract.findById(Tools.parseInt(id));
			if(menu != null) {
				if((status == 1 && menu.getStatus() != EStatus.AVAILABLE.getCode())//恢复
					|| (status == 0 && menu.getStatus() != EStatus.UNAVAILABLE.getCode())) {//停用
					AdminMenuEntity temp = new AdminMenuEntity();
					temp.setId(menu.getId());
					temp.setUpdateDate(new Date());
					temp.setCreateAdminId(RequestUtils.getCurrent().getAdminId());
					temp.setStatus(status==1?EStatus.AVAILABLE.getCode():EStatus.UNAVAILABLE.getCode());
					adminMenuContract.update(temp);
				}
			}
		}
		
		return ActionResult.success(ActionResult.RESULT_TYPE_REFRESH_TABLE);
	}

}
