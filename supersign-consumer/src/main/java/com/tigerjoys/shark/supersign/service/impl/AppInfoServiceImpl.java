package com.tigerjoys.shark.supersign.service.impl;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.page.PageModel;
import com.tigerjoys.nbs.mybatis.core.sql.Restrictions;
import com.tigerjoys.shark.supersign.exception.RevertVirtualLimitException;
import com.tigerjoys.shark.supersign.inter.contract.IAppContract;
import com.tigerjoys.shark.supersign.inter.contract.IAppInfoContract;
import com.tigerjoys.shark.supersign.inter.contract.IDeveloperCerContract;
import com.tigerjoys.shark.supersign.inter.contract.IDeveloperContract;
import com.tigerjoys.shark.supersign.inter.contract.IUdidAddLogContract;
import com.tigerjoys.shark.supersign.inter.contract.IUdidSuccessContract;
import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperCerEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidSuccessEntity;
import com.tigerjoys.shark.supersign.process.JobProcessContext;
import com.tigerjoys.shark.supersign.service.IAppInfoService;

/**
 * APP INFO相关的安装接口实现类
 *
 */
@Service
public class AppInfoServiceImpl implements IAppInfoService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppInfoServiceImpl.class);
	
	@Autowired
	private IAppContract appContract;
	
	@Autowired
	private IAppInfoContract appInfoContract;
	
	@Autowired
	private IUdidSuccessContract udidSuccessContract;
	
	@Autowired
	private IUdidAddLogContract udidAddLogContract;
	
	@Autowired
	private IDeveloperContract developerContract;
	
	@Autowired
	private JobProcessContext jobProcessContext;
	
	@Autowired
	private IDeveloperCerContract developerCerContract;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void consume(long appId, String udid) throws Exception {
		AppEntity app = appContract.findById(appId);
		if(app == null) {
			LOGGER.info("APP = {} Not Found", appId);
			return;
		}
		//那么从数据库中读取，是否已经成功安装过了
		UdidSuccessEntity us = udidSuccessContract.findByUdidAndAppId(udid, appId);
		if(us != null) {
			LOGGER.info("APP = {}, udid = {} installed" , appId, udid);
			return;
		}
		
		//首先查询此udid是否更新过设备信息
		UdidAddLogEntity addLog = udidAddLogContract.findByUdidAndAppId(udid, appId);
		
		DeveloperEntity developer = null;
		if(addLog == null) {
			//查询出可用的开发者账户
			PageModel pageModel = PageModel.getLimitModel(0, 10);
			pageModel.addQuery(Restrictions.eq("app_id", appId));
			pageModel.addQuery(Restrictions.gt("virtual_limit", 0));
			pageModel.asc("id");
			
			List<DeveloperEntity> list = developerContract.load(pageModel);
			if(Tools.isNull(list)) {
				LOGGER.info("APP = {}, udid = {} not avaliable developer" , appId, udid);
				return;
			}
			//随机挑选一个
			developer = list.size() == 1 ? list.get(0) : list.get(ThreadLocalRandom.current().nextInt(list.size()));
		} else {
			developer = developerContract.findById(addLog.getDeveloper_id());
		}
		
		//此处需要获取证书
		DeveloperCerEntity cer = developerCer(developer);
		if(cer == null) {
			LOGGER.info("APP = {}, udid = {} , developer_id={} not cert" , appId, udid, developer.getId());
			return;
		}
		
		AppInfoEntity appInfo = appInfoContract.findById(developer.getApp_info_id());
		if(addLog == null) {
			//此处需要暂时扣除扣减一个虚拟的安装量
			developerContract.updateVirtualLimit(developer.getId(), 1);
		}
		try {
			//此处就需要跑职责链了
			jobProcessContext.doProcess(udid, app, appInfo, developer, cer, addLog);
		} catch (RevertVirtualLimitException e) {
			//归还
			LOGGER.error(e.getMessage() , e);
			//此处需要再查询一下是否已经被添加到udid_log中了
			if(udidAddLogContract.findByUdidAndAppId(udid, appId) == null) {
				developerContract.updateVirtualLimit(developer.getId(), -1);
			}
		}
	}
	
	/**
	 * 获取开发者的证书信息
	 * @param developer - DeveloperEntity
	 * @return DeveloperCerEntity
	 * @throws Exception
	 */
	private DeveloperCerEntity developerCer(DeveloperEntity developer) throws Exception {
		//获取可用的证书
		PageModel pageModel = PageModel.getLimitModel(0, 1);
		pageModel.addQuery(Restrictions.eq("developer_id", developer.getId()));
		pageModel.addQuery(Restrictions.eq("status", 1));
		pageModel.desc("id");
		
		List<DeveloperCerEntity> list = developerCerContract.load(pageModel);
		
		return Tools.isNull(list) ? null : list.get(0);
	}

}
