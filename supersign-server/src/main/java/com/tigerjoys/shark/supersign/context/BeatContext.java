package com.tigerjoys.shark.supersign.context;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	 * 是否记录日志
	 */
	private boolean log = true;
	
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

}
