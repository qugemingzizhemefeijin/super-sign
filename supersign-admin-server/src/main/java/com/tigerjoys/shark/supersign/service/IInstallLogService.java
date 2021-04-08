package com.tigerjoys.shark.supersign.service;

import java.util.List;

import com.tigerjoys.shark.supersign.dto.list.InstallLogDto;
import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;

/**
 * 安装日志服务接口
 *
 */
public interface IInstallLogService {
	
	/**
	 * 将安装日志列表转换成页面展示列表
	 * @param logList - List<InstallLogEntity>
	 * @return List<InstallLogDto>
	 * @throws Exception
	 */
	public List<InstallLogDto> transferLogList(List<InstallLogEntity> logList) throws Exception;

}
