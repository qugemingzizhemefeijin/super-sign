package com.tigerjoys.shark.supersign.utils;

import java.net.URLDecoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.mybatis.core.utils.SpringBeanApplicationContext;
import com.tigerjoys.nbs.web.annotations.LogRule;
import com.tigerjoys.shark.supersign.context.BeatContext;
import com.tigerjoys.shark.supersign.context.RequestUtils;
import com.tigerjoys.shark.supersign.inter.contract.IAdminRoleContract;
import com.tigerjoys.shark.supersign.inter.entity.AdminEntity;
import com.tigerjoys.shark.supersign.inter.entity.AdminRoleEntity;

public class LoggerUtil {
	
	//日志中指定的内容找不到的替代字符
	private static final String not_found = "no";
	//日志的分隔符
	private static final char separatorChars = '|';
	
	/**
	 * 记录点击日志
	 */
	public static void writerHitLog(){
		Logger hitlogger = LoggerFactory.getLogger("hitlogger");
		
		BeatContext beat = RequestUtils.getCurrent();
		
		HttpServletRequest request = beat.getRequest();
		String method = request.getMethod().toLowerCase();
		HttpSession session = (HttpSession)request.getSession();
		/**
		 * 1：时间
		 * 2：adminid
		 * 3：adminName
		 * 4 : 权限ID
		 * 5 : 权限名称
		 * 6：ip
		 * 7：SESSION ID
		 * 8：user_agent
		 * 9：ContentType
		 * 10：post/get
		 * 11 : referer
		 * 12 : 占位符
		 * 13 : 占位符
		 * 14 : 占位符
		 * 15 : 占位符
		 * 16 : 占位符
		 * 17 : 占位符
		 * 18 : 占位符
		 * 19 : 占位符
		 * 20 : 占位符
		 * 21 : 占位符
		 * 22 : 占位符
		 * 23：URI
		 * 24 : 只记录get请求的参数
		 */
		StringBuilder buf = new StringBuilder(128);
		AdminEntity admin = beat.getAdmin();
		boolean b = admin != null;//此处判断是否登陆
		if(b) {
			IAdminRoleContract roleService = SpringBeanApplicationContext.getBean(IAdminRoleContract.class);
			AdminRoleEntity role = null;
			try {
				role = roleService.findById(admin.getRoleId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			buf.append(admin.getId()).append(separatorChars);
			buf.append(admin.getRealname()).append(separatorChars);
			buf.append(admin.getRoleId()).append(separatorChars);
			buf.append(role!=null?role.getName():not_found).append(separatorChars);
		} else {
			buf.append("0").append(separatorChars);
			buf.append(not_found).append(separatorChars);
			buf.append("0").append(separatorChars);
			buf.append(not_found).append(separatorChars);
		}
		
		buf.append(Tools.formatString(Tools.getIP(request), not_found)).append(separatorChars);
		buf.append(session.getId()).append(separatorChars);
		buf.append(Tools.formatString(Tools.getUserAgent(request), not_found)).append(separatorChars);
		//请求头
		buf.append(Tools.getContentType(request)).append(separatorChars);
		buf.append(method).append(separatorChars);
		
		//点击来源
		String refererUrl = Tools.getReferer(request);
		if(Tools.isNull(refererUrl)) {
			buf.append(not_found);
		} else {
			try {
				buf.append(URLDecoder.decode(refererUrl, "UTF-8"));
			} catch (Exception e) {
				buf.append(not_found);
			}
		}
		buf.append(separatorChars);
		
		//11个占位符
		for(int i=0;i<11;i++) {
			buf.append(not_found).append(separatorChars);
		}
		//文件URI
		buf.append(request.getRequestURI()).append(separatorChars);
		
		//参数记录规则
		LogRule logRule = beat.getLogRule();
		if(logRule == null || logRule.param()) {
			String[] filter = (logRule!=null?logRule.filter():null);
			//参数信息
			Enumeration<?> en = request.getParameterNames();
			while(en.hasMoreElements()){
				String k = (String)en.nextElement();
				if(filter != null && filter.length > 0) {
					boolean c = false;
					for(String f : filter) {
						if(f.equals(k)) {
							c = true;
							break;
						}
					}
					
					if(c) continue;//如果包含了指定的特殊字符，则跳出并且不记录日志
				}
				
				buf.append(k).append("=").append(request.getParameter(k)).append("&");
			}
		}
		
		String logStr = buf.toString().replaceAll("\\s"," ");
		//记录日志
		hitlogger.info(logStr);
	}
   
}
