package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.home.activity.LoginActivity;
import cn.vko.ring.utils.WebViewUtil;
/**
 * 兑换码
 * @author Administrator
 *
 */
public class RedeemCodeActivity extends BaseActivity {
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

		WebViewUtil.synCookies(RedeemCodeActivity.this,
				"http://vko.cn",
				"vkoweb="
						+ VkoContext.getInstance(RedeemCodeActivity.this).getToken()
						+ ";domain=.vko.cn");
		WebViewUtil.synCookies(RedeemCodeActivity.this,
				"http://vko.cn",
				"userId="
						+ VkoContext.getInstance(RedeemCodeActivity.this)
								.getUserId() + ";domain=.vko.cn");
		
		mWebView.registerHandler("toChangeList", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if(!TextUtils.isEmpty(data)){
					JSONObject json = JSONObject.parseObject(data);
					String title = json.getString("title");
					String url = json.getString("url");
					Bundle bundle = new Bundle();
					bundle.putString("title", title);
					bundle.putString("url", url);
					StartActivityUtil.startActivity(RedeemCodeActivity.this,
							RedeemCodeActivity.class,bundle, Intent.FLAG_ACTIVITY_NEW_TASK);
				}
			}

		});
		mWebView.registerHandler("codeChangeSuccess", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				setResult(RESULT_OK);
				finish();
			}

		});
		
		mWebView.registerHandler("toLogin", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				// 清除webView cookiet
				WebViewUtil.clearCache(RedeemCodeActivity.this);
				StartActivityUtil.startActivity(RedeemCodeActivity.this,
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
			mWebView.clearCache(RedeemCodeActivity.this);
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
