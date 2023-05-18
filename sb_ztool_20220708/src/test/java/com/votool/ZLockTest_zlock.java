package com.votool;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.votool.cachememory.ZMemoryCache;
import com.votool.lock.ZLock;

import cn.hutool.core.lang.UUID;


/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月13日
 *
 */
@SpringBootTest
public class ZLockTest_zlock {

	@Autowired
	ZLock lock;

	@Test
	public void test2() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZLockTest_zlock.test2()");


		final boolean lock2 = this.lock.lock("A");

		final boolean lock3 = this.lock.lock("B", 1, TimeUnit.MINUTES, 1);

	}

	/**
	 * 超时1秒，2秒后再加锁，必须成功
	 *
	 */
	@Test
	public void test_chaoshi1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZLockTest_zlock.test_chaoshi1()");

		final String key = "key:" + UUID.randomUUID().toString();
		final boolean lock1 = this.lock.lock(key, 1, TimeUnit.SECONDS);
		assertThat(lock1);

		try {
			Thread.sleep(2000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		final boolean lock2 = this.lock.lock(key, 1, TimeUnit.SECONDS);
		assertThat(lock2);

	}

	@Test
	public void test_1() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZLockTest_zlock.test_1()");


		final String key = "key:" + UUID.randomUUID().toString();
		final boolean lock1 = this.lock.lock(key, 20, TimeUnit.MINUTES);
		assertThat(lock1);

		final boolean lock2 = this.lock.lock(key, 20, TimeUnit.MINUTES);
		// 第二次，重入
		assertThat(lock2);


		final int n = 300;
		final IntStream stream = IntStream.range(1, n + 1);
		final String key1 = "key:" + UUID.randomUUID().toString();
		final List<Boolean> collect = stream.parallel().mapToObj(i->{
			final boolean lock11 = this.lock.lock(key1, 20, TimeUnit.MINUTES);
			return lock11;
		}).collect(Collectors.toList());

		assertThat(collect.stream().filter(b -> b).count() == 1);
		assertThat(collect.stream().filter(b -> !b).count() == n - 1);

	}
	ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(20);




}
