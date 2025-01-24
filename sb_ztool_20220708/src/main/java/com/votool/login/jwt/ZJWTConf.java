package com.votool.login.jwt;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月9日
 *
 */
@Configuration
@ConfigurationProperties(value = "jwt.token")
public class ZJWTConf {

	@NotEmpty(message = "secretKey不能为空！")
	@Length(min = 10, max = 50, message = "secretKey长度必须在10-50之间！")
	private String secretKey;

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	

}
