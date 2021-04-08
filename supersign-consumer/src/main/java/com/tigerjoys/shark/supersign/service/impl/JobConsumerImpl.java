package com.tigerjoys.shark.supersign.service.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tigerjoys.nbs.common.cache.CacheRedis;
import com.tigerjoys.nbs.common.utils.JsonHelper;
import com.tigerjoys.nbs.common.utils.NameableThreadFactory;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.shark.supersign.comm.constant.RedisConstant;
import com.tigerjoys.shark.supersign.service.IAppInfoService;
import com.tigerjoys.shark.supersign.service.IJobConsumer;

/**
 * 消费安装请求接口实现类
 *
 */
@Service
public class JobConsumerImpl implements IJobConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobConsumerImpl.class);
	
	private static final ScheduledExecutorService SCNEDULED_EXECUTOR = Executors.newScheduledThreadPool(1 , new NameableThreadFactory("job-consumer-" , true));
	
	@Autowired
	private CacheRedis cacheRedis;
	
	@Autowired
	private IAppInfoService appInfoService;

	@Override
	public void run() throws InterruptedException {
		SCNEDULED_EXECUTOR.scheduleAtFixedRate(() -> {
			String task = cacheRedis.lpop(RedisConstant.SIGN_QUEUE_KEY);
			if(task == null) {
				return;
			}
			
			LOGGER.info("curr task : {}" , task);
			
			JSONObject json = JsonHelper.toJsonObject(task);
			
			long appId = json.getLongValue("appId");
			String udid = json.getString("udid");
			
			if(appId <= 0 || Tools.isNull(udid)) {
				LOGGER.warn("appId={},udid={} is error" , appId, udid);
			}
			try {
				appInfoService.consume(appId, udid);
			} catch (Exception e) {
				LOGGER.error(e.getMessage() , e);
			}
		}, 1, 1, TimeUnit.SECONDS);
		
		while(true) {
			LOGGER.info("======================================");
			Thread.sleep(2000L);
		}
	}
	
	@PreDestroy
    public void destory(){
		try {
			SCNEDULED_EXECUTOR.shutdown();
			SCNEDULED_EXECUTOR.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			LOGGER.error("shutdown interrupted", e);
		} finally {
			SCNEDULED_EXECUTOR.shutdownNow();
		}
    }

}
