package com.votool.sync;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * UserProductEntity中secretKey是唯一的，每个secretKey的请求单独使用一个Thread处理
 *
 * @author zhangzhen
 * @date 2022年7月10日
 *
 */
public class SKThread extends Thread {

	public static final String THREAD_NAME = "secretKey-thread-";

	private final Queue<Runnable> queue = new LinkedBlockingQueue<>();

	private final String secretKey;

//	public void add(final Callable callable) {
//		this.queue.add(callable);
//	}

	public void add(final Runnable runnable) {
		this.queue.add(runnable);
	}

	public SKThread(final String secretKey) {
		this.secretKey = secretKey;
	}

	@Override
	public void run() {
		System.out.println(
				java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "SKThread.run()");

		while (true) {
			final Runnable poll = this.queue.poll();
			if (poll == null) {
				continue;
			}
			poll.run();
		}
	}

	// FIXME 2022年7月10日 下午4:23:18 zhanghen: 写这里
}
