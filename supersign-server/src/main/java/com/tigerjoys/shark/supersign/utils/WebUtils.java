package com.tigerjoys.shark.supersign.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UrlPathHelper;


/**
 * web工具类
 *
 */
public class WebUtils {
	
	/**
	 * URL Path 解析帮助类
	 */
	private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();
	
	/**
	 * 获得本次Request的Url Path
	 * @param request - HttpServletRequest
	 * @return String
	 */
	public static String getLookupPathForRequest(HttpServletRequest request) {
		return URL_PATH_HELPER.getLookupPathForRequest(request);
	}
	
	/**
	 * 获得项目的ContextPath
	 * @param request - HttpServletRequest
	 * @return String
	 */
	public static String getContextPath(HttpServletRequest request) {
		return URL_PATH_HELPER.getContextPath(request);
	}

}
