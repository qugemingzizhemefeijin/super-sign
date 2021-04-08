package com.tigerjoys.shark.supersign.constant;

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
	
	private RedisConstant() {
		
	}

}
