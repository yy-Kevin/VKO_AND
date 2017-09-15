package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.utils.WebViewUtil;

/**
 * 提现结果页
 * @author shikh
 * @time 16/9/27 下午8:11
 */
public class TxBrgOverActivity extends BaseActivity {

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
        tvTitle.setText("提现到");
        tvRight.setText("收入规则");
        tvRight.setVisibility(View.VISIBLE);
        initWebView();
    }

    private void initWebView() {
        new WebViewUtil(this, mWebView, "TxBrgActivity");
        //确定
        mWebView.registerHandler("txHomeBrg", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null) {
                    EventBus.getDefault().post(Constants.TX_OVER);
                    finish();
                }
            }
        });

    }

    @Override
    public void initData() {
        String url = getIntent().getExtras().getString("URL");
        int payType = getIntent().getExtras().getInt("PAYTYPE");
        Map<Integer,String> map = new HashMap<>();
        map.put(3,"支付宝");
        map.put(2,"微信");
        map.put(1,"银行卡");
        tvTitle.setText("提现到"+map.get(payType));
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
}
