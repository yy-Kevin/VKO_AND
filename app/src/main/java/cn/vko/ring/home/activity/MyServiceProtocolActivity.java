package cn.vko.ring.home.activity;

import cn.vko.ring.R;
import cn.vko.ring.R.id;
import cn.vko.ring.R.layout;
import cn.vko.ring.common.base.BaseActivity;
import butterknife.Bind;
import butterknife.OnClick;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class MyServiceProtocolActivity extends BaseActivity {
	@Bind(id.wv)
	public WebView mWebView;
	@Bind(id.tv_title)
	public TextView tvTitle;
	@Override
	public int setContentViewResId() {
		return layout.activity_my_service_protocol;
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
		tvTitle.setText(R.string.service_protocol);
	}
	@OnClick(id.iv_back)
	void sayBack(){
		finish();
	}
	@Override
	public void initData() {
//		mWebView.loadUrl("http://m.vko.cn/mianzeapp.html");
		mWebView.loadUrl("file:///android_asset/deal.html");
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
