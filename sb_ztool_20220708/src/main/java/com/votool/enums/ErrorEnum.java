package com.votool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 *
 * @author zhangzhen
 * @date 2020-12-09 11:07:04
 *
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum {

	OK(0, "OK"),

	OK_REDIRECT(88888, "OK"),

	ERROR_NOT_LOGIN(10000, "NOT_LOGIN_IN"),
	ERROR_COMMON(50000, "ERROR"),

	;

	private int code;
	private String message;

}
