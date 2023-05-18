package com.votool.cacheredis;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 *
 * redis-缓存操作类
 *
 * @author zhangzhen
 * @date 2022年10月22日
 *
 */
@Service
public class ZRedisCacheService<T> implements InitializingBean {

	@Autowired
	private RedisTemplate<String, byte[]> redisTemplate;
	private ValueOperations<String, byte[]> vvv;

	public T getAndSet(final String key, final Supplier<T> supplier, final long timeout, final TimeUnit unit) {
		final T t = this.get(key);
		final T t2 = supplier.get();
		this.set(key, t2, timeout, unit);
		return t;
	}

	public T getAndSet(final String key, final Supplier<T> supplier) {
		final T t = this.get(key);

		final T t2 = supplier.get();
		this.set(key, t2);

		return t;
	}

	public T getBySupplier(final String key, final Supplier<T> supplier, final long timeout, final TimeUnit unit) {
		final T t = this.get(key);
		if (t != null) {
			return t;
		}

		final T t2 = supplier.get();
		this.set(key, t2, timeout, unit);

		return t2;
	}

	public T getBySupplier(final String key, final Supplier<T> supplier) {
		final T t = this.get(key);
		if (t != null) {
			return t;
		}

		final T t2 = supplier.get();
		this.set(key, t2);

		return t2;
	}

	public T get(final String key) {
		final byte[] ba = this.vvv.get(buildKey(key));
		if (Objects.isNull(ba) || ba.length <= 0) {
			return null;
		}

		final Object deserialize = ZSerializeJAVA.deserialize(ba);
		final RedisCacheDTO cacheDTO = (RedisCacheDTO) deserialize;
		return (T) cacheDTO.getObject();
	}

	public T set(final String key, final Supplier<T> supplier, final long timeout, final TimeUnit unit) {
		final RedisCacheDTO redisCacheDTO = new RedisCacheDTO();
		final T t = supplier.get();
		redisCacheDTO.setObject(t);
		final byte[] ba = ZSerializeJAVA.serialize(redisCacheDTO);
		this.vvv.set(buildKey(key), ba, timeout, unit);
		return t;
	}

	public T set(final String key, final Object object, final long timeout, final TimeUnit unit) {
		final RedisCacheDTO redisCacheDTO = new RedisCacheDTO();
		redisCacheDTO.setObject(object);
		final byte[] ba = ZSerializeJAVA.serialize(redisCacheDTO);
		// 不复用this.set(Key,OBject)
		this.vvv.set(buildKey(key), ba, timeout, unit);

		return (T) object;
	}

	public void set(final String key, final Object object) {
		final RedisCacheDTO redisCacheDTO = new RedisCacheDTO();
		redisCacheDTO.setObject(object);
		final byte[] ba = ZSerializeJAVA.serialize(redisCacheDTO);
		this.vvv.set(buildKey(key), ba);
	}

	public Boolean remove(final String key) {
		final String k = buildKey(key);
		final Boolean delete = this.redisTemplate.delete(k);
		return delete;
	}

	private static String buildKey(final String key) {
		return CACHE + SEPARATOR + key;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.vvv = this.redisTemplate.opsForValue();
	}

	private static final String SEPARATOR = ":";
	private static final String CACHE = "CACHE";

}
