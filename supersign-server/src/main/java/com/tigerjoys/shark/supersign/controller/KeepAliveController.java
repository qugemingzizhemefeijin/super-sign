package com.tigerjoys.shark.supersign.controller;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tigerjoys.nbs.common.ActionResult;

/**
 * 主要用于监控整个服务是否能够被访问
 *
 */
@RestController
@RequestMapping("/monitor")
public class KeepAliveController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KeepAliveController.class);
	
	/**
	 * 应用健康检测
	 * @param response - HttpServletResponse
	 * @return ActionResult
	 * @throws Exception 
	 */
	@RequestMapping("/health")
	public void health(HttpServletResponse response) throws Exception {
		LOGGER.info("===============Monitor OK================");
		
		response.getWriter().print("OK");
	}

	@RequestMapping("/test")
	public ActionResult test() {
		return ActionResult.success(new Date());
	}
	
}
