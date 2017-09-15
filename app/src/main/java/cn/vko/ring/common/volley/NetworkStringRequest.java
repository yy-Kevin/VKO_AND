package cn.vko.ring.common.volley;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.VkoApplication;
import cn.vko.ring.VkoContext;
import cn.vko.ring.utils.FileUtil;

/**
 * Created by shikh on 2016/4/28.
 */
public class NetworkStringRequest extends StringRequest {
    private Map<String, String> mParams = new HashMap<String, String>();
    public NetworkStringRequest(int method, String url,Map<String,String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(params != null){
            setParams(params);
        }
        setShouldCache(true);
       /* setHttpCookie("userId=" + VkoContext.getInstance(context).getUserId() + ";channel_name="
                + VkoApplication.getInstance().getUMController() + ";version_name="
                + ViewUtils.getVersion(ctx));*/
    }
    public NetworkStringRequest(Uri.Builder builder, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(builder.toString(), listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        setShouldCache(true);
    }

    private void setParams(Map<String,String> map){
        try{
            if(getParams() != null){
                getParams().putAll(map);
            }
        }catch(Exception e){

        }
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        // TODO Auto-generated method stub
        return mParams;
    }
    public void setHttpCookie(String cookie) {
        try {
            getHeaders().put("cookie", cookie);
        } catch (AuthFailureError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private Map mHeaders = new HashMap(1);
    @Override
    public Map getHeaders() throws AuthFailureError {
//		mHeaders.put("http.keepAlive", "false");
//		Map<String,String> mHeaders = new HashMap<String, String>();
//		mHeaders.put("Content-Type", "application/json; charset=utf-8");
        return mHeaders;
    }
}
