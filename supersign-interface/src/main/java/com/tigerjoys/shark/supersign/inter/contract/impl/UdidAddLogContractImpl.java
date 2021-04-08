package com.tigerjoys.shark.supersign.inter.contract.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.contract.AbstractBaseContract;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.shark.supersign.inter.contract.IUdidAddLogContract;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;
import com.tigerjoys.shark.supersign.inter.mapper.UdidAddLogMapper;

/**
 * 数据库中  UDID更新日志[t_udid_add_log]表 接口实现类
 * @Date 2020-03-26 15:39:15
 *
 */
@Repository
public class UdidAddLogContractImpl extends AbstractBaseContract<UdidAddLogEntity , UdidAddLogMapper> implements IUdidAddLogContract {

	@Override
	public UdidAddLogEntity findByUdidAndAppId(String udid, long appId) throws Exception {
		PageModel pageModel = PageModel.getLimitModel(0, 1);
		pageModel.addQuery(Restrictions.eq("udid", udid));
		pageModel.addQuery(Restrictions.eq("app_id", appId));
		
		List<UdidAddLogEntity> list = mapper.load(pageModel);
		return Tools.isNotNull(list) ? list.get(0) : null;
	}
	
}
