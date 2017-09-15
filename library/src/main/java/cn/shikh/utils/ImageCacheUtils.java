package cn.shikh.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.webkit.URLUtil;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.shikh.utils.okhttp.OkHttpUtils;
import cn.shikh.utils.okhttp.callback.BitmapCallback;
import okhttp3.Call;


/**
 * 图片缓存管理
 * @author Administrator
 * 
 */
public class ImageCacheUtils {
	private ImageMemoryCache imageMemoryCache =null;
	private File cacheDir = FileUtils.createSDDir("cache-dir");
	private  List<String> downloadUrls = new ArrayList<String>();

	private static ImageCacheUtils mInstance;
	public static ImageCacheUtils getInstance(){
		if(mInstance == null){
			synchronized (ImageCacheUtils.class){
				mInstance = new ImageCacheUtils();
			}
		}
		return mInstance;
	}
	private ImageCacheUtils() {

	}
	//在application 中先初始化
	public void init(Context context,File cacheDir){
		this.cacheDir = cacheDir;
		imageMemoryCache = new ImageMemoryCache(context);
	}
	public  Bitmap loadImage(final String url, final BitmapCallback callback, final int w, final int h){
			Bitmap bitmap = getBitmapFromCache(url);
			if(bitmap != null){
				if(callback != null){
					callback.onResponse(bitmap);
				}
				return bitmap;
			}

		if (!downloadUrls.contains(url)) {
			downloadUrls.add(url);
			ThreadPools.execute(new DownloadTask(url, new BitmapCallback(w,h) {
				@Override
				public void onError(Call call, Exception e) {
					if(callback != null){
						callback.onError(call,e);
					}
					downloadUrls.remove(url);
				}

				@Override
				public void onResponse(Bitmap bitmap) {
					if(null!=bitmap){
						addBitmapToCache(url,bitmap);
						//将图片存入本地
						try {
							String imgePath = DownloadUtils.getInstance().getDownloadDesk(url, null);
							BitmapUtils.saveBmpToSd(bitmap,new File(cacheDir,imgePath),100);
						} catch (IOException e) {
							e.printStackTrace();
						}
						if(callback != null){
							callback.onResponse(bitmap);
						}
					}

				}

				@Override
				public void inProgress(float progress) {
					super.inProgress(progress);
					if(callback != null){
						callback.inProgress(progress);
					}
				}
			}));
		}
		return null;
	}
	
	public  Bitmap loadImage(String url,BitmapCallback callback){return loadImage(url,callback,0,0);
		}


	 class DownloadTask implements Runnable{
		private cn.shikh.utils.okhttp.callback.BitmapCallback callback;
		private String uri;
		public DownloadTask(String uri,BitmapCallback callback){
			this.callback = callback;
			this.uri=uri;
		}
		@Override
		public void run() {
			OkHttpUtils.get().url(uri).build().execute(callback);
		}
	}

	/**
	 * 从磁盘加载图片 如果图片存在 加入到缓存
	 */
	public  Bitmap getBitmapFromDesk(String desk, String key) {
		Bitmap bitmap = imageMemoryCache.getBitmapFromCache(key);
		if (null != bitmap)
			return bitmap;
		File fileDesk = new File(desk);
		if (fileDesk.exists()) {
			bitmap = BitmapUtils.getBitmapFromFile(fileDesk);
			if (null != bitmap) {
				imageMemoryCache.addBitmapToCache(key, bitmap);
			}
			return bitmap;
		}
		return null;
	}


	public  Bitmap getBitmapFromCache(String imgeUrl) {
		Bitmap bitmap = imageMemoryCache.getBitmapFromCache(imgeUrl);
		if (null != bitmap)
			return bitmap;
		File fileDesk;
		if(URLUtil.isNetworkUrl(imgeUrl)){
			String imgePath = DownloadUtils.getInstance().getDownloadDesk(imgeUrl, null);
			fileDesk = new File(cacheDir,imgePath);
		}else{
			fileDesk = new File(imgeUrl);
		}
		if (fileDesk.exists()) {
			bitmap = BitmapUtils.getBitmapFromFile(fileDesk);
			if (null != bitmap) {
				imageMemoryCache.addBitmapToCache(imgeUrl, bitmap);
			}
			return bitmap;
		}
		return null;
	}

	public  void addBitmapToCache(String key, Bitmap bitmap) {
		imageMemoryCache.addBitmapToCache(key, bitmap);
	}

	public  void removeCache(String key) {
		imageMemoryCache.remove(key);
	}

	public  void clear() {
		imageMemoryCache.clearCache();
	}

	 class ImageMemoryCache {
		private static final int SOFT_CACHE_SIZE = 15; // 软引用缓存容量
		private  LruCache<String, Bitmap> mLruCache; // 硬引用缓存
		private  LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache; // 软引用缓存

		public ImageMemoryCache(Context context) {
			int memClass = ((ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			int cacheSize = 1024 * 1024 * memClass / 4; // 硬引用缓存容量，为系统可用内存的1/4
			mLruCache = new LruCache<String, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					if (value != null)
						return value.getRowBytes() * value.getHeight();
					else
						return 0;
				}

				@Override
				protected void entryRemoved(boolean evicted, String key,
						Bitmap oldValue, Bitmap newValue) {
					if (oldValue != null)
						// 硬引用缓存容量满的时候，会根据LRU算法把最近没有被使用的图片转入此软引用缓存
						mSoftCache
								.put(key, new SoftReference<Bitmap>(oldValue));
				}
			};
			mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(
					SOFT_CACHE_SIZE, 0.75f, true) {
				private static final long serialVersionUID = 6040103833179403725L;

				@Override
				protected boolean removeEldestEntry(
						Entry<String, SoftReference<Bitmap>> eldest) {
					if (size() > SOFT_CACHE_SIZE) {
						return true;
					}
					return false;
				}
			};
		}

		/**
		 * 从缓存中获取图片
		 */
		public Bitmap getBitmapFromCache(String url) {
			if(url == null) return null;
			Bitmap bitmap;
			// 先从硬引用缓存中获取
			synchronized (mLruCache) {
				bitmap = mLruCache.get(url);
				if (bitmap != null) {
					// 如果找到的话，把元素移到LinkedHashMap的最前面，从而保证在LRU算法中是最后被删除
					mLruCache.remove(url);
					mLruCache.put(url, bitmap);
					return bitmap;
				}
			}
			// 如果硬引用缓存中找不到，到软引用缓存中找
			synchronized (mSoftCache) {
				SoftReference<Bitmap> bitmapReference = mSoftCache.get(url);
				if (bitmapReference != null) {
					bitmap = bitmapReference.get();
					if (bitmap != null) {
						// 将图片移回硬缓存
						mLruCache.put(url, bitmap);
						mSoftCache.remove(url);
						return bitmap;
					} else {
						mSoftCache.remove(url);
					}
				}
			}
			return null;
		}

		public void remove(String url) {
			mLruCache.remove(url);
			mSoftCache.remove(url);
		}

		/**
		 * 添加图片到缓存
		 */
		public void addBitmapToCache(String url, Bitmap bitmap) {
			if (bitmap != null && url != null) {
				synchronized (mLruCache) {
					mLruCache.put(url, bitmap);
				}
			}
		}

		public void clearCache() {
			mSoftCache.clear();
		}
	}

}
