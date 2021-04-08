package com.tigerjoys.shark.supersign.inter.contract.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.contract.AbstractBaseContract;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.shark.supersign.inter.contract.IUdidSuccessContract;
import com.tigerjoys.shark.supersign.inter.entity.UdidSuccessEntity;
import com.tigerjoys.shark.supersign.inter.mapper.UdidSuccessMapper;

/**
 * 数据库中  UDID安装成功信息[t_udid_success]表 接口实现类
 * @Date 2020-03-26 00:08:54
 *
 */
@Repository
public class UdidSuccessContractImpl extends AbstractBaseContract<UdidSuccessEntity , UdidSuccessMapper> implements IUdidSuccessContract {
	
	@Override
	public UdidSuccessEntity findByUdidAndAppId(String udid, long appId) throws Exception {
		PageModel pageModel = PageModel.getLimitModel(0, 1);
		pageModel.addQuery(Restrictions.eq("udid", udid));
		pageModel.addQuery(Restrictions.eq("app_id", appId));
		
		List<UdidSuccessEntity> list = mapper.load(pageModel);
		return Tools.isNotNull(list) ? list.get(0) : null;
	}
	
}
