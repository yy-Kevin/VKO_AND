package cn.vko.ring.utils;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;

import cn.shikh.utils.BitmapUtils;
import cn.shikh.utils.DownloadUtils;
import cn.shikh.utils.ImageCacheUtils;
import cn.shikh.utils.okhttp.callback.BitmapCallback;
import cn.vko.ring.ConstantUrl;
import okhttp3.Call;

public class ImageUtils {


	public static Bitmap getBitmapForLocal(String imgeUrl, int w, int h) {
		// TODO Auto-generated method stub
		try {
			String imgePath;
			if (URLUtil.isNetworkUrl(imgeUrl)) {
				imgePath = DownloadUtils.getInstance().getDownloadDesk(imgeUrl, null);
			} else {
				imgePath = imgeUrl;
			}
			File file = new File(imgePath);
			if (file.exists()) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(imgePath, opts);
				opts.inSampleSize = BitmapUtils.computeSampleSize(opts, -1, w
						* h);
				// 这里一定要将其设置回false，因为之前我们将其设置成了true
				opts.inJustDecodeBounds = false;
				Bitmap bitmap = BitmapFactory.decodeFile(imgePath, opts);
				ImageCacheUtils.getInstance().addBitmapToCache(imgeUrl, bitmap);
				return bitmap;
			}
		} catch (OutOfMemoryError e) {

		}

		return null;
	}

	/**
	 * 加载缩略图
	 * 
	 * @param shortUri
	 * @param iv
	 * @param w
	 * @param h
	 */
	public static Bitmap loadPerviewHead(final ImageView iv, String shortUri, final int w, final int h) {
		if(TextUtils.isEmpty(shortUri)){
//			view.setImageResource(defaultImageResource);
			return null;
		}
		final String imgeUrl = ConstantUrl.getPerviewUrl(shortUri);
		// 先设置默认图片，再下载
//		view.setImageResource(defaultImageResource);
		Bitmap bm = ImageCacheUtils.getInstance().loadImage(imgeUrl, new BitmapCallback() {

			@Override
			public void onError(Call call, Exception e) {

			}

			@Override
			public void onResponse(Bitmap response) {
				response = BitmapUtils.toRoundCorner(response, 10);
				if(iv != null){
					iv.setImageBitmap(response);
				}
			}
		},w,h);
		if(bm != null){
			bm = BitmapUtils.toRoundCorner(bm, 10);
			// bm = BitmapUtils.toRoundBitmap(bm);
			if(iv != null){
				iv.setImageBitmap(bm);
			}
			return bm;
		}
		return bm;

	}

	/**
	 * 加载缩略图
	 * @param shortUri
	 * @param w
	 * @param h
	 * @param iv
     * @return
     */
	public static Bitmap loadPerviewImage(String shortUri, final int w, final int h, final ImageView iv) {
		if(TextUtils.isEmpty(shortUri)){
//			view.setImageResource(defaultImageResource);
			return null;
		}
		final String imgeUrl = ConstantUrl.getPerviewUrl(shortUri);
		// 先设置默认图片，再下载
//		view.setImageResource(defaultImageResource);
		Bitmap bm = ImageCacheUtils.getInstance().loadImage(imgeUrl, new BitmapCallback() {

			@Override
			public void onError(Call call, Exception e) {

			}

			@Override
			public void onResponse(Bitmap response) {
				if(iv != null && response!= null){
					iv.setImageBitmap(response);
				}

			}
		},w,h);
		if(bm != null){
			iv.setImageBitmap(bm);
			return bm;
		}
		return bm;
	}

	public static Bitmap loadNoWHImage(String shortUri, final ImageView iv) {
		if(TextUtils.isEmpty(shortUri)){
//			view.setImageResource(defaultImageResource);
			return null;
		}
		final String imgeUrl = ConstantUrl.getPerviewUrl(shortUri);
		// 先设置默认图片，再下载
//		view.setImageResource(defaultImageResource);
		Bitmap bm = ImageCacheUtils.getInstance().loadImage(imgeUrl, new BitmapCallback() {

			@Override
			public void onError(Call call, Exception e) {

			}

			@Override
			public void onResponse(Bitmap response) {
				if(iv != null && response!= null){
					iv.setImageBitmap(response);
				}

			}
		});
		if(bm != null){
			iv.setImageBitmap(bm);
			return bm;
		}
		return bm;
	}
}
