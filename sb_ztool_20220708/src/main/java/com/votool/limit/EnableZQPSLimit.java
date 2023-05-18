package com.votool.limit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import com.votool.login.LoginInterceptor;
import com.votool.login.LoginVerificationConfiguration;

/**
 * 启用qps限制,使用@QPSLimit 来标识需要限制qps的方法
 *
 * @author zhangzhen
 * @date 2022年7月8日
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Import(value = { ZQPSLimitConf.class, LimitWebConfig.class, LoginInterceptor.class,
		LoginVerificationConfiguration.class, ZRedisLimit.class })
public @interface EnableZQPSLimit {

}
