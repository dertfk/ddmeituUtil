package com.image.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.common.data.BaseResponse;

/**
 * @author huhao
 */
public class CertificatePhoto {

	/**
	 * 证件照文件类型：JPG
	 */
	public final static String FILE_TYPE_JPG = "jpg";
	
	/**
	 * 证件照文件类型：JPEG
	 */
	public final static String FILE_TYPE_JPEG = "jpeg";

	/**
	 * 证件照文件类型：PNG
	 */
	public final static String FILE_TYPE_PNG = "png";

	/**
	 * 1寸照片，3*3(竖)
	 */
	public final static int PHOTO_SIZE_1INCH = 1;

	/**
	 * 2寸照片，2*2(竖)
	 */
	public final static int PHOTO_SIZE_2INCH = 2;
	
	// 是否显示打印
	private final static boolean PRINT_LOG = false;

	// 是否有边框
	private final static boolean BORDER_DRAW = false;
	
	// 边框颜色
	private final static Color BORDER_COLOR = Color.RED;
	
	// 背景颜色
	private final static Color BG_COLOR = Color.WHITE;

	// 1寸相片像素宽度
	private final static int _1_INCH_WIDTH = 295;
	// 1寸相片像素高度
	private final static int _1_INCH_HEIGHT = 413;
	// 1寸相片宽高比
	private final static float _1_INCH_RATIO = 0.714f;

	// 2寸相片像素宽度
	private final static int _2_INCH_WIDTH = 413;
	// 2寸相片像素高度
	private final static int _2_INCH_HEIGHT = 626;
	// 2寸相片宽高比
	private final static float _2_INCH_RATIO = 0.66f;

	// 5寸相纸像素宽度
	private final static int _5_INCH_WIDTH = 1500;
	// 5寸相纸像素高度
	private final static int _5_INCH_HEIGHT = 1051;
	
	// 以下两参数是为了配合打印机和打印效果做的调整，如无需调整，可设为0
	// 计算照片间距时的余量
	private final static int BLANK_WIDTH = 50;
	// 定位照片时的偏移量
	private final static int BLANK_OFFSET = 25;
	
	// 手机照片常用宽高比为3/4(0.75)或9/16(0.5625)，为增加兼容性，将判断的等于条件放宽为位于区间
	// 将3/4上限增加到0.8，将9/16下限增加到0.5，因此合法的宽高比区间为[0.5, 0.8]
	// 最小宽高比
	private final static float MIN_RATIO = 0.5f;
	// 最大宽高比
	private final static float MAX_RATIO = 0.8f;

	// 错误代码和类型
	public final static String ERROR_FILEPATH_CODE = "000001";
	public final static String ERROR_FILEPATH_MSG = "文件路径为空";
	public final static String ERROR_FILETYPE_CODE = "000002";
	public final static String ERROR_FILETYPE_MSG = "文件类型错误，只支持jpg/jpeg/png";
	public final static String ERROR_FILEMATCH_CODE = "000003";
	public final static String ERROR_FILEMATCH_MSG = "目标文件与源文件类型不匹配";
	public final static String ERROR_PHOTOSIZE_CODE = "000004";
	public final static String ERROR_PHOTOSIZE_MSG = "照片尺寸错误，只支持1寸或2寸";
	public final static String ERROR_FILEWH_CODE = "000005";
	public final static String ERROR_FILEWH_MSG = "文件宽度或高度为0";
	public final static String ERROR_RATIO_CODE = "000006";
	public final static String ERROR_RATIO_MSG = "文件宽高比需要在0.5~0.8之间";
	public final static String ERROR_MATRIXIMAGE_CODE = "000007";
	public final static String ERROR_MATRIXIMAGE_MSG = "生成文件失败";
	public final static String ERROR_OPERATE_CODE = "000008";
	public final static String ERROR_OPERATE_MSG = "处理过程异常";

