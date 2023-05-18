package com.votool.cacheredis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * java的序列化
 *
 * @author zhangzhen
 * @date 2022年10月25日
 *
 */
public class ZSerializeJAVA {

	public static byte[] serialize(final Serializable serializable) {

		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(serializable);
			final byte[] byteArray = byteArrayOutputStream.toByteArray();
			return byteArray;
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Object deserialize(final byte[] ba) {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(ba);
		try {
			final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			final Object readObject = objectInputStream.readObject();
			return readObject;
		} catch (final IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

}
