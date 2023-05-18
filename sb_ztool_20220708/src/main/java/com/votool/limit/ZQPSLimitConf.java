package com.votool.limit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.votool.login.LoginInterceptor;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月8日
 *
 */
@Configuration
public class ZQPSLimitConf {

	@Autowired
	private LoginInterceptor loginInterceptor;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private ZRedisLimit limit;

	@Bean
	public QPSLimitInterceptor qpsLimitInterceptor() {

		return new QPSLimitInterceptor(this.loginInterceptor, this.redisTemplate, this.limit);
	}

}
