package com.votool.redis.mq;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息协议体
 *
 * @author zhangzhen
 * @date 2022年8月18日
 *
 */
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

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Date getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(Date consumeTime) {
		this.consumeTime = consumeTime;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	
}
