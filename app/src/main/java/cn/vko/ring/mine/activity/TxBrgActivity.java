package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.utils.WebViewUtil;

/**
 * 提现
 * @author shikh
 * @time 16/9/27 下午8:11
 */
public class TxBrgActivity extends BaseActivity {

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
        tvTitle.setText("提现");
        tvRight.setText("收入规则");
        tvRight.setVisibility(View.VISIBLE);
        initWebView();
    }

    private void initWebView() {
        new WebViewUtil(this, mWebView, "TxBrgActivity");
        //提现
        mWebView.registerHandler("txBtnBrg", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null) {
                    try {
                        JSONObject json = JSONObject.parseObject(data);
                        int payType = json.getIntValue("payType");
                        String url = json.getString("url");
                        Bundle b = new Bundle();
                        b.putString("URL",url);
                        b.putInt("PAYTYPE",payType);
                        StartActivityUtil.startActivity(TxBrgActivity.this,TxBrgOverActivity.class,b, Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    } catch (Exception e) {

                    }
                }
            }
        });

    }

    @Override
    public void initData() {
        String url = getIntent().getExtras().getString("URL");
        mWebView.loadUrl(url);
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
        finish();
    }
    @OnClick(R.id.tv_right)
    void sayRule() {
        StartActivityUtil.startActivity(this,RuleActivity.class);
    }
    @Subscribe
    public void onEventMainThread(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (msg.equals(Constants.TX_OVER)) {
                finish();
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
}
