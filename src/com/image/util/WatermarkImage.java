package com.image.util;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;









import com.common.data.BaseResponse;

import sun.misc.BASE64Encoder;

public class WatermarkImage {

	// 错误代码和类型
	public final static String ERROR_SRCPATH_NULL_CODE = "000001";
	public final static String ERROR_SRCPATH_NULL_MSG = "源文件路径为空";
	public final static String ERROR_SRCTYPE_CODE = "000002";
	public final static String ERROR_SRCTYPE_MSG = "源文件类型错误，只支持jpg/jpeg/png";
	public final static String ERROR_SRCFILE_NULL_CODE = "000003";
	public final static String ERROR_SRCFILE_NULL_MSG = "源文件不存在";
	public final static String ERROR_SRCIMG_NULL_CODE = "000004";
	public final static String ERROR_SRCIMG_NULL_MSG = "源图像为空";
	public final static String ERROR_DSTPATH_NULL_CODE = "000005";
	public final static String ERROR_DSTPATH_NULL_MSG = "目标文件路径为空";
	public final static String ERROR_DSTTYPE_CODE = "000006";
	public final static String ERROR_DSTTYPE_MSG = "目标文件类型错误，只支持jpg/jpeg/png";
	public final static String ERROR_DSTFILE_NULL_CODE = "000007";
	public final static String ERROR_DSTFILE_NULL_MSG = "目标文件不存在";
	public final static String ERROR_DSTIMG_NULL_CODE = "000008";
	public final static String ERROR_DSTIMG_NULL_MSG = "目标图像为空";
	public final static String ERROR_TYPE_MATCH_CODE = "000009";
	public final static String ERROR_TYPE_MATCH_MSG = "源文件与目标文件类型不匹配";
	
	public final static String ERROR_CROP_WIDTH_CODE = "000011";
	public final static String ERROR_CROP_WIDTH_MSG = "裁剪宽度必须大于0";
	public final static String ERROR_CROP_HEIGHT_CODE = "000012";
	public final static String ERROR_CROP_HEIGHT_MSG = "裁剪高度必须大于0";
	public final static String ERROR_CROP_X_CODE = "000013";
	public final static String ERROR_CROP_X_MSG = "源文件裁剪x坐标必须大于等于0";
	public final static String ERROR_CROP_Y_CODE = "000014";
	public final static String ERROR_CROP_Y_MSG = "源文件裁剪y坐标必须大于等于0";
	public final static String ERROR_PASTE_X_CODE = "000015";
	public final static String ERROR_PASTE_X_MSG = "目标图像叠加x坐标必须大于等于0";
	public final static String ERROR_PASTE_Y_CODE = "000016";
	public final static String ERROR_PASTE_Y_MSG = "目标图像叠加y坐标必须大于等于0";
	
	public final static String ERROR_SRCWIDTH_CODE = "000021";
	public final static String ERROR_SRCWIDTH_MSG = "源文件宽度小于裁剪x坐标+裁剪宽度";
	public final static String ERROR_SRCHEIGHT_CODE = "000022";
	public final static String ERROR_SRCHEIGHT_MSG = "源文件高度小于裁剪y坐标+裁剪高度";
	public final static String ERROR_DSTWIDTH_CODE = "000023";
	public final static String ERROR_DSTWIDTH_MSG = "目标图像宽度小于叠加x坐标+裁剪宽度";
	public final static String ERROR_DSTHEIGHT_CODE = "000024";
	public final static String ERROR_DSTHEIGHT_MSG = "目标图像高度小于叠加y坐标+裁剪高度";
	
	public final static String ERROR_IO_CODE = "000031";
	public final static String ERROR_IO_MSG = "IO错误";
	public final static String ERROR_EXCEPTION_CODE = "000032";
	public final static String ERROR_EXCEPTION_MSG = "处理异常";
	
