package com.votool.login;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * 用于启用登录校验，新项目的配置类上加入此注解
 *
 * @EnableLoginVerification
 *
 * @author zhangzhen
 * @date 2022年7月8日
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Import(value = { LoginInterceptor.class, LoginApplicationContextAware.class, LoginVerificationConfiguration.class,
		LoginWebConfig.class, ZControllerAdvice.class })
public @interface EnableLoginVerification {

}
