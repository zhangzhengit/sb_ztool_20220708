package com.votool.exception;

import lombok.Getter;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年6月30日
 *
 */
public class NotLoginException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	@Getter
	private final Integer code;
	private final String message;


	public NotLoginException(final Integer code, final String message) {
		this.code = code;
		this.message = message;
	}

}
