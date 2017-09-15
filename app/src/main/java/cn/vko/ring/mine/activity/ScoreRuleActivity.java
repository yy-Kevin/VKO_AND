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


public class ScoreRuleActivity extends BaseActivity {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.webview)
	public BridgeWebView mWebView;

	private final static String URL = "http://m.vko.cn/intro/integral.html";

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_score_rule;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		initWebView();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tvTitle.setText("V币规则");
	}

	@OnClick(R.id.iv_back)
	public void sayBack() {
		finish();
	}

	private void initWebView() {
		// TODO Auto-generated method stub

		mWebView.setDefaultHandler(new DefaultHandler());
		WebViewUtil.synCookies(ScoreRuleActivity.this,
				"http://vko.cn",
				"vkoweb="
						+ VkoContext.getInstance(ScoreRuleActivity.this)
								.getToken() + ";domain=.vko.cn");
		WebViewUtil.synCookies(ScoreRuleActivity.this,
				"http://vko.cn",
				"userId="
						+ VkoContext.getInstance(ScoreRuleActivity.this)
								.getUserId() + ";domain=.vko.cn");
		mWebView.registerHandler("toLogin", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				// 清除webView cookiet
				WebViewUtil.clearCache(ScoreRuleActivity.this);
				StartActivityUtil.startActivity(ScoreRuleActivity.this,
						LoginActivity.class);
				overridePendingTransition(R.anim.bottom_in, 0);
			}

		});
		mWebView.loadUrl(URL);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mWebView != null) {
			mWebView.clearCache(ScoreRuleActivity.this);
		}
	}

}
