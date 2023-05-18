package com.votool.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.sound.midi.Receiver;

import ch.qos.logback.classic.turbo.ReconfigureOnChangeFilter;
import ch.qos.logback.core.property.FileExistsPropertyDefiner;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

/**
 *
 *
 * @author zhangzhen
 * @date 2022年11月24日
 *
 */
// FIXME 2022年11月24日 下午5:28:30 zhanghen: 写这里
public class FileUtil {

	public static final int HTTP_OK = 200;


	public static File newFile(final String filePath) {
		final File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	public static File mkdirs(final String dirPath) {

		final File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		return dir;
	}

	/**
	 * 从网络URL下载文件
	 *
	 * @param url        网络URL
	 * @param targetFile 要存储的目标文件
	 *
	 * @return 返回http状态码
	 *
	 */
	public static int downloadFile(final String url, final File targetFile) {

		try {

			final URL url_ = new URL(url);
			final HttpURLConnection connection = (HttpURLConnection) url_.openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:107.0) Gecko/20100101 Firefox/107.0");
			connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
			connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
			connection.setRequestProperty("Sec-Fetch-Dest", "document");
			connection.setRequestProperty("Sec-Fetch-Mode", "navigate");
			connection.setRequestProperty("Sec-Fetch-Site", "cross-site");
			connection.setRequestProperty("Pragma", "no-cache");
			connection.setRequestProperty("Cache-Control", "no-cache");

			connection.connect();

			final int responseCode = connection.getResponseCode();

			if (responseCode != HTTP_OK) {
				System.out.println("downloadFile-responseCode = " + responseCode + "\t" + "url = " + url);
				return responseCode;
			}

			final InputStream inputStream = connection.getInputStream();
			final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
			final FileOutputStream fileOutputStream = new FileOutputStream(targetFile);

			final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

			final byte[] bs = new byte[1204];

			int length = 0;
			while ((length = bufferedInputStream.read(bs)) != -1) {
				bufferedOutputStream.write(bs, 0, length);
			}

			bufferedOutputStream.flush();
			bufferedOutputStream.close();

			fileOutputStream.flush();
			fileOutputStream.close();

			bufferedInputStream.close();

		} catch (final IOException e) {
			e.printStackTrace();
		}

		return HTTP_OK;
	}

	/**
	 * 从url解析出文件名,仅限于url最后面是xx.xx格式，此时把xx.xx看作是文件名
	 *
	 * @param url
	 * @return
	 *
	 */
	public static String parseFileNameFromURL(final String url) {
		final int i = url.lastIndexOf(".");
		if (i > -1) {
			final String houzhui = url.substring(i + ".".length());

			final int i2 = url.lastIndexOf("/", i);
			if (i2 > -1) {
				final String fileName = url.substring(i2 + "/".length());

				return fileName;

			}
		}

		return "";
	}

}
