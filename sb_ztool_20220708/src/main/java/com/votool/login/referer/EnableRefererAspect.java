package com.votool.login.referer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * 使用 @RefererAnnotation 注解来标识需要记录referer的接口
 *
 * @author zhangzhen
 * @date 2022年10月25日
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Import(value = { RefererAspect.class})
public @interface EnableRefererAspect {

}
