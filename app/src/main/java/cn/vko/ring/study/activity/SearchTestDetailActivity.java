package cn.vko.ring.study.activity;

import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.utils.WebViewUtil;


/**
 * 搜索试题详情页
 * @author Administrator
 * 
 */
public class SearchTestDetailActivity extends BaseActivity {
	
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.webview)
	public BridgeWebView mWebView;
	
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_advert;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		new WebViewUtil(this, mWebView, "SearchTestDetailActivity");
		tvTitle.setText("搜题详情");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		String url = getIntent().getExtras().getString("URL");
		mWebView.loadUrl(url);
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

	@OnClick(R.id.iv_back)
	void sayBack() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			finish();
		}
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
