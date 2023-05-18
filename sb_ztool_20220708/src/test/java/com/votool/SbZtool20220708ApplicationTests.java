package com.votool;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.votool.redis.mq.MQClient;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月13日
 *
 */
@SpringBootTest
class SbZtool20220708ApplicationTests {

	@Autowired
	MQClient mqClient;
	@Test
	void contextLoads() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "SbZtool20220708ApplicationTests.contextLoads()");

		System.out.println("mqClient = " + this.mqClient);

		final M m  = new M("putong");
		this.mqClient.send("putong", m);

		final M m2  = new M("延迟5秒");
		this.mqClient.send("yanshi", m2, 5, TimeUnit.SECONDS);

		final M m3 = new M("不延迟");
		this.mqClient.send("yanshi", m3, 0, TimeUnit.SECONDS);

		try {
			Thread.sleep(1000 * 6);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("--------------------------------------end----------------------------------");
	}


}
