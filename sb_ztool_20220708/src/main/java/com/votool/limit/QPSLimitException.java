package com.votool.limit;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月6日
 *
 */
public class QPSLimitException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public QPSLimitException(final String message) {
		super(message);
	}
}
