package com.votool.login;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月8日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(value = "login.verification")
public class LoginVerificationConfiguration {

	/**
	 * 用于校验是否登录的关键字
	 */
	private String keyword = "user_login";

	/**
	 * 放在redis中的sessionId对应的登录的用户信息，在几分钟后过期
	 */
	private Integer minute;

	/**
	 * 需要校验是否登陆的path
	 */
	private Set<String> pathPatterns = new HashSet<>(Lists.newArrayList("/**", "/*"));

	/**
	 * 不校验是否登陆的path
	 */
	private Set<String> excludePathPatterns = new HashSet<>(Lists.newArrayList(
			"/index",
					"/error",
					"/favicon.ico",
					"/user/toreg",
					"/user/reg",
					"/user/login",
					"/user/tologin",
					"/static/**"
			));

}
