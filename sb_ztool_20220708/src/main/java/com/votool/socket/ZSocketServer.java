package com.votool.socket;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.votool.common.ZPU;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Server端，与Client使用定长协议体，一个对象序列化后的字节数组为定长4字节+4字节的byte数组表示的长度的数据
 *
 * @param <T> 通信协议体
 *
 * @author zhangzhen
 * @date 2023年9月4日
 *
 */
@Getter
@AllArgsConstructor
public abstract class ZSocketServer<T> extends Thread {

	private final Integer port;
	private final String threadName;

	private static final int LENGHT_ = 4;

	private static final AtomicBoolean STARTED = new AtomicBoolean(false);
	private static final AtomicReference<Class> CLASS_AR = new AtomicReference<>();

	private void initT() {
		final Type superClass = this.getClass().getGenericSuperclass();

		final Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];

		final String typeName = type.getTypeName();
		try {
			final Class<?> cls = Class.forName(typeName);
			CLASS_AR.set(cls);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public synchronized void startServer() {

		if (ZSocketServer.STARTED.get()) {
			return;
		}

		this.initT();
		this.setName(this.threadName);
		this.start();

		ZSocketServer.STARTED.set(true);
	}

	/**
	 * 有新连接了
	 *
	 * @param socketChannel
	 */
	public abstract void onOpen(final SocketChannel socketChannel);

	/**
	 * 有连接关闭了
	 *
	 * @param socketChannel
	 *
	 */
	public abstract void onClose(final SocketChannel socketChannel);

	/**
	 * 有消息来了，覆盖此方法去处理数据
	 *
	 * @param socketChannel
	 * @param message
	 *
	 */
	public abstract void onMessage(ZSocketChannel socketChannel,T message);

	private  void handleAccept(final SelectionKey key, final Selector selector) {
		final ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = null;
		try {
			socketChannel = serverSocketChannel.accept();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		try {
			socketChannel.configureBlocking(false);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		try {
			socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (final ClosedChannelException e) {
			e.printStackTrace();
		}
		this.onOpen(socketChannel);

	}

	private void handleRead(final SelectionKey key) {

		final SocketChannel socketChannel = (SocketChannel) key.channel();
		if (!socketChannel.isOpen()) {
			return;
		}

		// 2 读4字节，读长度的数据
		final ByteBuffer lengthBuffer = ByteBuffer.allocate(LENGHT_);
		int lengthREAD = 0;
		while (lengthREAD < LENGHT_) {
			int lengthT = 0;
			try {
				lengthT = socketChannel.read(lengthBuffer);
			} catch (final IOException e) {
				this.onClose(socketChannel);
				try {
					socketChannel.close();
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
				key.cancel();
				return;
			}
			if (lengthT == -1) {
				this.onClose(socketChannel);
				try {
					socketChannel.close();
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
				key.cancel();
				return;
			}
			lengthREAD += lengthT;
		}

		final byte[] lengthArray = lengthBuffer.array();
		final int dataLength = ZPU.byteArrayToInt(lengthArray);
		final ByteBuffer dataBuffer = ByteBuffer.allocate(dataLength);

		int dataLengthREAD = 0;
		while (dataLengthREAD < dataLength) {
			int readT = 0;
			try {
				readT = socketChannel.read(dataBuffer);
			} catch (final IOException e) {
				this.onClose(socketChannel);
				try {
					socketChannel.close();
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
				key.cancel();
				return;
			}
			if (readT == -1) {
				this.onClose(socketChannel);
				try {
					socketChannel.close();
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
				key.cancel();
				return;
			}
			dataLengthREAD += readT;
		}

		final byte[] dataArray = dataBuffer.array();


		final Object deserialize = ZPU.deserialize(dataArray, CLASS_AR.get());
		final T t = (T) deserialize;
		final ZSocketChannel socketChannel2 = new ZSocketChannel(socketChannel);
		this.onMessage(socketChannel2, t);
	}

	@Override
	public void run() {
		System.out.println(
				java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "ZSocketServer.run()");

		Selector selector = null;
		ServerSocketChannel serverSocketChannel;
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(this.port));

			selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				selector.select();
			} catch (final IOException e) {
				e.printStackTrace();
			}

			final Set<SelectionKey> selectedKeys = selector.selectedKeys();
			final Iterator<SelectionKey> iterator = selectedKeys.iterator();

			while (iterator.hasNext()) {
				final SelectionKey key = iterator.next();
				iterator.remove();

				if (!key.isValid()) {
					continue;
				}

				if (key.isAcceptable()) {
					this.handleAccept(key, selector);
				} else if (key.isReadable()) {
					this.handleRead(key);
				}
			}
		}
	}

}
