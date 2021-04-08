package com.tigerjoys.shark.supersign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tigerjoys.nbs.common.ApiResult;
import com.tigerjoys.nbs.web.BaseController;
import com.tigerjoys.shark.supersign.comm.sign.SignProcess;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;
import com.tigerjoys.shark.supersign.inter.contract.IAppContract;
import com.tigerjoys.shark.supersign.inter.contract.IUdidSuccessContract;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidSuccessEntity;
import com.tigerjoys.shark.supersign.service.IAppInstallService;
import com.tigerjoys.shark.supersign.utils.ServiceHelper;

/**
 * APP安装相关的接口
 *
 */
@Controller
@RequestMapping("/html")
public class AppController extends BaseController {
	
	@Autowired
	private IAppContract appContract;
	
	@Autowired
	private IAppInstallService appInstallService;
	
	@Autowired
	private IUdidSuccessContract udidSuccessContract;

	/**
	 * IOS APP安装界面
	 * @param appid - 要安装的ID
	 * @param udid - 待安装用户的udid
	 * @param model - Model
	 * @return String
	 * @throws Exception 
	 */
    @GetMapping("/app/{appid}")
    public String showAppPage(@PathVariable("appid") long appid, @RequestParam(value="udid",required=false) String udid, Model model) throws Exception {
    	if(appid <= 0) {
    		return errorPage("appid参数异常", model);
    	}
    	
    	AppEntity app = appContract.findById(appid);
    	if(app == null || app.getStatus() == 0) {
    		return errorPage("app不存在或已下架", model);
    	}
    	
    	app.setFull_icon_path(ServiceHelper.getCdnPhoto(app.getFull_icon_path()));
    	app.setIcon_path(ServiceHelper.getCdnPhoto(app.getIcon_path()));
    	app.setMbconfig(ServiceHelper.getCdnPhoto(app.getMbconfig()));
    	model.addAttribute("app", app);
        return "app";
    }

    /**
     * 开始安装
	 * @param appid - 要安装的ID
	 * @param udid - 待安装用户的udid
	 * @param model - Model
     * @return ApiResult<SignProcess>
     * @throws Exception 
     */
    @GetMapping(value = "/app/init/{appid}/{udid}")
    public @ResponseBody ApiResult<SignProcess> initSign(@PathVariable("appid") long appid, @PathVariable("udid") String udid, Model model) throws Exception {
    	if(appid <= 0) {
    		return ApiResult.fail(ErrCodeEnum.parameter_error);
    	}
    	AppEntity app = appContract.findById(appid);
    	if(app == null || app.getStatus() == 0) {
    		return ApiResult.fail(ErrCodeEnum.state_error.getCode(), "app不存在或已下架");
    	}
    	//判断是否在签名队列中
        if (appInstallService.inTranscocdQueue(appid, udid)) {
            return ApiResult.success(SignProcess.process(3));
        }
        //此处判断是否已经成功安装过了
        UdidSuccessEntity us = udidSuccessContract.findByUdidAndAppId(udid, appid);
        if(us != null) {
        	return ApiResult.success(SignProcess.success(us.getResign_plist_path()));
        }
        //此处触发分配一个开发者帐号以及一个appinfo
        DeveloperEntity developer = appInstallService.consume(appid, udid);
        if (developer == null) {
            return ApiResult.fail(20011, "无可安装设备数量");
        }
        //加入到安装队列中
        appInstallService.sendJob(appid, udid);
        
        return ApiResult.success(SignProcess.process(3));
    }

    /**
     * H5页面轮询安装进度
     * @param appid - APPID
     * @param udid - UDID
     * @return ApiResult<SignProcess>
     * @throws Exception 
     */
    @ResponseBody
    @GetMapping(value = "/app/process/{appid}/{udid}")
    public ApiResult<SignProcess> signProcess(@PathVariable("appid") long appid, @PathVariable("udid") String udid) throws Exception {
        return ApiResult.success(appInstallService.getSignProcess(appid, udid));
    }

}
