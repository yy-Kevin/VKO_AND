package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
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


public class ScoreActivity extends BaseActivity {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.webview)
	public BridgeWebView mWebView;

	private final static String URL = "http://m.vko.cn/member/scoreRecord.html?version2=2.0.9";

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
		tvTitle.setText(getResources().getString(R.string.i_task));
	}

	private void initWebView() {
		// TODO Auto-generated method stub
		mWebView.setDefaultHandler(new DefaultHandler());

		WebViewUtil.synCookies(ScoreActivity.this,
				"http://vko.cn",
				"vkoweb="
						+ VkoContext.getInstance(ScoreActivity.this).getToken()
						+ ";domain=.vko.cn");
		WebViewUtil.synCookies(ScoreActivity.this,
				"http://vko.cn",
				"userId="
						+ VkoContext.getInstance(ScoreActivity.this)
								.getUserId() + ";domain=.vko.cn");
		// 注册一个监听 当js 方法触发时调用
		mWebView.registerHandler("scoreRule", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				// System.out.println("data" + data);
				// Toast.makeText(ScoreActivity.this, data, Toast.LENGTH_SHORT)
				// .show();
				StartActivityUtil.startActivity(ScoreActivity.this,
						ScoreRuleActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK);
			}

		});
		mWebView.registerHandler("toLogin", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				// 清除webView cookiet
				WebViewUtil.clearCache(ScoreActivity.this);
				StartActivityUtil.startActivity(ScoreActivity.this,
						LoginActivity.class);
				overridePendingTransition(R.anim.bottom_in, 0);
			}

		});
		mWebView.registerHandler("toExchange", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if(!TextUtils.isEmpty(data)){
					JSONObject json = JSONObject.parseObject(data);
					String title = json.getString("title");
					String url = json.getString("url");
					Bundle bundle = new Bundle();
					bundle.putString("title", title);
					bundle.putString("url", url);
					StartActivityUtil.startForResult(ScoreActivity.this,
							RedeemCodeActivity.class,bundle, 100);
				}
			}

		});
		
		mWebView.loadUrl(URL);

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if(arg0 == 100 && arg1 == RESULT_OK){
			mWebView.callHandler("refreshUserScore",
					"data from java", new CallBackFunction() {
						@Override
						public void onCallBack(String data) {

						}
					});
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mWebView != null) {
			mWebView.clearCache(ScoreActivity.this);
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
