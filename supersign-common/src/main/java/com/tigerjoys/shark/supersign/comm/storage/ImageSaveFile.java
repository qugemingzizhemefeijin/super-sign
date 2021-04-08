package com.tigerjoys.shark.supersign.comm.storage;

/**
 * 图片保存
 *
 */
public class ImageSaveFile {
	
	private final String filePath;
	
	private ImageCommand command;
	
	public ImageSaveFile(String filePath , ImageCommand command) {
		this.filePath = filePath;
		this.command = command;
	}

	public ImageCommand getCommand() {
		return command;
	}

	public void setCommand(ImageCommand command) {
		this.command = command;
	}

	public String getFilePath() {
		return filePath;
	}

}
