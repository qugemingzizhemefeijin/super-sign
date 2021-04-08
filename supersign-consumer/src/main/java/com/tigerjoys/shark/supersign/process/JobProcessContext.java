package com.tigerjoys.shark.supersign.process;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.beust.jcommander.internal.Lists;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperCerEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;
import com.tigerjoys.shark.supersign.service.IInstallLogService;

/**
 * 处理器维护
 *
 */
@Service
public class JobProcessContext {
	
	public final LinkedList<IJobProcessChain> processList = Lists.newLinkedList();
	
	@Autowired
	private IInstallLogService installLogService;
	
	/**
     * 注入所以实现了IJobProcessChain接口的Bean
     * @param chainList
     */
	@Autowired
	public JobProcessContext(List<IJobProcessChain> chainList) {
		if(Tools.isNotNull(chainList)) {
			OrderComparator.sort(chainList);
			processList.addAll(chainList);
		}
	}
	
	/**
	 * 执行重签名逻辑
	 * @param udid - UDID
	 * @param app - AppEntity
	 * @param appInfo - AppInfoEntity
	 * @param developer - DeveloperEntity
	 * @param cer - 证书对象
	 * @param addLog - UdidAddLogEntity
	 * @throws Exception
	 */
	public void doProcess(String udid, AppEntity app, AppInfoEntity appInfo, DeveloperEntity developer, DeveloperCerEntity cer, UdidAddLogEntity addLog) throws Exception {
		StopWatch stopWatch = new StopWatch();
		//此处新增安装日志
		InstallLogEntity log = installLogService.createInstallLog(app.getId(), appInfo.getId(), developer.getId(), cer.getId(), udid);
		
		try {
			for(IJobProcessChain chain : processList) {
				if(!chain.doProcess(udid, app, appInfo, developer, cer, addLog, log, stopWatch)) {
					break;
				}
			}
		} catch (Exception e) {
			//这里需要记录错误
			installLogService.installFailure(log.getId(), e.getMessage());
			
			throw e;
		}
	}

}
