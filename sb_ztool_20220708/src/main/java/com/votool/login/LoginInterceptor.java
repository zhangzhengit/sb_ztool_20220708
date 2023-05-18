package com.votool.login;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.votool.cacheredis.ZRedisCacheService;
import com.votool.common.CR;
import com.votool.enums.ErrorEnum;
import com.votool.exception.NotLoginException;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年6月30日
 *
 */
@Component
public class LoginInterceptor implements HandlerInterceptor, InitializingBean {

	@Autowired
	private LoginVerificationConfiguration loginVerificationConfiguration;

	public static String USER_LOGIN = null;

	public static void put(final String sessionId,final Object value) {
		final ZRedisCacheService<Object> redisCacheService = getRedisBean();
		redisCacheService.set(buildRedisKey(sessionId), value, 30, TimeUnit.DAYS);
	}

	public static Object get(final String sessionId) {
		final ZRedisCacheService<Object> redisCacheService = getRedisBean();
		return redisCacheService.get(buildRedisKey(sessionId));
	}

	public static void remove(final String sessionId) {
		final ZRedisCacheService<Object> redisCacheService = getRedisBean();
		redisCacheService.remove(buildRedisKey(sessionId));
//		redisCacheService.set(buildRedisKey(sessionId), sessionId, 1, TimeUnit.NANOSECONDS);
	}

	public static Object getLoginUser() {

		final String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
		if (Objects.isNull(sessionId)) {
			return null;
		}

		return get(sessionId);
	}

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception {


		final HttpSession session = request.getSession();
		final Object ul = get(session.getId());

		if (ul == null) {
			final String requestURI = request.getRequestURI();
			System.out.println("requestURI = " + requestURI);
			throw new NotLoginException(ErrorEnum.ERROR_NOT_LOGIN.getCode(), ErrorEnum.ERROR_NOT_LOGIN.getMessage());
		}

		return true;
	}

	/**
	 * 判断网络请求是否为ajax
	 *
	 * @param req
	 * @return
	 */
	private boolean isAjax(final HttpServletRequest req) {
		final String contentTypeHeader = req.getHeader("Content-Type");
		final String acceptHeader = req.getHeader("Accept");
		final String xRequestedWith = req.getHeader("X-Requested-With");
		return (contentTypeHeader != null && contentTypeHeader.contains("application/json"))
				|| (acceptHeader != null && acceptHeader.contains("application/json"))
				|| "XMLHttpRequest".equalsIgnoreCase(xRequestedWith);
	}

	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
			final ModelAndView modelAndView) throws Exception {

		final HttpSession session = request.getSession();
		final Object ul = get(session.getId());

		if (Objects.nonNull(modelAndView)) {
			modelAndView.addObject(LoginInterceptor.USER_LOGIN, ul);
		}

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public CR<String> argumentValidException(final MethodArgumentNotValidException e) {
		return CR.error(e.getBindingResult().getFieldError().getDefaultMessage());
	}

	@ExceptionHandler({ NotLoginException.class })
	public Object handlerNotLoginException(final NotLoginException e, final HttpServletRequest request,
			final HttpServletResponse response) {

		if (this.isAjax(request)) {
			return CR.error(e.getCode(), e.getMessage());
		}

		final ModelAndView mv = new ModelAndView();
		mv.addObject("message", "");
		mv.setViewName("login_page");
		return mv;
	}

	@ExceptionHandler({ Exception.class })
	public CR handlerException(final Exception e, final HttpServletRequest request,
			final HttpServletResponse response) {
		e.printStackTrace();
		return CR.error(e.getMessage());
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		final String keyword = this.loginVerificationConfiguration.getKeyword();
		LoginInterceptor.USER_LOGIN = keyword;
		System.out.println(
				"初始化loginVerificationConfiguration.keyword = " + this.loginVerificationConfiguration.getKeyword());
	}

	private static ZRedisCacheService<Object> getRedisBean() {
		return (ZRedisCacheService<Object>) LoginApplicationContextAware.getBean(ZRedisCacheService.class);
	}

	public static String buildRedisKey(final String key) {
		return "sessionId:" + key;
	}

}
