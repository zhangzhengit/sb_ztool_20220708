package com.votool.random;

import java.security.SecureRandom;

/**
 *
 * 随机数
 *
 * @author zhangzhen
 * @date 2022年10月21日
 *
 */
public class ZR {

	static SecureRandom random = new SecureRandom();

	public static int nextInt(final int bound) {
		final int nextInt = ZR.random.nextInt(bound);
		return nextInt;
	}
}