	// 文件类型
	private static String File_Type;
	// 打印尺寸
	private static int Photo_Size;
	// 相纸像素宽度
	private static int Paper_Width;
	// 相纸像素高度
	private static int Paper_Height;
	// 每张照片像素宽度
	private static int Photo_Width;
	// 每张照片像素高度
	private static int Photo_Height;
	// 每行照片数量
	private static int Horizontal_Count;
	// 每列照片数量
	private static int Vertical_Count;
	// 每张照片水平空白(两张照片水平间隔为Horizontal_Padding * 2)
	private static int Horizontal_Padding;
	// 每张照片垂直空白(两张照片水平间隔为Horizontal_Padding * 2)
	private static int Vertical_Padding;
	
	private static void print(String content) {
		if (PRINT_LOG) {
			System.out.print(content + "\n");
		}
	}

	/**
	 * 将原图生成对应尺寸的证件照
	 * 
	 * @param photo_size
	 *            证件照大小，只能取PHOTO_SIZE_1INCH或PHOTO_SIZE_2INCH
	 * @param src_path
	 *            原图路径，原图(或旋转90度后)宽高比要在[0.5, 0.8]区间范围内，文件类型必须以FILE_TYPE_JPG、FILE_TYPE_JPEG或FILE_TYPE_PNG结尾
	 * @param dst_path
	 *            目标文件路径，文件类型必须与src_path相同
	 * @return BaseResponse
	 * 			code为"000000"表示创建成功，否则创建失败
	 */
	public static BaseResponse createCertificatePhoto(int photo_size, String src_path, String dst_path) {
		// TODO Auto-generated method stub
		BaseResponse baseResponse = new BaseResponse();

		if (src_path == null || src_path.length() == 0 || dst_path == null || dst_path.length() == 0) {
			print("src_path or dst_path is null");
			baseResponse.setCode(ERROR_FILEPATH_CODE);
			baseResponse.setMsg(ERROR_FILEPATH_MSG);
			return baseResponse;
		}
		
		print("src_path====" + src_path);
		print("dst_path====" + dst_path);
		// 判断原文件和目标文件的文件名合法性
		String src_suffix = FILE_TYPE_JPG, dst_suffix = FILE_TYPE_JPG;
		int pos = src_path.lastIndexOf(".");
		if (pos < 0) {
			print("src_path is not end with " + FILE_TYPE_JPG + " or " + FILE_TYPE_JPEG + " or " + FILE_TYPE_PNG);
			baseResponse.setCode(ERROR_FILETYPE_CODE);
			baseResponse.setMsg(ERROR_FILETYPE_MSG);
			return baseResponse;
		} else {
			src_suffix = src_path.substring(pos+1).toLowerCase();
		}
		pos = dst_path.lastIndexOf(".");
		if (pos < 0) {
			print("dst_path is not end with " + FILE_TYPE_JPG + " or " + FILE_TYPE_JPEG + " or " + FILE_TYPE_PNG);
			baseResponse.setCode(ERROR_FILETYPE_CODE);
			baseResponse.setMsg(ERROR_FILETYPE_MSG);
			return baseResponse;
		} else {
			dst_suffix = dst_path.substring(pos+1).toLowerCase();
		}

		if (!src_suffix.equals(dst_suffix)) {
			print("src_path and dst_path is not match");
			baseResponse.setCode(ERROR_FILEMATCH_CODE);
			baseResponse.setMsg(ERROR_FILEMATCH_MSG);
			return baseResponse;
		} 
		if (!(src_suffix.equals(FILE_TYPE_JPG) || src_suffix.equals(FILE_TYPE_JPEG) || src_suffix.equals(FILE_TYPE_PNG))) {
			print("src_path or dst_path is not end with " + FILE_TYPE_JPG + " or " + FILE_TYPE_JPEG + " or " + FILE_TYPE_PNG);
			baseResponse.setCode(ERROR_FILETYPE_CODE);
			baseResponse.setMsg(ERROR_FILETYPE_MSG);
			return baseResponse;
		}

		File_Type = dst_suffix;
		if (photo_size == PHOTO_SIZE_1INCH || photo_size == PHOTO_SIZE_2INCH) {
			setSize(photo_size);
			createPhoto(src_path, dst_path, baseResponse);
		} else {
			print("photo_size must be " + PHOTO_SIZE_1INCH + " or " + PHOTO_SIZE_2INCH);
			baseResponse.setCode(ERROR_PHOTOSIZE_CODE);
			baseResponse.setMsg(ERROR_PHOTOSIZE_MSG);
		}

		return baseResponse;
	}

