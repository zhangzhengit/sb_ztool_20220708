package com.votool.redis.mq;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.votool.common.ZPU;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;

/**
 * 消息client，用于发送消息.
 * 使用List实现，右进左出,发送用rightPush，取出消息用leftPop
 *
 * @author zhangzhen
 * @date 2022年8月18日
 *
 */
@Component
public final class MQClient implements InitializingBean {

	private static final int DELAYED_MILLISECOND = 5;

	@Autowired
	private ZRedisMQConfiguration zRedisMQConf;

	@Autowired
	private RedisTemplate<String, byte[]> redisTemplate;

	/**
	 * 发送消息,消费者按照【先发出先收到】接收消息
	 *
	 * @param topic  消息主题
	 * @param object 消息对象
	 * @return
	 *
	 */
	public boolean send(final String topic, final Serializable object) {
		final String messageId = UUID.randomUUID().toString();
		final boolean send = this.sendList(topic, messageId, object, false);
		return send;
	}

	/**
	 * 发送优先的消息，期望消费者优先收到此消息
	 *
	 * @param topic 消息主题
	 * @param object 消息对象
	 * @return
	 *
	 */
	public boolean sendPriority(final String topic, final Serializable object) {
		final String messageId = UUID.randomUUID().toString();
		final boolean send = this.sendList(topic, messageId ,object,  true);
		return send;
	}

	/**
	 * 发送延时消息，期望消费者在指定时刻收到此消息
	 *
	 * @param topic    消息主题
	 * @param object   消息对象
	 * @param time     延时时长
	 * @param timeUnit 延时时长单位
	 * @return
	 */
	public boolean send(final String topic, final Serializable object, final long time, final TimeUnit timeUnit) {

		final String messageId = UUID.randomUUID().toString();

		// 不用ZSet，score重复时会按字典顺序排列，不能保证消息先进先出了
		final boolean sendListDELAYED = this.sendListDELAYED(topic, messageId, object, time, timeUnit);

		return sendListDELAYED;
	}

	private boolean sendList(final String topic, final String messageId, final Object object, final boolean priority) {
		if (StrUtil.isBlank(topic)) {
			throw new IllegalArgumentException("topic不能为空");
		}
		if (StrUtil.isBlank(messageId)) {
			throw new IllegalArgumentException("messageId不能为空");
		}

		if (Objects.isNull(object)) {
			throw new IllegalArgumentException("object不能为空");
		}

		try {
			final MessageBody body = new MessageBody();
			body.setTopic(topic);
			body.setMessageId(messageId);
			body.setBody(object);
			final boolean send0 = this.sendList0(body, priority);
			return send0;
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean sendListDELAYED(final String topic, final String messageId, final Serializable object, final long time,
			final TimeUnit timeUnit) {

		if (StrUtil.isBlank(topic)) {
			throw new IllegalArgumentException("topic不能为空");
		}
		if (StrUtil.isBlank(messageId)) {
			throw new IllegalArgumentException("messageId不能为空");
		}

		if (Objects.isNull(object)) {
			throw new IllegalArgumentException("object不能为空");
		}

		if (Objects.isNull(timeUnit)) {
			throw new IllegalArgumentException("timeUnit不能为空");
		}

		final long time1 = time <= 0L ? 0 : time;
		final long addMillis = timeUnit.toMillis(time1);

		final Date consumeTime = new Date(new Date().getTime() + addMillis);

		final boolean priority = addMillis <= 0;

		try {
			final MessageBody body = new MessageBody();
			body.setTopic(topic);
			body.setMessageId(messageId);
			body.setBody(object);
			body.setConsumeTime(consumeTime);

			final boolean send0 = this.sendList0(body, priority);
			return send0;
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private final Queue<MessageBody> queueRight = new LinkedList<>();
	private final Queue<MessageBody> queueLeft = new LinkedList<>();
	private final String uuid = String.valueOf(UUID.randomUUID());

	private boolean sendList0(final MessageBody body, final boolean priority) {

		synchronized (this.uuid) {
			this.uuid.notify();
			if (priority) {
				this.queueLeft.add(body);
			} else {
				this.queueRight.add(body);
			}
		}

		return true;
	}

	private String generateRedisKeyFromTopic(final MessageBody body) {
		return this.zRedisMQConf.getTopicPrefix() + ":" + body.getTopic();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "MQClient.afterPropertiesSet()");

		final ListOperations<String, byte[]> list = this.redisTemplate.opsForList();

		final Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
						+ "MQClient.afterPropertiesSet().new Runnable() {...}.run()");

				while (true) {

					synchronized (MQClient.this.uuid) {
						try {
							MQClient.this.uuid.wait();
						} catch (final InterruptedException e) {
							e.printStackTrace();
						}

						final MessageBody right = MQClient.this.queueRight.poll();
						if (Objects.nonNull(right)) {
							final byte[] bsRIGHT = ZPU.serialize(right);
							list.rightPush(MQClient.this.generateRedisKeyFromTopic(right), bsRIGHT);
						}

						final MessageBody left = MQClient.this.queueLeft.poll();
						if (Objects.nonNull(left)) {
							final byte[] bsLEFT = ZPU.serialize(left);
							list.rightPush(MQClient.this.generateRedisKeyFromTopic(left), bsLEFT);
						}

					}
				}
			}
		});

		thread.setName("mqClient-Thread");
		thread.start();

	}

}
