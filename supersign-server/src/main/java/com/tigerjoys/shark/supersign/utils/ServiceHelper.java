package com.tigerjoys.shark.supersign.utils;

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.shark.supersign.constant.Const;

/**
 * 服务工具类
 *
 */
public final class ServiceHelper {
	
	/**
	 * 获得CDN的图片地址
	 * @param relativePhoto - String
	 * @return String
	 */
	public static String getCdnPhoto(String relativePhoto) {
		return getPhoto(Const.CDN_SITE , relativePhoto , null);
	}
	
	/**
	 * 获得CDN图片地址
	 * @param website - 域名前缀
	 * @param relativePhoto - 相对路径
	 * @param tag - 标签
	 * @return String
	 */
	public static String getPhoto(String website , String relativePhoto , String tag){
		if(relativePhoto == null || relativePhoto.length() == 0) {
			return Tools.EMPTY_STRING;
		}
		
		if(relativePhoto.charAt(0) == 'h') {
			return relativePhoto;
		}
		
		//组装
		StringBuilder buf = new StringBuilder(128);
		buf.append(website).append(relativePhoto);
		if(tag != null && tag.trim().length() > 0) {
			if(Const.IS_TEST) {
				buf.append("?t=");
			} else {
				buf.append("!");
			}
			buf.append(tag);
		}
		
		return buf.toString();
	}
	
	private ServiceHelper() {
		
	}

}