	/**
	 * 设置打印尺寸
	 * 
	 * @param photo_size
	 *            照片大小，1寸或2寸
	 * 
	 */
	private static void setSize(int photo_size) {
		// TODO Auto-generated method stub
		Photo_Size = photo_size;
		
		if (Photo_Size == PHOTO_SIZE_1INCH) {
			Photo_Width = _1_INCH_WIDTH;
			Photo_Height = _1_INCH_HEIGHT;
			
			// 3*3
			Paper_Width = _5_INCH_HEIGHT;
			Paper_Height = _5_INCH_WIDTH;
			Horizontal_Count = 3;
			Vertical_Count = 3;
			
			// 4*2
//			Paper_Width = _5_INCH_WIDTH;
//			Paper_Height = _5_INCH_HEIGHT;
//			Horizontal_Count = 4;
//			Vertical_Count = 2;
		} else if (Photo_Size == PHOTO_SIZE_2INCH) {
			Photo_Width = _2_INCH_WIDTH;
			Photo_Height = _2_INCH_HEIGHT;
			Paper_Width = _5_INCH_HEIGHT;
			Paper_Height = _5_INCH_WIDTH;
			Horizontal_Count = 2;
			Vertical_Count = 2;
		}

		Horizontal_Padding = (Paper_Width - Photo_Width * Horizontal_Count - BLANK_WIDTH) / (Horizontal_Count * 2);
		Vertical_Padding = (Paper_Height - Photo_Height * Vertical_Count - BLANK_WIDTH) / (Vertical_Count * 2);

		print("5 inch paper width====" + Paper_Width);
		print("5 inch paper height====" + Paper_Height);
		print(photo_size + " inch photo width====" + Photo_Width);
		print(photo_size + " inch photo height====" + Photo_Height);
		print(photo_size + " inch horizontal count====" + Horizontal_Count);
		print(photo_size + " inch vertical count====" + Vertical_Count);
		print(photo_size + " inch horizontal padding====" + Horizontal_Padding);
		print(photo_size + " inch vertical padding====" + Vertical_Padding);
	}

