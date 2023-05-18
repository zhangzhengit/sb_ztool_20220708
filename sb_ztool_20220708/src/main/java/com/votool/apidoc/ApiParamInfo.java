package com.votool.apidoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * api的方法的参数信息
 *
 *
 * @author zhangzhen
 * @date 2022年8月14日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiParamInfo {

	private String annoName;
	private String type;
	private String name;
}
