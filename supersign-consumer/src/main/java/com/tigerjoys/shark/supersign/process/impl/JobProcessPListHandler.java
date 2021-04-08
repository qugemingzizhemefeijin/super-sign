package com.tigerjoys.shark.supersign.process.impl;

import java.io.FileNotFoundException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.tigerjoys.nbs.common.cache.CacheRedis;
import com.tigerjoys.shark.supersign.comm.constant.RedisConstant;
import com.tigerjoys.shark.supersign.comm.sign.SignHelper;
import com.tigerjoys.shark.supersign.comm.sign.SignProcess;
import com.tigerjoys.shark.supersign.comm.storage.ICloudStorage;
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.exception.RevertVirtualLimitException;
import com.tigerjoys.shark.supersign.inter.contract.IAppInfoContract;
import com.tigerjoys.shark.supersign.inter.contract.IUdidSuccessContract;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperCerEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidSuccessEntity;
import com.tigerjoys.shark.supersign.process.IJobProcessChain;
import com.tigerjoys.shark.supersign.service.IAppModelFileService;
import com.tigerjoys.shark.supersign.service.IInstallLogService;

/**
 * 生成最新的plist文件
 *
 */
@Service
public class JobProcessPListHandler implements IJobProcessChain {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobProcessPListHandler.class);
	
	@Autowired
	private ICloudStorage cloudStorage;
	
	@Autowired
	private CacheRedis cacheRedis;
	
	@Autowired
	private IAppInfoContract appInfoContract;
	
	@Autowired
	private IAppModelFileService appModelFileService;
	
	@Autowired
	private IUdidSuccessContract udidSuccessContract;
	
	@Autowired
	private IInstallLogService installLogService;
	
	@Override
	public boolean doProcess(String udid, AppEntity app, AppInfoEntity appInfo, DeveloperEntity developer, DeveloperCerEntity cer, UdidAddLogEntity addLog, InstallLogEntity log, StopWatch sw) throws Exception {
		sw.start("PList Create");
		try {
			//生成最新的plist文件
			String plistPath = SignHelper.getPlistFilePath(app.getId());
			String newPlistContent = genPlist(app, appInfo);
			
			if(cloudStorage.writeFile(plistPath, newPlistContent, true)) {
				//更新到数据库
				appInfo.setResign_plist_path(plistPath);
				appInfoContract.update(appInfo);
				
				Date currDate = new Date();
				
				//更新数据
				installLogService.installSuccess(log.getId());
				
				//新增成功记录
				UdidSuccessEntity succ = new UdidSuccessEntity();
				succ.setApp_id(app.getId());
				succ.setApp_info_Id(appInfo.getId());
				succ.setCreate_time(currDate);
				succ.setCer_Id(cer.getId());
				succ.setDeveloper_id(developer.getId());
				succ.setResign_ipa_path(appInfo.getResign_ipa_path());
				succ.setResign_plist_path(appInfo.getResign_ipa_path());
				succ.setUdid(udid);
				succ.setUpdate_time(currDate);
				udidSuccessContract.insert(succ);
				
				String key = SignHelper.getSignProcessKey(appInfo.getApp_id(), udid);
				cacheRedis.setObject(key, RedisConstant.SIGN_PROCESS_KEY_EXPIRE, SignProcess.success(Const.CDN_SITE + plistPath));
				
				return true;
			} else {
				LOGGER.warn("appid = {}, udid = {} , write plist file error", app.getId(), udid);
				throw new RevertVirtualLimitException("上传plist文件反生异常");
				
			}
		} finally {
			sw.stop();
		}
	}
	
	/**
	 * 自动生成最新的plist文件
	 * @param app - AppEntity
	 * @param appInfo AppInfoEntity
	 * @return String
	 * @throws FileNotFoundException
	 */
	private String genPlist(AppEntity app, AppInfoEntity appInfo) throws FileNotFoundException {
		String plist = appModelFileService.getPListModelFile(appInfo.getId());
		
		return String.format(plist, Const.CDN_SITE + appInfo.getResign_ipa_path(), Const.CDN_SITE + appInfo.getIcon_path(), Const.CDN_SITE + appInfo.getIcon_path(),
				appInfo.getBundle_id(), appInfo.getVersion(), app.getApp_name());
	}

	@Override
	public int getOrder() {
		return 700;
	}

}
