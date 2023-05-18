package com.votool.limit;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * redis实现的限流，不太精确，不能精确控制到一秒内正好N次
 *
 * @author zhangzhen
 * @date 2022年7月6日
 *
 */
@Component
public class ZRedisLimit {

	private static final int TIMEOUT = 1 * 60;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public boolean check(final String key, final int limitMAX) {
		if (StringUtils.isEmpty(key) || key.trim().isEmpty()) {
			throw new IllegalArgumentException("key 必须不为空");
		}
		if (limitMAX <= 0) {
			throw new IllegalArgumentException("limitMAX 必须大于0");
		}


		final long currentTimeMillis = System.currentTimeMillis();
		final long second = currentTimeMillis / 1000;

		final String k2 = generateLimitCacheKey(key, second);

		final Long iv = this.redisTemplate.opsForValue().increment(k2, 1);
		this.redisTemplate.expire(k2, ZRedisLimit.TIMEOUT, TimeUnit.SECONDS);

		return iv.intValue() <= limitMAX;
	}

	public static String generateLimitCacheKey(final String key, final long second) {
		return "qpslimit:" + key + ":" + second;
	}

}
