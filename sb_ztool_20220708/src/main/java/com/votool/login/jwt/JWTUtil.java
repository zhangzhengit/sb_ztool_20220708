package com.votool.login.jwt;

import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.checkerframework.checker.units.qual.degrees;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年7月9日
 *
 */
@Component
public class JWTUtil {

	/**
	 * 7天的毫秒数
	 */
	private static final long DAY_7 = 1000 * 60L * 60L * 24 * 7;

	@Autowired
	private  ZJWTConf zjwtConf;

	public String encode(final Integer id,final String name,final String password) {
		final String token = com.auth0.jwt.JWT.create()
				.withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.DAY_7))
				.withClaim("uid", id)
	            .withClaim("uname", name)
				.sign(Algorithm.HMAC256(password));

		return token;
	}


//	public Object decode(final String token) {
//
//		final Object ue = new Object();
//
//		final DecodedJWT jwt = JWT.decode(token);
//		final Map<String, Claim> cm = jwt.getClaims();
//		final Claim claim = cm.get("uid");
//		ue.setId(claim.asInt());
//		ue.setName(cm.get("uname").asString());
//
//		return ue;
//	}


}
