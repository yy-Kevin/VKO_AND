package cn.vko.ring.circle.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.utils.WebViewUtil;

public class ScanDisplyAtcivity extends BaseActivity {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.webview)
	public BridgeWebView mWebView;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		return R.layout.layout_html;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		initWebView();
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		String url = getIntent().getExtras().getString("URL");
		mWebView.loadUrl(url);
	}

	private void initWebView() {
		// TODO Auto-generated method stub		
		new WebViewUtil(this, mWebView, "ScanDisplyAtcivity");
		WebChromeClient wvcc = new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				tvTitle.setText(title);
			}

		};
		// 设置setWebChromeClient对象
		mWebView.setWebChromeClient(wvcc);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			mWebView.getClass().getMethod("onResume")
					.invoke(mWebView, (Object[]) null);
			if (VkoContext.getInstance(this).isLogin()) {
				WebViewUtil.synCookies(this, "http://vko.cn",
						"vkoweb=" + VkoContext.getInstance(this).getToken()
								+ ";domain=.vko.cn");
				WebViewUtil.synCookies(this, "http://vko.cn",
						"userId=" + VkoContext.getInstance(this).getUserId()
								+ ";domain=.vko.cn");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mWebView.releaseAllWebViewCallback();
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	@Subscribe
	public void onEventMainThread(String event) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(event) && event.equals(cn.vko.ring.Constants.PAYMENT_COMPLETE)) {
			mWebView.callHandler("refreshOrderInfo",
					"data from java", new CallBackFunction() {
						@Override
						public void onCallBack(String data) {

						}
					});
		}
	}
}
