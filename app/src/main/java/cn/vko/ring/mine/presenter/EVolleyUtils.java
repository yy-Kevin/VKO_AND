package cn.vko.ring.mine.presenter;

import android.content.Context;

import cn.vko.ring.common.volley.VolleyUtils;

/**
 * desc:
 * Created by jiarh on 16/5/26 18:46.
 */
public class EVolleyUtils  extends VolleyUtils{
    private static final String TAG = "EVolleyUtils";
    public EVolleyUtils(Context context, Class clazz) {
        super(context, clazz);
    }

    @Override
    protected void disposeResponse(String response) {
//        super.disposeResponse(response);
        notifyDataChanged(response);
//        if(!TextUtils.isEmpty(response)){
//            Log.d(TAG, "disposeResponse() called with: " + "response = " + response + "");
//            try {
//                JSONObject json = new JSONObject(response);
//                int code = json.getInt("code");
//                int vb = json.optInt("vb");
//                String msg = json.optString("msg");
//
//                if(code == 0){//成功
//
//                    showRequestMsg(msg);
//
//                } else if(code ==2){//token 过期
//                    notifyErrorHappened("2","会话过期，请重新登录");
//                    startLogin();
//                } else if(code <=100 || code >200){//
//                    notifyErrorHappened(code+"",msg);
//                }else if(code >100){//服务器异常
//                    notifyErrorHappened(code+"","服务器异常");
//                }
//            }catch (Exception e){
//                Log.d(TAG, "Exception() called with: " + "e = [" + e + "]");
//                notifyErrorHappened("402","解析数据异常");
//            }
//
//        }else{
//            notifyErrorHappened("403","获取数据为空");
//        }
    }
}
