package com.tigerjoys.shark.supersign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.tigerjoys.shark.supersign.service.IJobConsumer;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableAsync
@EnableCaching
@PropertySource({"classpath:appservice.properties"})
public class Bootstrap {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);
	
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = SpringApplication.run(Bootstrap.class, args);
		String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
		for (String profile : activeProfiles) {
			LOGGER.info("Super Sign Consumer Service Spring Boot 2.2 当前使用profile为:{}", profile);
		}
		IJobConsumer jobConsumer = ctx.getBean(IJobConsumer.class);
		if(jobConsumer != null) {
			jobConsumer.run();
		} else {
			LOGGER.error("jobConsumer not found~~~~~~~~~~~~~~~~~~~~~");
		}
	}

}
