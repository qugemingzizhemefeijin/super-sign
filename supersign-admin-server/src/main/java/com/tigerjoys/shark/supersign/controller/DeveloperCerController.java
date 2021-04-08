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

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.page.PageBean;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.shark.supersign.context.ActionResult;
import com.tigerjoys.shark.supersign.dto.list.CerInfoDto;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;
import com.tigerjoys.shark.supersign.inter.contract.IAppInfoContract;
import com.tigerjoys.shark.supersign.inter.contract.IDeveloperCerContract;
import com.tigerjoys.shark.supersign.inter.contract.IDeveloperContract;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperCerEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.utils.DataTableOrder;
import com.tigerjoys.shark.supersign.utils.DataTableQueryDto;
import com.tigerjoys.shark.supersign.utils.DataTableResult;
import com.tigerjoys.shark.supersign.utils.WebUtis;

/**
 * 开发者秘钥管理
 */
@Controller
@RequestMapping(value="/developer/cer")
public class DeveloperCerController extends BaseController {


	@Autowired
	private IDeveloperContract developerContract;
	
	@Autowired
	private IDeveloperCerContract developerCerContract;
	
	@Autowired
	private IAppInfoContract appInfoContract;
	
	@RequestMapping(value="/list")
	public String accountList() throws Exception{
		return "developer/cer/list";
	}
	
	@RequestMapping(value="/list/ajax" , method=RequestMethod.POST)
	public @ResponseBody DataTableResult<CerInfoDto> ajaxList(@RequestBody DataTableQueryDto query) throws Exception{
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
		long totalLength = developerCerContract.count(pageModel);
		DataTableResult<CerInfoDto> result = DataTableResult.getEmptyResult(query.getDraw() , totalLength , CerInfoDto.class);
		PageBean pb = new PageBean(totalLength , pageSize , currentPage);
		List<CerInfoDto> dtos = new ArrayList<CerInfoDto>();
		if(totalLength > 0) {
			//分页查询
			pageModel.addPageField(pb.getCurrentPage(), pb.getPageSize());
			List<DeveloperCerEntity> cers = developerCerContract.load(pageModel);
			if(Tools.isNotNull(cers)) {
				for (DeveloperCerEntity cer : cers) {
					CerInfoDto dto = new CerInfoDto();
					dto.setId(cer.getId());
					DeveloperEntity account = developerContract.findById(cer.getDeveloper_id());
					if(Tools.isNotNull(account)) {
						AppInfoEntity appInfo = appInfoContract.findById(account.getApp_info_id());
						if(Tools.isNotNull(appInfo)) {
							dto.setApp_info(appInfo.getApp_name());
						}
						dto.setUsername(account.getUsername());
					}
					dto.setCertificate_id(cer.getCertificate_id());
					dto.setPrivate_pem_path(cer.getPrivate_pem_path());
					dto.setPublic_pem_path(cer.getPublic_pem_path());
					dto.setStatus(cer.getStatus());
					dto.setExpire_time(Tools.getDate(cer.getExpire_time()));
					dto.setCreate_time(Tools.getDateTime(cer.getCreate_time()));
					dtos.add(dto);
				}
			}
		}
		result.setData(dtos);
		return result;
	}
	
	@RequestMapping(value="/add")
	public String ajaxAdd(Model model) throws Exception{
		model.addAttribute("accounts", developerContract.load(PageModel.getPageModel()));
		return "developer/cer/ajax/add";
	}
	
	/**
	 * 保存APP信息
	 * @param request - HttpServletRequest
	 * @return ActionResult
	 * @throws Exception
	 */
	@RequestMapping(value="/add/ajax" , method=RequestMethod.POST)
	public @ResponseBody ActionResult ajaxAddSave(HttpServletRequest request) throws Exception{
		String account_id = request.getParameter("account_id");
		String certificate_id = request.getParameter("certificate_id");
		String status = request.getParameter("status");
		if(Tools.isNull(certificate_id)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode(),"秘钥标识不能为空");
		}
		String public_pem_path = request.getParameter("public_pem_path");
		if(Tools.isNull(public_pem_path)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode(),"公钥地址不能为空");
		}
		String private_pem_path = request.getParameter("private_pem_path");
		if(Tools.isNull(private_pem_path)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode(),"私钥地址不能为空");
		}
		//查看对应的开发者账号是否正常
		if(developerContract.findByProperty("id", account_id) == null) {
			return ActionResult.fail(ErrCodeEnum.db_error.getCode(), "绑定的开发者账号有问题");
		}
		
		Date currDate = new Date();
		DeveloperCerEntity cer = new DeveloperCerEntity();
		cer.setCertificate_id(certificate_id);
		cer.setDeveloper_id(Long.parseLong(account_id));
		cer.setPrivate_pem_path(private_pem_path);
		cer.setPublic_pem_path(public_pem_path);
		cer.setStatus(Integer.parseInt(status));
		cer.setCreate_time(currDate);
		cer.setUpdate_time(currDate);
		//cer.setExpire_time(expire_time);
		developerCerContract.insert(cer);
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_BOX_FUNCTION);
	}
	
	
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
			DeveloperCerEntity cer = developerCerContract.findById(Tools.parseInt(id));
			if(cer != null) {
				if((status == 1 && cer.getStatus() != 1)//解封
					|| (status == 0 && cer.getStatus() != 0)) {//查封
					DeveloperCerEntity temp = new DeveloperCerEntity();
					temp.setId(cer.getId());
					temp.setUpdate_time(new Date());
					temp.setStatus(status==1?1:0);
					developerCerContract.update(temp);
				}
			}
		}
		return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_REFRESH_CURRENT_PAGE_TABLE);
	}
}
