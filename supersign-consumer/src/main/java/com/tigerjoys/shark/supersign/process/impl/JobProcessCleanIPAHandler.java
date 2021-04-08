package com.tigerjoys.shark.supersign.process.impl;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.shark.supersign.comm.sign.SignHelper;
import com.tigerjoys.shark.supersign.comm.storage.FileInfo;
import com.tigerjoys.shark.supersign.comm.storage.ICloudStorage;
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperCerEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;
import com.tigerjoys.shark.supersign.process.IJobProcessChain;

/**
 * 清理已经废弃的包
 *
 */
@Service
public class JobProcessCleanIPAHandler implements IJobProcessChain {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobProcessCleanIPAHandler.class);
	
	@Autowired
	private ICloudStorage cloudStorage;

	@Override
	public boolean doProcess(String udid, AppEntity app, AppInfoEntity appInfo, DeveloperEntity developer, DeveloperCerEntity cer, UdidAddLogEntity addLog, InstallLogEntity log, StopWatch sw) throws Exception {
		sw.start("clean IPA");
		try {
			//清理过期的IPA文件
			//获取本地的IPA文件列表
			String relativeDir = SignHelper.getFileParentDiectory(appInfo.getResign_ipa_path());
			String currFileName = SignHelper.getFileName(appInfo.getResign_ipa_path());
			this.cleanFile(relativeDir, currFileName);
			
			//清理过期的PLIST文件
			relativeDir = SignHelper.getFileParentDiectory(appInfo.getResign_plist_path());
			currFileName = SignHelper.getFileName(appInfo.getResign_plist_path());
			this.cleanFile(relativeDir, currFileName);
			
			//清理过期的MP文件
			relativeDir = SignHelper.getFileParentDiectory(appInfo.getResign_mp_path());
			currFileName = SignHelper.getFileName(appInfo.getResign_mp_path());
			this.cleanFile(relativeDir, currFileName);
			
			return true;
		} finally {
			sw.stop();
		}
	}
	
	/**
	 * 需要清理的相对目录的文件
	 * @param relativeDir - 相对目录
	 * @param currFileName - 当前文件的名称
	 */
	private void cleanFile(String relativeDir, String currFileName) {
		LOGGER.info("清理目录：{} , currFileName = {}" , relativeDir, currFileName);
		
		int count = 0;
		long currTime = System.currentTimeMillis();
		
		//清理本地文件
		File f = new File(Const.STORAGE_PATH + relativeDir);
		if(f.exists() && f.isDirectory()) {
			for(File dfile : f.listFiles()) {
				String name = SignHelper.getFileName(dfile.getName());
				if(Tools.isNull(name)) {
					dfile.delete();
					count++;
				} else {
					if(!name.equals(currFileName)) {
						if(Tools.parseLong(name) < currTime - Tools.DAY_MILLIS) {
							dfile.delete();
							count++;
						}
					}
				}
			}
		}
		
		LOGGER.info("清理本地文件夹 {} , 清理过期文件数量：{}" , relativeDir, count);
		
		count = 0;
		//清理远程文件
		List<FileInfo> fileList = cloudStorage.readDir(relativeDir);
		if(Tools.isNotNull(fileList)) {
			for(FileInfo dfile : fileList) {
				String name = SignHelper.getFileName(dfile.getName());
				if(Tools.isNull(name)) {
					cloudStorage.deleteFile(dfile.getPath());
					count++;
				} else {
					if(!name.equals(currFileName)) {
						if(Tools.parseLong(name) < currTime - Tools.DAY_MILLIS) {
							cloudStorage.deleteFile(dfile.getPath());
							count++;
						}
					}
				}
			}
		}
		
		LOGGER.info("本次远程文件夹 {} , 清理过期文件数量：{}" , relativeDir, count);
	}
	
	public static void main(String[] args) {
		File f = new File("E:/");
		for(File a : f.listFiles()) {
			System.err.println(a.getName());
		}
	}
	
	@Override
	public int getOrder() {
		return 1000;
	}

}
