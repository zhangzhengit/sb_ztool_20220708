package com.votool.exception;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年6月30日
 *
 */
public class NotLoginException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private final Integer code;
	private final String message;

	public Integer getCode() {
		return code;
	}



	public NotLoginException(final Integer code, final String message) {
		this.code = code;
		this.message = message;
	}

}
