package cn.vko.ring.circle.activity;

import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import butterknife.Bind;
import butterknife.OnClick;

import cn.vko.ring.R;
import cn.vko.ring.circle.event.EventManager;
import cn.vko.ring.circle.event.ITopicEventListener;
import cn.vko.ring.circle.event.TopicEvent;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.utils.WebViewUtil;

public class RankListActivity extends BaseActivity implements
		ITopicEventListener<Integer> {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.webview)
	public BridgeWebView mWebView;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.layout_html;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		EventManager.register(this);
		initWebView();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		String url = getIntent().getExtras().getString("URL");
		String title = getIntent().getExtras().getString("TITLE");
		tvTitle.setText(title);
		mWebView.loadUrl(url);
	}

	private void initWebView() {
		// TODO Auto-generated method stub
		new WebViewUtil(this, mWebView, "RankListActivity");
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventManager.unRegister(this);
//		mWebView.clearCache(this);
		mWebView.releaseAllWebViewCallback();
		super.onDestroy();
	}

	@Override
	public void event(TopicEvent<Integer> event) {
		// TODO Auto-generated method stub
		if (mWebView == null || event == null)
			return;
		// 局部刷新文章列表
		mWebView.callHandler("refreshArticle", "data from java",
				new CallBackFunction() {
					@Override
					public void onCallBack(String data) {

					}
				});
	}

	@Override
	public int getFilter() {
		// TODO Auto-generated method stub
		return TopicEvent.DETEIL_TOPIC_EVENT | TopicEvent.SUBMIT_TOPIC_EVENT;
	}

}
