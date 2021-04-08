package com.tigerjoys.shark.supersign.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tigerjoys.nbs.common.utils.StreamUtils;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.dto.list.AppInfoDto;
import com.tigerjoys.shark.supersign.inter.contract.IAppContract;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.service.IAppInfoService;

/**
 * APP INFO 服务接口实现类
 *
 */
@Service
public class AppInfoServiceImpl implements IAppInfoService {
	
	@Autowired
	private IAppContract appContract;

	@Override
	public List<AppInfoDto> transferAppInfoList(List<AppInfoEntity> appInfoList) throws Exception {
		if(Tools.isNull(appInfoList)) {
			return Collections.emptyList();
		}
		
		List<Long> appIdList = StreamUtils.toUniqMapList(appInfoList, AppInfoEntity::getApp_id);
		Map<Long, AppEntity> appMap = appContract.findById(appIdList);
		
		return StreamUtils.toList(appInfoList, appInfo -> {
			AppEntity app = appMap.get(appInfo.getId());
			
			AppInfoDto dto = new AppInfoDto();
			dto.setAppId(app.getId());
			dto.setAppName(app.getApp_name());
			dto.setAppInfoName(appInfo.getApp_name());
			dto.setBundleId(appInfo.getBundle_id());
			dto.setCreateTime(Tools.getDateTime(appInfo.getCreate_time()));
			dto.setFullIconPath(Const.getCdn(appInfo.getFull_icon_path()));
			dto.setIconPath(Const.getCdn(appInfo.getIcon_path()));
			dto.setId(appInfo.getId());
			dto.setPath(Const.getCdn(appInfo.getPath()));
			dto.setResignIpaPath(Const.getCdn(appInfo.getResign_ipa_path()));
			dto.setResignMpPath(Const.getCdn(appInfo.getResign_mp_path()));
			dto.setResignPlistPath(Const.getCdn(appInfo.getResign_plist_path()));
			dto.setStatus(appInfo.getStatus());
			dto.setStatusStr(appInfo.getStatus() == 1 ? "上架" : "下架");
			dto.setVersion(appInfo.getVersion());
			dto.setVersionCode(appInfo.getVersion_code());
			
			return dto;
		});
	}

}
