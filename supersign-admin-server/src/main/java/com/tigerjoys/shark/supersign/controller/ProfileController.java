package com.tigerjoys.shark.supersign.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tigerjoys.nbs.common.utils.MD5;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.nbs.web.annotations.NoPermission;
import com.tigerjoys.shark.supersign.context.ActionResult;
import com.tigerjoys.shark.supersign.context.RequestUtils;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;
import com.tigerjoys.shark.supersign.inter.contract.IAdminContract;
import com.tigerjoys.shark.supersign.inter.entity.AdminEntity;
import com.tigerjoys.shark.supersign.utils.FileUploadResult;
import com.tigerjoys.shark.supersign.utils.Helper;

/**
 * 个人资料修改Controller
 *
 */
@Controller
@RequestMapping(value = "/user")
public class ProfileController extends BaseController {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private IAdminContract adminContract;
	
	/**
	 * 个人资料
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@NoPermission
	@RequestMapping(value = "/profile/info")
	public String userProfile(Model model) throws Exception {
		model.addAttribute("user", RequestUtils.getCurrent().getAdmin());
		model.addAttribute("tab", 1);
		return "profile/user";
	}
	
	/**
	 * 修改密码
	 * @param model - Model
	 * @return String
	 * @throws Exception
	 */
	@NoPermission
	@RequestMapping(value = "/profile/pwd")
	public String userPwd(Model model) throws Exception {
		model.addAttribute("user", RequestUtils.getCurrent().getAdmin());
		model.addAttribute("tab", 3);
		return "profile/user";
	}

	/**
	 * 修改当前登录用户的属性
	 * @param request - HttpServletRequest
	 * @return {@link ActionResult}
	 * @throws Exception
	 */
	@NoPermission
	@RequestMapping(value = "/profile/saveprofile")
	public @ResponseBody ActionResult saveProfile(HttpServletRequest request) throws Exception {
		String email = request.getParameter("email");
		String mobile = request.getParameter("mobile");
		
		if(!Tools.isEmail(email)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode() , "邮箱填写错误");
		}
		if(!Tools.isMobile(mobile)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode() , "手机填写错误");
		}
		
		AdminEntity temp = new AdminEntity();
		temp.setId(RequestUtils.getCurrent().getAdminId());
		temp.setMobile(mobile);
		temp.setEmail(email);
		temp.setUpdateDate(new Date());
		adminContract.update(temp);
		
		return ActionResult.success(ActionResult.RESULT_TYPE_NO_OPERATE);
	}
	
	/**
	 * 修改当前登录用户的属性
	 * @param request - HttpServletRequest
	 * @return {@link ActionResult}
	 * @throws Exception
	 */
	@NoPermission
	@RequestMapping(value = "/profile/savephoto")
	public @ResponseBody ActionResult savePhoto(HttpServletRequest request) throws Exception {
		String byteStr = request.getParameter("getDataURL");
		if (byteStr == null) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode(), "图片没有上传");
		}
		int delLength = byteStr.indexOf(',') + 1;
		if (delLength == -1) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode(), "图片上传格式有问题");
		}
		String str = byteStr.substring(delLength, byteStr.length() - delLength);
		try {
			byte[] b = Base64.decodeBase64(str);

			FileUploadResult fileResult = Helper.uploadPicture(b, "jpg", null);
			if (fileResult.getCode() != ErrCodeEnum.success.getCode()) {
				return ActionResult.fail(ErrCodeEnum.error.getCode(), fileResult.getMsg());
			}

			AdminEntity temp = new AdminEntity();
			temp.setId(RequestUtils.getCurrent().getAdminId());
			temp.setAvatar(fileResult.getFilePath());
			temp.setUpdateDate(new Date());
			adminContract.update(temp);

			return ActionResult.success(ActionResult.RESULT_TYPE_CLOSE_BOX_FUNCTION);
		} catch (Exception e) {
			logger.error("", e);

			return ActionResult.fail(ErrCodeEnum.error.getCode(), "上传图片保存发生异常");
		}
	}
	
	/**
	 * 修改当前登录用户的密码
	 * @param request - HttpServletRequest
	 * @return {@link ActionResult}
	 * @throws Exception
	 */
	@NoPermission
	@RequestMapping(value = "/profile/savepass")
	public @ResponseBody ActionResult savePass(HttpServletRequest request) throws Exception {
		String password = request.getParameter("password");
		if(Tools.isNull(password)) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode() , "密码没有填写");
		}
		if(password.length() < 5 || password.length() > 20) {
			return ActionResult.fail(ErrCodeEnum.parameter_error.getCode() , "密码设置只能填写5-20个字符");
		}
		
		AdminEntity temp = new AdminEntity();
		temp.setId(RequestUtils.getCurrent().getAdminId());
		temp.setPassword(MD5.encode(password));
		temp.setUpdateDate(new Date());
		adminContract.update(temp);
		
		return ActionResult.success(ActionResult.RESULT_TYPE_NO_OPERATE);
	}

}
