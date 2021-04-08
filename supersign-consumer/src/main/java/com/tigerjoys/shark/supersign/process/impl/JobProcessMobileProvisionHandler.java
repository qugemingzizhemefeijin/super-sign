package com.tigerjoys.shark.supersign.process.impl;

import java.io.File;

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
import com.tigerjoys.shark.supersign.comm.utils.ProcessCommandUtils;
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.exception.RevertVirtualLimitException;
import com.tigerjoys.shark.supersign.inter.contract.IAppInfoContract;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperCerEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;
import com.tigerjoys.shark.supersign.process.IJobProcessChain;

/**
 * 更新MobileProvision文件的操作
 *
 */
@Service
public class JobProcessMobileProvisionHandler implements IJobProcessChain {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobProcessMobileProvisionHandler.class);
	
	@Autowired
	private ICloudStorage cloudStorage;
	
	@Autowired
	private CacheRedis cacheRedis;
	
	@Autowired
	private IAppInfoContract appInfoContract;

	@Override
	public boolean doProcess(String udid, AppEntity app, AppInfoEntity appInfo, DeveloperEntity developer, DeveloperCerEntity cer, UdidAddLogEntity addLog, InstallLogEntity log, StopWatch sw) throws Exception {
		sw.start("Mobile Provision FILE");
		
		try {
			//判断此用户是否已经注册过udid了
			if(addLog == null) {
				//需要调用脚本生成最新的MP文件
				String mpPath = SignHelper.getMobileProvisionFilePath(app.getId());
				String fullPath = Const.STORAGE_PATH + mpPath;
				LOGGER.info("appid = {}, mpPath = {}" , app.getId(), fullPath);
				
				//创建目录
				SignHelper.createFileDirectory(fullPath);
				
				//此处注册udid并且下载到本地
				String[] command = new String[] {"ruby", Const.SCRIPT_RUBY_PATH + "/addUUid.rb", String.valueOf(cer.getId()), udid, fullPath};
				if(!ProcessCommandUtils.processCommend(command)) {
					throw new RevertVirtualLimitException("执行添加udid脚本发生异常");
				}
				
				//完成之后上传mp文件
				if(cloudStorage.writeFile(mpPath, new File(fullPath), true)) {
					appInfo.setResign_mp_path(mpPath);
					appInfoContract.update(appInfo);
				} else {
					throw new RevertVirtualLimitException("上传mobileProvision文件发生异常");
				}
			} else {
				String mpPath = appInfo.getResign_mp_path();
				
				//如果生成过了。则判断本地是否存在最新的文件
				String fullPath = Const.STORAGE_PATH + appInfo.getResign_mp_path();
				LOGGER.info("appid = {}, appname={}, appinfoid = {}, udid = {}, mbpath = {}", app.getId(), app.getApp_name(), appInfo.getId(), udid, fullPath);
				File mpFile = new File(fullPath);
				if(!mpFile.exists() && !cloudStorage.readFile(mpPath, fullPath)) {
					LOGGER.error("sourceIM cloud storage download failure!");
					
					//创建目录
					SignHelper.createFileDirectory(fullPath);
					
					//如果还是异常，则需要重新生成mobileProvision文件，应该可以调用同一个脚本，但是不会造成重复注册udid的
					String[] command = new String[] {"ruby", Const.SCRIPT_RUBY_PATH + "/addUUid.rb", String.valueOf(cer.getId()), udid, fullPath};
					if(!ProcessCommandUtils.processCommend(command)) {
						throw new RevertVirtualLimitException("执行下载mobileProvision文件发生异常");
					}
					
					//完成之后上传mp文件
					if(cloudStorage.writeFile(mpPath, new File(fullPath), true)) {
						appInfo.setResign_mp_path(mpPath);
						appInfoContract.update(appInfo);
					} else {
						throw new RevertVirtualLimitException("上传mobileProvision文件发生异常");
					}
				}
			}

			String key = SignHelper.getSignProcessKey(appInfo.getApp_id(), udid);
			cacheRedis.setObject(key, RedisConstant.SIGN_PROCESS_KEY_EXPIRE, SignProcess.process(25));
			
			return true;
		} finally {
			sw.stop();
		}
	}
	
	

	@Override
	public int getOrder() {
		return 300;
	}

}
