package com.votool.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import com.votool.common.CR;
import com.votool.exception.NotLoginException;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年6月30日
 *
 */
//@ControllerAdvice
@RestControllerAdvice
public class ZControllerAdvice {

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

}
