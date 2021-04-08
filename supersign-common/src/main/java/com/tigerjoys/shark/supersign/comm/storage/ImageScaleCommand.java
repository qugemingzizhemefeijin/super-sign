package com.tigerjoys.shark.supersign.comm.storage;

/**
 * 图片缩放命令
 *
 */
public class ImageScaleCommand implements ImageCommand {
	
	/**
	 * 图片宽度
	 */
	private final int width;
	
	/**
	 * 图片高度
	 */
	private final int height;
	
	public ImageScaleCommand(int width , int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public ImageHandlerEnums getHandlerEnums() {
		return ImageHandlerEnums.SCALE;
	}

}
