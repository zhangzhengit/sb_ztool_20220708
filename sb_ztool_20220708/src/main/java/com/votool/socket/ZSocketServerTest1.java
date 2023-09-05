package com.votool.socket;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.votool.common.ZPU;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年9月4日
 *
 */
public class ZSocketServerTest1 extends ZSocketServer<ZMP> {


	public static void main(final String[] args) {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZSocketServerTest1.main()");

		final int port = 9988;

		final ZSocketServerTest1 test1 = new ZSocketServerTest1(port,"server");
		test1.startServer();
		System.out.println("Start-ok");
	}

	public ZSocketServerTest1(final Integer port, final String threadName) {
		super(port, threadName);
	}


	@Override
	public void onOpen(final SocketChannel socketChannel) {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZSocketServerTest1.onOpen()");

		try {
			System.out.println("有新连接了：" + socketChannel.getRemoteAddress());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose(final SocketChannel socketChannel) {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZSocketServerTest1.onClose()");

		try {
			System.out.println("有连接关闭了：" + socketChannel.getRemoteAddress());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(final ZSocketChannel socketChannel, final ZMP t) {
		System.out.println(java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t"
				+ "ZSocketServerTest1.onMessage()");

		final ZMP zmp = t;
		System.out.println("新消息：" + zmp);

	}

}
