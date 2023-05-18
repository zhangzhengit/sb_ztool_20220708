package com.votool.login.referer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 final String referer = request.getHeader("referer");
 * 使用 RefererAspect.get() 获取referer。
 *
 * 如：接口调用路径 XXX > toLogin > login；
 * 在：在toLogin接口上加入此注解，在login接口成功后RefererAspect.get()获取referer
 * 使用CR.setRedirectURL来设置此值，然后前端判断CR.redirectURL 不为null则跳转到redirectURL
 *
 *
 * @author zhangzhen
 * @date 2022年10月25日
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RefererAnnotation {

}
