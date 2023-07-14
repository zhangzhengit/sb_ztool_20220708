package com.votool.http;

/**
 * 发送http请求的工具类
 *
 * @author zhangzhen
 * @date 2023年7月13日
 *
 */
public class ZHttpClient {

	public static void main(final String[] args) throws InterruptedException {
		test_1();
	}

	public static void test_1() throws InterruptedException {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZHttpClient.test_1()");

		final String url = "http://192.168.1.101/";
		final int n = 100;
//		final int n = 10000 * 200;
		final long t1 = System.currentTimeMillis();
		for (int i = 1; i <= n; i++) {
			final String r = ZHttpRequest.get(url).execute();
			System.out.println("i = " + i + "\t" + "r = \n" + r);
		}
		final long t2 = System.currentTimeMillis();

		System.out.println("n = " + n + "\t" + "ms = " + (t2 - t1));

	}

	public static ZHttpRequest get(final String url) {

		return new ZHttpRequest(url);
	}


}
