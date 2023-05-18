package com.votool.login;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年11月2日
 *
 */
@Component
public class LoginApplicationContextAware implements ApplicationContextAware {

	private static ApplicationContext context = null;

	public static Object getBean(final Class clsName) {
		final Object bean = LoginApplicationContextAware.context.getBean(clsName);
		return bean;
	}

	public static Object getBean(final String beanName) {
		final Object bean = LoginApplicationContextAware.context.getBean(beanName);
		return bean;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		LoginApplicationContextAware.context = applicationContext;
	}

}
