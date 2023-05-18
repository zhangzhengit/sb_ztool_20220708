package com.votool.redis.mq;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.FileAlreadyExistsException;

/**
 * 用在方法上，表示此方法是一个消息的消费者， 方法必须带一个(MessageBody messageBody)参数，用于接收消息生产者发出的消息
 *
 * @author zhangzhen
 * @date 2022年8月18日
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MQConsumer {

	/**
	 * 指定要监听的消息主题
	 *
	 * @return
	 *
	 */
	String topic();

	/**
	 * 确认模式，默认为自动确认. 设为AckEnum.AUTO时，业务逻辑方法不需要管消息确认的问题， 在执行完业务逻辑代码后，消息会自动确认。
	 *
	 * @return
	 *
	 */
	AckEnum ack() default AckEnum.AUTO;

	/**
	 * client处理消息出现异常后的选项
	 *
	 * @return
	 *
	 */
	FailEnum onFailure();

}
