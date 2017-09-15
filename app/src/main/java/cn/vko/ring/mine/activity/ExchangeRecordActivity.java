package cn.vko.ring.mine.activity;

import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

import butterknife.Bind;
import butterknife.OnClick;


import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.home.activity.LoginActivity;
import cn.vko.ring.utils.WebViewUtil;
/**
 * 兑换记录
 * @author Administrator
 *
 */
public class ExchangeRecordActivity extends BaseActivity {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.webview)
	public BridgeWebView mWebView;
	String url;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_score;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		initWebView();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		String title = getIntent().getExtras().getString("title");
		String url = getIntent().getExtras().getString("url");
		tvTitle.setText(title);
		mWebView.loadUrl(url);

	}

	private void initWebView() {
		// TODO Auto-generated method stub
		mWebView.setDefaultHandler(new DefaultHandler());

		WebViewUtil.synCookies(ExchangeRecordActivity.this,
				"http://vko.cn",
				"vkoweb="
						+ VkoContext.getInstance(ExchangeRecordActivity.this).getToken()
						+ ";domain=.vko.cn");
		WebViewUtil.synCookies(ExchangeRecordActivity.this,
				"http://vko.cn",
				"userId="
						+ VkoContext.getInstance(ExchangeRecordActivity.this)
								.getUserId() + ";domain=.vko.cn");
		mWebView.registerHandler("toLogin", new BridgeHandler() {
			@Override
			public void handler(String data, CallBackFunction function) {
				// 清除webView cookiet
				WebViewUtil.clearCache(ExchangeRecordActivity.this);
				StartActivityUtil.startActivity(ExchangeRecordActivity.this,
						LoginActivity.class);
				overridePendingTransition(R.anim.bottom_in, 0);
			}

		});
		
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mWebView != null) {
			mWebView.clearCache(ExchangeRecordActivity.this);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
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
}