	/**
	 * 将原图生成证件照
	 * 
	 * @param src_path
	 *            原图路径，原图(或旋转90度后)宽高比要在[0.5, 0.8]区间范围内，文件类型必须以FILE_TYPE_JPG或FILE_TYPE_PNG结尾
	 * @param dst_path
	 *            目标文件路径，文件类型必须与src_path相同
	 * @param baseResponse
	 * 				返回数据，code为"000000"表示创建成功，否则创建失败
	 */
	private static void createPhoto(String src_path, String dst_path, BaseResponse baseResponse) {
		// TODO Auto-generated method stub
		// 原图（或旋转后的原图）
		BufferedImage srcImage = null;
		// 裁剪后的原图
		BufferedImage cropImage = null;
		// 缩放后的裁剪图
		BufferedImage scaleImage = null;
		// 目标图
		BufferedImage dstImage = null;

		try {
			/**
			 * 步骤1：获取原图数据
			 */
			File srcFile = new File(src_path);
			srcImage = ImageIO.read(srcFile);
			// 原图大小
			int src_width = srcImage.getWidth();
			int src_height = srcImage.getHeight();
			if (src_width == 0 || src_height == 0) {
				print("the width or height of src photo is zero");
				baseResponse.setCode(ERROR_FILEWH_CODE);
				baseResponse.setMsg(ERROR_FILEWH_MSG);
				return;
			}
			// 原图宽高比
			float src_ratio = (float) ((src_width * 1.0) / src_height);

			print("src_width====" + src_width);
			print("src_height====" + src_height);
			print("src_ratio====" + src_ratio);

			// 原图宽大于高，进行旋转处理
			if (src_ratio > 1) {
				srcImage = rotateImage(srcImage, 90, BG_COLOR);
				// 重新获取数据
				src_width = srcImage.getWidth();
				src_height = srcImage.getHeight();
				src_ratio = (float) ((src_width * 1.0) / src_height);

				print("src_width after rotate 90 degree====" + src_width);
				print("src_height after rotate 90 degree====" + src_height);
				print("src_ratio after rotate 90 degree====" + src_ratio);
			}

			// 判断原图宽高比是否合法
			if (src_ratio < MIN_RATIO || src_ratio > MAX_RATIO) {
				print("the ratio of width to height must in [" + MIN_RATIO + ", " + MAX_RATIO + "]");
				baseResponse.setCode(ERROR_RATIO_CODE);
				baseResponse.setMsg(ERROR_RATIO_MSG);
				return;
			}

			/**
			 * 步骤2：对原图按比例裁剪
			 */
			// 是否需要裁剪
			boolean corpFlag = false;
			if (Photo_Size == PHOTO_SIZE_1INCH && (src_width * _1_INCH_HEIGHT == src_height * _1_INCH_WIDTH)) {
				print("the src photo is a standard 1 inch photo");
			} else if (Photo_Size == PHOTO_SIZE_2INCH && (src_width * _2_INCH_HEIGHT == src_height * _2_INCH_WIDTH)) {
				print("the src photo is a standard 2 inch photo");
			} else {
				corpFlag = true;
			}

			// 裁剪照片的大小
			int crop_width = 0, crop_height;
			// 裁剪照片在原图上的起始点坐标
			int crop_x, crop_y;
			// 裁剪宽高比
			float ratio_crop = 0;

			if (corpFlag) {
				if (Photo_Size == PHOTO_SIZE_1INCH) {
					ratio_crop = _1_INCH_RATIO;
					if (src_ratio >= ratio_crop) {
						// 原图宽高比大于裁剪宽高比，需裁剪宽度
						crop_height = src_height;
						crop_width = (crop_height * _1_INCH_WIDTH) / _1_INCH_HEIGHT;
						crop_x = (src_width - crop_width) / 2;
						crop_y = 0;
					} else {
						// 原图宽高比小于裁剪宽高比，需裁剪高度
						crop_width = src_width;
						crop_height = (crop_width * _1_INCH_HEIGHT) / _1_INCH_WIDTH;
						crop_x = 0;
						crop_y = (src_height - crop_height) / 2;
					}
				} else {
					ratio_crop = _2_INCH_RATIO;
					if (src_ratio >= ratio_crop) {
						// 原图宽高比大于裁剪宽高比，需裁剪宽度
						crop_height = src_height;
						crop_width = (crop_height * _2_INCH_WIDTH) / _2_INCH_HEIGHT;
						crop_x = (src_width - crop_width) / 2;
						crop_y = 0;
					} else {
						// 原图宽高比小于裁剪宽高比，需裁剪高度
						crop_width = src_width;
						crop_height = (crop_width * _2_INCH_HEIGHT) / _2_INCH_WIDTH;
						crop_x = 0;
						crop_y = (src_height - crop_height) / 2;
					}
				}

				// 对原图按比例裁剪
				cropImage = srcImage.getSubimage(crop_x, crop_y, crop_width, crop_height);

				print("crop_width====" + crop_width);
				print("crop_height====" + crop_height);
				print("crop_x====" + crop_x);
				print("crop_y====" + crop_y);
			} else {
				cropImage = srcImage;
			}

			/**
			 * 步骤3：对裁剪后的图片按比例缩放
			 */

			// 缩放图大小
			int scale_width = Photo_Width;
			int scale_height = Photo_Height;
			float scale_ratio;
			if (crop_width != 0) {
				scale_ratio = (float) ((scale_width * 1.0) / crop_width);
			} else {
				scale_ratio = (float) ((scale_width * 1.0) / src_width);
			}

			print("scale_width====" + scale_width);
			print("scale_height====" + scale_height);
			print("scale_ratio====" + scale_ratio);

			scaleImage = new BufferedImage(scale_width, scale_height, BufferedImage.TYPE_INT_RGB);
			scaleImage.getGraphics().drawImage(cropImage, 0, 0, scale_width, scale_height, null);

			/**
			 * 步骤4：对缩放后的图片按HORIZONTAL_COUNT * VERTICAL_COUNT生成新图片
			 */
			dstImage = matrixImage(scaleImage, Paper_Width, Paper_Height, Horizontal_Count, Vertical_Count, Horizontal_Padding, Vertical_Padding, BG_COLOR);

			/**
			 * 步骤5：保存文件
			 */
			if (dstImage != null) {
				File dstFile = new File(dst_path);
				ImageIO.write(dstImage, File_Type, dstFile);
			} else {
				baseResponse.setCode(ERROR_MATRIXIMAGE_CODE);
				baseResponse.setMsg(ERROR_MATRIXIMAGE_MSG);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			print("create5InchPhoto====" + e.toString());
			baseResponse.setCode(ERROR_OPERATE_CODE);
			baseResponse.setMsg(ERROR_OPERATE_MSG);
			baseResponse.setData(e.toString());
		} finally {
			if (srcImage != null) {
				srcImage.flush();
			}
			if (cropImage != null) {
				cropImage.flush();
			}
			if (scaleImage != null) {
				scaleImage.flush();
			}
			if (dstImage != null) {
				dstImage.flush();
			}
		}
	}

	/**
	 * 将原图进行任意角度旋转
	 * 
	 * @param original_image
	 *            原图
	 * @param rotate_degree
	 *            旋转度数
	 * @param bg_color
	 *            背景颜色
	 * 
	 * @return 旋转后的图像，如果有异常则返回原图
	 */
	private static BufferedImage rotateImage(BufferedImage original_image, int rotate_degree, Color bg_color) {
		// TODO Auto-generated method stub
		// 旋转后的宽度
		int rotate_width = 0;
		// 旋转后的高度
		int rotate_height = 0;
		// 原点横坐标
		int x;
		// 原点纵坐标
		int y;
		// 旋转后图片
		BufferedImage rotateImage = null;

		try {
			// 处理角度--确定旋转弧度
			rotate_degree = rotate_degree % 360;
			// 将角度转换到0-360度之间
			if (rotate_degree < 0) {
				rotate_degree = 360 + rotate_degree;
			}
			// 将角度转为弧度
			double theta = Math.toRadians(rotate_degree);

			// 确定旋转后的宽和高
			if (rotate_degree == 180 || rotate_degree == 0 || rotate_degree == 360) {
				rotate_width = original_image.getWidth();
				rotate_height = original_image.getHeight();
			} else if (rotate_degree == 90 || rotate_degree == 270) {
				rotate_height = original_image.getWidth();
				rotate_width = original_image.getHeight();
			} else {
				rotate_width = (int) (Math.sqrt(original_image.getWidth() * original_image.getWidth() + original_image.getHeight() * original_image.getHeight()));
				rotate_height = (int) (Math.sqrt(original_image.getWidth() * original_image.getWidth() + original_image.getHeight() * original_image.getHeight()));
			}

			// 确定原点坐标
			x = (rotate_width / 2) - (original_image.getWidth() / 2);
			y = (rotate_height / 2) - (original_image.getHeight() / 2);

			rotateImage = new BufferedImage(rotate_width, rotate_height, original_image.getType());
			// 设置图片背景颜色
			Graphics2D g2 = (Graphics2D) rotateImage.getGraphics();
			g2.setColor(bg_color);
			// 以给定颜色绘制旋转后图片的背景
			g2.fillRect(0, 0, rotate_width, rotate_height);
			g2.dispose();

			AffineTransform at = new AffineTransform();
			at.rotate(theta, rotate_width / 2, rotate_height / 2);
			at.translate(x, y);
			AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
			rotateImage = op.filter(original_image, rotateImage);

			// 写文件
			// File dstFile = new File("rotate." + File_Type);
			// ImageIO.write(rotateImage, File_Type, dstFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rotateImage = original_image;
			print("rotateImage====" + e.toString());
		}

		return rotateImage;
	}

	/**
	 * 将原图拼接为horizontal_count*vertical_count的图片
	 * 
	 * @param dst_width
	 *            目标图像宽度
	 * @param dst_height
	 *            目标图像高度
	 * @param horizontal_count
	 *            每行的图像数
	 * @param vertical_count
	 *            每列的图像数
	 * @param horizontal_padding
	 *            每行图像间的间隔
	 * @param vertical_padding
	 *            每列图像间的间隔
	 * @param bg_color
	 *            背景颜色
	 * 
	 * @return 生成后的图像，如果有异常则返回空
	 */
	private static BufferedImage matrixImage(BufferedImage srcImage, int dst_width, int dst_height, int horizontal_count, int vertical_count, int horizontal_padding, int vertical_padding,
			Color bg_color) {
		// TODO Auto-generated method stub
		BufferedImage dstImage = null;
		try {
			// 原图大小
			int src_width = srcImage.getWidth();
			int src_height = srcImage.getHeight();
			print("src_width====" + src_width);
			print("src_height====" + src_height);
			print("dst_width====" + dst_width);
			print("dst_height====" + dst_height);

			int total_count = horizontal_count * vertical_count;
			int[][] imageRGBs = new int[total_count][];
			for (int i = 0; i < total_count; i++) {
				imageRGBs[i] = new int[src_width * src_height];
				// 从图片中读取RGB
				imageRGBs[i] = srcImage.getRGB(0, 0, src_width, src_height, imageRGBs[i], 0, src_width);
			}

			// 生成新图片
			dstImage = new BufferedImage(dst_width, dst_height, BufferedImage.TYPE_INT_RGB);

			// 设置背景色
			Graphics2D g2 = (Graphics2D) dstImage.getGraphics();
			g2.setBackground(bg_color);
			// 通过使用当前绘图表面的背景色进行填充指定的矩形
			g2.clearRect(0, 0, dst_width, dst_height);

			int startX, startY;
			int nPoints = 4;
			for (int i = 0; i < vertical_count; i++) {
				for (int j = 0; j < horizontal_count; j++) {
					startX = src_width * j + horizontal_padding * (j * 2 + 1) + BLANK_OFFSET;
					startY = src_height * i + vertical_padding * (i * 2 + 1) + BLANK_OFFSET;
					print((i * horizontal_count + j) + "====(" + startX + ", " + startY + ")");
					dstImage.setRGB(startX, startY, src_width, src_height, imageRGBs[i * horizontal_count + j], 0, src_width);
					if (BORDER_DRAW) {
						// 添加边框
						int[] xPoints = {startX, startX + src_width, startX + src_width, startX};
						int[] yPoints = {startY, startY, startY + src_height, startY + src_height};
						g2.setColor(BORDER_COLOR);
						g2.drawPolygon(xPoints, yPoints, nPoints);
					}
				}
			}
			
			g2.dispose();
		} catch (Exception e) {
			e.printStackTrace();
			dstImage = null;
			print("matrixImage====" + e.toString());
		}

		return dstImage;
	}


}

