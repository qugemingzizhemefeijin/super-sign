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
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.exception.RevertVirtualLimitException;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperCerEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;
import com.tigerjoys.shark.supersign.process.IJobProcessChain;

/**
 * 检查要签名的IPA文件
 *
 */
@Service
public class JobProcessCheckIPAHandler implements IJobProcessChain {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobProcessCheckIPAHandler.class);
	
	@Autowired
	private ICloudStorage cloudStorage;
	
	@Autowired
	private CacheRedis cacheRedis;

	@Override
	public boolean doProcess(String udid, AppEntity app, AppInfoEntity appInfo, DeveloperEntity developer, DeveloperCerEntity cer, UdidAddLogEntity addLog, InstallLogEntity log, StopWatch sw) throws Exception {
		sw.start("checkIPA FILE");
		
		String key = SignHelper.getSignProcessKey(appInfo.getApp_id(), udid);
		//设置安装进度
		cacheRedis.setObject(key, RedisConstant.SIGN_PROCESS_KEY_EXPIRE, SignProcess.process(5));
		
		try {
			String ipaPath = appInfo.getPath();
			String sourceIPA = Const.STORAGE_PATH + ipaPath;
			LOGGER.info("source IPA PATH = {}" , sourceIPA);
			//判断原始IPA是否存在
			File file = new File(sourceIPA);
			if(!file.exists()) {
				LOGGER.error("sourceIPA not exists!");
				//如果文件不存在，则判断远程存储平台是否有
				if(!cloudStorage.existsFile(ipaPath)) {
					LOGGER.error("sourceIPA cloud storage not exists!");
					throw new RevertVirtualLimitException("远程与本地未找到源IPA文件");
				}
				
				//创建目录
				SignHelper.createFileDirectory(sourceIPA);
				
				//如果远程有此文件，则下载到本地
				if(!cloudStorage.readFile(ipaPath, sourceIPA)) {
					LOGGER.error("sourceIPA cloud storage download failure!");
					
					throw new RevertVirtualLimitException("下载源IPA文件发生异常");
				}
			}
			
			cacheRedis.setObject(key, RedisConstant.SIGN_PROCESS_KEY_EXPIRE, SignProcess.process(10));
			
			return true;
		} finally {
			sw.stop();
		}
	}

	@Override
	public int getOrder() {
		return 200;
	}

}
