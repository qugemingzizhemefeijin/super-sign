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
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.shark.supersign.context.ActionResult;
import com.tigerjoys.shark.supersign.dto.list.AppInfoDto;
import com.tigerjoys.shark.supersign.dto.query.AppInfoQueryDto;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;
import com.tigerjoys.shark.supersign.inter.contract.IAppContract;
import com.tigerjoys.shark.supersign.inter.contract.IAppInfoContract;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.service.IAppInfoService;
import com.tigerjoys.shark.supersign.utils.DataTableOrder;
import com.tigerjoys.shark.supersign.utils.DataTableResult;
import com.tigerjoys.shark.supersign.utils.FileUploadResult;
import com.tigerjoys.shark.supersign.utils.Helper;
import com.tigerjoys.shark.supersign.utils.WebUtis;

/**
 * APP INFO信息管理
 *
 */
@Controller
@RequestMapping(value="/sign/appinfo")
public class AppInfoController extends BaseController {
	
	@Autowired
	private IAppInfoContract appInfoContract;
	
	@Autowired
	private IAppContract appContract;
	
	@Autowired
	private IAppInfoService appInfoService;
	
	/**
	 * APP INFO列表页面
	 * @param appId - 所属APPID
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public String adminList(@RequestParam(value="appid",required=false)Long appId, HttpServletRequest request , Model model) throws Exception{
		model.addAttribute("appId", Tools.longValue(appId));
		//此处获取所有的app列表
		model.addAttribute("appList", appContract.load(PageModel.getPageModel()));
		
		return "sign/appinfo/list";
	}
	
	/**
	 * Ajax加载APP INFO数据
	 * @param query - 查询对象
	 * @param request - HttpServletRequest
	 * @return DataTableResult<AppInfoDto>
	 * @throws Exception
	 */
	@RequestMapping(value="/list/ajax" , method=RequestMethod.POST)
	public @ResponseBody DataTableResult<AppInfoDto> ajaxList(@RequestBody AppInfoQueryDto query , HttpServletRequest request) throws Exception{
		int pageSize = WebUtis.getPageSize(query);
		int currentPage =WebUtis.getCurrentPage(query);
		
		PageModel pageModel = PageModel.getPageModel();
		
		if(Tools.longValue(query.getAppId()) > 0) {
			pageModel.addQuery(Restrictions.eq("app_id", query.getAppId()));
		}
		if(query.getStatus() != null) {
			pageModel.addQuery(Restrictions.eq("status", query.getStatus()));
		}
		
		//排序
		if(Tools.isNotNull(query.getOrder())) {
			for(DataTableOrder order : query.getOrder()) {
				pageModel.addSort(order.getData(), order.getDir());
			}
		}
		
		//总计
		long totalLength = appInfoContract.count(pageModel);
		
		DataTableResult<AppInfoDto> result = DataTableResult.getEmptyResult(query.getDraw() , totalLength , AppInfoDto.class);
		
		PageBean pb = new PageBean(totalLength , pageSize , currentPage);
		if(totalLength > 0) {
			//分页查询
			pageModel.addPageField(pb.getCurrentPage(), pb.getPageSize());
			List<AppInfoEntity> appInfoList = appInfoContract.load(pageModel);
			if(Tools.isNotNull(appInfoList)) {
				result.setData(appInfoService.transferAppInfoList(appInfoList));
			}
		}
		
		return result;
	}
	
