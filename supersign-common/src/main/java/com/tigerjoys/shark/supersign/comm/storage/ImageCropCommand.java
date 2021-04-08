package com.tigerjoys.shark.supersign.comm.storage;

/**
 * 图片裁剪命令
 *
 */
public class ImageCropCommand implements ImageCommand {
	
	private final int width;
	
	private final int height;
	
	private final int x;
	
	private final int y;
	
	public ImageCropCommand(int width , int height , int x , int y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public ImageHandlerEnums getHandlerEnums() {
		return ImageHandlerEnums.CROP;
	}

}
