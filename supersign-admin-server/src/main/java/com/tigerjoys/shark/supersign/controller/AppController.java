package com.tigerjoys.shark.supersign.controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.page.PageBean;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.shark.supersign.context.ActionResult;
import com.tigerjoys.shark.supersign.dto.list.AppDto;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;
import com.tigerjoys.shark.supersign.inter.contract.IAppContract;
import com.tigerjoys.shark.supersign.inter.contract.IAppInfoContract;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.service.IAppService;
import com.tigerjoys.shark.supersign.utils.DataTableOrder;
import com.tigerjoys.shark.supersign.utils.DataTableQueryDto;
import com.tigerjoys.shark.supersign.utils.DataTableResult;
import com.tigerjoys.shark.supersign.utils.FileUploadResult;
import com.tigerjoys.shark.supersign.utils.Helper;
import com.tigerjoys.shark.supersign.utils.WebUtis;

/**
 * APP信息管理
 *
 */
@Controller
@RequestMapping(value="/sign/app")
public class AppController extends BaseController {
	
	@Autowired
	private IAppContract appContract;
	
	@Autowired
	private IAppInfoContract appInfoContract;
	
	@Autowired
	private IAppService appService;
	
	/**
	 * APP列表页面
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public String adminList(HttpServletRequest request , Model model) throws Exception{
		return "sign/app/list";
	}
	
	/**
	 * Ajax加载APP数据
	 * @param query - 查询对象
	 * @param request - HttpServletRequest
	 * @return DataTableResult<AppDto>
	 * @throws Exception
	 */
	@RequestMapping(value="/list/ajax" , method=RequestMethod.POST)
	public @ResponseBody DataTableResult<AppDto> ajaxList(@RequestBody DataTableQueryDto query , HttpServletRequest request) throws Exception{
		int pageSize = WebUtis.getPageSize(query);
		int currentPage =WebUtis.getCurrentPage(query);
		
		PageModel pageModel = PageModel.getPageModel();
		
		//排序
		if(Tools.isNotNull(query.getOrder())) {
			for(DataTableOrder order : query.getOrder()) {
				pageModel.addSort(order.getData(), order.getDir());
			}
		}
		
		//总计
		long totalLength = appContract.count(pageModel);
		
		DataTableResult<AppDto> result = DataTableResult.getEmptyResult(query.getDraw() , totalLength , AppDto.class);
		
		PageBean pb = new PageBean(totalLength , pageSize , currentPage);
		if(totalLength > 0) {
			//分页查询
			pageModel.addPageField(pb.getCurrentPage(), pb.getPageSize());
			List<AppEntity> appList = appContract.load(pageModel);
			if(Tools.isNotNull(appList)) {
				result.setData(appService.transferAppList(appList));
			}
		}
		
		return result;
	}
	
