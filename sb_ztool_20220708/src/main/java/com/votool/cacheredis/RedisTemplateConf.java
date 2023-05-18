package com.votool.cacheredis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年8月10日
 *
 */
@Configuration
@Primary
public class RedisTemplateConf {

	@Bean(name = "bytesRedisTemplateForZRedisCache")
	public RedisTemplate<String, byte[]> bytesRedisTemplate(final RedisConnectionFactory connectionFactory) {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "RedisTemplateConf.bytesRedisTemplate()");

		final RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		// 设置key和value的序列化规则
		redisTemplate.setValueSerializer(RedisSerializer.byteArray());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.afterPropertiesSet();

		return redisTemplate;

	}

}
