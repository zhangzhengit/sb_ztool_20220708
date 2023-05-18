package com.votool.apidoc;

import java.util.List;

import com.google.common.collect.Lists;

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
public class APIClassInfo {

	private String className;

	private List<APIInfo> apiList;

	public void addApi(final APIInfo apiInfo) {
		if (this.getApiList() == null) {
			this.setApiList(Lists.newArrayList());
			;
		}

		this.getApiList().add(apiInfo);
	}
}
