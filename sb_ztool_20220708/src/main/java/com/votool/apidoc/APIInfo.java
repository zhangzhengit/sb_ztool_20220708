package com.votool.apidoc;

import java.lang.reflect.Method;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年8月14日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIInfo {

	private String className;

	private Method method;
	private String requestMethod;
	private String[] requestValues;

	private String modifier;
	private String returnType;
	private String returnTypeT;
	private ApiReturnTypeClassInfo apiReturnTypeClassInfo;

	private String name;

	private List<ApiParamInfo> paramList;

}
