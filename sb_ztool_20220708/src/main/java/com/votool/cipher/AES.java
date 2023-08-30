package com.votool.cipher;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

/**
 * AES
 *
 * @author zhangzhen
 * @date 2023年8月22日
 *
 */
public class AES {

	private static final String ALGORITHM = "AES";

	private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

	private static final int AES_SECRETKEY_LENGTH = 16;

//	public static void main(final String[] args) {
//		final String en = "tt3iDZFNvyoDVOoeadKrQUSAxU8k2xz/by+Tk1/froClp1QTxOXPV846JHp6DmZoRZYTMcRh+tg15kJeVpGOEorRAp0d3or6P5tsp2baScrlmgcK+SmOLe4B/PEW2XuqqftRrB0pWdtFU1Hsa/hgsoOPiModlTWrw4H2Qz7m2Sp9BPCYelswOWWD/xMSnXUT5cG0FN59HeLuDV0Nwjad9sCW8+NhMDS5sSEFBLm6d8acPVHbYBXmIvb71rJH/ftwZN+ZdSt+nTuT1FmzGjRWz6GdA6elNGNbW/VCidSDqS9WXyGhSudCF9e8cQ9TT6nLTyzhJbrRXJVa4H/XedwuUz80/EVz8X2Wcs511C2rWYcAkIVTfvvAzqQyBN1D/M9f76HAtIRSYxBqL1DB9bhwPnXOQFwIRwkp5TODTbApQd49iWRJgZQ2hluvJttib2aS6ADS9qVV1pKW6m+WBqWN2mpi5GHgWTnivRzyMuJf0dLCE5Git57AKUGrM1cQ4cdlW1K07s3Q+4H1kf4Y0jy206I5PB1+gbNmSc4yni6phXPMzmFI9Vyl2j3QN7zZ28yBP5tiIa1yjf3Uztj1RC8JLJR3qLLjmbBjVLl6mFSBKdqr6PXeFzxEymxH2fb81bx7Pbnmb1YR4YxASeGmZ36TbUl1JkAv7bhMTFfRQYFFcJ45IJ4q2D5ul5f+/5r4NnHR49lcLbkGWO6RUVxQZINOj87qVo5hjJp6fw1wWKIseJkWsxKv9ieRmwWR7hoJ8NpBsRwdZiAVB7EAKcD/WXfD/jIJwWqyef7A1rJMIuZ69gQ/VxHwvqZytuZLWGDHHVH+S1njNLENPyHCKgg0sbD6wLDmS9olct9l5gxHIA1ZHsMsKsc0TCvaNMd7qhrrLqsE8cp2Z8X4bipzRNrmO8IBD/SXf+olUOyplbuusg4N5sbjyKn6kDPQu0qTp0Q4ZF1RFKbJ4MOlh329JQyIWpNdlghXA9Hzwh73TEI9i9EIwPA0UpO3nYFPgOb5P3NdFkQcdmDHzkMexYFp4ORu56Mn434t3b5phBC6/wcjxk996ohmyoZOIydtsxZ4563H9/VIEVoTWy38QX4wEQ4gQQKIW+pzCzQi5kw/Bo30lZaVe5HUnEWztQ+O0Dbu4meeRlJpWeUa/Mghm1q+ZYlZg4dNZUSAxU8k2xz/by+Tk1/froDk7RtSl9QpbImhOoIJ+x9tvbawXLFGnciM9MPbk2U9ZcmNEj3rOouXvpCG0opVbcjlmgcK+SmOLe4B/PEW2XuqrJac9Y";
//
//		final String sk = "6b35e068c45d84a9";
//		final String decrypt = decrypt(en, sk);
//		System.out.println(decrypt);
//	}

	/**
	 * 加密
	 *
	 * @param plainText
	 * @param secretKey
	 * @return
	 *
	 */
	public static String encrypt(final String plainText, final String secretKey) {
		final String encrypt = encrypt(plainText.getBytes(), secretKey);
		return encrypt;
	}

	public static String encrypt(final byte[] byteArray , final String secretKey) {
		try {
			final Key key = generateKey(secretKey);
			final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			final byte[] encryptedBytes = cipher.doFinal(byteArray);
			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 *
	 * @param encryptedText
	 * @param secretKey
	 * @return
	 *
	 */
	public static String decrypt(final String encryptedText, final String secretKey) {
		try {
			final Key key = generateKey(secretKey);
			final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, key);
			final byte[] decode = Base64.getDecoder().decode(encryptedText);
			final byte[] decryptedBytes = cipher.doFinal(decode);
			return new String(decryptedBytes);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从输入的字符串使用SHA256生成一个指定长度的字符串，当做AES秘钥
	 *
	 * @param string
	 * @return
	 *
	 */
	public static String gSecretKeyFromString(final String string) {

		final String input = string;
		final String sha256 = Hashing.sha256().newHasher().putString(input, Charset.defaultCharset()).hash().toString();
		final byte[] bytes = sha256.getBytes();
		final byte[] copyOf = Arrays.copyOf(bytes, AES_SECRETKEY_LENGTH);

		final String secretKey = new String(copyOf);

		return secretKey;
	}

	private static SecretKeySpec generateKey(final String secretKey) throws Exception {
		final byte[] keyBytes = secretKey.getBytes();
		return new SecretKeySpec(keyBytes, ALGORITHM);
	}

}
