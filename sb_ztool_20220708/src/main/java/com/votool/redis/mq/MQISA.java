package com.votool.redis.mq;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;

import cn.hutool.core.util.ArrayUtil;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月15日
 *
 */
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class MQISA implements ApplicationContextAware {

	public static ArrayListMultimap<String, Method> methodMap = ArrayListMultimap.create();
	public static ConcurrentMap<String, Object> objectMap = Maps.newConcurrentMap();

	private static final Class<MQConsumer> MQCONSUMER_CLASS = MQConsumer.class;

	private static ApplicationContext context = null;

	@Autowired
	private ZRedisMQConfiguration zRedisMQConf;
	@Autowired
	private MQChannel mqChannel;

	@Autowired
	private RedisTemplate<String, byte[]> redisTemplate;

	public static Object getBean(final Class clsName) {
		final Object bean = MQISA.context.getBean(clsName);
		return bean;
	}

	public static Object getBean(final String beanName) {
		final Object bean = MQISA.context.getBean(beanName);
		return bean;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "MQISA.setApplicationContext()");

		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
						+ "MQISA.setApplicationContext(...).new Runnable() {...}.run()");
				MQISA.this.f1(applicationContext);
			}
		});

		thread.start();
	}

	private void f1(final ApplicationContext applicationContext) {
		System.out
				.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "MQISA.f1()");

		final String[] beanNameArray = applicationContext.getBeanDefinitionNames();
		for (final String beanName : beanNameArray) {
			final Object bean = applicationContext.getBean(beanName);
			final Method[] methodArray = bean.getClass().getDeclaredMethods();
			for (final Method method : methodArray) {

				final MQConsumer mqConsumer = AnnotationUtils.findAnnotation(method, MQConsumer.class);

				if (mqConsumer != null) {
					MQISA.methodMap.put(this.zRedisMQConf.getTopicPrefix() + ":" + mqConsumer.topic(), method);
					MQISA.objectMap.put(this.zRedisMQConf.getTopicPrefix() + ":" + mqConsumer.topic(), bean);
					System.out.println("@找到一个mqConsumer的方法,method = " + method);

					final AckEnum ack = mqConsumer.ack();
					switch (ack) {
					case AUTO:
						// 带此注解的方法必须有一个(MessageBody messageBody)参数
						final Parameter[] pa = method.getParameters();
						if (ArrayUtil.isEmpty(pa) || pa.length != 1
								|| !(pa[0].getType().getCanonicalName().equals(MessageBody.class.getCanonicalName()))) {
							throw new IllegalArgumentException("@" + MQISA.MQCONSUMER_CLASS.getSimpleName() + "方法 "
									+ method.getName() + " 参数必须声明为(MessageBody messageBody)");
						}

						break;

					case MANUAL:
						final Parameter[] pa2 = method.getParameters();
						if (ArrayUtil.isEmpty(pa2) || pa2.length != 2
								|| !(pa2[0].getType().getCanonicalName().equals(MessageBody.class.getCanonicalName()))
								|| !(pa2[1].getType().getCanonicalName().equals(MQChannel.class.getCanonicalName()))

						) {
							throw new IllegalArgumentException("@" + MQISA.MQCONSUMER_CLASS.getSimpleName() + "方法 "
									+ method.getName() + " 参数必须声明为(MessageBody messageBody,MQChannel mQChannel)");
						}

						break;
					// FIXME 2022年8月21日 下午10:37:38 zhanghen:
					// 处理为：同一个topic，不可以同时存在手动和自动确认的两种不同确认模式的消费者
					default:
						break;
					}
				}
			}
		}

		if (MQISA.methodMap.size() > 0) {
			final MQThread thread = new MQThread(this.zRedisMQConf, this.redisTemplate, MQISA.methodMap, MQISA.objectMap,
					this.mqChannel);
			thread.setName("mq-redis-thread");
			thread.start();
		}
	}
}
