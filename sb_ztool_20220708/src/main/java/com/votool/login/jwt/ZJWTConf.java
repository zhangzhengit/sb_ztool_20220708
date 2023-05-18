package com.votool.login.jwt;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import com.votool.login.LoginVerificationConfiguration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月9日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(value = "jwt.token")
//@Validated
public class ZJWTConf {

	@NotEmpty(message = "secretKey不能为空！")
	@Length(min = 10, max = 50, message = "secretKey长度必须在10-50之间！")
	private String secretKey;

}
