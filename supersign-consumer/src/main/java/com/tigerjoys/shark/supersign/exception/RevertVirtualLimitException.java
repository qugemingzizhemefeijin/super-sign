package com.tigerjoys.shark.supersign.exception;

/**
 * 抛出此异常需要将虚拟安装数量归还
 *
 */
public class RevertVirtualLimitException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3815497464306866457L;

	public RevertVirtualLimitException() {
	}

	public RevertVirtualLimitException(String message) {
		super(message);
	}

	public RevertVirtualLimitException(String message, Throwable cause) {
		super(message, cause);
	}

	public RevertVirtualLimitException(Throwable cause) {
		super(cause);
	}

}
