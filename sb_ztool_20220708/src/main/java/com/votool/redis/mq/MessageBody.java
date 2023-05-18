package com.votool.redis.mq;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息协议体
 *
 * @author zhangzhen
 * @date 2022年8月18日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageBody implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 唯一的消息ID
	 */
	private String messageId;

	private String topic;

	/**
	 * 消费时间，需要消费的时间，比如1点从client发出消息， 消息体中consumeTime设为2点，则消费者在2点才回收到消息
	 */
	private Date consumeTime;

	/**
	 * 消息体对象
	 */
	private Object body;

}
