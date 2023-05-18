package com.votool.apidoc;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.DeclareMixin;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.votool.common.CR;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import cn.hutool.setting.SettingRuntimeException;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年8月14日
 *
 */
// FIXME 2022年8月14日 下午3:51:24 zhanghen: 测试类，删除掉
@RestController
public class TestAPIDoc {


//	@GetMapping(value = { "/findById", "/getById", "/", "/x3" })
//	public CR<TUser> findById(
//			@RequestParam final Integer id
//			,
//			final @RequestBody @Validated String name,
//			final @RequestBody @Validated  Boolean isOK
//			,
//			@RequestBody final TUser user1,
//			@RequestBody final TUser user2
//
//			) {
//
//		final TUser user = new TUser();
//		user.setId(200);
//		user.setName("zhang");
//		return CR.ok(user);
//	}


	@GetMapping(value = "/a")
	public String post1() {
		return null;
	}

//	@GetMapping(value = "/test1")
//	public CR test1(final HttpServletRequest request
//			,
//			final @RequestBody @Validated String name,
//			final Integer id,
//			final @RequestBody @Validated  Boolean isOK
//
//			) {
//
//		return CR.ok();
//	}

//	@PutMapping
//	public void put1() {
//
//	}
//	@DeleteMapping
	public void delete1() {

	}
//
//	@GetMapping(value = "/testA")
	public String testA(@RequestParam(required = false) final String name
			,
			@RequestParam
			@Validated
			final Integer id

			) {
		System.out.println(
				java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "TestAPIDoc.testA()");

		return "testA";
	}

}
