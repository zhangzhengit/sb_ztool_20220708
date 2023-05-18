package com.votool.redis.mq;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * zredis-mq 配置项
 *
 * @author zhangzhen
 * @date 2022年11月24日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@Validated
@ConfigurationProperties(prefix = "zmq")
public class ZRedisMQConfiguration {

	@NotEmpty(message = "zmq.topicPrefix 不能配置为空")
	private String topicPrefix = "message_queue:topic";

	@NotNull(message = "zmq.maxThreadSize 不能配置为空")
	@Min(value = 1, message = "zmq.maxThreadSize 最小配置为1")
	private Integer maxThreadSize = Runtime.getRuntime().availableProcessors();

	@NotEmpty(message = "zmq.threadNamePrefix 不能配置为空")
	private String threadNamePrefix = "z-redis-mq-Thread-";
}
