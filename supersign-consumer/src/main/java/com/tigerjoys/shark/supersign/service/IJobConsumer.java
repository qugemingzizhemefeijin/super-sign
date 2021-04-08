package com.tigerjoys.shark.supersign.service;

/**
 * 消费安装请求接口
 *
 */
public interface IJobConsumer {
	
	/**
	 * 启动
	 * @throws InterruptedException
	 */
	public void run() throws InterruptedException;

}
