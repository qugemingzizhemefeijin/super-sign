package com.tigerjoys.shark.supersign.configuration;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UrlPathHelper;

import com.tigerjoys.nbs.common.enums.ECharset;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.nbs.web.annotations.LogRule;
import com.tigerjoys.nbs.web.annotations.NoLog;
import com.tigerjoys.nbs.web.annotations.NoLogin;
import com.tigerjoys.nbs.web.utils.PageLoadClassUtil;
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.constant.Param;
import com.tigerjoys.shark.supersign.context.BeatContext;
import com.tigerjoys.shark.supersign.context.RequestUtils;
import com.tigerjoys.shark.supersign.dto.list.ShowMenuDto;
import com.tigerjoys.shark.supersign.inter.contract.IAdminContract;
import com.tigerjoys.shark.supersign.inter.contract.IAdminMenuContract;
import com.tigerjoys.shark.supersign.inter.entity.AdminEntity;
import com.tigerjoys.shark.supersign.inter.entity.AdminMenuEntity;
import com.tigerjoys.shark.supersign.service.IAdminService;
import com.tigerjoys.shark.supersign.utils.Helper;
import com.tigerjoys.shark.supersign.utils.LoggerUtil;
import com.tigerjoys.shark.supersign.utils.WebUtis;

/**
 * 支付API拦截器
 *
 */
