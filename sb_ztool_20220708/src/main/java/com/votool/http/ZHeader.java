package com.votool.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *	header
 *
 * @author zhangzhen
 * @date 2023年7月13日
 *
 */
@Data
@AllArgsConstructor
public class ZHeader {

	final String name;
	final Object value;

}
