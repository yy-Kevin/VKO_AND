package cn.vko.ring.classgroup.activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.speex.SpeexPlayer;
import cn.vko.ring.utils.WebViewUtil;

public class TaskDetailActivity extends BaseActivity {
    String TAG="TaskDetailActivity";
    @Bind(R.id.tv_title)
    public TextView tvTitle;
    @Bind(R.id.webview)
    public BridgeWebView mWebView;
    private SpeexPlayer mPlay;
    @Override
    public int setContentViewResId() {
        return R.layout.activity_task_detail;
    }

    @Override
    public void initView() {
        new WebViewUtil(this, mWebView, "TaskDetailActivity");
        mWebView.registerHandler("auditPlay", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null) {
                    Log.e(TAG, "data: "+data );
                    JSONObject json = JSONObject.parseObject(data);
                    String auditUrl = json.getString("url");
                    Log.e(TAG, "auditUrl: "+auditUrl );
                    if(!TextUtils.isEmpty(auditUrl)){
                        play(auditUrl);
                    }
                }
            }

        });


    }

    @Override
    public void initData() {
        String url = getIntent().getExtras().getString("URL");
        String title = getIntent().getExtras().getString("TITLE");
        mWebView.loadUrl(url);
        tvTitle.setText(title);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        sayBack();
    }

    @OnClick(R.id.iv_back)
    void sayBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        try {
            mWebView.getClass().getMethod("onPause")
                    .invoke(mWebView, (Object[]) null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 局部刷新群详情
        mWebView.callHandler("stopVedio", "data from java",
                new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {

                    }
                });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try {
            mWebView.getClass().getMethod("onResume")
                    .invoke(mWebView, (Object[]) null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (VkoContext.getInstance(this).isLogin()) {
            WebViewUtil.synCookies(this, "http://vko.cn",
                    "vkoweb=" + VkoContext.getInstance(this).getToken()
                            + ";domain=.vko.cn");
            WebViewUtil.synCookies(this, "http://vko.cn",
                    "userId=" + VkoContext.getInstance(this).getUserId()
                            + ";domain=.vko.cn");
        }
    }
    private void play(String url) {
        if (mPlay == null) {
            mPlay = SpeexPlayer.getInstance(this);
        }
        mPlay.createPlayer(url, null);
        mPlay.startPlay();
    }
}