public class WebControllerInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebControllerInterceptor.class);
	
	@Autowired
	private IAdminContract adminContract;
	
	@Autowired
	private IAdminMenuContract adminMenuContract;
	
	@Autowired
	private IAdminService adminService;
	
	/**
	 * 记录接口调用完毕的执行时间
	 */
	private static final ThreadLocal<Long> executiveTime = new ThreadLocal<Long>();
	
	/**
	 * 内存管理Bean
	 */
	private MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
	
	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 *
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//必须先获取一下session，否则下面的LogUtil记录sessionId的时候，会报错
		//Cannot create a session after the response has been committed
		HttpSession session = request.getSession();
		
		//此处必须重置线程变量
		BeatContext context = new BeatContext(request , response);
		RequestUtils.bindBeatContextToCurrentThread(context);
		
		try {
			boolean isLog = true;
			if(handler != null && handler instanceof HandlerMethod) {
				executiveTime.set(System.currentTimeMillis());
				
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				Method method = handlerMethod.getMethod();
				
				//查看是否不需要记录日志。。
				if(AnnotationUtils.findAnnotation(method, NoLog.class) != null) {
					isLog = false;
					context.setLog(isLog);
				} else {
					//日志记录规则
					LogRule logRule = AnnotationUtils.findAnnotation(method, LogRule.class);
					if(logRule != null) {
						context.setLogRule(logRule);
					}
				}
				
				//设置context method
				context.setMethod(method);
				context.setClazz(handlerMethod.getBeanType());
				
				//TODO 此处到时候要换成这个代码，测试开发阶段可以直接写死
				long adminId = 1;
				if(!Const.IS_TEST) {
					adminId = Tools.parseLong(String.valueOf(session.getAttribute(Param.ADMID_LOGIN_SESSION)));
				}
				//long adminId = Tools.parseLong(String.valueOf(session.getAttribute(Param.ADMID_LOGIN_SESSION)));
				AdminEntity adminUser = adminContract.findById(adminId);
				
				context.setAdminId(adminId);
				context.setAdmin(adminUser);
				
				//此处如果不需要验证登录，则直接返回。但是也不去获取session中的用户属性
				if(AnnotationUtils.findAnnotation(method, NoLogin.class) == null) {
					if(adminUser == null) {
						if(Const.isAjax(request)) {
							response.addHeader(Param.RESPONSE_HEADER, "0");
						} else {
							String redurectURL = Helper.addOrUpdateParameter(request);
							response.sendRedirect(request.getContextPath()+"/login?"+Param.REDIRECT_URL+"="+Tools.encoder(redurectURL, ECharset.UTF_8.getName()));
						}
						return false;
					}
					//此处判断方法是否需要验证权限。
					/*if(adminUser.getRoleId() != Const.SUPER_ROLE && AnnotationUtils.findAnnotation(method, NoPermission.class) == null) {
						//此处判断是否有权限
						if(!isPermission(request, session)) {
							if(Const.isAjax(request)) {
								response.addHeader(Param.RESPONSE_HEADER, "1");
							}
							response.getWriter().print("没有权限，不允许访问，喵了个咪的。");
							
							logger.error("没有权限，不允许访问，喵了个咪的。");
							return false;
						}
					}*/
				} else {
					context.setNeedLogin(false);
					context.setPermission(false);
				}
			} else {
				LOGGER.warn(WebUtis.getLookupPathForRequest(request) + " handler is not HandlerMethod!");
			}
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			//记录访问日志。
			//如果要求加密，并且加密方法中没有参数，则在这里记录日志
			//否则将在解密的地方记录日志
			if(context.isLog()) {
				LoggerUtil.writerHitLog();
			}
		}
		
		return false;
	}
	
	/*
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	 */
	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
		//加载类
		PageLoadClassUtil.setVelocityContext("Const", Const.class, modelAndView);
		PageLoadClassUtil.setVelocityContext("Tools", Tools.class, modelAndView);
		
		//此处判断是否需要展示左侧按钮列表
		if(modelAndView != null &&
			//是redirect方式，则不要进来。
			!(modelAndView.getView() instanceof RedirectView) &&
			!(modelAndView.getViewName() != null && modelAndView.getViewName().startsWith("redirect"))) {
			modelAndView.addObject("LOGIN_ADMIN", RequestUtils.getCurrent().getAdmin());
			//如果是ajax则不需要获取按钮了
			if(!Const.isAjax(request)) {
				//必须要登录的
				if(RequestUtils.getCurrent().isNeedLogin()) {
					List<ShowMenuDto> menuList = adminService.getShowMenuTreeData(RequestUtils.getCurrent().getAdmin().getRoleId());
					//传递参数到前端
					modelAndView.addObject("permissionMenuList", menuList);
					
					//根据URL获得按钮
					boolean isSelectMenu = false;//是否选中了按钮
					UrlPathHelper urlPathHelper = new UrlPathHelper();
					String lookupPath = urlPathHelper.getLookupPathForRequest(request);
					if(lookupPath != null && lookupPath.startsWith("/")) {//选取出前3个/
						String[] p = lookupPath.substring(1).split("/");
						if(p != null && p.length >= 3) {
							//String newPath = '/'+p[0]+'/'+p[1]+'/'+p[2];
							
							AdminMenuEntity[] selectMenus = getSelectMenus(lookupPath);
							if(selectMenus[0] == null && selectMenus[1] == null && selectMenus[2] == null) {
								selectMenus = getSelectMenus('/'+p[0]+'/'+p[1]+'/'+p[2]);
							}
							
							modelAndView.addObject("permissionFstMenuId", selectMenus[0]!=null?selectMenus[0].getId():0);
							modelAndView.addObject("permissionSecMenuId", selectMenus[1]!=null?selectMenus[1].getId():0);
							modelAndView.addObject("permissionThdMenuId", selectMenus[2]!=null?selectMenus[2].getId():0);
							
							if(selectMenus[0] != null) {
								modelAndView.addObject("permissionFstMenu", selectMenus[0]);
								for(ShowMenuDto dto : menuList) {
									if(dto.getId() == selectMenus[0].getId()) {
										isSelectMenu = true;
										break;
									}
								}
							}
							if(selectMenus[1] != null) modelAndView.addObject("permissionSecMenu", selectMenus[1]);
							if(selectMenus[2] != null) modelAndView.addObject("permissionThdMenu", selectMenus[2]);
						}
					}
					
					modelAndView.addObject("isSelectMenu", isSelectMenu);
				}
				//此处返回到界面内存占用情况
				MemoryUsage heap = memory.getHeapMemoryUsage();
				modelAndView.addObject("memoryUsed", Tools.getDouble((double)heap.getUsed()/1024/1024/1024, 3));
				modelAndView.addObject("memoryMax", Tools.getDouble((double)heap.getMax()/1024/1024/1024, 3));
			}
		}
	}

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
	
	/**
	 * 根据URL获得选中的URL的按钮
	 * @param path - URL
	 * @return AdminMenuEntity[]
	 * @throws Exception
	 */
	private AdminMenuEntity[] getSelectMenus(String path) throws Exception{
		AdminMenuEntity[] selectMenuIds = new AdminMenuEntity[3];
		
		AdminMenuEntity selectMenu = adminService.getMenuByShowUrl(path);
		if(selectMenu != null) {
			if(selectMenu.getParentId() == 0) {
				selectMenuIds[0] = selectMenu;
			} else {
				AdminMenuEntity parentMenu = adminMenuContract.findById(selectMenu.getParentId());
				if(parentMenu != null) {
					if(parentMenu.getParentId() == 0) {
						selectMenuIds[0] = parentMenu;
						selectMenuIds[1] = selectMenu;
					} else {
						AdminMenuEntity top_ParentMenu = adminMenuContract.findById(parentMenu.getParentId());
						if(top_ParentMenu != null) {
							selectMenuIds[0] = top_ParentMenu;
							selectMenuIds[1] = parentMenu;
							selectMenuIds[2] = selectMenu;
						} else {
							selectMenuIds[0] = parentMenu;
							selectMenuIds[1] = selectMenu;
						}
					}
				} else {
					selectMenuIds[0] = selectMenu;
				}
			}
		}
		
		return selectMenuIds;
	}

}
