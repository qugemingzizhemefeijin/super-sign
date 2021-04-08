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
import org.springframework.web.bind.annotation.ResponseBody;

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.page.PageBean;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.shark.supersign.context.ActionResult;
import com.tigerjoys.shark.supersign.dto.list.AccountInfoDto;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;
import com.tigerjoys.shark.supersign.inter.contract.IAppContract;
import com.tigerjoys.shark.supersign.inter.contract.IAppInfoContract;
import com.tigerjoys.shark.supersign.inter.contract.IDeveloperContract;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.utils.DataTableOrder;
import com.tigerjoys.shark.supersign.utils.DataTableQueryDto;
import com.tigerjoys.shark.supersign.utils.DataTableResult;
import com.tigerjoys.shark.supersign.utils.WebUtis;

/**
 * 开发者账号管理
 *
 */
@Controller
@RequestMapping(value="/developer/account")
public class DeveloperController extends BaseController {


	@Autowired
	private IDeveloperContract developerContract;
	
	@Autowired
	private IAppInfoContract appInfoContract;
	
	@Autowired
	private IAppContract appContract;
	
	@RequestMapping(value="/list")
	public String accountList() throws Exception{
		return "developer/account/list";
	}
	
	@RequestMapping(value="/list/ajax" , method=RequestMethod.POST)
	public @ResponseBody DataTableResult<AccountInfoDto> ajaxList(@RequestBody DataTableQueryDto query) throws Exception{
		int pageSize = WebUtis.getPageSize(query);
		int currentPage = WebUtis.getCurrentPage(query);
		PageModel pageModel = PageModel.getPageModel();
		//排序
		if(Tools.isNotNull(query.getOrder())) {
			for(DataTableOrder order : query.getOrder()) {
				pageModel.addSort(order.getData(), order.getDir());
			}
		}
		
		//总计
		long totalLength = developerContract.count(pageModel);
		DataTableResult<AccountInfoDto> result = DataTableResult.getEmptyResult(query.getDraw() , totalLength , AccountInfoDto.class);
		PageBean pb = new PageBean(totalLength , pageSize , currentPage);
		List<AccountInfoDto> dtos = new ArrayList<AccountInfoDto>();
		if(totalLength > 0) {
			//分页查询
			pageModel.addPageField(pb.getCurrentPage(), pb.getPageSize());
			List<DeveloperEntity> accounts = developerContract.load(pageModel);
			if(Tools.isNotNull(accounts)) {
				for (DeveloperEntity account : accounts) {
					AccountInfoDto dto = new AccountInfoDto();
					AppEntity app = appContract.findById(account.getApp_id());
					AppInfoEntity appInfo = appInfoContract.findById(account.getApp_info_id());
					dto.setId(account.getId());
					dto.setUsername(account.getUsername());
					if(Tools.isNotNull(app))
						dto.setApp(app.getApp_name());
					if(Tools.isNotNull(appInfo))
						dto.setApp_info(appInfo.getApp_name());
					dto.setVirtual_limit(account.getVirtual_limit());
					dto.setInstall_limit(account.getInstall_limit());
					dto.setCreate_time(Tools.getDateTime(account.getCreate_time()));
					dtos.add(dto);
				}
			}
		}
		result.setData(dtos);
		return result;
	}
	
	@RequestMapping(value="/add")
	public String ajaxAdd(Model model) throws Exception{
		model.addAttribute("appList", appContract.load(PageModel.getPageModel()));
		return "developer/account/ajax/add";
	}
	
	/**
	 * 保存APP信息
	 * @param request - HttpServletRequest
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/add/ajax" , method=RequestMethod.POST)
	public @ResponseBody ActionResult ajaxAddSave(HttpServletRequest request) throws Exception{
		String appId = request.getParameter("app_id");
		String username = request.getParameter("username");
		if(Tools.isNull(username)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode(),"账号不能为空");
		}
		String password = request.getParameter("password");
		if(Tools.isNull(password)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode(),"密码不能为空");
		}
		AppEntity app = appContract.findById(Tools.parseLong(appId));
		if(app == null) {
			return ActionResult.fail(ErrCodeEnum.db_not_found.getCode(), "appId="+appId+"不存在");
		}
		//查看是否已经绑定对应的appid
		if(developerContract.findByProperty("app_info_id", appId) != null) {
			return ActionResult.fail(ErrCodeEnum.db_error.getCode(), "对应的app已经绑定开发者");
		}
		//检测是否已经录入了对应的开发者账号
		if(developerContract.findByProperty("username", username) != null) {
			return ActionResult.fail(ErrCodeEnum.db_error.getCode(), "对应的开发者账号已经存在");
		}
		AppInfoEntity appInfo = appInfoContract.findById(Long.parseLong(appId));
		if(Tools.isNull(appInfo)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error);
		}
		
		Date currDate = new Date();
		DeveloperEntity account = new DeveloperEntity();
		account.setUsername(username);
		account.setPassword(password);
		account.setApp_info_id(Long.parseLong(appId));
		account.setApp_id(appInfo.getApp_id());
		account.setCreate_time(currDate);
		account.setUpdate_time(currDate);
		developerContract.insert(account);
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_BOX_FUNCTION);
	}
	
}
