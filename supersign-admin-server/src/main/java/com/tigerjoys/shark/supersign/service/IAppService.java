package com.tigerjoys.shark.supersign.service;

import java.util.List;

import com.tigerjoys.shark.supersign.dto.list.AppDto;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;

/**
 * APP服务接口
 *
 */
public interface IAppService {
	
	/**
	 * 将APP列表转换成页面展示列表
	 * @param appList - List<AppEntity>
	 * @return List<AppDto>
	 * @throws Exception
	 */
	public List<AppDto> transferAppList(List<AppEntity> appList) throws Exception;

}
