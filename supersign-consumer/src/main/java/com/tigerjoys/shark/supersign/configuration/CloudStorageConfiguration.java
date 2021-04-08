package com.tigerjoys.shark.supersign.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tigerjoys.shark.supersign.comm.storage.AliyunCloudStorage;
import com.tigerjoys.shark.supersign.comm.storage.ICloudStorage;
import com.tigerjoys.shark.supersign.comm.storage.MinioCloudStorage;

/**
 * 云存储配置信息
 *
 */
@Configuration
public class CloudStorageConfiguration {
	
	@Value("${spring.storage.type}")
	private String type;
	
	@Value("${spring.storage.bucketname}")
	private String bucket;
	
	@Value("${spring.storage.endpoint}")
	private String endpoint;
	
	@Value("${spring.storage.username}")
	private String username;
	
	@Value("${spring.storage.password}")
	private String password;
	
	/**
	 * 创建出云存储客户端
	 * @return ICloudStorage
	 * @throws Exception
	 */
	@Bean(name = "cloudStorage")
	public ICloudStorage cloudStorage() throws Exception{
		ICloudStorage cloudStorage;
		if(!"aliyun".equals(type)) {
			cloudStorage = new MinioCloudStorage(endpoint , bucket , username , password);
		} else {
			cloudStorage = new AliyunCloudStorage(endpoint , bucket , username , password);
		}
		
		return cloudStorage;
	}

}
