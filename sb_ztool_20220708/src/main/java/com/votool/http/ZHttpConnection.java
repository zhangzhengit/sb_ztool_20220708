package com.votool.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.units.qual.s;
import org.springframework.boot.json.YamlJsonParser;

import com.google.common.io.ByteProcessor;

import cn.hutool.extra.qrcode.BufferedImageLuminanceSource;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 *
 * @author zhangzhen
 * @date 2023年7月13日
 *
 */
@Data
@AllArgsConstructor
public class ZHttpConnection {
	final OutputStream outputStream;
	final InputStream inputStream;

	BufferedInputStream bufferedInputStream;

	public synchronized void write(final String json) {
		this.write(json.getBytes());
	}

	public synchronized void write(final byte[] ba) {

		try {
			this.outputStream.write(ba);
			this.outputStream.flush();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized String readString() {

		this.initB();

		final byte[] ba = new byte[1000];
		final List<Byte> r = new ArrayList<>();
		while (true) {

			try {
				final int read = this.bufferedInputStream.read(ba);

				if (read > -1) {
					for (final Byte byte1 : ba) {
						r.add(byte1);
					}
				} else {
					break;
				}

			} catch (final IOException e) {
				e.printStackTrace();
			}

		}

		final byte[] baR = new byte[r.size()];
		for (int i = 0; i < r.size(); i++) {
			baR[i] = r.get(i);
		}

		final String string = new String(baR);
		return string;

	}

	private synchronized void initB() {
		if (this.bufferedInputStream == null) {
			this.bufferedInputStream = new BufferedInputStream(this.inputStream);
		}
	}

	public ZHttpConnection(final OutputStream outputStream, final InputStream inputStream) {
		this.outputStream = outputStream;
		this.inputStream = inputStream;
	}

}