	/**
	 * 添加APP页面
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/add")
	public String ajaxAdd(HttpServletRequest request , Model model) throws Exception{
		return "sign/app/ajax/add";
	}
	
	/**
	 * 保存APP信息
	 * @param request - HttpServletRequest
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/addsave" , method=RequestMethod.POST)
	public @ResponseBody ActionResult ajaxAddSave(@RequestParam("icon")MultipartFile icon,
			@RequestParam("fullIcon")MultipartFile fullIcon,
			@RequestParam("mbconfig")MultipartFile mbconfig,
			@RequestParam("ipa")MultipartFile ipa,
			HttpServletRequest request) throws Exception{
		String appName = request.getParameter("app_name");
		String bundleId = request.getParameter("bundle_id");
		String version = request.getParameter("version");
		String versionCode = request.getParameter("version_code");
		String status = request.getParameter("status");
		if(!"1".equals(status)) {
			status = "0";
		}
		String remark = request.getParameter("remark");
		if(remark == null) {
			remark = Tools.EMPTY_STRING;
		}
		
		//查看是否budleId有一样的
		if(appInfoContract.findByProperty("bundle_id", bundleId) != null) {
			return ActionResult.fail(ErrCodeEnum.db_error.getCode(), "bundleId已存在");
		}
		
		Date currDate = new Date();
		
		AppEntity app = appContract.findByProperty("app_name", appName);
		if(app == null) {
			String iconPath = null;
			FileUploadResult fileResult = Helper.uploadPicture(icon, "app");
			if (fileResult.getCode() == ErrCodeEnum.success.getCode()) {
				iconPath = fileResult.getFilePath();
			} else {
				return ActionResult.fail(-1, fileResult.getMsg());
			}
			
			String fullIconPath = null;
			fileResult = Helper.uploadPicture(fullIcon, "app");
			if (fileResult.getCode() == ErrCodeEnum.success.getCode()) {
				fullIconPath = fileResult.getFilePath();
			} else {
				return ActionResult.fail(-1, fileResult.getMsg());
			}
			
			String mbconfigPath = null;
			fileResult = Helper.uploadPicture(mbconfig, "app");
			if (fileResult.getCode() == ErrCodeEnum.success.getCode()) {
				mbconfigPath = fileResult.getFilePath();
			} else {
				return ActionResult.fail(-1, fileResult.getMsg());
			}
			
			//新增APP信息
			app = new AppEntity();
			app.setApp_name(appName);
			app.setCreate_time(currDate);
			app.setFull_icon_path(fullIconPath);
			app.setIcon_path(iconPath);
			app.setMbconfig(mbconfigPath);
			app.setStatus(Integer.parseInt(status));
			app.setUpdate_time(currDate);
			app.setRemark(remark);
			appContract.insert(app);
		}
		
		String ipaPath = null;
		//新增APP INFO信息
		FileUploadResult fileResult = Helper.uploadPicture(ipa, "app");
		if (fileResult.getCode() == ErrCodeEnum.success.getCode()) {
			ipaPath = fileResult.getFilePath();
		} else {
			return ActionResult.fail(-1, fileResult.getMsg());
		}
		
		AppInfoEntity appInfo = new AppInfoEntity();
		appInfo.setApp_id(app.getId());
		appInfo.setApp_name(appName);
		appInfo.setBundle_id(bundleId);
		appInfo.setCreate_time(currDate);
		appInfo.setFull_icon_path(app.getFull_icon_path());
		appInfo.setIcon_path(app.getIcon_path());
		appInfo.setPath(ipaPath);
		appInfo.setStatus(Integer.parseInt(status));
		appInfo.setUpdate_time(currDate);
		appInfo.setVersion(version);
		appInfo.setVersion_code(versionCode);
		appInfoContract.insert(appInfo);
		
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_BOX_FUNCTION);
	}
	
	/**
	 * 修改APP页面
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
		
		AppEntity app = appContract.findById(id);
		if(app == null) {
			return errorPage("ID=" + id +"的信息无法找到", model);
		}
		
		model.addAttribute("app", app);
		return "sign/app/ajax/modify";
	}
	
	/**
	 * 保存APP信息
	 * @param request - HttpServletRequest
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/modifysave" , method=RequestMethod.POST)
	public @ResponseBody ActionResult ajaxAddSave(@RequestParam(value="icon",required=false)MultipartFile icon,
			@RequestParam(value="fullIcon",required=false)MultipartFile fullIcon,
			@RequestParam(value="mbconfig",required=false)MultipartFile mbconfig,
			@RequestParam("id") long appId,
			HttpServletRequest request) throws Exception{
		String appName = request.getParameter("app_name");
		String status = request.getParameter("status");
		if(!"1".equals(status)) {
			status = "0";
		}
		String remark = request.getParameter("remark");
		if(remark == null) {
			remark = Tools.EMPTY_STRING;
		}
		
		AppEntity app = appContract.findByProperty("app_name", appName);
		if(app != null && app.getId() != appId) {
			return ActionResult.fail(ErrCodeEnum.state_error.getCode(), appName+"已存在");
		}
		
		String iconPath = null;
		if(icon != null) {
			FileUploadResult fileResult = Helper.uploadPicture(icon, "app");
			if (fileResult.getCode() == ErrCodeEnum.success.getCode()) {
				iconPath = fileResult.getFilePath();
			} else {
				return ActionResult.fail(-1, fileResult.getMsg());
			}
		}
		
		String fullIconPath = null;
		if(fullIcon != null) {
			FileUploadResult fileResult = Helper.uploadPicture(fullIcon, "app");
			if (fileResult.getCode() == ErrCodeEnum.success.getCode()) {
				fullIconPath = fileResult.getFilePath();
			} else {
				return ActionResult.fail(-1, fileResult.getMsg());
			}
		}
		
		String mbconfigPath = null;
		if(mbconfig != null) {
			FileUploadResult fileResult = Helper.uploadPicture(mbconfig, "app");
			if (fileResult.getCode() == ErrCodeEnum.success.getCode()) {
				mbconfigPath = fileResult.getFilePath();
			} else {
				return ActionResult.fail(-1, fileResult.getMsg());
			}
		}
		
		AppEntity temp = new AppEntity();
		temp.setId(appId);
		temp.setApp_name(appName);
		temp.setFull_icon_path(fullIconPath);
		temp.setIcon_path(iconPath);
		temp.setMbconfig(mbconfigPath);
		temp.setStatus(Integer.parseInt(status));
		temp.setRemark(remark);
		appContract.update(temp);
		
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_BOX_FUNCTION);
	}

	/**
	 * 修改APP状态
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
			AppEntity app = appContract.findById(Tools.parseInt(id));
			if(app != null) {
				if((status == 1 && app.getStatus() != 1)//解封
					|| (status == 0 && app.getStatus() != 0)) {//查封
					AppEntity temp = new AppEntity();
					temp.setId(app.getId());
					temp.setUpdate_time(new Date());
					temp.setStatus(status==1?1:0);
					appContract.update(temp);
				}
			}
		}
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_REFRESH_CURRENT_PAGE_TABLE);
	}

}
