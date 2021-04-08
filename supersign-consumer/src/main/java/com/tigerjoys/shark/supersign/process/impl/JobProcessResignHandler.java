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
 * 重签名服务
 *
 */
@Service
public class JobProcessResignHandler implements IJobProcessChain {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobProcessResignHandler.class);
	
	@Autowired
	private ICloudStorage cloudStorage;
	
	@Autowired
	private CacheRedis cacheRedis;
	
	@Autowired
	private IAppInfoContract appInfoContract;

	@Override
	public boolean doProcess(String udid, AppEntity app, AppInfoEntity appInfo, DeveloperEntity developer, DeveloperCerEntity cer, UdidAddLogEntity addLog, InstallLogEntity log, StopWatch sw) throws Exception {
		sw.start("Resign");
		
		try {
			String key = SignHelper.getSignProcessKey(appInfo.getApp_id(), udid);
			
			String ipaPath = appInfo.getPath();
			String mpPath = appInfo.getResign_mp_path();
			String publicPath = cer.getPublic_pem_path();
			String privatePath = cer.getPrivate_pem_path();
			String targetPath = SignHelper.getSignIpaFilePath(app.getId());
			
			String sourceIPA = Const.STORAGE_PATH + ipaPath;//源IPA文件
			String fullPath = Const.STORAGE_PATH + mpPath;//MP文件
			String publicPathFull = Const.STORAGE_PATH + publicPath;//公钥
			String privatePathFull = Const.STORAGE_PATH + privatePath;//私钥
			String targetIPA = Const.STORAGE_PATH + targetPath;//目标IPA
			
			LOGGER.info("resign source = {}, mp = {}, public key = {}, private key = {}, targetIPA = {}", sourceIPA, fullPath, publicPathFull, privatePathFull, targetIPA);
			//设置安装进度
			cacheRedis.setObject(key, RedisConstant.SIGN_PROCESS_KEY_EXPIRE, SignProcess.process(47));
			
			//获取公私钥
			checkKeyFile(publicPath, publicPathFull);
			checkKeyFile(privatePath, privatePathFull);
			
			//创建目录
			SignHelper.createFileDirectory(targetIPA);
			
			//resign
			//如果还是异常，则需要重新生成mobileProvision文件，应该可以调用同一个脚本，但是不会造成重复注册udid的
			String[] command = new String[] {"ruby", Const.SCRIPT_RUBY_PATH + "/resign.rb", String.valueOf(cer.getId()), udid, Const.STORAGE_PATH, targetIPA};
			if(!ProcessCommandUtils.processCommend(command)) {
				throw new RevertVirtualLimitException("执行ipa重签名脚本发生异常");
			}

			sw.stop();
			cacheRedis.setObject(key, RedisConstant.SIGN_PROCESS_KEY_EXPIRE, SignProcess.process(65));
			
			sw.start("Resign Upload");
			//此处将打包后的数据更新到远程
			if(cloudStorage.writeFile(targetPath, new File(targetIPA), true)) {
				//重签名成功更新数据库
				appInfo.setResign_ipa_path(targetPath);
				appInfoContract.update(appInfo);
			} else {
				throw new RevertVirtualLimitException("上传重签名后的IPA文件失败");
			}
			cacheRedis.setObject(key, RedisConstant.SIGN_PROCESS_KEY_EXPIRE, SignProcess.process(90));
			
			return true;
		} finally {
			sw.stop();
		}
	}
	
	/**
	 * 检查密钥文件是否存在
	 * @param relativePath - 公钥相对路径
	 * @param fullPath - 公钥全路径
	 * @throws RevertVirtualLimitException 
	 */
	private void checkKeyFile(String relativePath, String fullPath) throws RevertVirtualLimitException {
		File f1 = new File(fullPath);
		if(!f1.exists()) {
			//此处直接从远程拉取，如果远程也拉取失败，则抛出异常
			if(!cloudStorage.readFile(relativePath, f1)) {
				throw new RevertVirtualLimitException("relativePath = " + fullPath + " is not found");
			}
		}
	}

	@Override
	public int getOrder() {
		return 500;
	}

}
