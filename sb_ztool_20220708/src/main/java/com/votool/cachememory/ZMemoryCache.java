package com.votool.cachememory;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

/**
 * 缓存接口的内存实现，使用WeakHashMap实现，纯粹用于缓存(存丢了也无所谓的数据)，不要存储其他东西.
 *
 * @author zhangzhen
 * @date 2022年7月1日
 *
 */
@Component
public class ZMemoryCache<T> implements ZCache<T> {

	private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	private final Map<String, T> map = new WeakHashMap<>();

	@Override
	public synchronized T set(final String key, final T value) {
		final T put = this.map.put(key, value);
		return put;
	}

	@Override
	public synchronized boolean setIfExist(final String key, final T value) {
		final T v = this.get(key);
		if (v != null) {
			this.set(key, value);
			return true;
		}

		return false;
	}

	@Override
	public synchronized boolean setIfEquals(final String key, final T oldValue, final T newValue) {
		final T v = this.get(key);
		if (v != null && Objects.equals(v, oldValue)) {
			this.set(key, newValue);
			return true;
		}

		return false;
	}

	@Override
	public synchronized boolean setIfAbsent(final String key, final T value) {
		final T v = this.get(key);
		if (v == null) {
			this.set(key, value);
			return true;
		}

		return false;
	}

	@Override
	public synchronized T get(final String key) {
		return this.map.get(key);
	}

	@Override
	public synchronized boolean remove(final String key) {
		final T remove = this.map.remove(key);
		return remove != null;
	}

	@Override
	public synchronized T getBySupplier(final String key, final Supplier<T> supplier) {
		final T t = this.get(key);
		if (t != null) {
			return t;
		}

		final T t2 = supplier.get();
		this.set(key, t2);
		return t2;
	}

	@Override
	public synchronized T getAndExpire(final String key, final Integer time, final TimeUnit timeUnit,
			final Supplier<T> supplier) {

		final T v = this.getBySupplier(key, supplier);
		if (v == null) {
			return null;
		}

		this.expire(key, time, timeUnit);
		return v;
	}

	@Override
	public synchronized void expire(final String key, final Integer time, final TimeUnit timeUnit) {
		this.scheduledExecutorService.schedule(new Runnable() {
			@Override
			public void run() {
//				System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
//						+ "ZMemoryCache.expire(...).new Runnable() {...}.run()");
				final boolean remove = ZMemoryCache.this.remove(key);
			}
		}, time, timeUnit);
	}

	@Override
	public synchronized int size() {
		return this.map.size();
	}

}
