package com.votool.exception;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import com.votool.limit.LimitWebConfig;
import com.votool.limit.ZQPSLimitConf;

/**
 * 启用用户相关的功能(登录注册等)
 *
 * @author zhangzhen
 * @date 2022年7月8日
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
//@Import(value = { UserAPI.class, UserEntity.class, UserService.class, UserRepository.class })
@ComponentScan(basePackages = {"com.votool"})
public @interface EnableUser {

}
