package com.tigerjoys.shark.supersign.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.nbs.web.annotations.NoPermission;

/**
 * 首页
 *
 */
@Controller
public class DashboardController extends BaseController {
    
	/**
	 * 首页
	 * @param request - HttpServletRequest
	 * @return String
	 * @throws Exception
	 */
    @NoPermission
	@RequestMapping(value={"/","/index"})
	public String index(HttpServletRequest request , Model model) throws Exception{
		return "index";
	}

}
