package com.votool.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年10月29日
 *
 */
@Configuration
public class ZLockConf {

	@Value("${server.port}")
	private Integer serverPort;

	@Value("${spring.application.name}")
	private String applicationName;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Bean
	public ZLock zLock() {
		final ZLock lock = new ZLock(this.redisTemplate, this.serverPort, this.applicationName);
		return lock;
	}

}
