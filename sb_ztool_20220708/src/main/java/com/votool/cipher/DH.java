package com.votool.cipher;

import java.math.BigInteger;
import java.security.SecureRandom;


/**
 * Diffie-Hellman算法
 *
 * @author zhangzhen
 * @date 2023年8月22日
 *
 */
public class DH {

	private static final SecureRandom RANDOM = new SecureRandom();

	/**
	 * DH 中的g取值
	 */
	public static final BigInteger DH_G = BigInteger.valueOf(5L);

	/**
	 * DH 中的p取值
	 */
	public static final BigInteger DH_P = gPrimeNumber();

	public static final BigInteger ALICE_PRIVATEKEY= gAlicePrivatekey();

	public static final BigInteger BOB_PRIVATEKEY= gBobPrivatekey();

	private static final int DH_PRIVATEKEY_RANGE = 200;

	private static final int NUMBER_OF_BITS = 512;


//	public static void main(final String[] args) {
////		final String K = "13604847093016609482956908660166866542420347753409814338079707923140587417655588187928882731228940457751004810287256995795112230696591018843792772855148244126607194419740716412833366023151986303081110804562428712313417321499935795558105853686600937551506429377887126313430865340486307069477007060136782157396341328145318538069612712543734607802250850425408810943558148338139873843480427277057715970916157735182535927335155833206169248805108519299237665835891416209406561337711791143148439328164923143701511109812278066014605176706699061047163598580968492533351671835769695133887763453194166507587666478152969599241121";
////		final String gAESSecretKey = gAESSecretKey(K);
////		System.out.println(gAESSecretKey);
//
//		final String generateKeyRandom = AES.gSecretKeyFromString(DH_G.toString());
//		System.out.println("generateKeyRandom = " + generateKeyRandom);
//		test_dh1();
//	}


	public static void test_dh1() throws Exception {
		System.out.println(
				java.time.LocalDateTime.now() + "\t" + Thread.currentThread().getName() + "\t" + "DH.test_dh1()");

		final BigInteger calculateAlicePublickey = calculateAlicePublickey();
		final BigInteger calculateBobPublickey = calculateBobPublickey();

		final BigInteger aK =calculateBobPublickey.pow(ALICE_PRIVATEKEY.intValue()).mod(DH_P);
		final BigInteger bK =calculateAlicePublickey.pow(BOB_PRIVATEKEY.intValue()).mod(DH_P);

		System.out.println(aK);
		System.out.println(bK);

		final String aliceAESSK = AES.gSecretKeyFromString(String.valueOf(aK));
		final String bobAESSK = AES.gSecretKeyFromString(String.valueOf(bK));
		System.out.println("aliceAESSK = \n");
		System.out.println(aliceAESSK);

		final String message = "hello-world";
		System.out.println(message);
		final String aaa = AES.gSecretKeyFromString(DH_G.toString());
		final String encrypt = AES.encrypt(message, aaa);
//		final String encrypt = AES.encrypt(message, aliceAESSK);

		System.out.println(encrypt);

		final String message2 = AES.decrypt(encrypt, AES.gSecretKeyFromString(DH_G.toString()));
//		final String message2 = AES.decrypt(encrypt, bobAESSK);
		System.out.println(message2);
	}

	/**
	 * 生成一个大素数 p
	 *
	 * @return
	 *
	 */
	public static BigInteger gPrimeNumber() {
		final BigInteger prime = BigInteger.probablePrime(NUMBER_OF_BITS, RANDOM);
		return prime;
	}

	/**
	 * 根据随机生成的Bob私钥来生成Bob的公钥
	 *
	 * @return
	 *
	 */
	public static BigInteger calculateBobPublickey() {
		final BigInteger bobPublickey = DH_G.pow(BOB_PRIVATEKEY.intValue()).mod(DH_P);

		return bobPublickey;
	}

	/**
	 * 随机生成Bob的私钥
	 *
	 * @return
	 *
	 */
	public static BigInteger gBobPrivatekey() {
		final int privateKey = RANDOM.nextInt(DH_PRIVATEKEY_RANGE) + 1;
		return BigInteger.valueOf(privateKey);
	}
	/**
	 * 根据随机生成的Alice私钥来生成Alice的公钥
	 *
	 * @return
	 *
	 */
	public static BigInteger calculateAlicePublickey() {
		final BigInteger alicePublickey = DH_G.pow(ALICE_PRIVATEKEY.intValue()).mod(DH_P);

		return alicePublickey;
	}

	public static BigInteger calculateBK(final BigInteger aPublickey, final BigInteger bPrivatekey,
			final BigInteger p) {
		final BigInteger k = aPublickey.pow(bPrivatekey.intValue()).mod(p);
		return k;
	}

	public static BigInteger calculateAK(final BigInteger bPublickey, final BigInteger aPrivatekey,
			final BigInteger p) {
		final BigInteger k = bPublickey.pow(aPrivatekey.intValue()).mod(p);
		return k;
	}

	public static BigInteger calculatePublickey(final BigInteger g, final BigInteger privatekey,
			final BigInteger p) {
		final BigInteger publickey = g.pow(privatekey.intValue()).mod(p);
		return publickey;
	}

	/**
	 * 随机生成Alice的私钥
	 *
	 * @return
	 *
	 */
	public static BigInteger gAlicePrivatekey() {
		final int privateKey = RANDOM.nextInt(DH_PRIVATEKEY_RANGE) + 1;
		return BigInteger.valueOf(privateKey);
	}
}
