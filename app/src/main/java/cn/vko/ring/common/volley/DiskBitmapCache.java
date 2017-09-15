package cn.vko.ring.common.volley;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import cn.shikh.utils.BitmapUtils;

/**
 * @author shikh
 * 
 */
public class DiskBitmapCache extends DiskBasedCache implements ImageCache {

	public DiskBitmapCache(File rootDirectory, int maxCacheSizeInBytes) {

		super(rootDirectory, maxCacheSizeInBytes);
	}

	public DiskBitmapCache(File cacheDir) {

		super(cacheDir);
	}

	public Bitmap getBitmap(String url) {

		final Entry requestedItem = get(url);

		if (requestedItem == null)
			return null;
		try {
			return BitmapFactory.decodeByteArray(requestedItem.data, 0, requestedItem.data.length);
		} catch (OutOfMemoryError error) {

		}
		return null;
	}

	public void putBitmap(String url, Bitmap bitmap) {

		final Entry entry = new Entry();

		/*
		 * //Down size the bitmap.If not done, OutofMemoryError occurs while
		 * decoding large bitmaps. // If w & h is set during image request (
		 * using ImageLoader ) then this is not required. ByteArrayOutputStream
		 * baos = new ByteArrayOutputStream(); Bitmap downSized =
		 * BitmapUtil.downSizeBitmap(bitmap, 50);
		 * 
		 * downSized.compress(Bitmap.CompressFormat.JPEG, 100, baos); byte[]
		 * data = baos.toByteArray();
		 * 
		 * System.out.println("####### Size of bitmap is ######### "+url+" : "+data
		 * .length); entry.data = data ;
		 */

		entry.data = BitmapUtils.convertBitmapToBytes(bitmap);
		put(url, entry);
	}
}
