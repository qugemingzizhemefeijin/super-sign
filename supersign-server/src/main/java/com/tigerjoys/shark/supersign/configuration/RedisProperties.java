package com.tigerjoys.shark.supersign.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.redis")
public class RedisProperties {
	
	private String host;
	
	private int port;
	
	private int db;
	
	private int timeout;
	
	private int active;
	
	private int idle;
	
	private long waitMillis;
	
	private boolean borrowCheck;
	
	private boolean returnCheck;
	
	private String password;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getDb() {
		return db;
	}

	public void setDb(int db) {
		this.db = db;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getIdle() {
		return idle;
	}

	public void setIdle(int idle) {
		this.idle = idle;
	}

	public long getWaitMillis() {
		return waitMillis;
	}

	public void setWaitMillis(long waitMillis) {
		this.waitMillis = waitMillis;
	}

	public boolean isBorrowCheck() {
		return borrowCheck;
	}

	public void setBorrowCheck(boolean borrowCheck) {
		this.borrowCheck = borrowCheck;
	}

	public boolean isReturnCheck() {
		return returnCheck;
	}

	public void setReturnCheck(boolean returnCheck) {
		this.returnCheck = returnCheck;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