	public final static String ERROR_TEXT_NULL_CODE = "000041";
	public final static String ERROR_TEXT_NULL_MSG = "文本为空";
	public final static String ERROR_DSTPATH_INVALID_CODE = "000005";
	public final static String ERROR_DSTPATH_INVALID_MSG = "目标文件路径无效";
	public final static String ERROR_TEXT_X_CODE = "000042";
	public final static String ERROR_TEXT_X_MSG = "文本x坐标必须大于等于0";
	public final static String ERROR_TEXT_Y_CODE = "000042";
	public final static String ERROR_TEXT_Y_MSG = "文本y坐标必须大于等于0";
	public final static String ERROR_TEXT_WIDTH_CODE = "000042";
	public final static String ERROR_TEXT_WIDTH_MSG = "文本x坐标大于源文件宽度";
	public final static String ERROR_TEXT_HEIGHT_CODE = "000042";
	public final static String ERROR_TEXT_HEIGHT_MSG = "文本y坐标大于源文件高度";

	/**
	 * 文件类型：JPG
	 */
	private final static String FILE_TYPE_JPG = "jpg";
	/**
	 * 文件类型：JPEG
	 */
	private final static String FILE_TYPE_JPEG = "jpeg";
	/**
	 * 文件类型：PNG
	 */
	private final static String FILE_TYPE_PNG = "png";
	
	private final static boolean PRINT_LOG = true;

	private static void print(String content) {
		if (PRINT_LOG) {
			System.out.print(content + "\n");
		}
	}
	
