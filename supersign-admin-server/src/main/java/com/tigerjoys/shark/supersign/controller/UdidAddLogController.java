package com.tigerjoys.shark.supersign.controller;

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
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.shark.supersign.dto.list.UdidAddLogDto;
import com.tigerjoys.shark.supersign.dto.query.UdidAddLogQueryDto;
import com.tigerjoys.shark.supersign.inter.contract.IUdidAddLogContract;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;
import com.tigerjoys.shark.supersign.service.IUdidAddLogService;
import com.tigerjoys.shark.supersign.utils.DataTableOrder;
import com.tigerjoys.shark.supersign.utils.DataTableResult;
import com.tigerjoys.shark.supersign.utils.WebUtis;

/**
 * UUID添加日志查询
 *
 */
@Controller
@RequestMapping(value="/sign/udidlog")
public class UdidAddLogController extends BaseController {
	
	@Autowired
	private IUdidAddLogContract udidAddLogContract;
	
	@Autowired
	private IUdidAddLogService udidAddLogService;
	
	/**
	 * UUID绑定列表页面
	 * @param request - HttpServletRequest
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public String adminList(HttpServletRequest request , Model model) throws Exception {
		return "sign/udidlog/list";
	}
	
	/**
	 * Ajax加载安装日志数据
	 * @param query - 查询对象
	 * @param request - HttpServletRequest
	 * @return DataTableResult<InstallLogDto>
	 * @throws Exception
	 */
	@RequestMapping(value="/list/ajax" , method=RequestMethod.POST)
	public @ResponseBody DataTableResult<UdidAddLogDto> ajaxList(@RequestBody UdidAddLogQueryDto query , HttpServletRequest request) throws Exception{
		int pageSize = WebUtis.getPageSize(query);
		int currentPage =WebUtis.getCurrentPage(query);
		
		PageModel pageModel = PageModel.getPageModel();
		if(query.getUdid() != null) {
			pageModel.addQuery(Restrictions.eq("udid", query.getUdid()));
		}
		
		//排序
		if(Tools.isNotNull(query.getOrder())) {
			for(DataTableOrder order : query.getOrder()) {
				pageModel.addSort(order.getData(), order.getDir());
			}
		}
		
		//总计
		long totalLength = udidAddLogContract.count(pageModel);
		
		DataTableResult<UdidAddLogDto> result = DataTableResult.getEmptyResult(query.getDraw() , totalLength , UdidAddLogDto.class);
		
		PageBean pb = new PageBean(totalLength , pageSize , currentPage);
		if(totalLength > 0) {
			//分页查询
			pageModel.addPageField(pb.getCurrentPage(), pb.getPageSize());
			List<UdidAddLogEntity> logList = udidAddLogContract.load(pageModel);
			if(Tools.isNotNull(logList)) {
				result.setData(udidAddLogService.transferLogList(logList));
			}
		}
		
		return result;
	}

}
