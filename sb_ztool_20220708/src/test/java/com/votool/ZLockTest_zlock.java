package com.votool;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.common.collect.Lists;
import com.votool.lock.ZLock;
import com.votool.ze.AbstractZETask;
import com.votool.ze.ZE;
import com.votool.ze.ZERunnable;
import com.votool.ze.ZES;

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

	/**
	 *
	 * 同一个K，并发lock N次，所有成功的调用，必须是同一个线程
	 *
	 * @author zhangzhen
	 * @date 2023年5月20日
	 */
	@Test
	public void test25() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZLockTest_zlock.test25()");

		final int n = 10000 * 5;

		final String key = "key:" + UUID.randomUUID().toString();

		final Vector<String> t = new Vector<>();

		final IntStream stream = IntStream.range(1, n + 1);
		final List<Boolean> collect = stream.parallel().mapToObj(i->{
			final boolean lock = this.lock.lock(key, 233223, TimeUnit.SECONDS);
			if(lock) {
				t.add(Thread.currentThread().getName());
			}

			return lock;
		}).collect(Collectors.toList());

		final long okcount = collect.stream().filter(b -> b).count();

		assertThat(okcount == t.size());
		final long okC = t.stream().distinct().count();
		assertThat(okC == 1);
		System.out.println("okC-0 = " + t.get(0));
	}


	/**
	 *
	 *	N个K，必须都成功，然后再试，必须都失败；等N个K都超时了，再试，必须都成功
	 * @author zhangzhen
	 * @date 2023年5月20日
	 */
	@Test
	public void test23() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZLockTest_zlock.test23()");

		final int n = 5000;

		final IntStream stream = IntStream.range(1, n + 1);

		final Vector<String> set = new Vector<>();

		final List<Boolean> collect = stream.parallel().mapToObj(i->{
			final String key = "key:" + UUID.randomUUID().toString();
			set.add(key);
			final boolean lock = this.lock.lock(key, 5, TimeUnit.SECONDS);
			return lock;
		}).collect(Collectors.toList());

		// 第一次，必须全部成功
		assertThat(set.size() == n);
		assertThat(collect.size() == n);
		final long count = collect.stream().filter(lock -> Boolean.TRUE.equals(lock)).count();
		assertThat(count == n);


		// 马上第二次，必须全部失败
		final ZE ze1 = ZES.newZE();
		final List<AbstractZETask<Boolean>> zelX = Lists.newArrayList();
		for (final String k : set) {
			final AbstractZETask<Boolean> task = new AbstractZETask<Boolean>() {

				@Override
				public Boolean call() {
					final boolean lock2 = ZLockTest_zlock.this.lock.lock(k);
					return lock2;
				}
			};
			zelX.add(task);
		}
		final List<Boolean> faillist = ze1.submitInQueueAndGet(zelX);
		final long fail2 = faillist.stream().filter(b -> !b).count();
		assertThat(fail2 == n);

		// 等全部超时后，再试，必须再次全部成功
		try {
			Thread.sleep(6 * 1000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}


		final ZE ze = ZES.newZE();
		final List<AbstractZETask<Boolean>> zel = Lists.newArrayList();
		for (final String k : set) {
			final AbstractZETask<Boolean> task = new AbstractZETask<Boolean>() {

				@Override
				public Boolean call() {
					final boolean lock2 = ZLockTest_zlock.this.lock.lock(k);
					return lock2;
				}
			};
			zel.add(task);
		}
		final List<Boolean> ok2list = ze.submitInQueueAndGet(zel);

		final long ok2x = ok2list.stream().filter(b -> b).count();
		assertThat(ok2x == n);

	}

	/**
	 *	不同K，必须都成功
	 *
	 * @author zhangzhen
	 * @date 2023年5月20日
	 */
	@Test
	public void test2() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZLockTest_zlock.test2()");


		final boolean lock2 = this.lock.lock("A");

		assertThat(lock2);
		final boolean lock3 = this.lock.lock("B", 1, TimeUnit.MINUTES, 1);

		assertThat(lock3);

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


		final Vector<String> set = new Vector<>();
		final int n = 200;
		final IntStream stream = IntStream.range(1, n + 1);
		final String key1 = "key:" + UUID.randomUUID().toString();
		System.out.println("key1 = " + key1);
		final List<Boolean> collect = stream.parallel().mapToObj(i->{
			final boolean lock11 = this.lock.lock(key1, 20, TimeUnit.MINUTES);
			if(lock11) {
				set.add(Thread.currentThread().getName());
			}
			return lock11;

		}).collect(Collectors.toList());

		System.out.println("collect.stream().filter(b -> b).count() = " + collect.stream().filter(b -> b).count());
		System.out.println("collect.stream().filter(b -> !b).count() = " + collect.stream().filter(b -> !b).count());
		System.out.println("set.size = " + set.size());
		for (final String v : set) {
			System.out.println("tn = " + v);
		}
		System.out.println("set.size = " + set.size());

		// 同一个key ，只能有一个线程获取到锁
		assertThat(collect.stream().filter(b -> b).distinct().count() == 1);

	}
	ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(20);




	public static void assertThat(final boolean ok) {
		if(!ok) {
			throw new IllegalStateException("断言失败");
		}

	}
}