	/**
	 * 从src_path文件抠取图像添加到dst_path文件
	 * @param src_path 源文件路径
	 * @param dst_path 目标文件路径
	 * @param crop_width 抠取图像宽度
	 * @param crop_height 抠取图像高度
	 * @param crop_x 抠取图像在原图的x坐标
	 * @param crop_y 抠取图像在原图的y坐标
	 * @param paste_x 抠取图像在目标图的x坐标
	 * @param paste_y 抠取图像在目标图的y坐标
	 * @return code为"000000"为成功，否则为失败，msg为错误原因
	 */
	public static BaseResponse drawImage2File(String src_path, String dst_path, int crop_width, int crop_height,
								int crop_x, int crop_y, int paste_x, int paste_y) {
		// TODO Auto-generated method stub
		BaseResponse response = new BaseResponse();
		// 源图像
		BufferedImage srcImage = null;
		// 目标图像
		BufferedImage dstImage = null;
		// 从源图裁剪的子图
		BufferedImage cropImage = null;

		print("src_path====" + src_path);
		print("dst_path====" + dst_path);
		// 判断源文件和目标文件的路径合法性
		if (src_path == null || src_path.length() == 0) {
			response.setCode(ERROR_SRCPATH_NULL_CODE);
			response.setMsg(ERROR_SRCPATH_NULL_MSG);
			return response;
		}
		if (dst_path == null || dst_path.length() == 0) {
			response.setCode(ERROR_DSTPATH_NULL_CODE);
			response.setMsg(ERROR_DSTPATH_NULL_MSG);
			return response;
		}
		String src_suffix = FILE_TYPE_JPG, dst_suffix = FILE_TYPE_JPG;
		int pos = src_path.lastIndexOf(".");
		if (pos < 0) {
			response.setCode(ERROR_SRCTYPE_CODE);
			response.setMsg(ERROR_SRCTYPE_MSG);
			return response;
		} else {
			src_suffix = src_path.substring(pos + 1).toLowerCase();
			if (!(src_suffix.equals(FILE_TYPE_JPG) || src_suffix.equals(FILE_TYPE_JPEG) || src_suffix.equals(FILE_TYPE_PNG))) {
				response.setCode(ERROR_SRCTYPE_CODE);
				response.setMsg(ERROR_SRCTYPE_MSG);
				return response;
			}
		}
		pos = dst_path.lastIndexOf(".");
		if (pos < 0) {
			response.setCode(ERROR_DSTTYPE_CODE);
			response.setMsg(ERROR_DSTTYPE_MSG);
			return response;
		} else {
			dst_suffix = dst_path.substring(pos + 1).toLowerCase();
			if (!(dst_suffix.equals(FILE_TYPE_JPG) || dst_suffix.equals(FILE_TYPE_JPEG) || dst_suffix.equals(FILE_TYPE_PNG))) {
				response.setCode(ERROR_SRCTYPE_CODE);
				response.setMsg(ERROR_SRCTYPE_MSG);
				return response;
			}
		}
		if (!src_suffix.equals(dst_suffix)) {
			response.setCode(ERROR_TYPE_MATCH_CODE);
			response.setMsg(ERROR_TYPE_MATCH_MSG);
			return response;
		}

		print("crop_x = " + crop_x + ", crop_y = " + crop_y);
		print("crop_width = " + crop_width + ", crop_height = " + crop_height);
		if (crop_width <= 0) {
			response.setCode(ERROR_CROP_WIDTH_CODE);
			response.setMsg(ERROR_CROP_WIDTH_MSG);
			return response;
		}
		if (crop_height <= 0) {
			response.setCode(ERROR_CROP_HEIGHT_CODE);
			response.setMsg(ERROR_CROP_HEIGHT_MSG);
			return response;
		}
		if (crop_x < 0) {
			response.setCode(ERROR_CROP_X_CODE);
			response.setMsg(ERROR_CROP_X_MSG);
			return response;
		}
		if (crop_y < 0) {
			response.setCode(ERROR_CROP_Y_CODE);
			response.setMsg(ERROR_CROP_Y_MSG);
			return response;
		}
		if (paste_x < 0) {
			response.setCode(ERROR_PASTE_X_CODE);
			response.setMsg(ERROR_PASTE_X_MSG);
			return response;
		}
		if (paste_y < 0) {
			response.setCode(ERROR_PASTE_Y_CODE);
			response.setMsg(ERROR_PASTE_Y_MSG);
			return response;
		}
		
		try {
			File srcFile = new File(src_path);
			if (!srcFile.exists()) {
				response.setCode(ERROR_SRCFILE_NULL_CODE);
				response.setMsg(ERROR_SRCFILE_NULL_MSG);
				return response;
			}
			File dstFile = new File(dst_path);
			if (!dstFile.exists()) {
				response.setCode(ERROR_DSTFILE_NULL_CODE);
				response.setMsg(ERROR_DSTFILE_NULL_MSG);
				return response;
			}
			
			srcImage = ImageIO.read(srcFile);
			int src_width = srcImage.getWidth();
			int src_height = srcImage.getHeight();
			print("src_width = " + src_width + ", src_height = " + src_height);
			if (src_width < crop_x + crop_width) {
				response.setCode(ERROR_SRCWIDTH_CODE);
				response.setMsg(ERROR_SRCWIDTH_MSG);
				return response;
			}
			if (src_height < crop_y + crop_height) {
				response.setCode(ERROR_SRCHEIGHT_CODE);
				response.setMsg(ERROR_SRCHEIGHT_MSG);
				return response;
			}

			dstImage = ImageIO.read(dstFile);
			int dst_width = dstImage.getWidth();
			int dst_height = dstImage.getHeight();
			print("dst_width = " + dst_width + ", dst_height = " + dst_height);
			if (dst_width < crop_x + crop_width) {
				response.setCode(ERROR_DSTWIDTH_CODE);
				response.setMsg(ERROR_DSTWIDTH_MSG);
				return response;
			}
			if (dst_height < crop_y + crop_height) {
				response.setCode(ERROR_DSTHEIGHT_CODE);
				response.setMsg(ERROR_DSTHEIGHT_MSG);
				return response;
			}

			cropImage = srcImage.getSubimage(crop_x, crop_y, crop_width, crop_height);
			Graphics2D g2d = dstImage.createGraphics();
			// 在图形和图像中实现混合和透明效果
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
			// 绘制
			g2d.drawImage(cropImage, paste_x, paste_y, crop_width, crop_height, null);
			// 释放图形上下文使用的系统资源
			g2d.dispose();

			// 写入文件
			ImageIO.write(dstImage, dst_suffix, dstFile);
		} catch (Exception e) {
			print("watermark2File====" + e.toString());
			response.setCode(ERROR_EXCEPTION_CODE);
			response.setMsg(ERROR_EXCEPTION_MSG);
		} finally {
			if (srcImage != null) {
				srcImage.flush();
			}
			if (cropImage != null) {
				cropImage.flush();
			}
			if (dstImage != null) {
				dstImage.flush();
			}
		}

		return response;
	}
	
