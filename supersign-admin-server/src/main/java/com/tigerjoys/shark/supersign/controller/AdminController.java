package com.tigerjoys.shark.supersign.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tigerjoys.nbs.common.utils.MD5;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.page.PageBean;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.shark.supersign.context.ActionResult;
import com.tigerjoys.shark.supersign.context.RequestUtils;
import com.tigerjoys.shark.supersign.dto.list.AdminDto;
import com.tigerjoys.shark.supersign.enums.EStatus;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;
import com.tigerjoys.shark.supersign.inter.contract.IAdminContract;
import com.tigerjoys.shark.supersign.inter.entity.AdminEntity;
import com.tigerjoys.shark.supersign.service.IAdminService;
import com.tigerjoys.shark.supersign.utils.DataTableOrder;
import com.tigerjoys.shark.supersign.utils.DataTableQueryDto;
import com.tigerjoys.shark.supersign.utils.DataTableResult;
import com.tigerjoys.shark.supersign.utils.DataTableSearch;
import com.tigerjoys.shark.supersign.utils.WebUtis;

/**
 * 管理员Controller
 *
 */
@Controller
@RequestMapping(value="/conf/admin")
public class AdminController extends BaseController {
	
	@Autowired
	private IAdminContract adminContract;
	
	@Autowired
	private IAdminService adminService;
	
	/**
	 * 管理员页面
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public String adminList(HttpServletRequest request , Model model) throws Exception{
		return "conf/admin/list";
	}

	/**
	 * Ajax加载管理员数据
	 * @param searchDto - 查询对象
	 * @param request - HttpServletRequest
	 * @return DataTableResult<AdminUserDto>
	 * @throws Exception
	 */
	@RequestMapping(value="/list/ajax" , method=RequestMethod.POST)
	public @ResponseBody DataTableResult<AdminDto> ajaxList(@RequestBody DataTableQueryDto query , HttpServletRequest request) throws Exception{
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
		long totalLength = adminContract.count(pageModel);
		
		DataTableResult<AdminDto> result = DataTableResult.getEmptyResult(query.getDraw() , totalLength , AdminDto.class);
		
		PageBean pb = new PageBean(totalLength , pageSize , currentPage);
		if(totalLength > 0) {
			//分页查询
			pageModel.addPageField(pb.getCurrentPage(), pb.getPageSize());
			List<AdminEntity> list = adminContract.load(pageModel);
			if(Tools.isNotNull(list)) {
				List<AdminDto> dtoList = new ArrayList<>();
				for(AdminEntity user : list) {
					dtoList.add(adminService.parseUser(user));
				}
				result.setData(dtoList);
			}
		}
		
		return result;
	}
	
	/**
	 * 添加管理员页面
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/add")
	public String ajaxAdd(HttpServletRequest request , Model model) throws Exception{
		//角色列表
		model.addAttribute("roleList", adminService.getAllRoles(EStatus.AVAILABLE.getCode(), null));
		return "conf/admin/ajax/add";
	}
	
	/**
	 * 修改管理员页面
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
		
		AdminEntity user = adminContract.findById(id);
		if(user == null) {
			return errorPage("ID=" + id +"的信息无法找到", model);
		}
		
		model.addAttribute("user", user);
		//角色列表
		model.addAttribute("roleList", adminService.getAllRoles(EStatus.AVAILABLE.getCode(), user.getRoleId()));
		return "conf/admin/ajax/add";
	}
	
	/**
	 * 保存管理员信息
	 * @param request - HttpServletRequest
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/addsave" , method=RequestMethod.POST)
	public @ResponseBody ActionResult ajaxAddSave(AdminEntity user , HttpServletRequest request) throws Exception{
		Tools.trimStringField(user);
		//判断用户名是否存在
		AdminEntity temp = adminContract.findByProperty("username", user.getUsername());
		if(user.getId() != null) {
			if(temp != null && temp.getId().intValue() != user.getId().intValue()) {
				return ActionResult.fail(ErrCodeEnum.error.getCode(), "用户名已经存在，请更换");
			}
		} else {
			if(temp != null) {
				return ActionResult.fail(ErrCodeEnum.error.getCode(), "用户名已经存在，请更换");
			}
		}
		
		AdminEntity admin = RequestUtils.getCurrent().getAdmin();
		Date currDate = new Date();
		
		user.setUpdateAdminId(admin.getId());
		user.setUpdateDate(currDate);
		
		if(user.getId() == null) {//新增
			user.setCreateDate(currDate);
			user.setCreateAdminId(admin.getId());
			user.setPassword(MD5.encode(user.getPassword()));
			adminContract.insert(user);
		} else {
			if(Tools.isNotNull(user.getPassword())) {
				user.setPassword(MD5.encode(user.getPassword()));
			} else {
				user.setPassword(null);
			}
			adminContract.update(user);
		}
		
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_BOX_FUNCTION);
	}

	/**
	 * 修改管理员状态
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
			AdminEntity user = adminContract.findById(Tools.parseInt(id));
			if(user != null) {
				if((status == 1 && user.getStatus() != EStatus.AVAILABLE.getCode())//解封
					|| (status == 0 && user.getStatus() != EStatus.UNAVAILABLE.getCode())) {//查封
					adminService.chageAdminStatus(user.getId(), status);
				}
			}
		}
		return ActionResult.success(ActionResult.RESULT_TYPE_REFRESH_TABLE);
	}

}
