package com.tigerjoys.shark.supersign.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tigerjoys.nbs.common.utils.StreamUtils;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.shark.supersign.dto.list.UdidAddLogDto;
import com.tigerjoys.shark.supersign.inter.contract.IDeveloperContract;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;
import com.tigerjoys.shark.supersign.service.IUdidAddLogService;

/**
 * UUID添加日志接口实现类
 *
 */
@Service
public class UdidAddLogServiceImpl implements IUdidAddLogService {
	
	@Autowired
	private IDeveloperContract developerContract;

	@Override
	public List<UdidAddLogDto> transferLogList(List<UdidAddLogEntity> logList) throws Exception {
		if(Tools.isNull(logList)) {
			return Collections.emptyList();
		}
		
		List<Long> developerIdList = StreamUtils.toUniqMapList(logList, UdidAddLogEntity::getDeveloper_id);
		Map<Long, DeveloperEntity> developerMap = developerContract.findById(developerIdList);
		
		return StreamUtils.toList(logList, log -> {
			DeveloperEntity developer = developerMap.get(log.getDeveloper_id());
			
			UdidAddLogDto dto = new UdidAddLogDto();
			dto.setCreateTime(Tools.getDateTime(log.getCreate_time()));
			dto.setDeveloperId(log.getDeveloper_id());
			dto.setDeveloperName(developer.getUsername());
			dto.setId(log.getId());
			dto.setUdid(log.getUdid());
			
			return dto;
		});
	}

}
