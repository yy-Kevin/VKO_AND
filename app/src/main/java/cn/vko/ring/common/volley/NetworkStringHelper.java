package cn.vko.ring.common.volley;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;


import java.util.Map;

import cn.shikh.utils.NetUtils;
import cn.shikh.utils.ViewUtils;
import cn.vko.ring.VkoApplication;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.widget.dialog.LoadingDialog;
import cn.vko.ring.utils.FileUtil;

/**
 * Created by shikh on 2016/4/28.
 */
public abstract class NetworkStringHelper<T> implements Response.Listener<String>, Response.ErrorListener {

    public Context context;
    private RequestQueue mVolleyQueue;
    private ImageLoader mImageLoader;
    private LoadingDialog progressDialog;
    private boolean isCancelable = true;
    public void setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }
    public RequestQueue getmVolleyQueue() {
        if(mVolleyQueue == null){
            mVolleyQueue = VolleyQueueController.getInstance().getRequestQueue(getContext());
        }
        return mVolleyQueue;
    }


    public NetworkStringHelper(Context context)
    {
        this.context = context;
    }

    protected Context getContext()
    {
        return context;
    }



    protected NetworkStringRequest getRequestForGet(Uri.Builder builder)
    {
        return new NetworkStringRequest(builder, this, this);
    }

    protected NetworkStringRequest getRequestForPost(String url, Map<String,String> params)
    {
        return new NetworkStringRequest(Request.Method.POST,url,params, this, this);
    }

    public void sendGETRequest(boolean isShowDialog,Uri.Builder builder)
    {
        if(builder == null)return;
        NetworkStringRequest request = getRequestForGet(builder);
        sendRequest(isShowDialog,request,builder.toString());
    }

    public void sendPostRequest(boolean isShowDialog, String url,Map<String,String> params)
    {
        if(TextUtils.isEmpty(url))return;

        NetworkStringRequest request = getRequestForPost( url,params);
        sendRequest(isShowDialog,request,url);
    }

    private void sendRequest(boolean isShowDialog, NetworkStringRequest request, final String url){
        if (!NetUtils.isCheckNetAvailable(context)) {
            notifyErrorHappened("401","网络异常");
//            Log.i("Net",url);
//            return;
        }
        Log.i("Net",url);
        request.setHttpCookie("userId=" + VkoContext.getInstance(context).getUserId() + ";channel_name="
                + VkoApplication.getInstance().getChannel() + ";version_name="
                + ViewUtils.getVersion(context));
        //防止重复请求
                getmVolleyQueue().cancelAll(new RequestQueue.RequestFilter() {
                    @Override
                    public boolean apply(Request<?> request) {
//                        Log.i("url",request.getUrl()+"---"+url);
                        return request.getUrl() == url;
            }
        });
        if(isShowDialog){
            showProgress();
        }
        request.setTag(context);
        getmVolleyQueue().add(request);
    }
    @Override
    public void onErrorResponse(VolleyError error)
    {
        try {
            Log.d("Amuro", error == null?"":error.getMessage());
            stopProgress();
            disposeVolleyError(error);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void disposeVolleyError(VolleyError error);

    @Override
    public void onResponse(String response)
    {
        try {
            Log.d("Amuro", response);
            stopProgress();
            disposeResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void disposeResponse(String response);

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
        //按逻辑应在activity 中处理提示事件，但为配合以前代码只好如此
        showRequestMsg(errorMessage);

        if(uiDataListener != null)
        {
            uiDataListener.onErrorHappened(errorCode, errorMessage);
        }
    }

    public void showRequestMsg(final String msg){
        if(!TextUtils.isEmpty(msg)){
            try{
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                    }
                });
            }catch (Exception e){

            }

        }
    }
    public Uri.Builder getBuilder(String url) {
//        this.url = url;
        Uri.Builder builder = Uri.parse(url).buildUpon();
        return builder;
    }
    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(getmVolleyQueue(), new DiskBitmapCache(FileUtil.getCacheDir()));
        }
        return mImageLoader;
    }

    public void showProgress() {
        if (progressDialog == null && context != null) {
            progressDialog = new LoadingDialog(context);
            progressDialog.setCancelable(isCancelable);
        }
        if (progressDialog != null) {
            try {
                progressDialog.show();
            } catch (Exception e) {

            }
        }
    }

    public void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {

            }

        }
    }
}
