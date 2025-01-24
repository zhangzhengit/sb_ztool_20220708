package com.votool.apidoc;

import java.util.Set;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年8月14日
 *
 */
public class TUser {
	private Integer id;
	private String name;

	private String mobile;

	private Set<Integer> idSet;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Set<Integer> getIdSet() {
		return idSet;
	}

	public void setIdSet(Set<Integer> idSet) {
		this.idSet = idSet;
	}
	
}
