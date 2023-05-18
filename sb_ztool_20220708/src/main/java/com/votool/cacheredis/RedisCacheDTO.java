package com.votool.cacheredis;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 存储/取出对象到redis的对象，此对象使用java序列化后存储byte[]到redis，
 * 取出byte[]后用java反序列化到此对象,然后getObject()得到存储的对象。
 *
 * @author zhangzhen
 * @date 2022年10月25日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RedisCacheDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 具体要存储/取出的对象
	 */
	private Object object;

}
