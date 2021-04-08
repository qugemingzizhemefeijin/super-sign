package com.tigerjoys.shark.supersign.service;

import java.util.List;

import com.tigerjoys.shark.supersign.dto.list.AppInfoDto;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;

/**
 * APP INFO 服务接口
 *
 */
public interface IAppInfoService {
	
	/**
	 * 将APP INFO列表转换成页面展示列表
	 * @param appInfoList - List<AppInfoEntity>
	 * @return List<AppInfoDto>
	 * @throws Exception
	 */
	public List<AppInfoDto> transferAppInfoList(List<AppInfoEntity> appInfoList) throws Exception;

}
