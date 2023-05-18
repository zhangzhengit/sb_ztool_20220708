package com.votool.lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 *
 * 启用ZLock分布式锁，使用@see ZLock 类来加锁/解锁
 *
 * @author zhangzhen
 * @date 2022年10月29日
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Import(value = { ZLockConf.class })
public @interface EnableZLock {

}
