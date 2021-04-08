package com.tigerjoys.shark.supersign.inter.contract.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.contract.AbstractBaseContract;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.shark.supersign.inter.contract.IInstallLogContract;
import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;
import com.tigerjoys.shark.supersign.inter.mapper.InstallLogMapper;

/**
 * 数据库中  安装日志[t_install_log]表 接口实现类
 * @Date 2020-03-26 00:08:53
 *
 */
@Repository
public class InstallLogContractImpl extends AbstractBaseContract<InstallLogEntity , InstallLogMapper> implements IInstallLogContract {
	
	@Override
	public InstallLogEntity findByInstalling(String udid) throws Exception {
		PageModel pageModel = PageModel.getLimitModel(0, 1);
		pageModel.addQuery(Restrictions.eq("udid", udid));
		pageModel.addQuery(Restrictions.eq("ident", 1));
		
		List<InstallLogEntity> list = mapper.load(pageModel);
		return Tools.isNotNull(list) ? list.get(0) : null;
	}
	
}
