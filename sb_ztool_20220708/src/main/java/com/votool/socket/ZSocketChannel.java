package com.votool.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import com.votool.common.ZPU;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年9月4日
 *
 */
@Getter
@AllArgsConstructor
public class ZSocketChannel {

	private final SocketChannel socketChannel;

	public synchronized void write(final Object message) {
		final byte[] wan = ZPU.wanzhangbytearray(ZPU.serialize(message));
		final ByteBuffer byteBuffer = ByteBuffer.wrap(wan);
		while (byteBuffer.hasRemaining()) {
			try {
				this.socketChannel.write(byteBuffer);
			} catch (final IOException e) {
				if (e instanceof ClosedChannelException) {
					try {
						this.socketChannel.close();
					} catch (final IOException e1) {
						e1.printStackTrace();
					}
				} else {
					e.printStackTrace();
				}
			}
		}
	}

//	/**
//	 * 响应
//	 *
//	 * @param dataArray
//	 *
//	 */
//	public synchronized void write(final byte[] dataArray) {
//		final ByteBuffer byteBuffer = ByteBuffer.wrap(dataArray);
//		while (byteBuffer.hasRemaining()) {
//			try {
//				this.socketChannel.write(byteBuffer);
//			} catch (final IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

}
