package com.tigerjoys.shark.supersign.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.tigerjoys.nbs.common.enums.ECharset;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.utils.SpringBeanApplicationContext;
import com.tigerjoys.shark.supersign.comm.storage.ICloudStorage;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;

/**
 * 辅助类
 *
 */
public final class Helper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Helper.class);
	
	/**
	 * 随机类
	 */
	private static final Random RANDOM = new Random();
	
	/**
	 * 取得文件的地址
	 * @param file - 文件的全路径
	 * @param prefix - 后缀
	 * @return String /upload/home/12345.jpg  变成 /upload/home/12345_s.jpg
	 */
	public static String getPrefixFile(String file,String prefix) {
		if(Tools.isNull(file)) {
			return null;
		}
		if(file.charAt(0) =='h') {
			return file;
		}
		if(file.indexOf(".")<0){
			return file+prefix;
		}
		
		return file.substring(0,file.lastIndexOf(".")) +prefix+"." +file.substring(file.lastIndexOf(".")+1);
	}
	
	/**
	 * 得到一个上传目录名
	 * @return String
	 */
	public static String getUploadFilePath(){
		return getUploadFilePath(null);
	}
	
	/**
	 * 得到一个上传目录名，格式：upload/filePath/2014/11/11/
	 * @param filePath - upload下的文件名
	 * @return String
	 */
	public static String getUploadFilePath(String filePath){
		Calendar calendar = Calendar.getInstance();
		
		int m = (calendar.get(Calendar.MONTH)+1);
		String month = null;
		if(m<10)month="0"+m;
		else month=m+"";
		
		int d = calendar.get(Calendar.DAY_OF_MONTH);
		String day = null;
		if(d<10)day="0"+d;
		else day = ""+d;
		
		String dirName = String.valueOf(calendar.get(Calendar.YEAR))+"/"+month+"/"+day+"/";
		return "/upload/"+(Tools.isNull(filePath)?"":filePath+"/")+dirName;
	}
	
	/**
	 * 生成一个上传文件名
	 * @param ext - 后缀
	 * @return String
	 */
	public static String getUploadFileName(String ext){
		if(ext==null)return null;
		Random r = new Random();
		
		return String.valueOf(System.currentTimeMillis())+"_"+r.nextInt(10000)+ "." + ext.toLowerCase();
	}
	
	/**
	 * 生成一个上传文件名
	 * @param ext - 后缀
	 * @param s - 区分字符，可以为null
	 * @return String
	 */
	public static String getUploadFileName(String ext , String s){
		if(ext==null)return null;
		Calendar calendar = Calendar.getInstance();

		int m = (calendar.get(Calendar.MONTH)+1);
		String month = null;
		if(m<10)month="0"+m;
		else month=m+"";
		
		int d = calendar.get(Calendar.DAY_OF_MONTH);
		String day = null;
		if(d<10)day="0"+d;
		else day = ""+d;
			
		String dirName = String.valueOf(calendar.get(Calendar.YEAR))+"/"+month+"/"+day+"/";
		String fileName = s+"_"+RANDOM.nextInt(10000)+ "." + ext.toLowerCase();
		return dirName+fileName ;
	}
	
	/**
	 * 往request里加入一个参数或者更新一个参数，组成参数形式c.jsp?g=23434&pg=10
	 * @param request - HttpServletRequest
	 * @param filter - 要过滤的参数
	 * @return String
	 * @throws UnsupportedEncodingException 
	 */
	public static String addOrUpdateParameter(HttpServletRequest request , String... filter) throws UnsupportedEncodingException{
		return addOrUpdateParameter(request,null,null,filter);
	}
	
	/**
	 * 往request里加入一个参数或者更新一个参数，组成参数形式c.jsp?g=23434&pg=10
	 * @param request - HttpServletRequest
	 * @param pName - 名字
	 * @param pValue - 值
	 * @param filter - 过滤的参数
	 * @return String
	 * @throws UnsupportedEncodingException 
	 */
	public static String addOrUpdateParameter(HttpServletRequest request,String pName,String pValue , String... filter) throws UnsupportedEncodingException{
		Enumeration<?> en = request.getParameterNames();
		Map<String,String> pMap = new HashMap<>();
		while(en.hasMoreElements()){
			String k = en.nextElement().toString();
			if(filter != null && Tools.isInArray(filter,k)) {
				continue;
			}
			pMap.put(k,request.getParameter(k));
		}
		
		if(Tools.isNotNull(pName) && pValue!=null) {
			pMap.put(pName, pValue);
		}
		Iterator<String> it = pMap.keySet().iterator();
		
		StringBuilder buf = new StringBuilder(request.getRequestURI());
		buf.append("?");
		while(it.hasNext()){
			String k = it.next();
			buf.append(k).append("=").append(Tools.encoder(pMap.get(k), ECharset.UTF_8.getName())).append("&");
		}
	    
		return buf.toString();
	}
	
	/**
	 * 获得访问来源路径，包含参数
	 * @param request - HttpServletRequest
	 * @return String
	 * @throws UnsupportedEncodingException 
	 */
	public static String getRefererPath(HttpServletRequest request) {
		String referer = Tools.getReferer(request);
		
		if(Tools.isNull(referer)){
			String str = request.getScheme() + "://"+request.getServerName();
			if(request.getServerPort() != 80){
				str += ":"+request.getServerPort();
			}
			referer = str + referer;
		}
		
		if(referer != null && (referer.endsWith("?") || referer.endsWith("&"))){
			referer = referer.substring(0,referer.length() - 1);
		}
		
		return referer;
	}
	
	/**
	 * 上传图片
	 * @param fileBytes - byte[]
	 * @param ext - 图片后缀
	 * @param directory - 上传存放的目录
	 * @return FileUploadResult
	 */
	public static FileUploadResult uploadPicture(byte[] fileBytes , String ext , String directory) {
		if(fileBytes == null || fileBytes.length == 0) {
			return FileUploadResult.getFileUploadDto(80001, "请上传jpg,jpeg,png,gif格式的图片");
		}
		
		if(Tools.isNull(ext)){
			return FileUploadResult.getFileUploadDto(80003, "没有获得到图片格式");                                                                    
		}
		
		//判断文件格式
		if(Tools.isNull(ext)){
			return FileUploadResult.getFileUploadDto(80101, "没有获得到文件格式");                                                                    
		}
		ext = ext.toLowerCase();
		if("jpg,gif,png,jpeg".indexOf(ext)==-1){
			return FileUploadResult.getFileUploadDto(80102, "文件格式只能上传jpg,gif,png,jpeg");
		}
		
		//上传图片的路径
		String filePath = Helper.getUploadFilePath(directory)+Helper.getUploadFileName(ext);
		
		return uploadFile(new ByteArrayInputStream(fileBytes), filePath);
	}
	
	/**
	 * 上传视频到资源服务器
	 * @param file - MultipartFile
	 * @param ext - 后缀名
	 * @param directory - 上传的目录
	 * @return FileUploadResult
	 * @throws IOException 
	 */
	public static FileUploadResult uploadVideo(MultipartFile file, String ext, String directory) throws IOException {
		if(file == null || file.isEmpty()) {
			return FileUploadResult.getFileUploadDto(80005, "上传视频为空");
		}
		if(Tools.isNull(ext)){
			return FileUploadResult.getFileUploadDto(80006, "没有获得视频格式");
		}
		ext = ext.toLowerCase();
		if("aiv;mp4".indexOf(ext)==-1){
			return FileUploadResult.getFileUploadDto(80102, "文件格式只能上传avi,mp4");
		}
		
		//上传图片的路径
		String filePath = Helper.getUploadFilePath(directory)+Helper.getUploadFileName(ext);
		
		return uploadFile(file.getInputStream(), filePath);
	}
	
	/**
	 * 上传图片
	 * @param file - MultipartFile
	 * @param directory - 上传存放的目录
	 * @return FileUploadResult
	 * @throws IOException 
	 */
	public static FileUploadResult uploadPicture(MultipartFile file , String directory) throws IOException {
		if(file == null || Tools.isNull(file.getOriginalFilename())){
			return FileUploadResult.getFileUploadDto(80001, "请上传jpg,jpeg,png,gif格式的图片");
		}
		
		//获得文件格式
		String fileExt = null;
		String picFileName = file.getOriginalFilename();
		if(picFileName.indexOf(".")>-1){
			fileExt = picFileName.substring(picFileName.lastIndexOf(".")+1).toLowerCase();
		}
		if(Tools.isNull(fileExt)){
			return FileUploadResult.getFileUploadDto(80012, "没有获得到图片格式");                                                                    
		}
		
		fileExt = fileExt.toLowerCase();
		if("jpg,gif,png,jpeg".indexOf(fileExt)==-1){
			return FileUploadResult.getFileUploadDto(80102, "文件格式只能上传jpg,gif,png,jpeg");
		}
		
		//上传图片的路径
		String filePath = Helper.getUploadFilePath(directory)+Helper.getUploadFileName(fileExt);
		
		return uploadFile(file.getInputStream(), filePath);
	}
	
	/**
	 * 上传图片
	 * @param file - MultipartFile
	 * @param directory - 上传存放的目录
	 * @return FileUploadResult
	 * @throws IOException 
	 */
	public static FileUploadResult uploadPicture(File file , String directory) throws IOException {
		if(file == null){
			return FileUploadResult.getFileUploadDto(80001, "请上传jpg,jpeg,png,gif格式的图片");
		}
		
		//获得文件格式
		String fileExt = null;
		String picFileName = file.getName();
		if(picFileName.indexOf(".")>-1){
			fileExt = picFileName.substring(picFileName.lastIndexOf(".")+1).toLowerCase();
		}
		if(Tools.isNull(fileExt)){
			return FileUploadResult.getFileUploadDto(80012, "没有获得到图片格式");                                                                    
		}
		
		fileExt = fileExt.toLowerCase();
		if("jpg,gif,png,jpeg".indexOf(fileExt)==-1){
			return FileUploadResult.getFileUploadDto(80102, "文件格式只能上传jpg,gif,png,jpeg");
		}
		
		//上传图片的路径
		String filePath = Helper.getUploadFilePath(directory)+Helper.getUploadFileName(fileExt);
		
		return uploadFile(new FileInputStream(file), filePath);
	}
	
	/**
	 * 上传文件到资源服务器
	 * @param file - MultipartFile
	 * @param ext - 后缀名
	 * @param directory - 上传的目录
	 * @return FileUploadResult
	 * @throws IOException 
	 */
	public static FileUploadResult uploadFile(MultipartFile file, String directory) throws IOException {
		if(file == null || file.isEmpty()) {
			return FileUploadResult.getFileUploadDto(80005, "上传文件为空");
		}
		//获得文件格式
		String fileExt = null;
		String picFileName = file.getOriginalFilename();
		if(picFileName.indexOf(".")>-1){
			fileExt = picFileName.substring(picFileName.lastIndexOf(".")+1).toLowerCase();
		}
		if(Tools.isNull(fileExt)){
			return FileUploadResult.getFileUploadDto(80012, "没有获得到文件格式");                                                                    
		}
		fileExt = fileExt.toLowerCase();
		
		//上传图片的路径
		String filePath = Helper.getUploadFilePath(directory)+Helper.getUploadFileName(fileExt);
		
		return uploadFile(file.getInputStream(), filePath);
	}
	
	/**
	 * 上传文件，不管成功与否，0流终会被关闭
	 * @param file - InputStream
	 * @param filePath - 上传文件的后缀
	 * @param directory - 上传存放的目录
	 * @return FileUploadResult
	 * @throws IOException 
	 */
	public static FileUploadResult uploadFile(InputStream stream , String filePath) {
		if(stream == null){
			return FileUploadResult.getFileUploadDto(80100, "找不到你要上传的文件信息");
		}
		if(filePath == null) {
			return FileUploadResult.getFileUploadDto(80101, "参数异常");
		}
		
		try {
			//上传
			boolean ret = getMinioCloudStorage().writeFile(filePath, stream, true);
			if(!ret) {
				return FileUploadResult.getFileUploadDto(80103, "保存文件到服务器失败");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage() , e);
			
			return FileUploadResult.getFileUploadDto(80104, "保存文件出错，请重新上传");
		} finally {
			if(stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage() , e);
				}
			}
		}
		
		return FileUploadResult.getFileUploadDto(ErrCodeEnum.success.getCode(), "成功", filePath);
	}
	
	/**
	 * 按照逗号分割字符数据，返回一个字符串数组
	 * @param ids - 数字组合
	 * @return String[]
	 */
	public static String[] toNumberArrays(String ids){
		if(ids == null) return null;
		String[] idArray = ids.replace("，", ",").split(",");
		Set<String> idList = new HashSet<String>();
		if(Tools.isNotNull(ids)) {
			for(String id : idArray) {
				id = id.trim();
				if(Tools.isNumber(id)) idList.add(id);
			}
		}
		
		return idList.toArray(new String[0]);
	}
	
	/**
	 * 按照逗号分割字符数据，返回一个List
	 * @param ids - 数字组合
	 * @return List<String>
	 */
	public static List<String> toNumberCollections(String ids){
		if(ids == null) return null;
		String[] idArray = ids.replace("，", ",").split(",");
		Set<String> idList = new HashSet<String>();
		if(Tools.isNotNull(ids)) {
			for(String id : idArray) {
				id = id.trim();
				if(Tools.isNumber(id)) idList.add(id);
			}
			List<String> l = new ArrayList<String>();
			for(String id : idList) {
				l.add(id);
			}
			
			return l;
		}
		
		return null;
	}
	
	/**
	 * 从Spring容器中获取ICloudStorage对象
	 * @return ICloudStorage
	 */
	public static ICloudStorage getMinioCloudStorage() {
		return SpringBeanApplicationContext.getBean("cloudStorage", ICloudStorage.class);
	}

}
