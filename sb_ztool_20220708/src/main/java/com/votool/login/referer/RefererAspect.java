package com.votool.login.referer;

import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 拦截 @RefererAnnotation 的方法，把referer放入AtomicReference供其他地方使用
 *
 * @author zhangzhen
 * @date 2022年8月8日
 *
 */
@Aspect
@Component
public class RefererAspect {

	private final static AtomicReference<String> tl = new AtomicReference<>();

	private static final String REFERER = "referer";

	@Pointcut("@annotation(com.votool.login.referer.RefererAnnotation)")
	public void pointcut() {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "RefererAspect.pointcut()");
	}

	@Around(value = "pointcut()")
	public final Object around(final ProceedingJoinPoint proceedingJoinPoint) {

		final Signature signature = proceedingJoinPoint.getSignature();
		if (!(signature instanceof MethodSignature)) {
			try {
				return proceedingJoinPoint.proceed();
			} catch (final Throwable e) {
				e.printStackTrace();
			}
		}

		try {
			final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
			final HttpServletRequest request = servletRequestAttributes.getRequest();
			final String referer = request.getHeader(REFERER);
			set(referer);

			return proceedingJoinPoint.proceed();
		} catch (final Throwable e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final String get() {
		return tl.get();
	}

	public static void set(final String referer) {
		tl.set(referer);
	}
}
