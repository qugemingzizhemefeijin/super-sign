package com.tigerjoys.shark.supersign.configuration;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.tigerjoys.nbs.web.annotations.NoLog;
import com.tigerjoys.shark.supersign.context.BeatContext;
import com.tigerjoys.shark.supersign.context.RequestUtils;
import com.tigerjoys.shark.supersign.utils.WebUtils;

/**
 * 支付API拦截器
 *
 */
public class WebControllerInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebControllerInterceptor.class);
	
	/**
	 * 记录接口调用完毕的执行时间
	 */
	private static final ThreadLocal<Long> executiveTime = new ThreadLocal<Long>();
	
	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 *
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//此处必须重置线程变量
		BeatContext context = new BeatContext(request , response);
		RequestUtils.bindBeatContextToCurrentThread(context);
		
		if(handler != null && handler instanceof HandlerMethod) {
			executiveTime.set(System.currentTimeMillis());
			
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			
			//查看是否不需要记录日志。。
			if(AnnotationUtils.findAnnotation(method, NoLog.class) != null) {
				context.setLog(false);
			}
			
			//设置context method
			context.setMethod(method);
			context.setClazz(handlerMethod.getBeanType());
		} else {
			LOGGER.warn(WebUtils.getLookupPathForRequest(request) + " handler is not HandlerMethod!");
		}
		return true;
	}
	
	/*
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	 */
	/*@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
		if(handler != null && handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			
		} else {
			LOGGER.warn(WebUtils.getContextPath(request) + " postHandle is error!");
		}
	}*/

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		long handlerTimeMillis = -1L;
		
		if(handler != null && handler instanceof HandlerMethod) {
			handlerTimeMillis = System.currentTimeMillis() - executiveTime.get().longValue();
			executiveTime.remove();
			LOGGER.info("接口 "+request.getRequestURI()+" 执行耗时："+handlerTimeMillis+"毫秒。");
		}
		
		BeatContext context = RequestUtils.getCurrent();
		if(context.isLog()) {
			//记录日志
			//PayCenterLogUtils.click(handlerTimeMillis);
		}
		//清空绑定数据
		RequestUtils.remove();
	}

}
