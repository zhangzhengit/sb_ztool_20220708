package com.votool.cacheredis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 *
 * 启用 ZRedisCacheService，T 必须是 implements Serializable的
 *
 * @author zhangzhen
 * @date 2022年10月29日
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Import(value = { ZRedisCacheService.class, RedisTemplateConf.class })
public @interface EnableZRedisCache {

}
