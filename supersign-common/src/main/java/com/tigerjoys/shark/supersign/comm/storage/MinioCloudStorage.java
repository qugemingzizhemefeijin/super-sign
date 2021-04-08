package com.tigerjoys.shark.supersign.comm.storage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.omg.CORBA.BooleanHolder;

import com.google.common.io.ByteStreams;
import com.tigerjoys.nbs.common.enums.EFileContentTypeEnum;
import com.tigerjoys.nbs.common.http.HttpUtils;
import com.tigerjoys.nbs.common.utils.Tools;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

/**
 * Minio文件上传
 *
 */
public class MinioCloudStorage extends AbstractCloudStorage {

	/**
	 * minio的地址
	 */
	protected final String endpoint;
	
	/**
	 * minio客户端
	 */
	protected final MinioClient minioClient;
	
	/**
	 * 构造函数
	 * @param endpoint - 地址
	 * @param bucketname - 空间名称
	 * @param username - 登录用户
	 * @param password - 登录密码
	 * @throws MinioException
	 */
	public MinioCloudStorage(String endpoint , String bucketname , String username , String password) throws MinioException {
		super(bucketname, username, password);

		this.endpoint = endpoint;
		// 初始化Minio客户端
		this.minioClient = new MinioClient(endpoint, username, password);
	}
	
