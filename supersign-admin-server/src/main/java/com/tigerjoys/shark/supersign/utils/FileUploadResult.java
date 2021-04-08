package com.tigerjoys.shark.supersign.utils;

/**
 * 上传图片返回码
 *
 */
public class FileUploadResult {
	
	/**
	 * 返回码
	 */
	private int code;
	
	/**
	 * 返回信息
	 */
	private String msg;
	
	/**
	 * 图片保存地址
	 */
	private String filePath;
	
	private FileUploadResult(){}
	
	public static FileUploadResult getFileUploadDto(int code , String msg) {
		return getFileUploadDto(code , msg , null);
	}
	
	public static FileUploadResult getFileUploadDto(int code , String msg , String filePath) {
		FileUploadResult dto = new FileUploadResult();
		dto.code = code;
		dto.msg = msg;
		dto.filePath = filePath;
		return dto;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public String getFilePath() {
		return filePath;
	}

}
