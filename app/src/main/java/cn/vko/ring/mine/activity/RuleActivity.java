package cn.vko.ring.mine.activity;

import android.view.View;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.utils.WebViewUtil;

/**
 * 收入规则
 * @author shikh
 * @time 16/9/27 下午8:11
 */
public class RuleActivity extends BaseActivity {

    @Bind(R.id.webview)
    public BridgeWebView mWebView;
    @Bind(R.id.tv_title)
    public TextView tvTitle;
    @Override
    public int setContentViewResId() {
        return R.layout.activity_my_in_come;
    }

    @Override
    public void initView() {
        tvTitle.setText("详情");
        initWebView();
    }

    private void initWebView() {
        new WebViewUtil(this, mWebView, "RuleActivity");
    }

    @Override
    public void initData() {
        mWebView.loadUrl("http://static.vko.cn/m/tixian/rule.html");
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
}
