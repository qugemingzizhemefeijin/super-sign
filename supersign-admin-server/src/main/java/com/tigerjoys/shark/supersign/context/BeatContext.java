package com.tigerjoys.shark.supersign.context;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tigerjoys.nbs.web.annotations.LogRule;
import com.tigerjoys.shark.supersign.inter.entity.AdminEntity;

/**
 * BeatContext
 *
 */
public class BeatContext {
	
	/**
	 * HttpServletRequest
	 */
	private HttpServletRequest request;
	
	/**
	 * HttpServletResponse
	 */
	private HttpServletResponse response;
	
	/**
	 * request body
	 */
	private Object requestBody;
	
	/**
	 * 请求的body体类型
	 */
	private Class<?> rawClass;
	
	/**
	 * response body
	 */
	private Object responseBody;
	
	/**
	 * 用户ID
	 */
	private long adminId;
	
	/**
	 * 用户对象
	 */
	private AdminEntity admin;
	
	/**
	 * 是否记录日志
	 */
	private boolean log = true;
	
	/**
	 * 是否需要登录
	 */
	private boolean needLogin = true;
	
	/**
	 * 是否需要验证权限。前置条件是needLogin必须为true
	 */
	private boolean permission = true;
	
	/**
	 * 日志参数记录规则
	 */
	private LogRule logRule;
	
	/**
	 * 对应的mapping的方法
	 */
	private Method method;
	
	/**
	 * 对应的mapping的class
	 */
	private Class<?> clazz;
	
	public BeatContext() {
		
	}
	
	public BeatContext(HttpServletRequest request , HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public Object getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(Object requestBody) {
		this.requestBody = requestBody;
	}

	public Object getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(Object responseBody) {
		this.responseBody = responseBody;
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getRawClass() {
		return rawClass;
	}

	public void setRawClass(Class<?> rawClass) {
		this.rawClass = rawClass;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public AdminEntity getAdmin() {
		return admin;
	}

	public void setAdmin(AdminEntity admin) {
		this.admin = admin;
	}

	public boolean isNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}

	public boolean isPermission() {
		return permission;
	}

	public void setPermission(boolean permission) {
		this.permission = permission;
	}

	public LogRule getLogRule() {
		return logRule;
	}

	public void setLogRule(LogRule logRule) {
		this.logRule = logRule;
	}

}
