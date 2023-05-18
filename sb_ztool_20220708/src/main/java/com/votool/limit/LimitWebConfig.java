package com.votool.limit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年6月30日
 *
 */
@Configuration
@Order(value = 1)
public class LimitWebConfig implements WebMvcConfigurer{

	@Autowired
	private QPSLimitInterceptor qpsLimitInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
    	System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "LimitWebConfig.addInterceptors()");

        registry
	        .addInterceptor(this.qpsLimitInterceptor)
			// 需要校验登录的
			.addPathPatterns("/**","/*");
			// 排除掉的（不校验是否登录的）
//			.excludePathPatterns(this.loginVerificationConfiguration.getExcludePathPatterns().toArray(new String[] {}));
	}


}
