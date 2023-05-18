package com.votool.sync;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.collect.Maps;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月10日
 *
 */
public class SKTMap {

	private static ConcurrentMap<String, ExecutorService> map = Maps.newConcurrentMap();

	public static <T> T submit(final String k, final Callable<T> callable) {
		final ExecutorService service = get(k);
		final Future<T> f = service.submit(callable);
		try {
			return f.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ExecutorService get(final String k) {
		final ExecutorService service = SKTMap.map.get(k);
		if (service != null) {
			return service;
		}

		return generate(k);
	}

	private static ExecutorService generate(final String k) {
		final ExecutorService service = Executors.newSingleThreadExecutor();

//		final ThreadPoolExecutor service = new ThreadPoolExecutor(1, 1,
//                0L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>());

		SKTMap.map.put(k, service);
		return service;
	}

}
