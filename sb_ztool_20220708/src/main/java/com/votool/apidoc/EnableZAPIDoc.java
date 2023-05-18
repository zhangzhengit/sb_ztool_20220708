package com.votool.apidoc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * 启动ZAPIDoc
 *
 * @author zhangzhen
 * @date 2022年8月1日
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Import(value = { APIScanner.class, APIDocAPI.class })
public @interface EnableZAPIDoc {

}
