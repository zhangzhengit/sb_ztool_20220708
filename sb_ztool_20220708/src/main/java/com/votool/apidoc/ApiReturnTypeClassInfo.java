package com.votool.apidoc;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年8月14日
 *
 */
public class ApiReturnTypeClassInfo {

	private String className;

	private String json;

	private ApiReturnTypeTClassInfo t;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public ApiReturnTypeTClassInfo getT() {
		return t;
	}

	public void setT(ApiReturnTypeTClassInfo t) {
		this.t = t;
	}

}
