package com.tigerjoys.shark.supersign.configuration;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.tigerjoys.nbs.common.ActionResult;
import com.tigerjoys.shark.supersign.context.BeatContext;
import com.tigerjoys.shark.supersign.context.RequestUtils;

/**
 * 系统异常类
 *
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

	@ExceptionHandler(Exception.class)
	public ActionResult globalException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Throwable ex) {
    	//到这里可能还有一些未绑定变量，造成写日志出现异常
		if(!RequestUtils.isBindThreadLocalVariable()) {
			RequestUtils.bindBeatContextToCurrentThread(new BeatContext(request , response));
		}
		
		Method method = handlerMethod.getMethod();;
		
		StringBuilder buf = new StringBuilder("拦截器拦截到异常，class:");
		buf.append(handlerMethod!=null?handlerMethod.getBeanType():null);
		buf.append(",method:").append(method!=null?method.getName():null);
		
		LOGGER.error(buf.toString() , ex);
		
		/*ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/error/500"); //这里需要在templates文件夹下新建一个/error/500.html文件用作错误页面
        modelAndView.addObject("errorMsg",ex.getMessage());
        return modelAndView;*/
		
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		
		return ActionResult.fail(500 , "【"+request.getMethod()+"】"+request.getRequestURI()+"方法异常");
	}
	
	/**
     * 捕捉404异常,这个方法只在配置
     * spring.mvc.throw-exception-if-no-handler-found=true来后起作用
     * 
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ActionResult handlerNotFound(HttpServletRequest request , HttpServletResponse response , NoHandlerFoundException e) {
    	response.setStatus(HttpStatus.NOT_FOUND.value());
    	
    	return ActionResult.fail(404 , "没有【"+request.getMethod()+"】"+request.getRequestURI()+"方法可以访问");
    }

}
