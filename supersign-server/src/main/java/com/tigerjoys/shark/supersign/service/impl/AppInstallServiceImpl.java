package com.tigerjoys.shark.supersign.service.impl;

import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tigerjoys.nbs.common.cache.CacheRedis;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.shark.supersign.comm.constant.RedisConstant;
import com.tigerjoys.shark.supersign.comm.sign.SignHelper;
import com.tigerjoys.shark.supersign.comm.sign.SignProcess;
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.inter.contract.IAppInfoContract;
import com.tigerjoys.shark.supersign.inter.contract.IDeveloperContract;
import com.tigerjoys.shark.supersign.inter.contract.IUdidSuccessContract;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidSuccessEntity;
import com.tigerjoys.shark.supersign.service.IAppInstallService;

/**
 * APP安装服务接口实现类
 *
 */
@Service
public class AppInstallServiceImpl implements IAppInstallService {
	
	@Autowired
	private CacheRedis cacheRedis;
	
	@Autowired
	private IDeveloperContract developerContract;
	
	@Autowired
	private IUdidSuccessContract udidSuccessContract;
	
	@Autowired
	private IAppInfoContract appInfoContract;

	@Override
	public boolean inTranscocdQueue(long appId, String udid) throws Exception {
		String key = SignHelper.getSignProcessKey(appId, udid);
		
		return cacheRedis.exists(key);
	}
	
	@Override
	public DeveloperEntity consume(long appId, String udid) throws Exception {
		PageModel pageModel = PageModel.getLimitModel(0, 1);
		pageModel.addQuery(Restrictions.eq("app_id", appId));
		pageModel.addQuery(Restrictions.gt("virtual_limit", 0));
		pageModel.asc("id");
		
		List<DeveloperEntity> list = developerContract.load(pageModel);
		if(Tools.isNull(list)) {
			return null;
		}
		
		return list.get(0);
	}
	
	@Override
	public void sendJob(long appId, String udid) throws Exception {
		String key = SignHelper.getSignProcessKey(appId, udid);
		
		//设置安装进度
		cacheRedis.setObject(key, RedisConstant.SIGN_PROCESS_KEY_EXPIRE, SignProcess.process(3));
		//加入到签名队列中
		cacheRedis.rpush(RedisConstant.SIGN_QUEUE_KEY, "{\"appId\":\""+appId+"\",\"udid\":\""+udid+"\"}");
	}
	
	@Override
	public SignProcess getSignProcess(long appId, String udid) throws Exception {
		String key = SignHelper.getSignProcessKey(appId, udid);
		
		//首先从redis中 读取信息
		SignProcess process = (SignProcess)cacheRedis.getObject(key);
		if(process == null) {
			//那么从数据库中读取，是否已经成功安装过了
			UdidSuccessEntity us = udidSuccessContract.findByUdidAndAppId(udid, appId);
			if(us != null) {
				AppInfoEntity appInfo = appInfoContract.findById(us.getApp_info_Id());
				
				//重新丢到redis中
				SignProcess sp = SignProcess.success(URLEncoder.encode(Const.CDN_SITE + appInfo.getResign_plist_path(),"UTF-8"));
				cacheRedis.setObject(key, RedisConstant.SIGN_PROCESS_KEY_EXPIRE, sp);
				return sp;
			} else {
				return SignProcess.process(0);//返回0，页面则需要判断
			}
		} else {
			return process;
		}
	}

}
