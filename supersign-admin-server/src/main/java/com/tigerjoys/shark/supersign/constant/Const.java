package com.tigerjoys.shark.supersign.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * 常量
 *
 */
public final class Const {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Const.class);
	
	private static Properties properties = new Properties();
	
	static {
		try(InputStream is = new ClassPathResource("appservice.properties").getInputStream()) {
			properties.load(is);
		} catch (IOException e) {
			LOGGER.error(e.getMessage() , e);
		}
	}
	
	/**
	 * 项目的属性dev test prod
	 */
	public static final String PROJECT_PROFILE = properties.getProperty("env");
	
	//是否是测试环境
	public static final boolean IS_TEST = !"prod".equals(PROJECT_PROFILE);
	
	/**
	 * 图片域名
	 */
	public static final String HTTP_PIC_URL = properties.getProperty("image_url", "http://imgcdn.lanmifeng.com");
	
	/**
	 * 网站名称
	 */
	public static final String WEB_NAME = "管理平台";
	
	/**
	 * CopyRight
	 */
	public static final String COPY_RIGHT = "2020 © "+WEB_NAME+".";
	
	/**
	 * 超级管理员角色ID
	 */
	public static final int SUPER_ROLE = 1;
	
	/**
	 * 获得图片的根路径
	 * 
	 * @return String
	 */
	public final static String getCdn() {
		return HTTP_PIC_URL;
	}
	
	/**
	 * 获得CDN的绝对地址
	 * @param relativeUrl - 相对地址
	 * @return String
	 */
	public final static String getCdn(String relativeUrl) {
		if(relativeUrl == null || relativeUrl.length() == 0) return HTTP_PIC_URL;
		
		if(relativeUrl.charAt(0) == 'h') {
			return relativeUrl;
		} else {
			return HTTP_PIC_URL + relativeUrl;
		}
	}
	
	/**
	 * 判断是否是AJAX请求
	 * @param request - HttpServletRequest
	 * @return True or False
	 */
	public static boolean isAjax(HttpServletRequest request){
		return "true".equals(request.getParameter(Param._AJAX));
	}
	
}
