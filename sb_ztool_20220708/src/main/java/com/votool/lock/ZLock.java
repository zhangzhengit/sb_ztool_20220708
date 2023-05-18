package com.votool.lock;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * 一个Redis分布式锁，支持可重入性和超时自动解锁和自旋.
 *
 * Strig key = "xxx";
 * try {
 * 	 lock(key);
 * 	 xxxxxx...
 * } finally {
 * 	 unlock(key);
 * }
 *
 * @author zhangzhen
 * @date 2022年10月29日
 *
 */
public class ZLock {

	private static final String SEPARATOR = "_";

	private static final int WAIT_MS_NEVER = -1;

	private static final int TIMEOUT_NEVER_EXPIRE = -1;

	private final RedisTemplate<String, String> redisTemplate;
	private final ValueOperations<String, String> vvvv;

	private final Integer serverPort;
	private final String applicationName;

	public ZLock(final RedisTemplate<String, String> redisTemplate, final Integer serverPort, final String applicationName) {
		this.redisTemplate = redisTemplate;
		this.serverPort = serverPort;
		this.applicationName = applicationName;
		this.vvvv = this.redisTemplate.opsForValue();
	}


	/**
	 * 请求加锁，锁永不超时，并且请求加锁时不进行自旋
	 *
	 * @param key
	 * @return
	 *
	 */
	public boolean lock(final String key) {
		final boolean lock = this.lock(key, TIMEOUT_NEVER_EXPIRE, TimeUnit.MINUTES, WAIT_MS_NEVER);
		return lock;
	}

	/**
	 * 加锁，请求加锁时不进行自旋
	 *
	 * @param key      具体的锁key
	 * @param timeout  锁的超时时间
	 * @param timeUnit 锁的超时时间单位
	 * @return
	 *
	 */
	public boolean lock(final String key, final long timeout, final TimeUnit timeUnit) {
		return this.lock(key, timeout, timeUnit, WAIT_MS_NEVER);
	}

	/**
	 * 加锁
	 *
	 * @param key      具体的锁key
	 * @param timeout  锁的超时时间
	 * @param timeUnit 锁的超时时间单位
	 * @param waitMS   自旋毫秒,小于0则不自旋
	 * @return
	 */
	public boolean lock(final String key, final long timeout, final TimeUnit timeUnit, final int waitMS) {
		final String name = Thread.currentThread().getName();
		final String lockKey = ZLock.buildLockKey(key);
		final String value = this.genV();
		final Boolean setIfAbsent = timeout <= 0 ? this.vvvv.setIfAbsent(lockKey, value)
				: this.vvvv.setIfAbsent(lockKey, value, timeout, timeUnit);
		// 无此锁值，加锁成功
		if (setIfAbsent) {
			System.out.println(name + "\t" + lockKey + " 第一次加锁ok");
			return true;
		}

		// 有此锁值，如果value相等，则是重入
		final String v = this.vvvv.get(lockKey);
		final boolean equals = String.valueOf(v).equals(value);
		if (equals) {
			System.out.println(name + "\t" + lockKey + " 重入");
			return true;
		}

		// 自旋
		final int n = waitMS;
		for (int i = 1; i <= n; i++) {
			try {
				Thread.sleep(1);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			final Boolean r2 = timeout <= 0 ? this.vvvv.setIfAbsent(lockKey, value)
					: this.vvvv.setIfAbsent(lockKey, value, timeout, timeUnit);
			if (r2) {
				System.out.println(name + "\t" + lockKey + " 自旋加锁OK");
				return true;
			}
		}

		System.out.println(name + "\t" + lockKey + " 加锁fasle");
		return false;
	}


	private static String buildLockKey(final String key) {
		return "lock:" + key;
	}

	/**
	 * 为了实现可重入而设置的value，在取出value时，判断如果与当前相同则是重入.
	 * 当前实现：value = 【HostName_HostAddress_cpu核心数_应用名_应用端口号_当前请求加锁的线程名】
	 * value 要尽量保证不会重复，如果value是固定值，则任意一个线程
	 * 在加锁时都会被认为是重入
	 *
	 * @return
	 *
	 */
	private String genV() {
		final String name = Thread.currentThread().getName();
		final int availableProcessors = Runtime.getRuntime().availableProcessors();
		final String hostNameAndAddress = ZLock.getHostNameAndAddress();
		final String value = hostNameAndAddress + SEPARATOR + availableProcessors + SEPARATOR + this.applicationName
				+ SEPARATOR + this.serverPort + SEPARATOR + name;
		return value;
	}

	private static String getHostNameAndAddress() {
		try {
			final InetAddress localHost = InetAddress.getLocalHost();
			final String hostAddress = localHost.getHostAddress();
			return  localHost.getHostName() + SEPARATOR  + hostAddress;
		} catch (final UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解锁
	 *
	 * @param key
	 * @return
	 *
	 */
	public boolean unlock(final String key) {
		final String lockKey = ZLock.buildLockKey(key);
		final Boolean delete = this.redisTemplate.delete(lockKey);
		return delete;
	}
	// XXX 使用lua？

}
