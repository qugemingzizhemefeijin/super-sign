package com.tigerjoys.shark.supersign.comm.sign;

import java.io.File;

import com.tigerjoys.shark.supersign.comm.constant.RedisConstant;

/**
 * 签名工具类
 */
public final class SignHelper {
	
	/**
	 * 获取签名进度Redis Key名称
	 * @param appId - 安装APPID
	 * @param udid - UDID
	 * @return String
	 */
	public static String getSignProcessKey(long appId, String udid) {
		return String.format(RedisConstant.SIGN_PROCESS_KEY, appId, udid);
	}
	
	/**
	 * 获取重签名IPA的保存地址
	 * @param appId - 安装APPID
	 * @return String
	 */
	public static String getSignIpaFilePath(long appId) {
		return "/upload/dist/ipa/"+appId+"/"+System.currentTimeMillis()+".ipa";
	}
	
	/**
	 * 获取重签名MobileProvision文件的保存地址
	 * @param appId - 安装APPID
	 * @return String
	 */
	public static String getMobileProvisionFilePath(long appId) {
		return "/upload/dist/mp/"+appId+"/"+System.currentTimeMillis()+".mobileprovision";
	}
	
	/**
	 * 获取重签名plist文件的保存地址
	 * @param appId - 安装APPID
	 * @return String
	 */
	public static String getPlistFilePath(long appId) {
		return "/upload/dist/plist/"+appId+"/"+System.currentTimeMillis()+".plist";
	}
	
	/**
	 * 获取MobileConfig文件的保存地址
	 * @param appId - 安装APPID
	 * @return String
	 */
	public static String getMobileConfigFilePath(long appId) {
		return "/upload/dist/mc/"+appId+"/source.mobileconfig";
	}
	
	/**
	 * 获取签名MobileConfig文件的保存地址
	 * @param appId - 安装APPID
	 * @return String
	 */
	public static String getSignMobileConfigFilePath(long appId) {
		return "/upload/dist/mc/"+appId+"/udid.mobileconfig";
	}
	
	/**
	 * 获取mbcrt地址
	 * @return String
	 */
	public static String getMbPublicCrt() {
		return "/upload/dist/crt/mb.public.crt";
	}
	
	/**
	 * 获取mbkey地址
	 * @return String
	 */
	public static String getMbComKey() {
		return "/upload/dist/crt/mb.com.key";
	}
	
	/**
	 * 获取cacrt地址
	 * @return String
	 */
	public static String getMbComPem() {
		return "/upload/dist/crt/mb.com.pem";
	}
	
	/**
	 * 获得开发者的证书保存目录
	 * @param developerId - 开发者ID
	 * @return String
	 */
	public static String getCertificateDirectory(long developerId) {
		return "/upload/dist/cert/" + developerId;
	}
	
	/**
	 * 创建指定路径的父目录
	 * @param path - 磁盘全路径
	 */
	public static void createFileDirectory(String path) {
		int idx = path.lastIndexOf("/");
		if(idx > -1) {
			String dir = path.substring(0, idx);
			
			File f = new File(dir);
			if(!f.exists()) {
				f.mkdirs();
			}
		}
	}
	
	/**
	 * 获取指定路径的父路径
	 * @param path - 路径
	 * @return String
	 */
	public static String getFileParentDiectory(String path) {
		int idx = path.lastIndexOf("/");
		if(idx > -1) {
			return path.substring(0, idx);
		}
		return null;
	}
	
	/**
	 * 获取指定路径的文件名称，不包含格式名称
	 * @param path - 路径
	 * @return String
	 */
	public static String getFileName(String path) {
		int idx = path.lastIndexOf("/");
		int idxe = path.lastIndexOf(".");
		if(idxe > -1) {
			if(idx > -1) {
				return path.substring(idx+1, idxe);
			} else {
				return path.substring(0, idxe);
			}
		}
		return null;
	}
	
	private SignHelper() {
		
	}

}
