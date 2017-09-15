package cn.vko.ring.study.activity;


import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;

public class ThousandCityActivity extends BaseActivity {
    @Bind(R.id.wv)
    public WebView mWebView;
    @Bind(R.id.tv_title)
    public TextView tvTitle;

    @Override
    public int setContentViewResId() {
        return R.layout.activity_thousand_city;
    }

    @Override
    public void initView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        mWebView.requestFocusFromTouch();
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        tvTitle.setText(R.string.thousands_citsy);
    }

    @OnClick(R.id.iv_back)
    void sayBack(){
        finish();
    }

    @Override
    public void initData() {
        mWebView.loadUrl("http://ad.toutiao.com/tetris/preview_page/?page_id=61900442875");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
