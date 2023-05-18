package com.votool.limit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月6日
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QPSLimit {

	public final static int MIN_QPS = 1;
	public final static int DEFAULT_QPS = 50;

	public final static int MAX_QPS = 200;

	public final static String DEFAULT_ERROR_MESSAGE = "访问过于频繁，请稍后重试！";

	/**
	 * 不区分任务请求，所有请求共享一个QPS最大值
	 */
	public final static String DISTINGUISH_NULL = "";

	/**
	 * 根据用户token区分
	 */
	public final static String TOKEN = "TOKEN";

	/**
	 * 根据后台统一签发的秘钥区分
	 */
	public final static String SECRET_KEY = "SECRET_KEY";
	/**
	 * 根据session区分
	 */
	public final static String SESSION = "SESSION";

	/**
	 * 按什么区分限制qps，默认不区分
	 *
	 * @return
	 */
	String distinguish() default QPSLimit.DISTINGUISH_NULL;

	int qps() default QPSLimit.DEFAULT_QPS;

	String message() default QPSLimit.DEFAULT_ERROR_MESSAGE;

}
