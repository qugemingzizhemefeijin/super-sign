package com.tigerjoys.shark.supersign.comm.storage;

public class ImageRotateCommand implements ImageCommand {
	
	/**
	 * 0-360，0表示不旋转
	 */
	private final int rotate;
	
	/**
	 * 是否进行自动旋转<br>
	 * 某些手机拍摄出来的照片可能带有旋转参数（存放在照片exif信息里面）。可以设置是否对这些图片进行旋转。默认是设置自适应方向。
	 */
	private boolean autoOrient;
	
	public ImageRotateCommand(int rotate) {
		this(rotate , true);
	}
	
	public ImageRotateCommand(int rotate , boolean autoOrient) {
		this.rotate = rotate;
		this.autoOrient = autoOrient;
	}

	public boolean isAutoOrient() {
		return autoOrient;
	}

	public void setAutoOrient(boolean autoOrient) {
		this.autoOrient = autoOrient;
	}

	public int getRotate() {
		return rotate;
	}

	@Override
	public ImageHandlerEnums getHandlerEnums() {
		return ImageHandlerEnums.ROTATE;
	}

}
