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
public enum ZToolEE {
	
	OK(0, "OK"),
	
	ERROR_COMMON(50000, "ERROR"),
	
	API_LIMIT(50001,"API LIMIT"),
	
	ERROR_TOKEN_EXPIRED(40001,"TOKEN_EXPIRED_EXCEPTION"),
	
	ERROR_TOKEN(40002,"TOEKN:"),
	
	ERROR_TOKEN_DECODE(40003,"TOKEN_DECODE_EXCEPTION"),

	;

	private int code;
	private String message;

}
