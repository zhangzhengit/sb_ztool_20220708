package com.votool.apidoc;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年8月14日
 *
 */
public class ApiReturnTypeTClassInfo {

	private String className;
	private String json;

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

	public ApiReturnTypeTClassInfo(String className, String json) {
		super();
		this.className = className;
		this.json = json;
	}

	public ApiReturnTypeTClassInfo() {
		super();
	}
	
}
