package com.tigerjoys.shark.supersign.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.shark.supersign.comm.enums.InstallLogStatusEnums;
import com.tigerjoys.shark.supersign.inter.contract.IInstallLogContract;
import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;
import com.tigerjoys.shark.supersign.service.IInstallLogService;

/**
 * 安装日志相关的接口实现类
 *
 */
@Service
public class InstallLogServiceImpl implements IInstallLogService {
	
	@Autowired
	private IInstallLogContract installLogContract;

	@Override
	public void installFailure(long id, String error) throws Exception {
		if(error == null) {
			error = Tools.EMPTY_STRING;
		} else if(error.length() > 200) {
			error = error.substring(0, 200);
		}
		
		Map<String, Object> updateStatement = Maps.newHashMap();
		updateStatement.put("status", InstallLogStatusEnums.FAILURE.getCode());
		updateStatement.put("ident", null);
		updateStatement.put("error", error);
		updateStatement.put("finish_time", new Date());
		installLogContract.updateById(updateStatement, id);
	}

	@Override
	public void installSuccess(long id) throws Exception {
		Map<String, Object> updateStatement = Maps.newHashMap();
		updateStatement.put("status", InstallLogStatusEnums.SUCCESS.getCode());
		updateStatement.put("error", Tools.EMPTY_STRING);
		updateStatement.put("finish_time", new Date());
		installLogContract.updateById(updateStatement, id);
	}

	@Override
	public InstallLogEntity createInstallLog(long appId, long appInfoId, long developerId, long cerId, String udid) throws Exception {
		Date currDate = new Date();
		
		InstallLogEntity log = new InstallLogEntity();
		log.setApp_id(appId);
		log.setApp_info_Id(appInfoId);
		log.setCreate_time(currDate);
		log.setCer_Id(cerId);
		log.setDeveloper_id(developerId);
		log.setError(Tools.EMPTY_STRING);
		log.setIdent(1);
		log.setStatus(InstallLogStatusEnums.INSTALLING.getCode());
		log.setUdid(udid);
		log.setUpdate_time(currDate);
		installLogContract.insert(log);
		
		return log;
	}

}
