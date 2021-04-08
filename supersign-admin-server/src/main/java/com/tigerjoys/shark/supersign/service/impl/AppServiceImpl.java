package com.tigerjoys.shark.supersign.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Maps;
import com.tigerjoys.nbs.common.utils.StreamUtils;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.mybatis.core.sql.Projections;
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.dto.list.AppDto;
import com.tigerjoys.shark.supersign.inter.contract.IAppInfoContract;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.service.IAppService;

/**
 * APP服务接口实现类
 *
 */
@Service
public class AppServiceImpl implements IAppService {
	
	@Autowired
	private IAppInfoContract appInfoContract;

	@Override
	public List<AppDto> transferAppList(List<AppEntity> appList) throws Exception {
		if(Tools.isNull(appList)) {
			return Collections.emptyList();
		}
		
		List<Long> appIdList = StreamUtils.toList(appList, AppEntity::getId);
		
		PageModel pageModel = PageModel.getPageModel();
		pageModel.addQuery(Restrictions.in("app_id", appIdList));
		pageModel.addProjection(Projections.groupProperty("app_id"));
		pageModel.addProjection(Projections.count("app_id").as("cc"));
		List<Map<String, Object>> appInfoCountList = appInfoContract.loadGroupBy(pageModel);
		
		Map<Long, Integer> appInfoCountMap = Maps.newHashMap();
		if(Tools.isNotNull(appInfoCountList)) {
			for(Map<String, Object> dataMap : appInfoCountList) {
				appInfoCountMap.put(Tools.longValue(dataMap.get("app_id")), Tools.intValue(dataMap.get("cc")));
			}
		}
		
		return StreamUtils.toList(appList, app -> {
			AppDto dto = new AppDto();
			dto.setAppInfoCount(Tools.intValue(appInfoCountMap.get(app.getId())));
			dto.setAppName(app.getApp_name());
			dto.setCreateTime(Tools.getDateTime(app.getCreate_time()));
			dto.setIcon(Const.getCdn(app.getFull_icon_path()));
			dto.setId(app.getId());
			dto.setMb(Const.getCdn(app.getMbconfig()));
			dto.setStatus(app.getStatus());
			dto.setStatusStr(app.getStatus() == 0 ? "下架" : "上架");
			
			return dto;
		});
	}

}
