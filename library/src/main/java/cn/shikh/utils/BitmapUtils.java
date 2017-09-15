package cn.shikh.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BitmapUtils {

	/**
	 * 等比列缩放
	 * @param bitmap
	 * @param screenWidth
	 * @param screenHight
     * @return
     */
	public static Bitmap scaleBitmap(Bitmap bitmap, int screenWidth, int screenHight) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		float scale = (float) screenWidth / w;
		float scale2 = (float) screenHight / h;

		scale = scale < scale2 ? scale : scale2;
		Matrix matrix = new Matrix();
		// 保证图片不变�?
		matrix.postScale(scale, scale);
		// w,h是原图的属�?.
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}

	/**
	 * 等比列缩放
	 * @param bitmap
	 * @param bitSize
     * @return
     */
	public static Bitmap scaleBitmap(Bitmap bitmap, int bitSize) {
		return scaleBitmap(bitmap,bitSize,bitSize);
	}

	/*压缩图片的方法*/
	public static File scalBitmap(String path,long fileMaxSize,File desk){
//        String path = fileUri.getPath();
		File outputFile = new File(path);
		if(desk == null){
			desk = outputFile.getParentFile();
		}
		long fileSize = outputFile.length();
//        final long fileMaxSize = 200 * 1024;
		if (fileSize >= fileMaxSize) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			int height = options.outHeight;
			int width = options.outWidth;

			double scale = Math.sqrt((float) fileSize / fileMaxSize);
			options.outHeight = (int) (height / scale);
			options.outWidth = (int) (width / scale);
			options.inSampleSize = (int) (scale + 0.5);
			options.inJustDecodeBounds = false;

			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			outputFile = new File(createImageFile(desk).getPath());
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(outputFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}else{
				File tempFile = outputFile;
				outputFile = new File(createImageFile(desk).getPath());
				copyFileUsingFileChannels(tempFile, outputFile);
			}

		}
		return outputFile;

	}

	public static Uri createImageFile(File storageDir){
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "vko_"+timeStamp;
//		File storageDir = FileUtil.getCameraDir();
		File image = null;
		try {
			image = File.createTempFile(imageFileName, ".jpg", storageDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Save a file: path for use with ACTION_VIEW intents
		return Uri.fromFile(image);
	}
	public static void copyFileUsingFileChannels(File source, File dest){
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			try {
				inputChannel = new FileInputStream(source).getChannel();
				outputChannel = new FileOutputStream(dest).getChannel();
				outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			try {
				inputChannel.close();
				outputChannel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存到SD�?
	 * 
	 * @param bm
	 * @param file
	 * @param quantity 	0-100
	 * @throws IOException
	 */

	public static void saveBmpToSd(Bitmap bm, File file, int quantity) throws IOException {
		file.createNewFile();
		OutputStream outStream = new FileOutputStream(file);
		bm.compress(CompressFormat.JPEG, quantity, outStream);
		outStream.flush();
		outStream.close();

	}
	public static byte[] compress(Bitmap bitmap, CompressFormat format, int quality) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(format, quality, os);
		byte[] content = os.toByteArray();
		return content;
	}
	/** 
	 * 获取圆角位图的方法 
	 * @param bitmap 需要转化成圆角的位图 
	 * @param pixels 圆角的度数，数值越大，圆角越大 
	 * @return 处理后的圆角位图 
	 */  
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		if (null == bitmap)
			return null;
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Config.ARGB_8888);  
	    Canvas canvas = new Canvas(output);  
	    final int color = 0xff424242;  
	    final Paint paint = new Paint();  
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
	    final RectF rectF = new RectF(rect);  
	    final float roundPx = pixels;  
	    paint.setAntiAlias(true);  
	    canvas.drawARGB(0, 0, 0, 0);  
	    paint.setColor(color);  
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
	    canvas.drawBitmap(bitmap, rect, rect, paint);  
	    return output;  
	}



	public static int getBitmapWidth(Context context, int resourceId) {

		BitmapFactory.Options outOptions = new BitmapFactory.Options();

		// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中�?
		outOptions.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(context.getResources(), resourceId, outOptions);
		return outOptions.outWidth;

	}

	/**
	 * 获取本地图片的宽度
	 * @param path
	 * @return
     */
	public static int getBitmapWidth(String path) {

		BitmapFactory.Options outOptions = new BitmapFactory.Options();

		// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中�?
		outOptions.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, outOptions);
		return outOptions.outWidth;

	}

	/**
	 * 根据File获取bitmap
	 * @param f
	 * @return Bitmap
     */
	public static Bitmap getBitmapFromFile(File f) {
		return getBitmapForLocal(f.getAbsolutePath(), 300, 300);
	}

	/**
	 * 根据本地地址获取bitmap
	 * @param imgeUrl
	 * @param w
	 * @param h
     * @return Bitmap
     */
	public static Bitmap getBitmapForLocal(String imgeUrl,int w,int h) {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(imgeUrl))return null;
		File file = new File(imgeUrl);
		if (file.exists()) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imgeUrl, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, w* h);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap bmp = BitmapFactory.decodeFile(imgeUrl, opts);
				return bmp;
			} catch (OutOfMemoryError err) {
			}
		}
		return null;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 根据宽度从本地图片路径获取该图片的缩略图
	 *
	 * @param localImagePath
	 *            本地图片的路�?
	 * @param width
	 *            缩略图的�?
	 * @param addedScaling
	 *            额外可以加的缩放比例
	 * @return bitmap 指定宽高的缩略图
	 */
	public static Bitmap getBitmapPreview(String localImagePath, int width, int addedScaling) {
		if (TextUtils.isEmpty(localImagePath)) {
			return null;
		}

		Bitmap temBitmap = null;

		try {
			BitmapFactory.Options outOptions = new BitmapFactory.Options();

			// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中�?
			outOptions.inJustDecodeBounds = true;

			// 加载获取图片的宽�?
			BitmapFactory.decodeFile(localImagePath, outOptions);

			int height = outOptions.outHeight;

			if (outOptions.outWidth > width) {
				// 根据宽设置缩放比�?
				outOptions.inSampleSize = outOptions.outWidth / width + 1
						+ addedScaling;
				outOptions.outWidth = width;

				// 计算缩放后的高度
				height = outOptions.outHeight / outOptions.inSampleSize;
				outOptions.outHeight = height;
			}

			// 重新设置该属性为false，加载图片返�?
			outOptions.inJustDecodeBounds = false;
			outOptions.inPurgeable = true;
			outOptions.inInputShareable = true;
			temBitmap = BitmapFactory.decodeFile(localImagePath, outOptions);

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return temBitmap;
	}

	/**
	 * 图片旋转角度
	 * 
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @param maxWH
	 *            当宽度 或者高度超过设定的值 那么缩放
	 * @return
	 */

	public static Bitmap rotaingImageView(int angle, Bitmap bitmap, int maxWH) {
		if (bitmap == null || bitmap.isRecycled()) {
			return null;
		}

		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		if (width > maxWH) {
			width = maxWH;
			height = height * maxWH / bitmap.getWidth();
		}

		if (height > maxWH) {
			width = width * maxWH / height;

			height = maxWH;

		}

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return resizedBitmap;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		if (bm == null)
			return null;
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	public static Bitmap getBitmapPreview(String localImagePath, int width,
			int height, int addedScaling) {
		if (TextUtils.isEmpty(localImagePath)) {
			return null;
		}

		Bitmap temBitmap = null;

		try {
			BitmapFactory.Options outOptions = new BitmapFactory.Options();

			// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中�?
			outOptions.inJustDecodeBounds = true;

			// 加载获取图片的宽�?
			BitmapFactory.decodeFile(localImagePath, outOptions);

			int outHeight = outOptions.outHeight;
			int outWidth = outOptions.outWidth;

			if (outHeight > height || outWidth > width) {
				float scale = (float) outWidth / width;
				float scale2 = (float) outHeight / height;
				if (scale < scale2) {
					// 根据宽设置缩放比�?
					outOptions.inSampleSize = outOptions.outWidth / width + 1
							+ addedScaling;
					outOptions.outWidth = width;
					// 计算缩放后的高度
					height = outOptions.outHeight / outOptions.inSampleSize;
					outOptions.outHeight = height;

				} else {
					// 根据宽设置缩放比�?
					outOptions.inSampleSize = outOptions.outHeight / height + 1
							+ addedScaling;
					outOptions.outHeight = height;
					// 计算缩放后的宽度
					width = outOptions.outWidth / outOptions.inSampleSize;
					outOptions.outWidth = width;
				}
			}

			// 重新设置该属性为false，加载图片返�?
			outOptions.inJustDecodeBounds = false;
			outOptions.inPurgeable = true;
			outOptions.inInputShareable = true;
			temBitmap = BitmapFactory.decodeFile(localImagePath, outOptions);

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return temBitmap;
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		if(bitmap == null) return null;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static int getBitmapMinWidth(String path) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		/**
		 * 最关键在此，把options.inJustDecodeBounds = true;
		 * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
		 */
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opt); // 此时返回的bitmap为null
		return opt.outWidth < opt.outHeight ? opt.outWidth : opt.outHeight;
	}

	public static byte[] convertBitmapToBytes(Bitmap bitmap) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
			bitmap.copyPixelsToBuffer(buffer);
			return buffer.array();
		} else {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] data = baos.toByteArray();
			return data;
		}
	}

	/**
	 * 加水印 也可以加文字
	 * @param src
	 * @param watermark
	 * @param title
	 * @return
	 */
	private static Bitmap newb;
	public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark,String title) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		//需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
		// mBitmap 为全局变量
		if (newb != null && !newb.isRecycled())
		{
			newb.recycle();
			newb = null;
		}
		newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图

		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		Paint paint=new Paint();
		//加入图片
		if (watermark != null) {
			int ww = watermark.getWidth();
			int wh = watermark.getHeight();
			paint.setAlpha(50);
			cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);// 在src的右下角画入水印
//            cv.drawBitmap(watermark, 0, 0, paint);// 在src的左上角画入水印
		}else{
//        Log.i("i", "water mark failed");
		}
		//加入文字
		if(title!=null)
		{
			String familyName ="宋体";
			Typeface font = Typeface.create(familyName,Typeface.NORMAL);
			TextPaint textPaint=new TextPaint();
			textPaint.setColor(Color.RED);
			textPaint.setTypeface(font);
			textPaint.setTextSize(40);
			//这里是自动换行的
//            StaticLayout layout = new StaticLayout(title,textPaint,w,Alignment.ALIGN_OPPOSITE,1.0F,0.0F,true);
//            layout.draw(cv);
			//文字就加左上角算了
			cv.drawText(title,w-400,h-40,textPaint);
		}
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储
		return newb;
	}
}
