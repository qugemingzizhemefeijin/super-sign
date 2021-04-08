package com.tigerjoys.shark.supersign.controller;

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

import com.google.common.collect.Maps;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.page.PageBean;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.shark.supersign.comm.enums.InstallLogStatusEnums;
import com.tigerjoys.shark.supersign.context.ActionResult;
import com.tigerjoys.shark.supersign.dto.list.InstallLogDto;
import com.tigerjoys.shark.supersign.dto.query.InstallLogQueryDto;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;
import com.tigerjoys.shark.supersign.inter.contract.IAppContract;
import com.tigerjoys.shark.supersign.inter.contract.IInstallLogContract;
import com.tigerjoys.shark.supersign.inter.contract.IUdidSuccessContract;
import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;
import com.tigerjoys.shark.supersign.service.IInstallLogService;
import com.tigerjoys.shark.supersign.utils.DataTableOrder;
import com.tigerjoys.shark.supersign.utils.DataTableResult;
import com.tigerjoys.shark.supersign.utils.WebUtis;

/**
 * APP安装日志
 *
 */
@Controller
@RequestMapping(value="/sign/installlog")
public class InstallLogController extends BaseController {
	
	@Autowired
	private IInstallLogContract installLogContract;
	
	@Autowired
	private IUdidSuccessContract udidSuccessContract;
	
	@Autowired
	private IAppContract appContract;
	
	@Autowired
	private IInstallLogService installLogService;
	
	/**
	 * 安装日志列表页面
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public String adminList(HttpServletRequest request , Model model) throws Exception {
		//此处获取所有的app列表
		model.addAttribute("appList", appContract.load(PageModel.getPageModel()));
		//状态列表
		model.addAttribute("statusList", InstallLogStatusEnums.values());
		return "sign/installlog/list";
	}
	
	/**
	 * Ajax加载安装日志数据
	 * @param query - 查询对象
	 * @param request - HttpServletRequest
	 * @return DataTableResult<InstallLogDto>
	 * @throws Exception
	 */
	@RequestMapping(value="/list/ajax" , method=RequestMethod.POST)
	public @ResponseBody DataTableResult<InstallLogDto> ajaxList(@RequestBody InstallLogQueryDto query , HttpServletRequest request) throws Exception{
		int pageSize = WebUtis.getPageSize(query);
		int currentPage =WebUtis.getCurrentPage(query);
		
		PageModel pageModel = PageModel.getPageModel();
		
		if(query.getAppId() != null) {
			pageModel.addQuery(Restrictions.eq("app_id", query.getAppId()));
		}
		if(query.getUdid() != null) {
			pageModel.addQuery(Restrictions.eq("udid", query.getUdid()));
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
		long totalLength = installLogContract.count(pageModel);
		
		DataTableResult<InstallLogDto> result = DataTableResult.getEmptyResult(query.getDraw() , totalLength , InstallLogDto.class);
		
		PageBean pb = new PageBean(totalLength , pageSize , currentPage);
		if(totalLength > 0) {
			//分页查询
			pageModel.addPageField(pb.getCurrentPage(), pb.getPageSize());
			List<InstallLogEntity> logList = installLogContract.load(pageModel);
			if(Tools.isNotNull(logList)) {
				result.setData(installLogService.transferLogList(logList));
			}
		}
		
		return result;
	}
	
	/**
	 * 移除掉成功标识
	 * @param request - HttpServletRequest
	 * @param id - 日志ID
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax/delete" , method=RequestMethod.POST)
	public @ResponseBody ActionResult ajaxRemoveSuccess(HttpServletRequest request , @RequestParam("id") long id) throws Exception {
		if(id <= 0) {
			return ActionResult.fail(ErrCodeEnum.parameter_error);
		}
		InstallLogEntity log = installLogContract.findById(id);
		if(log.getStatus() != InstallLogStatusEnums.SUCCESS.getCode()) {
			return ActionResult.fail(ErrCodeEnum.state_error.getCode(), "只有成功的记录才能操作");
		}
		//移除标识就可以被重复安装了
		Map<String, Object> updateStatement = Maps.newHashMap();
		updateStatement.put("ident", null);
		installLogContract.updateById(updateStatement, id);
		
		PageModel pageModel = PageModel.getPageModel();
		pageModel.addQuery(Restrictions.eq("udid", log.getUdid()));
		pageModel.addQuery(Restrictions.eq("app_id", log.getApp_id()));
		udidSuccessContract.deleteByCondition(pageModel);
		
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_REFRESH_CURRENT_PAGE_TABLE);
	}

}
