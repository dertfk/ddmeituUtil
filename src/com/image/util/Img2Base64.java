package com.image.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

@SuppressWarnings("restriction")
public class Img2Base64 {

	/**
	 * 将图片base64编码转换为文件
	 * @param imgBase64 图片base64编码
	 * @param imgPath 图片文件路径
	 * @return 成功或失败
	 */
	public static boolean generateImage(String imgBase64, String imgPath) {
		if (imgBase64 == null)
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// 解密
			byte[] b = decoder.decodeBuffer(imgBase64);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(imgPath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 将图片文件转化为base64编码
	 * @param imgPath 图片文件路径
	 * @return 图片base64编码
	 */
	public static String getImageStr(String imgPath) {
		InputStream inputStream = null;
		byte[] data = null;
		try {
			inputStream = new FileInputStream(imgPath);
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 加密
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}
}
