package cn.vko.ring.common.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import cn.vko.ring.utils.FileUtil;

/**
 * Created by Administrator on 2016/4/27.
 */
public class VolleyQueueController {
    private RequestQueue mVolleyQueue;
    private static VolleyQueueController ourInstance;
    private ImageLoader mImageLoader;
    public static VolleyQueueController getInstance() {
        if(ourInstance == null){
            synchronized (VolleyQueueController.class){
                if(ourInstance == null){
                    ourInstance = new VolleyQueueController();
                }
            }
        }
        return ourInstance;
    }

    private VolleyQueueController() {
    }

    public RequestQueue getRequestQueue(Context context){
        if (mVolleyQueue == null) {
            mVolleyQueue = Volley.newRequestQueue(context);
        }
        return mVolleyQueue;
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(mVolleyQueue, new DiskBitmapCache(FileUtil.getCacheDir()));
        }
        return mImageLoader;
    }
}
