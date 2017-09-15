package cn.vko.ring.common.volley;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by shikh on 2016/4/27.
 */
public class NetworkJsonRequest extends JsonRequest<JSONObject> {

    private Priority mPriority = Priority.HIGH;

    public NetworkJsonRequest(int method, String url, Map<String,String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)
    {
        super(method,url,paramstoString(params), listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        setShouldCache(true);
        setHttpCookie("");
    }

    public NetworkJsonRequest(Uri.Builder builder, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)
    {
        this(Method.GET, builder.toString(), null, listener, errorListener);
    }

    public NetworkJsonRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)
    {
        this(Method.GET, url, null, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(new String(response.data, "UTF-8"));
            return Response.success(jsonObject,
                    HttpHeaderParser.parseCacheHeaders(response));

        }
        catch (Exception e)
        {

            return Response.error(new ParseError(e));

        }
    }

    @Override
    public Priority getPriority()
    {
        return mPriority;
    }

    public void setPriority(Priority priority)
    {
        mPriority = priority;
    }
    public Uri.Builder getBuilder(String url) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        return builder;
    }
//    private static String urlBuilder(String url, List<NameValuePair> params)
//    {
//        return url + "?" + URLEncodedUtils.format(params, "UTF-8");
//    }
    private void setHttpCookie(String cookie) {
        try {
            getHeaders().put("cookie", cookie);
        } catch (AuthFailureError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String paramstoString(Map<String, String> params) {
        if (params != null && params.size() > 0) {
            String paramsEncoding = "UTF-8";
            StringBuilder encodedParams = new StringBuilder();
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    encodedParams.append(URLEncoder.encode(entry.getKey(),
                            paramsEncoding));
                    encodedParams.append('=');
                    encodedParams.append(URLEncoder.encode(entry.getValue(),
                            paramsEncoding));
                    encodedParams.append('&');

                }
                return encodedParams.toString();
            } catch (UnsupportedEncodingException uee) {
                throw new RuntimeException("Encoding not supported: "
                        + paramsEncoding, uee);
            }
        }
        return null;
    }
}
