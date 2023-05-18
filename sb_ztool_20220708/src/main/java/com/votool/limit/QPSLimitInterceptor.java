package com.votool.limit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.votool.login.LoginInterceptor;

import cn.hutool.core.util.StrUtil;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月6日
 *
 */
public class QPSLimitInterceptor implements HandlerInterceptor{

	private final RedisTemplate<String, String> redisTemplate;
	private final ZRedisLimit limit;

	public QPSLimitInterceptor(final LoginInterceptor loginInterceptor,final RedisTemplate<String, String> redisTemplate,
			final ZRedisLimit limit) {

		this.redisTemplate = redisTemplate;
		this.redisTemplate.opsForValue();
		this.limit = limit;
	}

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		final HandlerMethod method = (HandlerMethod) handler;
		final QPSLimit la = method.getMethodAnnotation(QPSLimit.class);
		if (la == null) {
			return true;
		}


		String k = null;

		final String distinguish = la.distinguish();
		switch (distinguish) {
		case QPSLimit.DISTINGUISH_NULL:
			k = "distinguish_by_null";
			break;

		case QPSLimit.SESSION:
			// 下面方法已经抛出异常了
			// FIXME 2022年7月26日 上午4:47:55 zhanghen: 修复，不能用下面方法，因为下面是验证session登录的。比如找回密码等，是没有登录的，所以下面方法不能用
//			final boolean isLogin = this.loginInterceptor.preHandle(request, response, handler);
			final HttpSession session = request.getSession();
			final String id = session.getId();
			k = "distinguish_by_SESSION:" + id;

			break;
		case QPSLimit.TOKEN:
			final String accessToken = request.getHeader(QPSLimit.TOKEN);
			if (StrUtil.isEmpty(accessToken)) {
				throw new TokenException("TOKEN不能为空！");
			}

			k = "distinguish_by_TOKEN:" + accessToken;

			break;

		case QPSLimit.SECRET_KEY:
			final String secretKey = request.getHeader(QPSLimit.SECRET_KEY);
			if (StrUtil.isEmpty(secretKey)) {
				throw new SecretKeyException("SECRET_KEY不能为空！");
			}
			k = "distinguish_by_SECRET_KEY:" + secretKey;

			break;

		default:
			break;
		}

		final String key = method.getMethod().getName() + ":" + k;
		final boolean check = this.limit.check(key, la.qps());
		if (!check) {
			throw new QPSLimitException(la.message());
		}

		return true;

	}

}
