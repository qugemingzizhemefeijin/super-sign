package com.tigerjoys.shark.supersign.service;

import java.util.List;

import com.tigerjoys.shark.supersign.dto.list.UdidAddLogDto;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;

/**
 * UUID添加日志接口
 *
 */
public interface IUdidAddLogService {
	
	/**
	 * UDID日志转换成现实的列表
	 * @param logList - List<UdidAddLogEntity>
	 * @return List<UdidAddLogDto>
	 * @throws Exception
	 */
	public List<UdidAddLogDto> transferLogList(List<UdidAddLogEntity> logList) throws Exception;

}
