package com.tigerjoys.shark.supersign.configuration;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tigerjoys.nbs.common.cache.CacheRedis;
import com.tigerjoys.nbs.common.cache.RedisPoolConfig;
import com.tigerjoys.shark.supersign.constant.RedisConstant;

/**
 * redis配置
 *
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {
	
	@Autowired
	private RedisProperties properties;
	
	/**
     * 创建Redis客户端
     * @return CacheRedis
     * @throws SQLException
     */
    @Bean(name= {RedisConstant.PUBLIC_REDIS_SPRING_BEAN_NAME,RedisConstant.PUBLIC_MYBATIS_SPRING_BEAN_NAME})
    public CacheRedis cacheRedis() {
    	RedisPoolConfig config = new RedisPoolConfig();
    	config.setHost(properties.getHost());
    	config.setPort(properties.getPort());
    	config.setDatabase(properties.getDb());
    	config.setTimeout(properties.getTimeout());
    	config.setMaxTotal(properties.getActive());
    	config.setMaxIdle(properties.getIdle());
    	config.setMaxWaitMillis(properties.getWaitMillis());
    	config.setTestOnBorrow(properties.isBorrowCheck());
    	config.setTestOnReturn(properties.isReturnCheck());
    	config.setPassword(properties.getPassword());
    	
    	return new CacheRedis(config);
    }

}
