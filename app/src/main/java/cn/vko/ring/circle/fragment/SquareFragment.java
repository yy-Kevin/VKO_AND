package cn.vko.ring.circle.fragment;


import butterknife.Bind;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseFragment;

public class SquareFragment extends BaseFragment {
	
	@Bind(R.id.webview)
	public BridgeWebView mWebView;
	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_webview;
	}
	@Override
	public void initView(View root) {
		// TODO Auto-generated method stub
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
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		mWebView.loadUrl("http://m.vko.cn/group/square.html");
	}
}
