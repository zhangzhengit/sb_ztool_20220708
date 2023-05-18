package com.votool.login;

import java.security.KeyStore.PrivateKeyEntry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.votool.cacheredis.ZRedisCacheService;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年6月30日
 *
 */
@Configuration
@Order(value = 3)
public class LoginWebConfig implements WebMvcConfigurer{

	@Autowired
	private ZRedisCacheService<Object> redisCacheService;
	@Autowired
	private LoginInterceptor loginInterceptor;
	@Autowired
	private LoginVerificationConfiguration loginVerificationConfiguration;

//	@Bean
//	public LoginInterceptor loginInterceptor() {
//		return new LoginInterceptor(this.redisCacheService, this.loginVerificationConfiguration);
//	}

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
    	System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "LoginWebConfig.addInterceptors()");

        registry
	        .addInterceptor(this.loginInterceptor)
			// 需要校验登录的
			.addPathPatterns(this.loginVerificationConfiguration.getPathPatterns().toArray(new String[] {}))
			// 排除掉的（不校验是否登录的）
			.excludePathPatterns(this.loginVerificationConfiguration.getExcludePathPatterns().toArray(new String[] {}));
	}


}
