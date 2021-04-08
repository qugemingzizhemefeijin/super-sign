package com.tigerjoys.shark.supersign.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.shark.supersign.comm.utils.BeanToXml;
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.inter.contract.IUdidSuccessContract;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidSuccessEntity;
import com.tigerjoys.shark.supersign.service.IAppInstallService;

/**
 * 苹果浏览器跳转获取UDID的请求
 *
 */
@Controller
@RequestMapping("/html")
public class UDIDController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UDIDController.class);
	
	@Autowired
	private IUdidSuccessContract udidSuccessContract;
	
	@Autowired
	private IAppInstallService appInstallService;
    
	/**
	 * 获取苹果UDID回调接口
	 * @param appid - APPID
	 * @param request - HttpServletRequest
	 * @param response - HttpServletResponse
	 * @return String
	 * @throws IOException
	 */
    @PostMapping("/udid/{appid}/install")
    public String udid(@PathVariable("appid")long appid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        //获取HTTP请求的输入流
        InputStream is = request.getInputStream();
        //已HTTP请求输入流建立一个BufferedReader对象
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();

        //读取HTTP请求内容
        String buffer = null;
        while ((buffer = br.readLine()) != null) {
            sb.append(buffer);
        }
        
        String body = sb.toString();
        LOGGER.info(body);
        
        String content = body.substring(sb.toString().indexOf("<?xml"), sb.toString().indexOf("</plist>") + 8);

        try {
            Map<String, String> udidMap = BeanToXml.xmlToMap(content);
            if (Tools.isNotNull(udidMap)) {
                String udid = udidMap.get("UDID");
                if (!appInstallService.inTranscocdQueue(appid, udid)) {
                	//此处判断是否已经成功安装过了
                    UdidSuccessEntity us = udidSuccessContract.findByUdidAndAppId(udid, appid);
                    if(us == null) {
                    	//此处触发分配一个开发者帐号以及一个appinfo
                        DeveloperEntity developer = appInstallService.consume(appid, udid);
                        if (developer != null) {
                        	//加入到安装队列中
                            appInstallService.sendJob(appid, udid);
                        }
                    }
                }
                String url = Const.WEBSITE + "/html/app/"+appid+"?udid=" + udid;
                LOGGER.info(url);
                response.setHeader("Location", url);
                response.setStatus(301);
            }
        } catch (Exception e) {
            LOGGER.error("获取udid失败", e);
        }

        return "ping";
    }


}
