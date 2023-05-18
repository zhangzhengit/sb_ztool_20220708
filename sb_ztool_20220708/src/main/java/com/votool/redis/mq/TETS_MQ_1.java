package com.votool.redis.mq;

import org.springframework.stereotype.Component;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年11月29日
 *
 */
// FIXME 2022年11月29日 下午9:37:05 zhanghen: 删除此类，测试用的
//@Component
public class TETS_MQ_1 {

	@MQConsumer(topic = "putong", onFailure = FailEnum.REINSERT)
	public void putong(final MessageBody messageBody) {
		System.out.println(
				java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "TETS_MQ_1.putong()");

		System.out.println("putong.messageBody = " + messageBody);
	}

	@MQConsumer(topic = "yanshi", onFailure = FailEnum.REINSERT)
	public void yanshi(final MessageBody messageBody) {
		System.out.println(
				java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "TETS_MQ_1.yanshi()");

		System.out.println("yanshi.messageBody = " + messageBody);
	}

}
