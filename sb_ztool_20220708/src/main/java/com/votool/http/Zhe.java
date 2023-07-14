package com.votool.http;

import java.io.IOException;
import java.net.Socket;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年7月13日
 *
 */
public class Zhe {

	private static Socket socket;

	public static synchronized ZHttpConnection connnect(final String url, final int port) {

//		if (socket == null) {
//			System.out.println("新建链接...");
			try {
				socket = new Socket(url.replace("http://", "")
						.replace("https://", "")
						.replace("/", "")
						, port);
			} catch (final IOException e) {
				e.printStackTrace();
			}
//		}

		return newHttpConnection(socket);
	}

	private static ZHttpConnection newHttpConnection(final Socket socket) {
		try {
			return new ZHttpConnection(socket.getOutputStream(), socket.getInputStream());
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
