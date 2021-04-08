package com.tigerjoys.shark.supersign.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.tigerjoys.nbs.common.utils.StreamUtils;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.shark.supersign.comm.enums.InstallLogStatusEnums;
import com.tigerjoys.shark.supersign.comm.utils.TimeFormatUtis;
import com.tigerjoys.shark.supersign.dto.list.InstallLogDto;
import com.tigerjoys.shark.supersign.inter.contract.IAppContract;
import com.tigerjoys.shark.supersign.inter.contract.IAppInfoContract;
import com.tigerjoys.shark.supersign.inter.contract.IDeveloperContract;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;
import com.tigerjoys.shark.supersign.service.IInstallLogService;

/**
 * 安装日志服务接口实现类
 *
 */
@Service
public class InstallLogServiceImpl implements IInstallLogService {
	
	@Autowired
	private IAppContract appContract;
	
	@Autowired
	private IAppInfoContract appInfoContract;
	
	@Autowired
	private IDeveloperContract developerContract;

	@Override
	public List<InstallLogDto> transferLogList(List<InstallLogEntity> logList) throws Exception {
		if(Tools.isNull(logList)) {
			return Collections.emptyList();
		}
		
		Set<Long> appIds = Sets.newHashSet();
		Set<Long> appInfoIds = Sets.newHashSet();
		Set<Long> developerIds = Sets.newHashSet();
		
		for(InstallLogEntity log : logList) {
			appIds.add(log.getApp_id());
			appInfoIds.add(log.getApp_info_Id());
			developerIds.add(log.getDeveloper_id());
		}
		
		Map<Long, AppEntity> appMap = appContract.findById(appIds.toArray(new Long[0]));
		Map<Long, AppInfoEntity> appInfoMap = appInfoContract.findById(appInfoIds.toArray(new Long[0]));
		Map<Long, DeveloperEntity> developerMap = developerContract.findById(developerIds.toArray(new Long[0]));
		
		return StreamUtils.toList(logList, log -> {
			AppEntity app = appMap.get(log.getApp_id());
			AppInfoEntity appInfo = appInfoMap.get(log.getApp_info_Id());
			DeveloperEntity developer = developerMap.get(log.getDeveloper_id());
			Date createTime = log.getCreate_time(), finishTime = log.getFinish_time();
			
			InstallLogDto dto = new InstallLogDto();
			dto.setAppId(log.getApp_id());
			dto.setAppName(app.getApp_name());
			dto.setBundleId(appInfo.getBundle_id());
			dto.setCreateTime(Tools.getDateTime(log.getCreate_time()));
			dto.setDeveloperId(log.getDeveloper_id());
			dto.setDeveloperName(developer.getUsername());
			dto.setError(log.getError());
			dto.setFinishTime(Tools.getDateTime(finishTime));
			dto.setId(log.getId());
			dto.setIdent(log.getIdent());
			dto.setStatus(log.getStatus());
			dto.setStatusStr(InstallLogStatusEnums.getDescByCode(log.getStatus()));
			dto.setUdid(log.getUdid());
			
			if(finishTime == null) {
				dto.setCostTime("-");
			} else {
				dto.setCostTime(TimeFormatUtis.formatTimeby(finishTime.getTime() - createTime.getTime()));
			}
			
			return dto;
		});
	}

}
