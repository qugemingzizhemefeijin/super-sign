package com.tigerjoys.shark.supersign.comm.storage;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface ICloudStorage {
	
	/**
	 * 创建目录
	 * @param path - 目录路径，以/结尾
	 * @param auto - 若为 true 则自动创建父级目录（只支持自动创建10级以内的父级目录）
	 * @return boolean
	 * 结果为 true 创建目录成功
	 * 若空间相同目录下已经存在同名的文件，则将返回『不允许创建目录』的错误
	 */
	public boolean mkDir(String path, boolean auto);
	
	/**
	 * 删除目录
	 * @param path - 目录路径
	 * @return boolean
	 * 结果为 true 删除目录成功
	 * 若待删除的目录 path 下还存在任何文件或子目录，将返回『不允许删除』的错误
	 */
	public boolean rmDir(String path);
	
	/**
	 * 获取目录文件列表
	 * @param path - 目录路径
	 * @return List<FileInfo>
	 * 若 path 目录没有内容时，返回null
	 * 若 path 目录不存在时，则将返『不存在目录』的错误
	 */
	public List<FileInfo> readDir(String path);
	
	/**
	 * 上传文件
	 * @param filePath - 保存的文件路径
	 * @param datas - 文件内容
	 * @param auto - 若为 true 则自动创建父级目录（只支持自动创建10级以内的父级目录）
	 * @return boolean 结果为 true 上传文件成功
	 */
	public boolean writeFile(String filePath, String datas, boolean auto);
	
	/**
	 * 上传文件
	 * @param filePath - 保存的文件路径
	 * @param file - 文件
	 * @param auto - 若为 true 则自动创建父级目录（只支持自动创建10级以内的父级目录）
	 * @return boolean 结果为 true 上传文件成功
	 */
	public boolean writeFile(String filePath, File file, boolean auto);
	
	/**
	 * 上传文件，stream does not close.
	 * @param filePath - 保存的文件路径
	 * @param stream - 流
	 * @param auto - 若为 true 则自动创建父级目录（只支持自动创建10级以内的父级目录）
	 * @return boolean
	 * @throws Exception
	 */
	public boolean writeFile(String filePath , InputStream stream , boolean auto) throws Exception;
	
	/**
	 * 上传文件
	 * @param filePath - 保存的文件路径
	 * @param datas - 文件字节码
	 * @param auto - 若为 true 则自动创建父级目录（只支持自动创建10级以内的父级目录）
	 * @return boolean 结果为 true 上传文件成功
	 */
	public boolean writeFile(String filePath, byte[] datas, boolean auto);
	
	/**
	 * 将dirPath下的所有文件上传到远程的rootFilePath中，保持dirPath下的文件夹路径等<br/>
	 * 如本地/data/reptile/live/log/1/上传至/upload/live/log/1
	 * @param rootFilePath - 上传至远程路径
	 * @param dirPath - 本地文件夹路径
	 * @return boolean
	 * @throws Exception
	 */
	public boolean writeDir(String rootFilePath , String dirPath) throws Exception;
	
	/**
	 * 将dirPath下的所有文件上传到远程的rootFilePath中，保持dirPath下的文件夹路径等<br/>
	 * 如本地/data/reptile/live/log/1/上传至/upload/live/log/1
	 * @param rootFilePath - 上传至远程路径
	 * @param dirPath - 本地文件夹路径
	 * @return boolean
	 * @throws Exception
	 */
	public boolean writeDir(String rootFilePath , File dirPath) throws Exception;
	
	/**
	 * 查看文件是否存在
	 * @param filePath - 文件路径
	 * @return boolean
	 */
	public boolean existsFile(String filePath);
	
	/**
	 * 获取文件信息
	 * @param filePath - 文件路径
	 * @return 若 filePath 所指定文件不存在，则直接返回 null
	 */
	public FileInfo getFileInfo(String filePath);
	
	/**
	 * 获取整个空间的使用量情况
	 * @return long 返回值单位为 Byte 空间占用量，失败时返回 -1
	 */
	public long getBucketUsage();
	
	/**
	 * 获取某个目录的使用量情况
	 * @param path - 目录路径
	 * @return long 返回值单位为 Byte 空间占用量，失败时返回 -1
	 */
	public long getFolderUsage(String path);
	
	/**
	 * 下载文件
	 * @param filePath - 文件路径
	 * @return String 文本内容
	 */
	public String readFile(String filePath);
	
	/**
	 * 下载文件
	 * @param filePath - 文件在又拍云存储中的路径
	 * @param downFilePath - 本地临时文件（用来保存下载下来的数据）
	 * @return boolean
	 */
	public boolean readFile(String filePath , String downFilePath);
	
	/**
	 * 下载文件
	 * @param filePath - 文件路径
	 * @param file - 本地临时文件（用来保存下载下来的数据）
	 * @return boolean
	 */
	public boolean readFile(String filePath, File file);
	
	/**
	 * 读取文件获取流，外部需要主动去关闭流
	 * @param filePath - 文件路径
	 * @return InputStream
	 */
	public InputStream readInputStream(String filePath);
	
	/**
	 * 删除文件
	 * @param filePath - 文件路径
	 * @return boolean
	 * 若 filePath 指定的文件不存在，则返回『文件不存在』的错误
	 * 结果为 true 删除文件成功
	 */
	public boolean deleteFile(String filePath);
	
	/**
	 * 批量删除文件，此操作不是原子的
	 * @param filePaths - 文件集合
	 * @return boolean
	 * 若 filePath 指定的文件不存在，则返回『文件不存在』的错误
	 * 结果为 true 删除文件成功
	 */
	public boolean deleteFile(Iterable<String> filePaths);
	
	/**
	 * 制作图片缩略图
	 * @param filePath - 文件路径，必须是已存在的文件
	 * @param scaleCommand - 缩放要求
	 * @param saveFilePath - 缩放后保存图片路径
	 * @return boolean
	 */
	public boolean scalePicture(String filePath , ImageScaleCommand scaleCommand , String saveFilePath);
	
	/**
	 * 将file上传，并且制作图片缩略图
	 * @param filePath - 保存的文件路径
	 * @param file - 文件
	 * @param scaleCommand - 缩放要求
	 * @param saveFilePath - 缩放后保存图片路径
	 * @return boolean
	 */
	public boolean scalePicture(String filePath, File file , ImageScaleCommand scaleCommand , String saveFilePath);
	
	/**
	 * 制作图片缩略图
	 * @param filePath - 文件路径，必须是已存在的文件
	 * @param saveFiles - 缩放并且保存
	 * @return boolean
	 */
	public boolean scalePicture(String filePath , List<ImageSaveFile> saveFiles);
	
	/**
	 * 制作图片缩略图
	 * @param filePath - 文件路径，必须是已存在的文件
	 * @param file - 文件
	 * @param saveFiles - 缩放并且保存
	 * @return boolean
	 */
	public boolean scalePicture(String filePath , File file , List<ImageSaveFile> saveFiles);
	
	/**
	 * 图片裁剪
	 * @param filePath - 文件路径，必须是已存在的文件
	 * @param cropCommand - 图片裁剪要求
	 * @param saveFilePath - 裁剪后保存图片路径
	 * @return boolean
	 */
	public boolean cropPicture(String filePath , ImageCropCommand cropCommand , String saveFilePath);
	
	/**
	 * 将file上传，并且图片裁剪
	 * @param filePath - 保存的文件路径
	 * @param file - 文件
	 * @param cropCommand - 图片裁剪要求
	 * @param saveFilePath - 裁剪后保存图片路径
	 * @return boolean
	 */
	public boolean cropPicture(String filePath, File file , ImageCropCommand cropCommand , String saveFilePath);
	
	/**
	 * 图片裁剪
	 * @param filePath - 文件路径，必须是已存在的文件
	 * @param saveFiles - 裁剪并且保存
	 * @return boolean
	 */
	public boolean cropPicture(String filePath , List<ImageSaveFile> saveFiles);
	
	/**
	 * 图片裁剪
	 * @param filePath - 文件路径，必须是已存在的文件
	 * @param file - 文件
	 * @param saveFiles - 裁剪并且保存
	 * @return boolean
	 */
	public boolean cropPicture(String filePath , File file , List<ImageSaveFile> saveFiles);
	
	/**
	 * 图片旋转
	 * @param filePath - 文件路径，必须是已存在的文件
	 * @param rotateCommand - 图片缩放要求
	 * @param saveFilePath - 旋转后保存图片路径
	 * @return boolean
	 */
	public boolean rotatePicture(String filePath , ImageRotateCommand rotateCommand , String saveFilePath);
	
	/**
	 * 将file上传，并且图片旋转
	 * @param filePath - 保存的文件路径
	 * @param file - 文件
	 * @param rotateCommand - 图片缩放要求
	 * @param saveFilePath - 旋转后保存图片路径
	 * @return boolean
	 */
	public boolean rotatePicture(String filePath, File file , ImageRotateCommand rotateCommand , String saveFilePath);
	
	/**
	 * 图片旋转
	 * @param filePath - 文件路径，必须是已存在的文件
	 * @param saveFiles - 选中并且保存
	 * @return boolean
	 */
	public boolean rotatePicture(String filePath , List<ImageSaveFile> saveFiles);
	
	/**
	 * 图片旋转
	 * @param filePath - 文件路径，必须是已存在的文件
	 * @param file - 文件
	 * @param saveFiles - 选中并且保存
	 * @return boolean
	 */
	public boolean rotatePicture(String filePath , File file , List<ImageSaveFile> saveFiles);
	
	/**
	 * 视频截图
	 * @param filePath - 保存到又拍云存储的文件路径，以/开始
	 * @param savePath - 截图保存地址
	 * @param point - 截图时间点，格式为 HH:MM:SS
	 * @param videoDomain - minio需要使用此值，域名地址
	 * @return boolean
	 */
	public boolean mediaSnapshot(String filePath , String savePath , String point, String videoDomain);

}
