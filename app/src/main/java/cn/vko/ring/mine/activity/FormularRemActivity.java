package cn.vko.ring.mine.activity;


import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.mine.model.MessageInfo;


/** 
 * @ClassName: FormularRemActivity 
 * @Description: 公式速记
 * @author: JiaRH
 * @date: 2015-12-10 下午5:44:16  
 */
public class FormularRemActivity extends BaseActivity {

	@Bind(R.id.webview)
	public BridgeWebView mWebView;
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.iv_back)
	ImageView back;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_formular;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDatabaseEnabled(true);
		mWebView.requestFocusFromTouch();
//		mWebView.setWebViewClient(new WebViewClient() {
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				view.loadUrl(url);
//				return true;
//			}
//		});
		tvTitle.setText(R.string._formular);
	}

	@Override
	public void initData() {
		mWebView.loadUrl(ConstantUrl.VK_FORMULAR_REMEMBER);
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		}else{
			finish();
		}
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
