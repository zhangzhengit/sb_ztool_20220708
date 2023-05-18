package com.votool.redis.mq;

/**
 * 消费者处理消息出现异常后的选项
 *
 * @author zhangzhen
 * @date 2022年11月29日
 *
 */
public enum FailEnum {

	/**
	 * 什么也不管
	 */
	NULL,

	/**
	 * 重新入列，等待继续消费
	 */
	// FIXME 2022年11月29日 下午6:22:08 zhanghen: 考虑是否消息本身的问题？如果是则入列再消费仍然会异常，然后死循环？
	REINSERT;
}
