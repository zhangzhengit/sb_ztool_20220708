package com.votool.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年7月13日
 *
 */
@Getter
@AllArgsConstructor
public enum ZHttpMethodEnum {

	GET("GET"),

	POST("POST"),

	;

	private String method;

}
