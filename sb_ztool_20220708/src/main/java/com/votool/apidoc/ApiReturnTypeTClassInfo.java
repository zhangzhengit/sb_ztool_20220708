package com.votool.apidoc;

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
public class ApiReturnTypeTClassInfo {

	private String className;
	private String json;
}
