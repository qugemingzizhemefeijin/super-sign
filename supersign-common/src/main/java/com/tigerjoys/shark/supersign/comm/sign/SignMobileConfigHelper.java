package com.tigerjoys.shark.supersign.comm.sign;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tigerjoys.shark.supersign.comm.utils.ProcessCommandUtils;

/**
 * 生成签名的MobileConfig文件
 *
 */
public final class SignMobileConfigHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SignMobileConfigHelper.class);
	
	/**
	 * 生成MobileConfig文件
	 * @param appId - APPID
	 * @param rootPath - 文件存储根路径
	 * @param formatMobileConfig - 末班文件内容
	 * @param installUrl - uuid的返回路径
	 * @param appName - APP名称
	 * @return Pair<String, String> value0=源mb文件,vlaue1=签名mb文件
	 */
	public static Pair<String, String> createMobileConfig(long appId, String rootPath, String formatMobileConfig, String installUrl, String appName) {
		//生成的内容
		String mbConfig = String.format(formatMobileConfig, installUrl, appName, UUID.randomUUID().toString().toUpperCase());
		//返回源文件路径
		String sourceMbConfigPath = writeSourceMbConfig(appId, rootPath, mbConfig);
		if(sourceMbConfigPath == null) {
			return null;
		}
		//返回签名文件路径
		String signMbConfigPath = writeSignMbconfig(appId, rootPath, sourceMbConfigPath);
		if(signMbConfigPath == null) {
			return null;
		}
		
		return new Pair<>(sourceMbConfigPath, signMbConfigPath);
	}
	
	/**
	 * 生成源MobileConfig文件
	 * @param appId - APPID
	 * @param rootPath - 根目录
	 * @param mbConfig - MobileConfig文件内容
	 * @return String 返回的是相对路径
	 */
	private static String writeSourceMbConfig(long appId, String rootPath, String mbConfig) {
		String mbconfigPath = SignHelper.getMobileConfigFilePath(appId);
		
		try (FileWriter writer = new FileWriter(rootPath + mbconfigPath);) {
            writer.write(mbConfig);
            writer.flush();
        } catch (IOException e) {
        	LOGGER.error(e.getMessage(), e);
        	return null;
        }
        return mbconfigPath;
	}
	
	/**
	 * 生成签名的MobileConfig文件
	 * @param appId - APPID
	 * @param rootPath - 根目录
	 * @param sourceMbConfigPath - MobileConfig文件的相对路径
	 * @return String
	 */
	private static String writeSignMbconfig(long appId, String rootPath, String sourceMbConfigPath) {
		File source = new File(rootPath + sourceMbConfigPath);
        if (!source.exists()) {
            throw new RuntimeException(".mobileconfig文件不存在，无法签名");
        }
        
        String targetPath = SignHelper.getSignMobileConfigFilePath(appId);
        String[] command = new String[] {"openssl", "smime", "-sign", "-in", rootPath + sourceMbConfigPath, "-out", rootPath + targetPath, "-signer", rootPath + SignHelper.getMbPublicCrt(), "-inkey", rootPath + SignHelper.getMbComKey(), "-certfile", rootPath + SignHelper.getMbComPem(), "-outform", "der", "-nodetach"};
        
        try {
        	if(ProcessCommandUtils.processCommend(command)) {
            	return targetPath;
            }
        } catch (Exception e) {
        	LOGGER.error(e.getMessage(), e);
        }
        return null;
	}

}
