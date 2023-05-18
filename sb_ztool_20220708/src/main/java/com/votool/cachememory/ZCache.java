package com.votool.cachememory;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 缓存接口定义
 *
 * @author zhangzhen
 * @date 2022年7月1日
 *
 */
public interface ZCache<T> {

	public T set(String key, T value);

	public boolean setIfExist(String key, T value);

	public boolean setIfAbsent(String key, T value);

	public boolean setIfEquals(String key, T oldValue, T newValue);

	public T get(String key);

	public int size();

	public boolean remove(String key);

	public T getBySupplier(final String key, Supplier<T> supplier);

	public T getAndExpire(final String key, final Integer time, final TimeUnit timeUnit, Supplier<T> supplier);

	public void expire(String key, Integer time, TimeUnit timeUnit);

}
