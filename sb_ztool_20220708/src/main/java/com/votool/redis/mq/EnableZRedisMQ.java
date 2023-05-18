package com.votool.redis.mq;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.votool.cacheredis.RedisTemplateConf;

/**
 * 表示 启用ZRedisMQ
 *
 * @author zhangzhen
 * @date 2022年11月24日
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import(value = { MQISA.class, MQClient.class, MQChannel.class, ZRedisMQConfiguration.class, RedisTemplateConf.class })
public @interface EnableZRedisMQ {

}