	/**
	 * 添加APP INFO页面
	 * @param appid - APP ID
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/add")
	public String ajaxAdd(@RequestParam(value="appid",required=false)Long appId, HttpServletRequest request , Model model) throws Exception{
		if(Tools.longValue(appId) != 0) {
			model.addAttribute("appId", appId);
		} else {
			model.addAttribute("appList", appContract.load(PageModel.getPageModel()));
		}
		
		return "sign/appinfo/ajax/add";
	}
	
	/**
	 * 保存APP信息
	 * @param request - HttpServletRequest
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/addsave" , method=RequestMethod.POST)
	public @ResponseBody ActionResult ajaxAddSave(@RequestParam(value="icon",required=false)MultipartFile icon,
			@RequestParam(value="fullIcon",required=false)MultipartFile fullIcon,
			@RequestParam("ipa")MultipartFile ipa,
			HttpServletRequest request) throws Exception{
		String appId = request.getParameter("app_id");
		String appName = request.getParameter("app_name");
		if(Tools.isNull(appName)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error);
		}
		String bundleId = request.getParameter("bundle_id");
		if(Tools.isNull(bundleId)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error);
		}
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
		
		AppEntity app = appContract.findById(Tools.parseLong(appId));
		if(app == null) {
			return ActionResult.fail(ErrCodeEnum.db_not_found.getCode(), "appId="+appId+"不存在");
		}
		//查看是否budleId有一样的
		if(appInfoContract.findByProperty("bundle_id", bundleId) != null) {
			return ActionResult.fail(ErrCodeEnum.db_error.getCode(), "bundleId已存在");
		}
		
		Date currDate = new Date();
		
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
				iconPath = fileResult.getFilePath();
			} else {
				return ActionResult.fail(-1, fileResult.getMsg());
			}
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
		appInfo.setFull_icon_path(fullIconPath != null ? fullIconPath : app.getFull_icon_path());
		appInfo.setIcon_path(iconPath != null ? iconPath : app.getIcon_path());
		appInfo.setPath(ipaPath);
		appInfo.setStatus(Integer.parseInt(status));
		appInfo.setUpdate_time(currDate);
		appInfo.setVersion(version);
		appInfo.setVersion_code(versionCode);
		appInfoContract.insert(appInfo);
		
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_BOX_FUNCTION);
	}
	
	/**
	 * 修改APP INFO页面
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
		
		AppInfoEntity appInfo = appInfoContract.findById(id);
		if(appInfo == null) {
			return errorPage("ID=" + id +"的信息无法找到", model);
		}
		
		model.addAttribute("appInfo", appInfo);
		return "sign/appinfo/ajax/modify";
	}
	
	/**
	 * 保存APP信 INFO息
	 * @param request - HttpServletRequest
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/modifysave" , method=RequestMethod.POST)
	public @ResponseBody ActionResult ajaxAddSave(@RequestParam(value="icon",required=false)MultipartFile icon,
			@RequestParam(value="fullIcon",required=false)MultipartFile fullIcon,
			@RequestParam(value="ipa",required=false)MultipartFile ipa,
			@RequestParam("id") long appInfoId,
			HttpServletRequest request) throws Exception{
		String appName = request.getParameter("app_name");
		if(Tools.isNull(appName)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error);
		}
		String bundleId = request.getParameter("bundle_id");
		if(Tools.isNull(bundleId)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error);
		}
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
		
		AppInfoEntity appInfo = appInfoContract.findByProperty("bundle_id", bundleId);
		if(appInfo != null && appInfo.getId() != appInfoId) {
			return ActionResult.fail(ErrCodeEnum.state_error.getCode(), bundleId+"已存在");
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
				iconPath = fileResult.getFilePath();
			} else {
				return ActionResult.fail(-1, fileResult.getMsg());
			}
		}
		
		String ipaPath = null;
		if(ipa != null) {
			FileUploadResult fileResult = Helper.uploadPicture(ipa, "app");
			if (fileResult.getCode() == ErrCodeEnum.success.getCode()) {
				ipaPath = fileResult.getFilePath();
			} else {
				return ActionResult.fail(-1, fileResult.getMsg());
			}
		}
		
		AppInfoEntity temp = new AppInfoEntity();
		temp.setId(appInfoId);
		temp.setApp_name(appName);
		temp.setBundle_id(bundleId);
		temp.setFull_icon_path(fullIconPath);
		temp.setIcon_path(iconPath);
		temp.setPath(ipaPath);
		temp.setStatus(Integer.parseInt(status));
		temp.setRemark(remark);
		temp.setVersion(version);
		temp.setVersion_code(versionCode);
		appInfoContract.update(temp);
		
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
			AppInfoEntity appInfo = appInfoContract.findById(Tools.parseInt(id));
			if(appInfo != null) {
				if((status == 1 && appInfo.getStatus() != 1)//解封
					|| (status == 0 && appInfo.getStatus() != 0)) {//查封
					AppInfoEntity temp = new AppInfoEntity();
					temp.setId(appInfo.getId());
					temp.setUpdate_time(new Date());
					temp.setStatus(status==1?1:0);
					appInfoContract.update(temp);
				}
			}
		}
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_REFRESH_CURRENT_PAGE_TABLE);
	}

}
