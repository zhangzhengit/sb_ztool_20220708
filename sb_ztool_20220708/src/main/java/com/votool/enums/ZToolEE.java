package com.votool.enums;

/**
 *
 * 
 * @author zhangzhen
 * @date 2020-12-09 11:07:04
 * 
 */
public enum ZToolEE {
	
	OK(0, "OK"),
	
	ERROR_COMMON(50000, "ERROR"),
	
	API_LIMIT(50001,"API LIMIT"),
	
	ERROR_TOKEN_EXPIRED(40001,"TOKEN_EXPIRED_EXCEPTION"),
	
	ERROR_TOKEN(40002,"TOEKN:"),
	
	ERROR_TOKEN_DECODE(40003,"TOKEN_DECODE_EXCEPTION"),

	;

	private final int code;
	private final String message;

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	private ZToolEE(int code, String message) {
		this.code = code;
		this.message = message;
	}

}
