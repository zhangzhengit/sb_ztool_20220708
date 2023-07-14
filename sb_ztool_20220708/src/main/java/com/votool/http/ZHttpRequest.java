package com.votool.http;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;
import com.google.common.io.ByteProcessor;

/**
 * http请求
 *
 * @author zhangzhen
 * @date 2023年7月13日
 */
public class ZHttpRequest {

	private static final String NEW_LINE = "\n\r";
	private final AtomicReference<ZHttpMethodEnum> method = new AtomicReference<>();
	private final String url;
	private final List<ZHeader> headerList = new ArrayList<>();
	private final List<Byte> bodyList = new ArrayList<>();

	public ZHttpRequest(final String url) {
		this.url = url;
	}

	public static ZHttpRequest get(final String url) {
		final ZHttpRequest request = new ZHttpRequest(url);
		request.method(ZHttpMethodEnum.GET);
		return request;
	}

	private ZHttpRequest method(final ZHttpMethodEnum methodEnum) {
		this.method.set(methodEnum);
		return this;
	}

	public ZHttpRequest body(final byte[] ba) {
		for (final byte b : ba) {
			this.bodyList.add(b);
		}
		return this;
	}

	public ZHttpRequest body(final String json) {
		return this.body(json.getBytes());
	}

	public ZHttpRequest header(final String name, final Object value) {
		this.headerList.add(new ZHeader(name, value));
		return this;
	}

	public String execute() {

		// FIXME 2023年7月13日 下午8:23:49 zhanghen: 解析出端口号
		final ZHttpConnection connection = Zhe.connnect(this.url, 80);


		final String path = parsepathFromURL(this.url);
		final String line = this.method.get().getMethod() + " " +  path + " " +  "HTTP/1.1";
		final String host = "Host: " + this.url;
		final String userAgent = "User-Agent: ZConnection";
		// 以后再实现长连接
		final String connectionHeader = "Connection: keep-alive";

		final String newLine = NEW_LINE;

		final String hs =
			          line + newLine
			        + host + newLine
					+ userAgent + newLine
					+ connectionHeader + newLine
					+ newLine
					;

//		System.out.println("hs = " + hs);

		connection.write(hs);

		final String r = connection.readString();
		return r;
	}


	static Set<Integer> numberSet = new HashSet<>();
	static {
		numberSet.add(0);
		numberSet.add(1);
		numberSet.add(2);
		numberSet.add(3);
		numberSet.add(4);
		numberSet.add(5);
		numberSet.add(6);
		numberSet.add(7);
		numberSet.add(8);
		numberSet.add(9);
	}

	private static String parsepathFromURL(final String url) {
		final int i = url.lastIndexOf("/");
		if(i > -1) {
			final String path = url.substring(i);
			return path;
		}

		return "";
	}

}
