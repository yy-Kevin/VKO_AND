package cn.vko.ring.common.volley;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.common.widget.dialog.VbDialog;
import cn.vko.ring.home.activity.LoginActivity;
import cn.vko.ring.utils.ActivityUtilManager;
import cn.vko.ring.utils.WebViewUtil;

/**
 * Created by shikh on 2016/4/27.
 */
public class VolleyUtils<T> extends NetworkStringHelper<T> {

    private Class<T> clazz;
    private static final String TAG = "VolleyUtils";
    public VolleyUtils(Context context,Class<T> clazz) {
        super(context);
        this.clazz = clazz;
    }


    @Override
    protected void disposeVolleyError(VolleyError error) {
        notifyErrorHappened("400","服务器异常");
    }

    @Override
    protected void disposeResponse(String response) {
        if(!TextUtils.isEmpty(response)){
            Log.d(TAG, "disposeResponse() called with: " + "返回response = " + response + "");
            try {
                JSONObject json = new JSONObject(response);
                int code = json.getInt("code");
                int vb = json.optInt("vb");
                String msg = json.optString("msg");
                T t = com.alibaba.fastjson.JSONObject.parseObject(response,clazz);
                if(code == 0){//成功
                    notifyDataChanged(t);
                    showRequestMsg(msg);
                    if(vb >0){//请求的接口有赠V币功能
                        showVbDialog(vb);
                    }
                } else if(code ==2){//token 过期
                    notifyErrorHappened("2","会话过期，请重新登录");
                    startLogin();
                } else if(code <=100 || code >200){//
                    notifyErrorHappened(code+"",msg);
                }else if(code >100){//服务器异常
                    notifyErrorHappened(code+"","服务器异常");
                }
            }catch (Exception e){
                Log.d(TAG, "Exception() called with: " + "e = [" + e + "]");
                notifyErrorHappened("402","解析数据异常");
            }

        }else{
            notifyErrorHappened("403","获取数据为空");
        }
    }

    protected void startLogin() {
        // TODO Auto-generated method stub
        // 清除webView cookiet
        WebViewUtil.clearCache(context);
        StartActivityUtil.startActivity(context, LoginActivity.class);
        try {
            ((Activity) context).overridePendingTransition(R.anim.bottom_in, 0);
        } catch (Exception e) {

        }
    }
    private void showVbDialog(int vb) {
        try{
            if(((Activity)context).isFinishing()){
                Activity act = ActivityUtilManager.getInstance().getTopActivity(context);
                if(act != null ){
                    new VbDialog(act).showDialog(vb);
                }
            }else{
                new VbDialog(context).showDialog(vb);
            }
        }catch(Exception e){

        }

    }
}