	/**
	 * 从src_path文件抠取图像添加到dst_img
	 * @param src_path 源文件路径
	 * @param dst_img 目标图像
	 * @param crop_width 抠取图像宽度
	 * @param crop_height 抠取图像高度
	 * @param crop_x 抠取图像在原图的x坐标
	 * @param crop_y 抠取图像在原图的y坐标
	 * @param paste_x 抠取图像在目标图的x坐标
	 * @param paste_y 抠取图像在目标图的y坐标
	 * @return code为"000000"为成功，data为图像base64数据，否则为失败，msg为错误原因
	 */
	public static BaseResponse drawImage2Base64(String src_path, BufferedImage dst_img, int crop_width, int crop_height,
								int crop_x, int crop_y, int paste_x, int paste_y) {
		// TODO Auto-generated method stub
		BaseResponse response = new BaseResponse();
		// 源图
		BufferedImage srcImage = null;
		// 从源图裁剪的子图
		BufferedImage cropImage = null;

		if (src_path == null || src_path.length() == 0) {
			response.setCode(ERROR_SRCPATH_NULL_CODE);
			response.setMsg(ERROR_SRCPATH_NULL_MSG);
			return response;
		}
		print("src_path====" + src_path);
		
		// 判断源文件的合法性
		String src_suffix = FILE_TYPE_JPG;
		int pos = src_path.lastIndexOf(".");
		if (pos < 0) {
			response.setCode(ERROR_SRCTYPE_CODE);
			response.setMsg(ERROR_SRCTYPE_MSG);
			return response;
		} else {
			src_suffix = src_path.substring(pos + 1).toLowerCase();
			if (!(src_suffix.equals(FILE_TYPE_JPG) || src_suffix.equals(FILE_TYPE_JPEG) || src_suffix.equals(FILE_TYPE_PNG))) {
				response.setCode(ERROR_SRCTYPE_CODE);
				response.setMsg(ERROR_SRCTYPE_MSG);
				return response;
			}
		}
		File srcFile = new File(src_path);
		if (!srcFile.exists()) {
			response.setCode(ERROR_SRCFILE_NULL_CODE);
			response.setMsg(ERROR_SRCFILE_NULL_MSG);
			return response;
		}
		
		print("crop_x = " + crop_x + ", crop_y = " + crop_y);
		print("crop_width = " + crop_width + ", crop_height = " + crop_height);
		if (crop_width <= 0) {
			response.setCode(ERROR_CROP_WIDTH_CODE);
			response.setMsg(ERROR_CROP_WIDTH_MSG);
			return response;
		}
		if (crop_height <= 0) {
			response.setCode(ERROR_CROP_HEIGHT_CODE);
			response.setMsg(ERROR_CROP_HEIGHT_MSG);
			return response;
		}
		if (crop_x < 0) {
			response.setCode(ERROR_CROP_X_CODE);
			response.setMsg(ERROR_CROP_X_MSG);
			return response;
		}
		if (crop_y < 0) {
			response.setCode(ERROR_CROP_Y_CODE);
			response.setMsg(ERROR_CROP_Y_MSG);
			return response;
		}
		if (paste_x < 0) {
			response.setCode(ERROR_PASTE_X_CODE);
			response.setMsg(ERROR_PASTE_X_MSG);
			return response;
		}
		if (paste_y < 0) {
			response.setCode(ERROR_PASTE_Y_CODE);
			response.setMsg(ERROR_PASTE_Y_MSG);
			return response;
		}
		
		try {
			srcImage = ImageIO.read(srcFile);
			int src_width = srcImage.getWidth();
			int src_height = srcImage.getHeight();
			print("src_width = " + src_width + ", src_height = " + src_height);
			if (src_width < crop_x + crop_width) {
				response.setCode(ERROR_SRCWIDTH_CODE);
				response.setMsg(ERROR_SRCWIDTH_MSG);
				return response;
			}
			if (src_height < crop_y + crop_height) {
				response.setCode(ERROR_SRCHEIGHT_CODE);
				response.setMsg(ERROR_SRCHEIGHT_MSG);
				return response;
			}

			int dst_width = dst_img.getWidth();
			int dst_height = dst_img.getHeight();
			print("dst_width = " + dst_width + ", dst_height = " + dst_height);
			if (dst_width < crop_x + crop_width) {
				response.setCode(ERROR_DSTWIDTH_CODE);
				response.setMsg(ERROR_DSTWIDTH_MSG);
				return response;
			}
			if (dst_height < crop_y + crop_height) {
				response.setCode(ERROR_DSTHEIGHT_CODE);
				response.setMsg(ERROR_DSTHEIGHT_MSG);
				return response;
			}
			
			cropImage = srcImage.getSubimage(crop_x, crop_y, crop_width, crop_height);
			Graphics2D g2d = dst_img.createGraphics();
			// 在图形和图像中实现混合和透明效果
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
			// 绘制
			g2d.drawImage(cropImage, paste_x, paste_y, crop_width, crop_height, null);
			// 释放图形上下文使用的系统资源
			g2d.dispose();

			ByteArrayOutputStream out = new ByteArrayOutputStream();
	        boolean flag = ImageIO.write(dst_img, "jpg", out);
	        if (flag) {
	        	byte[] data = out.toByteArray();
		        out.write(data);
		        out.close();
		        // 转化为base64
				BASE64Encoder encoder = new BASE64Encoder();
				String dst_string = encoder.encode(data);
				response.setData(dst_string);
			} else {
				response.setCode(ERROR_EXCEPTION_CODE);
				response.setMsg(ERROR_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			print("watermark2Base64====" + e.toString());
			response.setCode(ERROR_EXCEPTION_CODE);
			response.setMsg(ERROR_EXCEPTION_MSG);
		} finally {
			if (srcImage != null) {
				srcImage.flush();
			}
			if (cropImage != null) {
				cropImage.flush();
			}
		}

		return response;
	}
	
	
	/**
	 * 将src_img添加到dst_img
	 * @param src_img 源图像
	 * @param dst_img 目标图像
	 * @param paste_x 叠加图像在目标图像的x坐标
	 * @param paste_y 叠加图像在目标图像的y坐标
	 * @return code为"000000"为成功，data为图像base64数据，否则为失败，msg为错误原因
	 */
	public static BaseResponse drawImage2Base64(BufferedImage src_img, BufferedImage dst_img, int paste_x, int paste_y) {
		// TODO Auto-generated method stub
		BaseResponse response = new BaseResponse();
		if (src_img == null) {
			response.setCode(ERROR_SRCIMG_NULL_CODE);
			response.setMsg(ERROR_SRCIMG_NULL_MSG);
			return response;
		}
		if (dst_img == null) {
			response.setCode(ERROR_DSTIMG_NULL_CODE);
			response.setMsg(ERROR_DSTIMG_NULL_MSG);
			return response;
		}
		if (paste_x < 0) {
			response.setCode(ERROR_PASTE_X_CODE);
			response.setMsg(ERROR_PASTE_X_MSG);
			return response;
		}
		if (paste_y < 0) {
			response.setCode(ERROR_PASTE_Y_CODE);
			response.setMsg(ERROR_PASTE_Y_MSG);
			return response;
		}
		
		int src_width = src_img.getWidth();
		int src_height = src_img.getHeight();
		print("src_width = " + src_width + ", src_height = " + src_height);
		int dst_width = dst_img.getWidth();
		int dst_height = dst_img.getHeight();
		print("dst_width = " + dst_width + ", dst_height = " + dst_height);
		if (dst_width < paste_x + src_width) {
			response.setCode(ERROR_DSTWIDTH_CODE);
			response.setMsg(ERROR_DSTWIDTH_MSG);
			return response;
		}
		if (dst_height < paste_y + src_height) {
			response.setCode(ERROR_DSTHEIGHT_CODE);
			response.setMsg(ERROR_DSTHEIGHT_MSG);
			return response;
		}
		
		try {
			Graphics2D g2d = dst_img.createGraphics();
			// 在图形和图像中实现混合和透明效果
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
			// 绘制
			g2d.drawImage(src_img, paste_x, paste_y, src_width, src_height, null);
			// 释放图形上下文使用的系统资源
			g2d.dispose();

			ByteArrayOutputStream out = new ByteArrayOutputStream();
	        boolean flag = ImageIO.write(dst_img, "jpg", out);
	        if (flag) {
	        	byte[] data = out.toByteArray();
		        out.write(data);
		        out.close();
		        // 转化为base64
				BASE64Encoder encoder = new BASE64Encoder();
				String dst_string = encoder.encode(data);
				response.setData(dst_string);
			} else {
				response.setCode(ERROR_EXCEPTION_CODE);
				response.setMsg(ERROR_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			print("watermark2Base64====" + e.toString());
			response.setCode(ERROR_EXCEPTION_CODE);
			response.setMsg(ERROR_EXCEPTION_MSG);
		}

		return response;
	}
	
	/**
	 * 将文本添加到src_path文件的图像并保存为dst_path文件
	 * @param src_path 源文件路径
	 * @param dst_path 目标文件路径，可为空，为空时添加文本后的图像保存在src_path，不为空时对应文件可不存在
	 * @param text 添加的文本
	 * @param draw_x 文本的x坐标
	 * @param draw_y 文本的y坐标
	 * @param font 文本的字体，可为空
	 * @param color 文本的颜色，可为空
	 * @return code为"000000"为成功，否则为失败，msg为错误原因
	 */
	public static BaseResponse drawText2File(String src_path, String dst_path, String text, int draw_x, int draw_y, Font font, Color color) {
		// TODO Auto-generated method stub
		BaseResponse response = new BaseResponse();
		// 目标图像
		BufferedImage dstImage = null;
		
		print("text====" + src_path);
		if (text == null || text.length() == 0) {
			response.setCode(ERROR_TEXT_NULL_CODE);
			response.setMsg(ERROR_TEXT_NULL_MSG);
			return response;
		}
		
		print("src_path====" + src_path);
		print("dst_path====" + dst_path);
		// 判断源文件和目标文件的路径合法性
		if (src_path == null || src_path.length() == 0) {
			response.setCode(ERROR_SRCPATH_NULL_CODE);
			response.setMsg(ERROR_SRCPATH_NULL_MSG);
			return response;
		}
		
		if (draw_x < 0) {
			response.setCode(ERROR_TEXT_X_CODE);
			response.setMsg(ERROR_TEXT_X_MSG);
			return response;
		}
		if (draw_y < 0) {
			response.setCode(ERROR_TEXT_Y_CODE);
			response.setMsg(ERROR_TEXT_Y_MSG);
			return response;
		}
		
		String src_suffix, dst_suffix = null;
		int pos = src_path.lastIndexOf(".");
		if (pos < 0) {
			response.setCode(ERROR_SRCTYPE_CODE);
			response.setMsg(ERROR_SRCTYPE_MSG);
			return response;
		} else {
			src_suffix = src_path.substring(pos + 1).toLowerCase();
			if (!(src_suffix.equals(FILE_TYPE_JPG) || src_suffix.equals(FILE_TYPE_JPEG) || src_suffix.equals(FILE_TYPE_PNG))) {
				response.setCode(ERROR_SRCTYPE_CODE);
				response.setMsg(ERROR_SRCTYPE_MSG);
				return response;
			}
		}
		
		if (dst_path == null || dst_path.length() == 0) {
			dst_suffix = src_suffix;
		} else {
			pos = dst_path.lastIndexOf(".");
			if (pos < 0) {
				response.setCode(ERROR_DSTTYPE_CODE);
				response.setMsg(ERROR_DSTTYPE_MSG);
				return response;
			} else {
				dst_suffix = dst_path.substring(pos + 1).toLowerCase();
				if (!(dst_suffix.equals(FILE_TYPE_JPG) || dst_suffix.equals(FILE_TYPE_JPEG) || dst_suffix.equals(FILE_TYPE_PNG))) {
					response.setCode(ERROR_SRCTYPE_CODE);
					response.setMsg(ERROR_SRCTYPE_MSG);
					return response;
				}
			}
			if (!src_suffix.equals(dst_suffix)) {
				response.setCode(ERROR_TYPE_MATCH_CODE);
				response.setMsg(ERROR_TYPE_MATCH_MSG);
				return response;
			}
		}
		
		try {
			File srcFile = new File(src_path);
			if (!srcFile.exists()) {
				response.setCode(ERROR_SRCFILE_NULL_CODE);
				response.setMsg(ERROR_SRCFILE_NULL_MSG);
				return response;
			}
			File dstFile;
			if (dst_path == null || dst_path.length() == 0) {
				dstFile = srcFile;
				dstImage = ImageIO.read(srcFile);
			} else {
				dstFile = new File(dst_path);
				if (dstFile.exists()) {
					dstImage = ImageIO.read(dstFile);
				} else {
					if (dstFile.createNewFile()) {
						dstImage = ImageIO.read(srcFile);
					} else {
						response.setCode(ERROR_DSTPATH_INVALID_CODE);
						response.setMsg(ERROR_DSTPATH_INVALID_MSG);
					}
				}
			}
			
			int dst_width = dstImage.getWidth();
			int dst_height = dstImage.getHeight();
			print("dst_width = " + dst_width + ", dst_height = " + dst_height);
			if (dst_width < draw_x) {
				response.setCode(ERROR_TEXT_WIDTH_CODE);
				response.setMsg(ERROR_TEXT_WIDTH_MSG);
				return response;
			}
			if (dst_height < draw_y) {
				response.setCode(ERROR_TEXT_HEIGHT_CODE);
				response.setMsg(ERROR_TEXT_HEIGHT_MSG);
				return response;
			}

			Graphics2D g2d = dstImage.createGraphics();
			if (font != null) {
				g2d.setFont(font);
			}
			if (color != null) {
				g2d.setColor(color);
			}
			// 在图形和图像中实现混合和透明效果
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
			// 抗锯齿
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// 绘制
			g2d.drawString(text, draw_x, draw_y);
			// 释放图形上下文使用的系统资源
			g2d.dispose();

			// 写入文件
			ImageIO.write(dstImage, dst_suffix, dstFile);
		} catch (Exception e) {
			print("watermark2File====" + e.toString());
			response.setCode(ERROR_EXCEPTION_CODE);
			response.setMsg(ERROR_EXCEPTION_MSG);
		} finally {
			if (dstImage != null) {
				dstImage.flush();
			}
		}
		
		return response;
	}
	
	/**
	 * 将文本添加到dst_img
	 * @param text 添加的文本
	 * @param dst_img 目标图像
	 * @param draw_x 文本的x坐标
	 * @param draw_y 文本的y坐标
	 * @param font 文本的字体，可为空
	 * @param color 文本的颜色，可为空
	 * @return code为"000000"为成功，data为图像base64数据，否则为失败，msg为错误原因
	 */
	public static BaseResponse drawText2Base64(String text, BufferedImage dst_img, int draw_x, int draw_y, Font font, Color color) {
		// TODO Auto-generated method stub
		BaseResponse response = new BaseResponse();
		
		print("text====" + text);
		if (text == null || text.length() == 0) {
			response.setCode(ERROR_TEXT_NULL_CODE);
			response.setMsg(ERROR_TEXT_NULL_MSG);
			return response;
		}
		
		if (dst_img == null) {
			response.setCode(ERROR_DSTIMG_NULL_CODE);
			response.setMsg(ERROR_DSTIMG_NULL_MSG);
			return response;
		}
		
		if (draw_x < 0) {
			response.setCode(ERROR_TEXT_X_CODE);
			response.setMsg(ERROR_TEXT_X_MSG);
			return response;
		}
		if (draw_y < 0) {
			response.setCode(ERROR_TEXT_Y_CODE);
			response.setMsg(ERROR_TEXT_Y_MSG);
			return response;
		}
		
		int dst_width = dst_img.getWidth();
		int dst_height = dst_img.getHeight();
		print("dst_width = " + dst_width + ", dst_height = " + dst_height);
		if (dst_width < draw_x) {
			response.setCode(ERROR_TEXT_WIDTH_CODE);
			response.setMsg(ERROR_TEXT_WIDTH_MSG);
			return response;
		}
		if (dst_height < draw_y) {
			response.setCode(ERROR_TEXT_HEIGHT_CODE);
			response.setMsg(ERROR_TEXT_HEIGHT_MSG);
			return response;
		}
		
		try {
			Graphics2D g2d = dst_img.createGraphics();
			if (font != null) {
				g2d.setFont(font);
			}
			if (color != null) {
				g2d.setColor(color);
			}
			// 在图形和图像中实现混合和透明效果
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
			// 抗锯齿
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// 绘制
			g2d.drawString(text, draw_x, draw_y);
			// 释放图形上下文使用的系统资源
			g2d.dispose();

			ByteArrayOutputStream out = new ByteArrayOutputStream();
	        boolean flag = ImageIO.write(dst_img, "jpg", out);
	        if (flag) {
	        	byte[] data = out.toByteArray();
		        out.write(data);
		        out.close();
		        // 转化为base64
				BASE64Encoder encoder = new BASE64Encoder();
				String dst_string = encoder.encode(data);
				response.setData(dst_string);
			} else {
				response.setCode(ERROR_EXCEPTION_CODE);
				response.setMsg(ERROR_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			print("watermark2Base64====" + e.toString());
			response.setCode(ERROR_EXCEPTION_CODE);
			response.setMsg(ERROR_EXCEPTION_MSG);
		}
		
		return response;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BaseResponse ret;
		
		String srcPath = "c:\\1.jpg";
//		watermark2File(srcPath, "c:\\1.jpg", 154, 154, 40, 408, 0, 0);
		String dstPath = "c:\\4234<>.jpg";
//		File dstFile = new File(dstPath);
//		BufferedImage dstImg = null;
//		try {
//			dstImg = ImageIO.read(dstFile);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ret = watermark2Base64(srcPath, dstImg, 154, 154, 40, 408, 0, 0);
		
		Font font = new Font("Arial", Font.PLAIN, 24);
//		ret = drawText2File(srcPath, dstPath, "1111", 100, 400, font, Color.BLUE);
		ret = drawText2File(srcPath, dstPath, "qqqqq", 50, 100, font, Color.BLUE);
		System.out.println("====" + ret.getCode());
//		if (ret.getCode().equals("000000")) {
//			Img2Base64.generateImage((String) ret.getData(), "c:\\2.jpg");
//		}
	}
}
