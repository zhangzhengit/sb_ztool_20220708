package com.votool.cachememory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 *
 * 启用 ZMemoryCache，使用 ZMemoryCache 类来操作
 *
 * @author zhangzhen
 * @date 2022年10月29日
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Import(value = { ZMemoryCache.class})
public @interface EnableZMemoryCache {

}
