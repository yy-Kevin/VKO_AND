package cn.vko.ring.common.volley;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONObject;

import java.util.Map;

import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.utils.FileUtil;

/**
 * Created by Administrator on 2016/4/27.
 */
public abstract class NetworkJsonHelper<T> implements Response.Listener<JSONObject>, Response.ErrorListener {
    public Context context;
    private RequestQueue mVolleyQueue;
    private ImageLoader mImageLoader;
    public RequestQueue getmVolleyQueue() {
        if(mVolleyQueue == null){
            mVolleyQueue = VolleyQueueController.getInstance().getRequestQueue(getContext());
        }
        return mVolleyQueue;
    }


    public NetworkJsonHelper(Context context)
    {
        this.context = context;
    }

    protected Context getContext()
    {
        return context;
    }



    protected NetworkJsonRequest getRequestForGet(Uri.Builder builder)
    {
            return new NetworkJsonRequest(builder, this, this);
    }

    protected NetworkJsonRequest getRequestForPost(String url, Map<String,String> params)
    {
        return new NetworkJsonRequest(Request.Method.POST,url,params, this, this);
    }

    public void sendGETRequest(boolean isShowDialog,Uri.Builder builder)
    {
        if(builder == null)return;
        NetworkJsonRequest request = getRequestForGet(builder);
        sendRequest(isShowDialog,request,builder.toString());
    }

    public void sendPostRequest(boolean isShowDialog, String url,Map<String,String> params)
    {
        if(TextUtils.isEmpty(url))return;
        NetworkJsonRequest request = getRequestForPost( url,params);
        sendRequest(isShowDialog,request,url);
    }

    private void sendRequest(boolean isShowDialog, NetworkJsonRequest request, final String url){
        //防止重复请求
        getmVolleyQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                Log.i("url",request.getUrl()+"---"+url);
                return request.getUrl() == url;
            }
        });
        request.setTag(context);
        getmVolleyQueue().add(request);
    }
    @Override
    public void onErrorResponse(VolleyError error)
    {
        Log.d("Amuro", error.getMessage());
        disposeVolleyError(error);
    }

    protected abstract void disposeVolleyError(VolleyError error);

    @Override
    public void onResponse(JSONObject response)
    {
        Log.d("Amuro", response.toString());
        disposeResponse(response);
    }

    protected abstract void disposeResponse(JSONObject response);

    private UIDataListener<T> uiDataListener;

    public void setUiDataListener(UIDataListener<T> uiDataListener)
    {
        this.uiDataListener = uiDataListener;
    }

    protected void notifyDataChanged(T data)
    {
        if(uiDataListener != null)
        {
            uiDataListener.onDataChanged(data);
        }
    }

    protected void notifyErrorHappened(String errorCode, String errorMessage)
    {
        if(uiDataListener != null)
        {
            uiDataListener.onErrorHappened(errorCode, errorMessage);
        }
    }

    public Uri.Builder getBuilder(String url) {
//        this.url = url;
        Uri.Builder builder = Uri.parse(url).buildUpon();
        return builder;
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(mVolleyQueue, new DiskBitmapCache(FileUtil.getCacheDir()));
        }
        return mImageLoader;
    }
}
