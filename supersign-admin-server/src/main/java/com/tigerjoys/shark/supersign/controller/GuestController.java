package com.tigerjoys.shark.supersign.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.nbs.web.annotations.LogRule;
import com.tigerjoys.nbs.web.annotations.NoLogin;
import com.tigerjoys.nbs.web.annotations.NoPermission;
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.constant.Param;
import com.tigerjoys.shark.supersign.context.ActionResult;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;
import com.tigerjoys.shark.supersign.service.IAdminService;
import com.tigerjoys.shark.supersign.utils.FileUploadResult;
import com.tigerjoys.shark.supersign.utils.Helper;

/**
 * 系统的控制层，如登录，500，400等
 *
 */
@Controller
public class GuestController extends BaseController {
	
	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private IAdminService adminService;
	
	/**
	 * 登录页面
	 * @param request - HttpServletRequest
	 * @return String
	 * @throws Exception
	 */
	@NoLogin
	@RequestMapping(value="/login")
	public String login(HttpServletRequest request , Model model) throws Exception{
		String redirectURL = request.getParameter(Param.REDIRECT_URL);
		if(redirectURL != null && redirectURL.length() > 0) {
			model.addAttribute(Param.REDIRECT_URL, redirectURL);
		}
		return "login";
	}
	
	/**
	 * 登录检查
	 * @param request - HttpServletRequest
	 * @return ActionResult
	 * @throws Exception
	 */
	@NoLogin
	@LogRule(filter= {"password"})
	@RequestMapping(value="/login/check")
	public @ResponseBody ActionResult loginCheck(HttpServletRequest request) throws Exception{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String redirectURL = request.getParameter(Param.REDIRECT_URL);
		if(Tools.isNull(redirectURL)) {
			redirectURL = request.getContextPath()+"/";
		}
		
		try {
			int resultCode = adminService.login(username, password);
			if(resultCode == ErrCodeEnum.success.getCode()) {
				return ActionResult.success("登录成功，正在跳转中...", redirectURL, ActionResult.RESULT_TYPE_URL);
			} else {
				return ActionResult.fail(resultCode, "登录失败");
			}
		} catch (Exception e) {
			logger.error("", e);
			return ActionResult.fail();
		}
	}
	
	/**
	 * 退出登录
	 * @param request - HttpServletRequest
	 * @param session - HttpSession
	 * @return String
	 * @throws Exception
	 */
	@NoPermission
	@RequestMapping(value="/loginout")
	public String loginout(HttpServletRequest request , HttpSession session , ModelMap model) throws Exception {
		session.removeAttribute(Param.ADMID_LOGIN_SESSION);
		session.removeAttribute(Param.ADMIN_PERMISSION_URL_SESSION);
		
		model.clear();
		
		return "redirect:/login";
	}
	
	/**
	 * kindeditor编辑器上传文件
	 * @param request - HttpServletRequest
	 * @param response - HttpServletResponse
	 * @return Map<String,Object>
	 * @throws IOException 
	 */
	@NoPermission
	@RequestMapping(value = "/keUpload", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> kindEditor_pic(HttpServletRequest request) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		FileUploadResult fileResult = Helper.uploadPicture(multipartRequest.getFile("imgFile"), null);
		
		Map<String,Object> map = new HashMap<String,Object>();
		if(fileResult.getCode() == ErrCodeEnum.success.getCode()) {
			map.put("error", 1);
			map.put("message", fileResult.getMsg());
		} else {
			map.put("error", 0);
			map.put("message", fileResult.getMsg());
			map.put("url", Const.getCdn(fileResult.getFilePath()));
		}
		
		return map;
	}
	
	/**
	 * 500错误页面
	 * @return String
	 * @throws Exception
	 */
	@NoPermission
	@NoLogin
	@RequestMapping(value="/500")
	public String error500() throws Exception {
		return "500";
	}
	
	/**
	 * 404错误页面
	 * @return String
	 * @throws Exception
	 */
	@NoPermission
	@NoLogin
	@RequestMapping(value="/404")
	public String error404() throws Exception {
		return "404";
	}

}