	/**
	 * 创建当前客户端需要的Bucket
	 * @return boolean
	 */
	protected boolean createBucket() {
		try {
			// Check if the bucket already exists.
			boolean isExist = minioClient.bucketExists(bucketName);
			if (isExist) {
				logger.info("Bucket " + bucketName + " already exists.");
			} else {
				// Make a new bucket.
				minioClient.makeBucket(bucketName);
			}
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		
		return false;
	}
	
	/**
	 * 返回所有的buckets
	 * @return List<BucketInfo>
	 */
	public List<BucketInfo> listBuckets() {
		try {
			// List buckets that have read access.
			List<Bucket> bucketList = this.minioClient.listBuckets();
			if(bucketList == null || bucketList.isEmpty()) {
				return null;
			}
			
			return bucketList.stream().map(bucket -> new BucketInfo(bucket.name() , bucket.creationDate())).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		
		return null;
	}

	/**
	 * 目录会自动创建，不需要调用此方法
	 */
	@Override
	public boolean mkDir(String path, boolean auto) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 如果目录中的文件被删除掉了，则目录也会被自动删除，不需要调用此方法
	 */
	@Override
	public boolean rmDir(String path) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<FileInfo> readDir(String path) {
		if(path == null || path.isEmpty()) {
			return null;
		}
		
		if(path.charAt(path.length()-1) != '/') {
			path += '/';
		}
		
		try {
			Iterable<Result<Item>> iterable = this.minioClient.listObjects(bucketName, path , false);
			if(iterable != null) {
				List<FileInfo> list = new ArrayList<>();
				for(Result<Item> res : iterable) {
					Item item = res.get();
					String filePath = item.objectName();
					
					FileInfo info = new FileInfo();
					
					info.setPath(filePath);
					info.setSize(item.size());
					if(item.isDir()) {
						info.setType("folder");
						
						filePath = filePath.substring(0, filePath.length()-1);
						
						//获取目录的名称
						String name;
						int idx = filePath.lastIndexOf("/");
						if(idx != -1) {
							name = filePath.substring(idx+1);
						} else {
							name = filePath;
						}
						info.setName(name);
					} else {
						info.setType("file");
						info.setCreateDate(item.lastModified());
						
						//获取文件的名称
						String name;
						int idx = filePath.lastIndexOf("/");
						if(idx != -1) {
							name = filePath.substring(idx+1);
						} else {
							name = filePath;
						}
						info.setName(name);
						
						//获取文件的扩展名
						String ext;
						idx = name.lastIndexOf(".");
						if(idx != -1) {
							ext = name.substring(idx + 1);
						} else {
							ext = "";
						}
						info.setContentType(EFileContentTypeEnum.getContentTypeByExt(ext));
						info.setExt(ext);
					}
					
					list.add(info);
				}
				
				return list;
			}
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		
		return null;
	}

	@Override
	public boolean writeFile(String filePath, String datas, boolean auto) {
		if(datas == null) {
			datas = "";
		}
		
		try(ByteArrayInputStream bais = toInputStream(datas)) {
			this.minioClient.putObject(bucketName, filePath, bais, EFileContentTypeEnum.getContentTypeByExt(getExt(filePath)));
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}
	
	@Override
	public boolean writeFile(String filePath , InputStream stream , boolean auto) throws Exception {
		if(stream == null) {
			return false;
		}
		
		try {
			this.minioClient.putObject(bucketName, filePath, stream, EFileContentTypeEnum.getContentTypeByExt(getExt(filePath)));
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		
		return false;
	}

	@Override
	public boolean writeFile(String filePath, File file, boolean auto) {
		if(file == null || !file.exists()) {
			return false;
		}
		
		try (FileInputStream fis = new FileInputStream(file)) {
			this.minioClient.putObject(bucketName, filePath, fis, EFileContentTypeEnum.getContentTypeByExt(getExt(filePath)));
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}

	@Override
	public boolean writeFile(String filePath, byte[] datas, boolean auto) {
		if(datas == null) {
			datas = new byte[0];
		}
		
		try(ByteArrayInputStream bais = toInputStream(datas)) {
			this.minioClient.putObject(bucketName, filePath, bais, EFileContentTypeEnum.getContentTypeByExt(getExt(filePath)));
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}
	
	@Override
	public boolean writeDir(String rootFilePath , File dirPath) throws Exception {
		return this.writeDir(rootFilePath, dirPath ,dirPath.getAbsolutePath());
	}
	
	@Override
	public boolean existsFile(String filePath) {
		if(filePath == null || filePath.isEmpty()) {
			return false;
		}
		
		try {
			return this.minioClient.statObject(bucketName, filePath) != null;
		} catch (Exception e) {
			
		}
		return false;
	}

	@Override
	public FileInfo getFileInfo(String filePath) {
		if(filePath == null || filePath.isEmpty()) {
			return null;
		}
		
		try {
			ObjectStat stat = this.minioClient.statObject(bucketName, filePath);
			if(stat != null) {
				FileInfo info = new FileInfo();
				info.setPath(filePath);
				info.setCreateDate(stat.createdTime());
				info.setSize(stat.length());
				info.setType("file");
				info.setContentType(stat.contentType());
				
				//获取文件的名称
				String name;
				int idx = filePath.lastIndexOf("/");
				if(idx != -1) {
					name = filePath.substring(idx+1);
				} else {
					name = filePath;
				}
				info.setName(name);
				
				//获取文件的扩展名
				String ext;
				idx = name.lastIndexOf(".");
				if(idx != -1) {
					ext = name.substring(idx + 1);
				} else {
					ext = "";
				}
				info.setExt(ext);
				
				return info;
			}
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return null;
	}

	/**
	 * 没有API可以获取空间的大小信息
	 */
	@Override
	public long getBucketUsage() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 没有API可以获取指定目录的大小信息
	 */
	@Override
	public long getFolderUsage(String path) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String readFile(String filePath) {
		if(filePath == null || filePath.isEmpty()) {
			return null;
		}
		
		try (InputStream is = this.minioClient.getObject(bucketName, filePath);
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			StringBuilder text = new StringBuilder();
			
			char[] chars = new char[4096];
            int length = 0;

            while ((length = br.read(chars)) != -1) {
                text.append(chars, 0, length);
            }
            
            return text.toString();
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return null;
	}
	
	@Override
	public boolean readFile(String filePath , String downFilePath) {
		if(filePath == null || filePath.isEmpty() || downFilePath == null || downFilePath.isEmpty()) {
			return false;
		}
		
		try {
			this.minioClient.getObject(bucketName, filePath, downFilePath);
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}

	@Override
	public boolean readFile(String filePath, File file) {
		if(filePath == null || filePath.isEmpty() || file == null) {
			return false;
		}
		
		try(InputStream is = this.minioClient.getObject(bucketName, filePath);
			OutputStream os = Files.newOutputStream(Paths.get(file.toURI()), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			
			long bytesWritten = ByteStreams.copy(is, os);
			logger.info(file.getAbsolutePath() + " written size : " + bytesWritten);
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		
		return false;
	}
	
	@Override
	public InputStream readInputStream(String filePath) {
		if(filePath == null || filePath.isEmpty()) {
			return null;
		}
		
		try {
			return this.minioClient.getObject(bucketName, filePath);
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		
		return null;
	}

	@Override
	public boolean deleteFile(String filePath) {
		if(filePath == null || filePath.isEmpty()) {
			return false;
		}
		
		try {
			this.minioClient.removeObject(bucketName, filePath);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}
	
	@Override
	public boolean deleteFile(Iterable<String> filePaths) {
		if(filePaths == null) {
			return false;
		}
		
		try {
			this.minioClient.removeObject(bucketName, filePaths);
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		
		return false;
	}
	
	/**
	 * 无法处理图片信息
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean scalePicture(String filePath , ImageScaleCommand scaleCommand , String saveFilePath) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 无法处理图片信息
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean scalePicture(String filePath , File file , ImageScaleCommand scaleCommand , String saveFilePath) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 无法处理图片信息
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean scalePicture(String filePath, List<ImageSaveFile> saveFiles) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 无法处理图片信息
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean scalePicture(String filePath, File file, List<ImageSaveFile> saveFiles) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 无法处理图片信息
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean cropPicture(String filePath , ImageCropCommand cropCommand , String saveFilePath) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 无法处理图片信息
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean cropPicture(String filePath , File file , ImageCropCommand cropCommand , String saveFilePath) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 无法处理图片信息
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean cropPicture(String filePath, List<ImageSaveFile> saveFiles) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 无法处理图片信息
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean cropPicture(String filePath, File file, List<ImageSaveFile> saveFiles) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 无法处理图片信息
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean rotatePicture(String filePath , ImageRotateCommand rotateCommand , String saveFilePath) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 无法处理图片信息
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean rotatePicture(String filePath , File file , ImageRotateCommand rotateCommand , String saveFilePath) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 无法处理图片信息
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean rotatePicture(String filePath, List<ImageSaveFile> saveFiles) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 无法处理图片信息
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean rotatePicture(String filePath, File file, List<ImageSaveFile> saveFiles) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean mediaSnapshot(String filePath, String savePath, String point, String videoDomain) {
		if(Tools.isNull(filePath) || Tools.isNull(savePath) || Tools.isNull(point)) {
			throw new NullPointerException();
		}
		if(!point.matches("\\d{2}:\\d{2}:\\d{2}")) {
			throw new IllegalArgumentException("传入的截图时间不正确!");
		}
		
		int s = filePath.lastIndexOf(".");
		if(s == -1) {
			throw new IllegalArgumentException("请指定合法的视频文件相对路径!");
		}
		String ext = filePath.substring(s + 1);
		if("mp4;avi;mov".indexOf(ext) == -1) {
			throw new IllegalArgumentException("视频只支持mp4;avi;mov!");
		}
		
		if(filePath.charAt(0) != '/') {
			filePath = "/" + filePath;
		}
		if(savePath.charAt(0) != '/') {
			savePath = "/" + savePath;
		}
		
		int second = 0;
		String[] h = point.split(":");
		
		second += Integer.parseInt(h[0])*3600;
		second += Integer.parseInt(h[1])*60;
		second += Integer.parseInt(h[2]);
		
		final String newFile = savePath;
		try {
			BooleanHolder b = new BooleanHolder();
			HttpUtils.getCallback(videoDomain+"/snapshot"+filePath+"?second="+second+"&height=500", null, null , null, v -> {
				b.value = writeFile(newFile, v.getEntity().getContent(), true);
			});
			return b.value;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
			return false;
		}
	}

	@Override
	public void destroy() throws Exception {
		
	}

}
