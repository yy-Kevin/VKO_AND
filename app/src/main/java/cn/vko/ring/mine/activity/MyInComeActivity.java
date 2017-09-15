package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.widget.dialog.CommonDialog;
import cn.vko.ring.utils.WebViewUtil;

/**
 * 我的收入
 * @author shikh
 * @time 16/9/27 下午8:12
 */
public class MyInComeActivity extends BaseActivity {

    @Bind(R.id.webview)
    public BridgeWebView mWebView;
    @Bind(R.id.tv_title)
    public TextView tvTitle;
    @Bind(R.id.tv_right)
    public TextView tvRight;
    @Override
    public int setContentViewResId() {
        return R.layout.activity_my_in_come;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
//        tvTitle.setText("我的收入");
        tvRight.setText("收入规则");
        tvRight.setVisibility(View.GONE);
        initWebView();

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.e("=====走了","换title");
                tvTitle.setText(title);
            }
        });
    }




    private void initWebView() {

        new WebViewUtil(this, mWebView, "MyInComeActivity");

        //提现
//        mWebView.registerHandler("toTxBrg", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                Log.e("=======提现data值>",data);
//                if (data != null) {
//                    try {
//                        JSONObject json = JSONObject.parseObject(data);
//                        String url = json.getString("url");
//                        Bundle b = new Bundle();
//                        b.putString("URL",url);
//                        StartActivityUtil.startActivity(MyInComeActivity.this,TxBrgActivity.class,b, Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    } catch (Exception e) {
//
//                    }
//                }
//            }
//        });
//
//        //明细
//        mWebView.registerHandler("toTxDetailBrg", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                Log.e("=======明细data值>",data);
//                if (data != null) {
//                    try {
//                        JSONObject json = JSONObject.parseObject(data);
//                        String url = json.getString("url");
//                        Bundle b = new Bundle();
//                        b.putString("URL",url);
//                        StartActivityUtil.startActivity(MyInComeActivity.this,TxBrgDetailActivity.class,b, Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    } catch (Exception e) {
//
//                    }
//                }
//            }
//        });

    }

    @Override
    public void initData() {
        mWebView.loadUrl("http://static.vko.cn/m/tixian/myIncome.html");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (VkoContext.getInstance(this).isLogin()) {
            WebViewUtil.synCookies(this, "http://vko.cn",
                    "vkoweb=" + VkoContext.getInstance(this).getToken()
                            + ";domain=.vko.cn");
            WebViewUtil.synCookies(this, "http://vko.cn",
                    "userId=" + VkoContext.getInstance(this).getUserId()
                            + ";domain=.vko.cn");
        }
    }
    @OnClick(R.id.iv_back)
    void sayBack() {
        if ( mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            mWebView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    Log.e("=====走了","换title");
                    tvTitle.setText(title);
                }
            });
        }else {
            finish();
        }
    }

    @OnClick(R.id.tv_right)
    void sayRule() {
        StartActivityUtil.startActivity(this,RuleActivity.class);
    }

    @Subscribe
    public void onEventMainThread(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (msg.equals(Constants.TX_OVER)) {
                // 局部刷新
                mWebView.callHandler("refreshCash", "data from java",
                        new CallBackFunction() {
                            @Override
                            public void onCallBack(String data) {

                            }
                        });
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        EventBus.getDefault().unregister(this);
        mWebView.releaseAllWebViewCallback();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            mWebView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    Log.e("=====走了","换title");
                    tvTitle.setText(title);
                }
            });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
