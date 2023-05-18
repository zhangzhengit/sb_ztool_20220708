package com.votool.apidoc;

import org.springframework.data.redis.connection.ReactiveSetCommands.SInterCommand;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年8月14日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TUser {
	private Integer id;
	private String name;

	private String mobile;

	private Set<Integer> idSet;
}
