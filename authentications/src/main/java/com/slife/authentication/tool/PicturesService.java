package com.slife.authentication.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 图片操作类
 * @author JingY
 */
public class PicturesService {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	/**
	 * 将图片对象转化成二进制流
	 * @param bm Bitmap对象
	 * @return byte[]
	 */
	public byte[] changeBitmapToByte(Bitmap bm){
		byte[] b=null;
		try {
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);  //30 压缩率  表示压缩70%  100标示不压缩
			b=baos.toByteArray();
			baos.reset();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 修改图片大小（按比例压缩）
	 * @param bitmap Bitmap
	 * @param newWidth 新的宽度
	 * @param newHeight 新的高度
	 * @return bitmap
	 */
	public Bitmap setBitmap(Bitmap bitmap) {
		float width= bitmap.getWidth();
		float height= bitmap.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = 0 ;
		float scaleHeight = 0;
		if (width>height) {
			scaleWidth= (720) / width;
			scaleHeight = (480) / height;
		}else {
			scaleWidth= (720) / height;
			scaleHeight = (480) / width;
		}
		if (scaleWidth>=1||scaleHeight>=1) {
			return bitmap;
		}
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width,
				(int) height, matrix, false);
		return bitmap;
	}

	/**
	 * 修改图片大小（质量压缩法）
	 * @param bitmap
	 * @return bitmap
	 */
	public Bitmap compressImage(Bitmap bitmap) {
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			options -= 10;//每次都减少10
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 加载大图片（超过4M,普通的Bitmap就会报OOM）
	 * @param urlPath 图片路径
	 * @param context 上下文对象
	 * @return bitmap
	 */
	public Bitmap loadPicture(String urlPath,Context context){
		Options opts = new Options();
		// 不读取像素数组到内存中，仅读取图片的信息
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig=Bitmap.Config.RGB_565;
		BitmapFactory.decodeFile(urlPath, opts);
		// 从Options中获取图片的分辨率
		int imageHeight = opts.outHeight;
		int imageWidth = opts.outWidth;
		// 获取Android屏幕的服务
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		// 获取屏幕的分辨率，getHeight()、getWidth已经被废弃掉了
		// 应该使用getSize()，但是这里为了向下兼容所以依然使用它们
		int windowHeight = wm.getDefaultDisplay().getHeight();
		int windowWidth = wm.getDefaultDisplay().getWidth();
		// 计算采样率
		int scaleX = imageWidth / windowWidth;
		int scaleY = imageHeight / windowHeight;
		int scale = 1;
		// 采样率依照最大的方向为准
		if (scaleX > scaleY && scaleY >= 1) {
			scale = scaleX;
		}
		if (scaleX < scaleY && scaleX >= 1) {
			scale = scaleY;
		}
		// false表示读取图片像素数组到内存中，依照设定的采样率
		opts.inJustDecodeBounds = false;
		// 采样率
		opts.inSampleSize = scale;
		Bitmap bitmap=null;
		try {
			bitmap = BitmapFactory.decodeFile(urlPath, opts);
		} catch (Exception e) {
			e.printStackTrace();	//捕获异常避免OOM
		}
		return bitmap;
	}

	/**
	 * 通过ExifInterface类读取图片文件的被旋转角度
	 * @param path ： 图片文件的路径
	 * @return 图片文件的被旋转角度
	 */
	public int readPicDegree(String path) {
		int degree = 0;

		// 读取图片文件信息的类ExifInterface
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;

				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;

				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		}
		return degree;
	}

	/**
	 * 将图片纠正到正确方向
	 *
	 * @param degree ： 图片被系统旋转的角度
	 * @param bitmap ： 需纠正方向的图片
	 * @return 纠向后的图片
	 */
	public Bitmap rotateBitmap(int degree, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);

		Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return bm;
	}
}
