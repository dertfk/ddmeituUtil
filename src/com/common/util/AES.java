package com.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具
 * 
 * @author YPZS
 *
 */
public class AES {
	/**
	 * AES加密
	 * 
	 * @param input
	 *            加密数据
	 * @param key
	 *            秘钥
	 * @return 加密结果,发生异常返回null
	 */
	public static String encrypt(String input, String key) {
		String result = "";
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			// 统一用UTF-8格式编码
			byte[] crypted = cipher.doFinal(input.getBytes("UTF-8"));
			result = new String(Base64.encode(crypted));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.replace(" ", "");
	}

	/**
	 * AES解密
	 * 
	 * @param input
	 *            解密数据
	 * @param key
	 *            秘钥
	 * @return 解密秘钥,发生异常返回null
	 */
	public static String decrypt(String input, String key) {
		String result = "";
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			byte[] output = cipher.doFinal(Base64.decode(input));
			// 统一用UTF-8格式编码
			result = new String(output, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
