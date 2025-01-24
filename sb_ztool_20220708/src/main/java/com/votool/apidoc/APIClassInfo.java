package com.votool.apidoc;

import java.util.List;

import com.google.common.collect.Lists;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年8月14日
 *
 */
public class APIClassInfo {

	private String className;

	private List<APIInfo> apiList;

	public void addApi(final APIInfo apiInfo) {
		if (this.getApiList() == null) {
			this.setApiList(Lists.newArrayList());
		}

		this.getApiList().add(apiInfo);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<APIInfo> getApiList() {
		return apiList;
	}

	public void setApiList(List<APIInfo> apiList) {
		this.apiList = apiList;
	}

	public APIClassInfo(String className, List<APIInfo> apiList) {
		super();
		this.className = className;
		this.apiList = apiList;
	}

	public APIClassInfo() {
	}
}
