package com.tigerjoys.shark.supersign.comm.storage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.GenericResult;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.ProcessObjectRequest;
import com.tigerjoys.nbs.common.enums.ECharset;
import com.tigerjoys.nbs.common.enums.EFileContentTypeEnum;
import com.tigerjoys.nbs.common.utils.Base64;
import com.tigerjoys.nbs.common.utils.Tools;

/**
 * 阿里云OSS存储
 *
 */
public class AliyunCloudStorage extends AbstractCloudStorage {
	
	/**
	 * minio的地址
	 */
	protected final String endpoint;
	
	/**
	 * bucket url safe encode
	 */
	private final String base64Bucket;
	
	/**
	 * oss client
	 */
	private final OSS client;

	public AliyunCloudStorage(String endpoint , String bucketName, String userName, String password) {
		super(bucketName, userName, password);
		
		this.endpoint = endpoint;
		this.base64Bucket = encodeBase64(bucketName);
		this.client = new OSSClientBuilder().build(endpoint, userName, password);
	}
	
	/**
	 * urlsafe base64
	 * @param s - String
	 * @return String
	 */
	private String encodeBase64(String s) {
		try {
			return URLEncoder.encode(Base64.encode(s), ECharset.UTF_8.getName());
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage() , e);
		}
		
		return null;
	}

	/**
	 * 不允许主动创建文件夹
	 */
	@Override
	public boolean mkDir(String path, boolean auto) {
		/*if(Tools.isNull(path)) {
			return false;
		}
		if(path.charAt(path.length()-1) != '/') {
			path = path + "/";
		}
		
		try {
			this.client.putObject(this.bucketName, path, new ByteArrayInputStream(new byte[0]));
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;*/
		throw new UnsupportedOperationException();
	}

	/**
	 * 不允许主动删除文件夹
	 */
	@Override
	public boolean rmDir(String path) {
		/*if(Tools.isNull(path)) {
			return false;
		}
		if(path.charAt(path.length()-1) != '/') {
			path = path + "/";
		}
		
		try {
			this.client.deleteObject(this.bucketName, path);
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;*/
		throw new UnsupportedOperationException();
	}

	@Override
	public List<FileInfo> readDir(String path) {
		if(path == null || path.isEmpty()) {
			return null;
		}
		
		if(path.charAt(0) == '/') {
			path = path.substring(1);
		}
		if(path.charAt(path.length()-1) != '/') {
			path += '/';
		}
		
		try {
			ObjectListing objectListing = this.client.listObjects(this.bucketName, path);
			List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
			if(sums != null && !sums.isEmpty()) {
				List<FileInfo> list = new ArrayList<>(sums.size());
				for(OSSObjectSummary s : sums) {
					String filePath = s.getKey();
					if(filePath == null || filePath.length() == 0) {
						filePath = "/";
					}
					
					FileInfo info = new FileInfo();
					
					info.setPath(filePath);
					info.setSize(s.getSize());
					info.setCreateDate(s.getLastModified());
					if(filePath.charAt(filePath.length()-1) == '/') {
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
		if(Tools.isNull(filePath)) {
			return false;
		}
		
		if(datas == null) {
			datas = "";
		}
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try(ByteArrayInputStream bais = toInputStream(datas)) {
			//EFileContentTypeEnum.getContentTypeByExt(getExt(filePath))
			this.client.putObject(this.bucketName, filePath, bais);
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}

	@Override
	public boolean writeFile(String filePath, File file, boolean auto) {
		if(Tools.isNull(filePath) || file == null || !file.exists()) {
			return false;
		}
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try {
			this.client.putObject(this.bucketName, filePath, file);
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}

	@Override
	public boolean writeFile(String filePath, InputStream stream, boolean auto) throws Exception {
		if(Tools.isNull(filePath) || stream == null) {
			return false;
		}
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try {
			this.client.putObject(this.bucketName, filePath, stream);
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}

	@Override
	public boolean writeFile(String filePath, byte[] datas, boolean auto) {
		if(Tools.isNull(filePath)) {
			return false;
		}
		
		if(datas == null) {
			datas = new byte[0];
		}
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try(ByteArrayInputStream bais = toInputStream(datas)) {
			this.client.putObject(this.bucketName, filePath, bais);
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}
	
	@Override
	public boolean writeDir(String rootFilePath , File dirPath) throws Exception {
		if(rootFilePath.charAt(0) == '/') {
			rootFilePath = rootFilePath.substring(1);
		}
		
		return this.writeDir(rootFilePath, dirPath ,dirPath.getAbsolutePath());
	}
	
	@Override
	public boolean existsFile(String filePath) {
		if(filePath == null || filePath.isEmpty()) {
			return false;
		}
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try {
			return this.client.doesObjectExist(this.bucketName, filePath);
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}

	@Override
	public FileInfo getFileInfo(String filePath) {
		if(filePath == null || filePath.isEmpty()) {
			return null;
		}
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try (OSSObject object = this.client.getObject(this.bucketName, filePath);) {
			if(object != null) {
				ObjectMetadata metaData = object.getObjectMetadata();
				if(metaData != null) {
					FileInfo info = new FileInfo();
					info.setPath(filePath);
					info.setCreateDate(metaData.getLastModified());
					info.setSize(metaData.getContentLength());
					info.setType("file");
					
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
					info.setContentType(EFileContentTypeEnum.getContentTypeByExt(ext));
					
					return info;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return null;
	}

	@Override
	public long getBucketUsage() {
		/*int totalLength = 0;
		try {
			ObjectListing listing = this.client.listObjects(this.bucketName);
			if(listing != null) {
				List<OSSObjectSummary> summaryList = listing.getObjectSummaries();
				if(Tools.isNotNull(summaryList)) {
					for(OSSObjectSummary s : summaryList) {
						totalLength += s.getSize();
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return totalLength;*/
		throw new UnsupportedOperationException();
	}

	@Override
	public long getFolderUsage(String path) {
		/*if(Tools.isNull(path)) {
			throw new NullPointerException("path is empty or null!");
		}
		if(path.charAt(path.length() - 1) != '/') {
			throw new IllegalArgumentException("path is't folder!");
		}
		
		int totalLength = 0;
		try {
			ObjectListing listing = this.client.listObjects(this.bucketName, path);
			if(listing != null) {
				List<OSSObjectSummary> summaryList = listing.getObjectSummaries();
				if(Tools.isNotNull(summaryList)) {
					for(OSSObjectSummary s : summaryList) {
						totalLength += s.getSize();
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return totalLength;*/
		throw new UnsupportedOperationException();
	}

	@Override
	public String readFile(String filePath) {
		if(filePath == null || filePath.isEmpty()) {
			return null;
		}
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try (OSSObject object = this.client.getObject(this.bucketName, filePath);) {
			BufferedReader br = new BufferedReader(new InputStreamReader(object.getObjectContent()));
					
			StringBuilder text = new StringBuilder();
			
			char[] chars = new char[2048];
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
	public boolean readFile(String filePath, String downFilePath) {
		if(downFilePath == null || downFilePath.isEmpty()) {
			return false;
		}
		
		return readFile(filePath , new File(downFilePath));
	}

	@Override
	public boolean readFile(String filePath, File file) {
		if(filePath == null || filePath.isEmpty() || file == null) {
			return false;
		}
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try {
			this.client.getObject(new GetObjectRequest(this.bucketName, filePath), file);
			
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
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try {
			return this.client.getObject(new GetObjectRequest(this.bucketName, filePath)).getObjectContent();
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
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try {
			this.client.deleteObject(bucketName, filePath);
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
		
		// 删除文件
		List<String> keys = new ArrayList<String>();
		filePaths.forEach(keys::add);
		try {
			this.client.deleteObjects(new DeleteObjectsRequest(this.bucketName).withKeys(keys));
			
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		
		return false;
	}
	
	/**
	 * 组装缩略图命令
	 * @param buf - StringBuilder
	 * @param scale - ImageScaleCommand
	 */
	private void assembleScaleCommand(StringBuilder buf , ImageScaleCommand scale) {
		buf.append("/resize,m_fill,w_").append(+scale.getWidth()).append(",h_").append(scale.getHeight());
	}

	@Override
	public boolean scalePicture(String filePath , ImageScaleCommand scaleCommand , String saveFilePath) {
		if(Tools.isNull(filePath) || scaleCommand == null || Tools.isNull(saveFilePath)) {
			throw new IllegalArgumentException();
		}
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try {
			String savePath = Tools.encoder(Base64.encode(saveFilePath), ECharset.UTF_8.getName());
			
			StringBuilder buf = new StringBuilder("image");
			assembleScaleCommand(buf, scaleCommand);
			buf.append("|sys/saveas,o_").append(savePath).append(",b_").append(base64Bucket);
			
			String param = buf.toString();
			logger.info(param);
			
			ProcessObjectRequest request = new ProcessObjectRequest(this.bucketName, filePath , param);
			GenericResult result = this.client.processObject(request);
			
			return result.getResponse().isSuccessful();
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		
		return false;
	}

	@Override
	public boolean scalePicture(String filePath , File file , ImageScaleCommand scaleCommand , String saveFilePath) {
		try {
			//先上传文件后执行缩略图功能
			boolean b = writeFile(filePath, file, true);
			if(b) {
				return scalePicture(filePath , scaleCommand , saveFilePath);
			}
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}

	@Override
	public boolean scalePicture(String filePath, List<ImageSaveFile> saveFiles) {
		if(Tools.isNull(saveFiles)) {
			throw new IllegalArgumentException();
		}
		
		boolean allTrust = true;
		for(ImageSaveFile saveFile : saveFiles) {
			if(!this.scalePicture(filePath, (ImageScaleCommand)saveFile.getCommand(), saveFile.getFilePath())) {
				allTrust = false;
			}
		}
		return allTrust;
	}

	@Override
	public boolean scalePicture(String filePath, File file, List<ImageSaveFile> saveFiles) {
		try {
			//先上传文件后执行缩略图功能
			boolean b = writeFile(filePath, file, true);
			if(b) {
				return this.scalePicture(filePath, saveFiles);
			}
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}
	
	/**
	 * 组装裁剪命令
	 * @param buf - StringBuilder
	 * @param crop - ImageCropCommand
	 */
	private void assembleCropCommand(StringBuilder buf , ImageCropCommand crop) {
		buf.append("/crop,w_").append(crop.getWidth()).append(",h_").append(crop.getHeight());
		buf.append(",x_").append(crop.getX()).append(",y_").append(crop.getY()).append(",g_nw");
	}
	
	@Override
	public boolean cropPicture(String filePath , ImageCropCommand cropCommand , String saveFilePath) {
		if(Tools.isNull(filePath) || cropCommand == null || Tools.isNull(saveFilePath)) {
			throw new IllegalArgumentException();
		}
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try {
			String savePath = Tools.encoder(Base64.encode(saveFilePath), ECharset.UTF_8.getName());
			
			StringBuilder buf = new StringBuilder("image");
			assembleCropCommand(buf, cropCommand);
			buf.append("|sys/saveas,o_").append(savePath).append(",b_").append(base64Bucket);
			
			String param = buf.toString();
			logger.info(param);
			
			ProcessObjectRequest request = new ProcessObjectRequest(this.bucketName, filePath , param);
			GenericResult result = this.client.processObject(request);
			
			return result.getResponse().isSuccessful();
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}

	@Override
	public boolean cropPicture(String filePath , File file , ImageCropCommand cropCommand , String saveFilePath) {
		try {
			boolean b = writeFile(filePath, file, true);
			if(b) {
				return cropPicture(filePath , cropCommand , saveFilePath);
			}
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}

	@Override
	public boolean cropPicture(String filePath, List<ImageSaveFile> saveFiles) {
		if(Tools.isNull(saveFiles)) {
			throw new IllegalArgumentException();
		}
		
		boolean allTrust = true;
		for(ImageSaveFile saveFile : saveFiles) {
			if(!this.cropPicture(filePath, (ImageCropCommand)saveFile.getCommand(), saveFile.getFilePath())) {
				allTrust = false;
			}
		}
		return allTrust;
	}

	@Override
	public boolean cropPicture(String filePath, File file, List<ImageSaveFile> saveFiles) {
		try {
			boolean b = writeFile(filePath, file, true);
			if(b) {
				return this.cropPicture(filePath, saveFiles);
			}
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}
	
	/**
	 * 组装旋转命令
	 * @param buf - StringBuilder
	 * @param rotate - ImageRotateCommand
	 */
	private void assembleScaleCommand(StringBuilder buf , ImageRotateCommand rotate) {
		buf.append("/rotate,").append(rotate.getRotate());
		if(!rotate.isAutoOrient()) {
			buf.append("/auto-orient,0");
		}
	}

	@Override
	public boolean rotatePicture(String filePath , ImageRotateCommand rotateCommand , String saveFilePath) {
		if(Tools.isNull(filePath) || rotateCommand == null || Tools.isNull(saveFilePath)) {
			throw new IllegalArgumentException();
		}
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		
		try {
			String savePath = Tools.encoder(Base64.encode(saveFilePath), ECharset.UTF_8.getName());
			
			StringBuilder buf = new StringBuilder("image");
			assembleScaleCommand(buf, rotateCommand);
			buf.append("|sys/saveas,o_").append(savePath).append(",b_").append(base64Bucket);
			
			String param = buf.toString();
			logger.info(param);
			
			ProcessObjectRequest request = new ProcessObjectRequest(this.bucketName, filePath , param);
			GenericResult result = this.client.processObject(request);
			
			return result.getResponse().isSuccessful();
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}

	@Override
	public boolean rotatePicture(String filePath, File file, ImageRotateCommand rotateCommand, String saveFilePath) {
		try {
			boolean b = writeFile(filePath, file, true);
			if(b) {
				return rotatePicture(filePath , rotateCommand , saveFilePath);
			}
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
	}

	@Override
	public boolean rotatePicture(String filePath, List<ImageSaveFile> saveFiles) {
		if(Tools.isNull(saveFiles)) {
			throw new IllegalArgumentException();
		}
		
		boolean allTrust = true;
		for(ImageSaveFile saveFile : saveFiles) {
			if(!this.rotatePicture(filePath, (ImageRotateCommand)saveFile.getCommand(), saveFile.getFilePath())) {
				allTrust = false;
			}
		}
		return allTrust;
	}

	@Override
	public boolean rotatePicture(String filePath, File file, List<ImageSaveFile> saveFiles) {
		try {
			boolean b = writeFile(filePath, file, true);
			if(b) {
				return this.rotatePicture(filePath, saveFiles);
			}
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		return false;
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
		
		if(filePath.charAt(0) == '/') {
			filePath = filePath.substring(1);
		}
		if(savePath.charAt(0) == '/') {
			savePath = savePath.substring(1);
		}
		
		int second = 0;
		String[] h = point.split(":");
		
		second += Integer.parseInt(h[0])*3600;
		second += Integer.parseInt(h[1])*60;
		second += Integer.parseInt(h[2]);
		
		try {
			StringBuilder buf = new StringBuilder("video/snapshot");
			buf.append(",t_").append(second*1000).append(",f_jpg,h_").append(500).append(",m_fast");
			buf.append("/auto-orient,0");
			buf.append("|sys/saveas,o_").append(Tools.encoder(Base64.encode(savePath), ECharset.UTF_8.getName())).append(",b_").append(base64Bucket);
			
			String param = buf.toString();
			logger.info(param);
			
			ProcessObjectRequest request = new ProcessObjectRequest(this.bucketName, filePath , param);
			GenericResult result = this.client.processObject(request);
			
			return result.getResponse().isSuccessful();
		} catch (Exception e) {
			logger.error(e.getMessage() , e);
		}
		
		return false;
	}

	@Override
	public void destroy() throws Exception {
		if(this.client != null) {
			this.client.shutdown();
		}
	}

}
