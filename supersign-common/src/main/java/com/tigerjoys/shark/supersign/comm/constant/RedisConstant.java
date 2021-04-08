package com.tigerjoys.shark.supersign.comm.constant;

/**
 * Redis常量
 *
 */
public final class RedisConstant {
	
	/**
	 * 公共使用的Redis缓存客户端名称
	 */
	public static final String PUBLIC_REDIS_SPRING_BEAN_NAME = "publicRedisCache";
	
	/**
	 * mybatis DAO的Redis缓存客户端名称
	 */
	public static final String PUBLIC_MYBATIS_SPRING_BEAN_NAME = "mybatisRedisCache";
	
	/**
	 * 签名进度KEY，String.format appid,udid
	 */
	public static final String SIGN_PROCESS_KEY = "signProcess::%s-%s";
	
	/**
	 * 签名进度KEY的过期时间
	 */
	public static final int SIGN_PROCESS_KEY_EXPIRE = 120;
	
	/**
	 * 签名队列名称
	 */
	public static final String SIGN_QUEUE_KEY = "sign::transcode_queue";
	
	private RedisConstant() {
		
	}

}
