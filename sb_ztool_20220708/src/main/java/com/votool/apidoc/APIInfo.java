package com.votool.apidoc;

import java.lang.reflect.Method;
import java.util.List;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年8月14日
 *
 */
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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String[] getRequestValues() {
		return requestValues;
	}

	public void setRequestValues(String[] requestValues) {
		this.requestValues = requestValues;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getReturnTypeT() {
		return returnTypeT;
	}

	public void setReturnTypeT(String returnTypeT) {
		this.returnTypeT = returnTypeT;
	}

	public ApiReturnTypeClassInfo getApiReturnTypeClassInfo() {
		return apiReturnTypeClassInfo;
	}

	public void setApiReturnTypeClassInfo(ApiReturnTypeClassInfo apiReturnTypeClassInfo) {
		this.apiReturnTypeClassInfo = apiReturnTypeClassInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ApiParamInfo> getParamList() {
		return paramList;
	}

	public void setParamList(List<ApiParamInfo> paramList) {
		this.paramList = paramList;
	}

	public APIInfo(String className, Method method, String requestMethod, String[] requestValues, String modifier,
			String returnType, String returnTypeT, ApiReturnTypeClassInfo apiReturnTypeClassInfo, String name,
			List<ApiParamInfo> paramList) {
		super();
		this.className = className;
		this.method = method;
		this.requestMethod = requestMethod;
		this.requestValues = requestValues;
		this.modifier = modifier;
		this.returnType = returnType;
		this.returnTypeT = returnTypeT;
		this.apiReturnTypeClassInfo = apiReturnTypeClassInfo;
		this.name = name;
		this.paramList = paramList;
	}

	public APIInfo() {
		super();
	}
	
}
