package com.tigerjoys.shark.supersign.process;

import org.springframework.core.Ordered;
import org.springframework.util.StopWatch;

import com.tigerjoys.shark.supersign.inter.entity.AppEntity;
import com.tigerjoys.shark.supersign.inter.entity.AppInfoEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperCerEntity;
import com.tigerjoys.shark.supersign.inter.entity.DeveloperEntity;
import com.tigerjoys.shark.supersign.inter.entity.InstallLogEntity;
import com.tigerjoys.shark.supersign.inter.entity.UdidAddLogEntity;

/**
 * 重签名工作流程
 *
 */
public interface IJobProcessChain extends Ordered {
	
	/**
	 * 重签名执行流程
	 * @param udid - 重签名的UDID
	 * @param app - APP对象
	 * @param appInfo - 指定的APPINFO ID
	 * @param developer - 对应的开发者帐号
	 * @param cer - 对应开发者的证书
	 * @param addLog - Udid的添加记录
	 * @param log - 安装日志
	 * @param sw - 记录耗时
	 * @return boolean
	 * @throws Exception
	 */
	public boolean doProcess(String udid, AppEntity app, AppInfoEntity appInfo, DeveloperEntity developer, DeveloperCerEntity cer, UdidAddLogEntity addLog, InstallLogEntity log, StopWatch sw) throws Exception;

}
