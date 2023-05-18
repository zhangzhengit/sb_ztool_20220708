package com.votool.redis.mq;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.ListOperations;

import com.google.common.collect.ArrayListMultimap;
import com.votool.common.ZPU;
import com.votool.ze.AbstractZETask;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年11月29日
 *
 */
public class MQConsumerTask extends AbstractZETask<Object> {

	private final ListOperations<String, byte[]> list;
	private final ArrayListMultimap<String, Method> methodMap;
	private final ConcurrentMap<String, Object> objectMap;
	private final MQChannel mqChannel;
	private final byte[] bs;
	private final String topic;


	public MQConsumerTask(final ListOperations<String, byte[]> list, final ArrayListMultimap<String, Method> methodMap,
			final ConcurrentMap<String, Object> objectMap, final MQChannel mqChannel, final byte[] bs, final String topic) {
		this.list = list;
		this.methodMap = methodMap;
		this.objectMap = objectMap;
		this.mqChannel = mqChannel;
		this.bs = bs;
		this.topic = topic;
	}

	@Override
	public Object call() {

		final List<Method> methodList = this.methodMap.get(this.topic);
		// FIXME 2022年12月5日 下午9:44:36 zhanghen: 对于多个method，是否每个都用newZE一个线程去处理？
		for (final Method method : methodList) {

			MQConsumer annotation = null;

			try {

				annotation = AnnotationUtils.findAnnotation(method, MQConsumer.class);
				final AckEnum ack = annotation.ack();
				final Object obj = this.objectMap.get(this.topic);
				switch (ack) {
				case AUTO:

					final MessageBody messageBody = ZPU.deserialize(this.bs, MessageBody.class);

					method.invoke(obj, messageBody);
					break;

				case MANUAL:
					final MessageBody messageBody2 = ZPU.deserialize(this.bs, MessageBody.class);
					method.invoke(obj, messageBody2, this.mqChannel);
					// FIXME 2022年8月21日 下午10:46:49 zhanghen: QMThread 中处理如果如果手动，则手动ack后再删除
					break;

				default:
					break;
				}

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();

				final FailEnum onFailure = annotation.onFailure();
				switch (onFailure) {

				case NULL:
					// 什么都不做
					break;

				case REINSERT:
					// 消费者处理异常，重新入列
					this.list.rightPush(this.topic, this.bs);
					final String name = Thread.currentThread().getName();
					final MessageBody messageBody = ZPU.deserialize(this.bs, MessageBody.class);
					System.out.println(name + " 消费者处理消息异常.message=" + e + "，重新放入队列.topic = " + this.topic
							+ ",messageBody = " + messageBody);
					break;

				default:
					break;
				}


			} finally {
				// zs.remove(topic, v);
			}
		}

		return null;
	}


}
