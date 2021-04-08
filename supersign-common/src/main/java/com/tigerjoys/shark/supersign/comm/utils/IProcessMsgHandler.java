package com.tigerjoys.shark.supersign.comm.utils;

/**
 * 消息处理接口
 *
 */
public interface IProcessMsgHandler {
	
	/**
	 * 每一行的命令行消息
	 * @param msg - 消息
	 */
	public void hand(String msg);

}
