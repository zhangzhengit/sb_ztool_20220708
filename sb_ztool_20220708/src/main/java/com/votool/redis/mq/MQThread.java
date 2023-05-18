package com.votool.redis.mq;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.io.UTF32Reader;
import com.google.common.collect.ArrayListMultimap;
import com.votool.common.ZPU;
import com.votool.ze.ZES;
import com.votool.ze.ZETaskResult;

/**
 * 循环从redis拉取消息,然后取出对应的topic的method来执行的Thread
 *
 * @author zhangzhen
 * @date 2022年8月18日
 *
 */
class MQThread extends Thread {

	private final ZRedisMQConfiguration zRedisMQConfiguration;

	private final RedisTemplate<String, byte[]> redisTemplate;

	/**
	 * k = topic
	 */
	private final ArrayListMultimap<String, Method> methodMap;

	/**
	 * k = topic
	 */
	private final ConcurrentMap<String, Object> objectMap;
	private final MQChannel mqChannel;
	private final com.votool.ze.ZE ze;

	private final ListOperations<String, byte[]> list;

	public MQThread(final ZRedisMQConfiguration zRedisMQConfiguration,final RedisTemplate<String, byte[]> redisTemplate,
			final ArrayListMultimap<String, Method> methodMap, final ConcurrentMap<String, Object> objectMap,
			final MQChannel mqChannel) {
		this.zRedisMQConfiguration = zRedisMQConfiguration;
		this.redisTemplate = redisTemplate;
		this.methodMap = methodMap;
		this.objectMap = objectMap;
		this.mqChannel = mqChannel;
		this.list = this.redisTemplate.opsForList();

		this.ze = ZES.newZE(this.zRedisMQConfiguration.getMaxThreadSize().intValue(),
				this.zRedisMQConfiguration.getThreadNamePrefix());
	}

	@Override
	public void run() {
		System.out.println(
				java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "MQThread.run()");

		if (this.methodMap.isEmpty()) {
			System.out.println("map.isEmpty,没有消费者，不处理了");
			return;
		}

		// 本进程内只关心并拉取程序内存在的消费者对应的topic
		final Set<String> topicSet = this.methodMap.keySet();
		System.out.println("topicSet.size = " + topicSet.size());
		for (final String topic : topicSet) {
			System.out.println("\t" + topic);
		}
		System.out.println("topicSet.size = " + topicSet.size());

		while (true) {

			final boolean anyIdle = this.ze.anyIdle();
			if (!anyIdle) {
				this.sleepMS(5);
				continue;
			}

			for (final String topic : topicSet) {
				final byte[] bs = this.list.leftPop(topic);

				if (Objects.isNull(bs)) {
					continue;
				}

				final MessageBody messageBody = ZPU.deserialize(bs, MessageBody.class);

				final Date consumeTime = messageBody.getConsumeTime();

				// Objects.nonNull(consumeTime) 表示是延迟消息（非延时消息没有设置此值）
				if (Objects.nonNull(consumeTime) && consumeTime.getTime() > System.currentTimeMillis()) {
					this.list.rightPush(topic, bs);
					continue;
				}

				final MQConsumerTask task = new MQConsumerTask(this.list, this.methodMap, this.objectMap,
						this.mqChannel, bs, topic);
				// 就是不用队列执行，就是用空闲线程执行
				final ZETaskResult<Object> result = this.ze.submitImmediately(task);
				if (!result.isArranged()) {
					// 没有执行成功（当前无空闲线程）,再把消息放入队列头部(left)
					this.list.leftPush(topic, bs);
				}
			}
		}

	}

	private void sleepMS(final int ms) {
		try {
			Thread.sleep(ms);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}
}
