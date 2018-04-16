package com.tencent.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class WXMediaUtil {

	private final static boolean PRINT_LOG = true;
	
	private static void print(String content) {
		if (PRINT_LOG) {
			System.out.print(content + "\n");
		}
	}
	
	/**
	 * 将微信多媒体文件存为本地文件
	 * @param accessToken
	 * @param mediaId
	 * @param saveFolder 保存本地文件目录
	 * @return 保存后的文件路径，若保存失败则为null
	 */
	public static String saveMedia2File(String accessToken, String mediaId, String saveFolder) {
		String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + accessToken
							+ "&media_id=" + mediaId;
		print("requestUrl====" + requestUrl);
		String filePath = null;
		if (!saveFolder.endsWith(String.valueOf(File.separatorChar))) {
			saveFolder += File.separatorChar;
		}
		
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			
//			String filePathUrl = conn.getURL().getFile();
//			String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);
//			String fileExt = fileFullName.substring(fileFullName.lastIndexOf("."));
//			filePath = savePath + mediaId + fileExt;
			
			print("ContentType====" + conn.getContentType());
			Map<String, String> header = getHttpResponseHeader(conn);
			String suffix = getFileSuffix(header);
			print("suffix====" + suffix);
			filePath = saveFolder + mediaId + suffix;
			
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1) {
				fos.write(buf, 0, size);;
			}
			
			fos.close();
			bis.close();
			conn.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
			filePath = null;
			print("getWXMedia2File====" + e.toString());
		}
		
		return filePath;
	}
	
	/**
	 * 将微信多媒体文件存为Image
	 * @param accessToken
	 * @param mediaId
	 * @return 保存后的图像，若保存失败或资源文件不是图像则为null
	 */
	public static BufferedImage saveMedia2Image(String accessToken, String mediaId) {
		String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + accessToken
							+ "&media_id=" + mediaId;
		print("requestUrl====" + requestUrl);
		BufferedImage uploadImage = null;
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod("GET");

			
			String contentType = conn.getContentType();
			print("ContentType====" + contentType);
			Map<String, String> header = getHttpResponseHeader(conn);
			String suffix = getFileSuffix(header);
			print("suffix====" + suffix);
			if (contentType != null && contentType.contains("image")) {
				uploadImage = ImageIO.read(conn.getInputStream());
			}
			conn.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
			print("getWXMedia2Image====" + e.toString());
		}
		
		return uploadImage;
	}
	
	private static Map<String, String> getHttpResponseHeader(HttpURLConnection conn) {
		Map<String, String> header = new LinkedHashMap<String, String>();
		for (int i = 0; ; i++) {
			String mine = conn.getHeaderField(i);
			if (mine == null) {
				break;
			}
			header.put(conn.getHeaderFieldKey(i), mine);
		}
		return header;
	}
	
	private static String getFileSuffix(Map<String, String> header) {
		String suffix = null;
		String contentDisposition = header.get("Content-disposition");
		int pos = contentDisposition.lastIndexOf('.');
		if (pos > 0) {
			suffix = contentDisposition.substring(pos, contentDisposition.length() - 1);
		}
		return suffix;
	}
	
}
