package com.tigerjoys.shark.supersign.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UrlPathHelper;


/**
 * web工具类
 *
 */
public class WebUtis {
	
	/**
	 * URL Path 解析帮助类
	 */
	private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	/**
	 * 获得分页数
	 * @return
	 */
	public static int getPageSize(DataTableQueryDto dto){
		int pageSize = dto.getLength();
		
		if(pageSize<= 0) return 20;
		else if(pageSize > 100) return 100;
		else return pageSize;
	}
	
	/**
	 * 获得当前页
	 * @param dto - BaseListSearchDto
	 * @return int
	 */
	public static int getCurrentPage(DataTableQueryDto dto){
		if(dto.getTotalLength() == 0) return 1;
		
		int currentPage = dto.getStart()/dto.getLength()+1;
		return currentPage;
	}
	
	/**
	 * 获得本次Request的Url Path
	 * @param request - HttpServletRequest
	 * @return String
	 */
	public static String getLookupPathForRequest(HttpServletRequest request) {
		return urlPathHelper.getLookupPathForRequest(request);
	}
	
	/**
	 * 获得项目的ContextPath
	 * @param request - HttpServletRequest
	 * @return String
	 */
	public static String getContextPath(HttpServletRequest request) {
		return urlPathHelper.getContextPath(request);
	}

}
