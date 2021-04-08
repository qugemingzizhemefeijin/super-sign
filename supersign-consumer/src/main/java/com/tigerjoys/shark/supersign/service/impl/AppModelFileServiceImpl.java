package com.tigerjoys.shark.supersign.service.impl;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.tigerjoys.shark.supersign.service.IAppModelFileService;

/**
 * APP模版文件服务接口实现类
 *
 */
@Service
public class AppModelFileServiceImpl implements IAppModelFileService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppModelFileServiceImpl.class);

	@Override
	@Cacheable(value = "plist", key = "#p0")
	public String getPListModelFile(long appInfoId) throws FileNotFoundException {
		Resource fileRource = new ClassPathResource("app/dist.plist");
		if (fileRource.exists()) {
			try (InputStream is = fileRource.getInputStream()){
				byte[] content = FileCopyUtils.copyToByteArray(fileRource.getInputStream());
				
				return new String(content, "UTF-8");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		} else {
			throw new FileNotFoundException("PList Model File Not Found");
		}
		return null;
	}

}
