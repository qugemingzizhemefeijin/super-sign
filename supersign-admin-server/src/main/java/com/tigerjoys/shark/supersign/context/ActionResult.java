package com.tigerjoys.shark.supersign.context;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.tigerjoys.nbs.common.IErrorCodeEnum;
import com.tigerjoys.nbs.common.utils.Tools;
import com.tigerjoys.shark.supersign.constant.Const;
import com.tigerjoys.shark.supersign.enums.ErrCodeEnum;

/**
 * 保存信息返回结果
 *
 */
public class ActionResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8274268114871910838L;
	
	/**
	 * 刷新本页面
	 */
	public static final int RESULT_TYPE_SELFPAGE = 1;
	
	/**
	 * 执行success函数
	 */
	public static final int RESULT_TYPE_FUNCTION = 2;
	
	/**
	 * 关闭窗口并且刷新页面
	 */
	public static final int RESULT_TYPE_CLOSE_BOX_SELFPAGE = 3;
	
	/**
	 * 关闭窗口并且执行函数
	 */
	public static final int RESULT_TYPE_CLOSE_BOX_FUNCTION = 4;
	
	/**
	 * 关闭窗口
	 */
	public static final int RESULT_TYPE_CLOSE_BOX = 5;
	
	/**
	 * 跳转到指定链接
	 */
	public static final int RESULT_TYPE_URL = 6;
	
	/**
	 * 不做任何事情
	 */
	public static final int RESULT_TYPE_NO_OPERATE = 7;
	
	/**
	 * 关闭本页面并且刷新父页面
	 */
	public  static final int RESULT_TYPE_CLOSE_SELFPAGE_RELOAD_OPENER = 8;
	
	/**
	 * 关闭本页面
	 */
	public  static final int RESULT_TYPE_CLOSE_SELFPAGE = 9;
	
	/**
	 * 关闭窗口并且刷新table表格
	 */
	public static final int RESULT_TYPE_REFRESH_TABLE = 10;
	
	/**
	 * 关闭窗口并且刷新table表格的当前页
	 */
	public static final int RESULT_TYPE_CLOSE_REFRESH_CURRENT_PAGE_TABLE = 11;
	
	/**
	 * 关闭当前页面并且刷新table表格的当前页
	 */
	public static final int RESULT_TYPE_CLOSE_OPENER_REFRESH_CURRENT_PAGE_TABLE = 12;

	/**
	 * 是否成功
	 */
	private boolean success;
	
	/**
	 * 返回码
	 */
	private int code;
	
	/**
	 * 返回描述
	 */
	private String msg;
	
	/**
	 * 返回的数据
	 */
	private Object data;
	
	/**
	 * 返回处理类型
	 */
	private Integer resultType;
	
	/**
	 * 返回ActionResult
	 * @param code - 返回码
	 * @param msg - 返回描述
	 * @param data - 返回的数据
	 * @param resultType - 返回处理类型
	 * @return ActionResult
	 */
	public static ActionResult getResult(int code , String msg , Object data , Integer resultType){
		ActionResult result = new ActionResult();
		result.code = code;
		result.msg = msg!=null?msg:ErrCodeEnum.error.getDesc();
		result.data = data;
		result.resultType = resultType;
		if(code == 0) result.success = true;
		
		return result;
	}
	
	/**
	 * 返回成功
	 * @return ActionResult
	 */
	public static ActionResult success() {
		return getResult(0 , "操作成功" , null , null);
	}
	
	/**
     * 返回成功
     * @return ActionResult
     */
    public static ActionResult success(Integer resultType) {
        return getResult(0 , "操作成功" , null , resultType);
    }
	
	/**
	 * 返回成功
	 * @return ActionResult
	 */
	public static ActionResult success(Integer resultType , Object data) {
		return getResult(0 , "操作成功" , data , resultType);
	}
	
	/**
	 * 返回成功
	 * @return ActionResult
	 */
	public static ActionResult success(String msg) {
		return getResult(0 , msg , null , null);
	}
	
	/**
	 * 返回成功
	 * @return ActionResult
	 */
	public static ActionResult success(String msg , Object data) {
		return getResult(0 , msg , data , null);
	}

	/**
	 * 返回成功
	 * @return ActionResult
	 */
	public static ActionResult success(Object data) {
		return getResult(0 , "操作成功" , data , null);
	}
	
	/**
	 * 返回成功
	 * @return ActionResult
	 */
	public static ActionResult success(String msg , Object data , Integer resultType) {
		return getResult(0 , msg , data , resultType);
	}
	
	/**
	 * 返回默认的错误失败
	 * @return - ActionResult
	 */
	public static ActionResult fail() {
		int code = ErrCodeEnum.error.getCode();
		
		return getResult(code , ErrCodeEnum.getDescByCode(code) , null , null);
	}
	
	/**
	 * 返回默认的错误失败
	 * @param error - IErrorCodeEnum
	 * @return - ActionResult
	 */
	public static ActionResult fail(IErrorCodeEnum error) {
		return getResult(error.getCode() , error.getDesc() , null , null);
	}
	
	/**
	 * 返回默认的错误失败
	 * @return - ActionResult
	 */
	public static ActionResult fail(int code) {
		return getResult(code , ErrCodeEnum.getDescByCode(code) , null , null);
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @param codemsg - 返回描述
	 * @return ActionResult
	 */
	public static ActionResult fail(int code , String codemsg) {
		return getResult(code , codemsg , null , null);
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @return ActionResult
	 */
	public static ActionResult fail(int code , Integer resultType) {
		return getResult(code , ErrCodeEnum.getDescByCode(code) , null , resultType);
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @param codemsg - 返回描述
	 * @param data - 返回的数据
	 * @return ActionResult
	 */
	public static ActionResult fail(int code , String codemsg , Object data) {
		return getResult(code , codemsg , data , null);
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @param codemsg - 返回描述
	 * @return ActionResult
	 */
	public static ActionResult fail(int code , String codemsg , Integer resultType) {
		return getResult(code , codemsg , null , resultType);
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @param codemsg - 返回描述
	 * @param data - 返回的数据
	 * @return ActionResult
	 */
	public static ActionResult fail(int code , String codemsg , Object data , Integer resultType) {
		return getResult(code , codemsg , data , resultType);
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @param data - 返回的数据
	 * @return ActionResult
	 */
	public static ActionResult fail(int code , Object data) {
		return getResult(code , ErrCodeEnum.getDescByCode(code) , data , null);
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @param data - 返回的数据
	 * @return ActionResult
	 */
	public static ActionResult fail(int code , Object data , Integer resultType) {
		return getResult(code , ErrCodeEnum.getDescByCode(code) , data , resultType);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getResultType() {
		return resultType;
	}

	public void setResultType(Integer resultType) {
		this.resultType = resultType;
	}
	
	/**
	 * 返回一个错误视图
	 * @param err - 错误原因
	 * @return ModelAndView
	 */
	public static ModelAndView errorModel(HttpServletRequest request, String err){
		String viewName;
		//如果是Ajax的话
		if(request != null && Const.isAjax(request)) {
			viewName = "error_ajax";
		} else {
			viewName = "error_page";
		}
		
		ModelAndView model =  new ModelAndView(viewName);
		model.addObject("msg", err);
		model.addObject("referer", Tools.getReferer(request));
		model.addObject("title", "错误警告");
		
		return model;
	}

}
