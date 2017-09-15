package cn.vko.ring.circle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.circle.event.Event;
import cn.vko.ring.circle.event.EventManager;

import cn.vko.ring.circle.event.IEventListener;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.utils.WebViewUtil;

public class GroupListActivity extends BaseActivity implements
		IEventListener<GroupInfo> {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.webview)
	public BridgeWebView mWebView;
	private String url;

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
		new WebViewUtil(this, mWebView, "GroupListAtcivity");
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// mWebView.clearCache(this);
		EventManager.unRegister(this);
		super.onDestroy();
	}

	@Override
	public void event(Event<GroupInfo> event) {
		// TODO Auto-generated method stub
		if (mWebView == null || event == null)
			// 局部刷新群详情
			mWebView.callHandler("refreshGroupPage", "data from java",
					new CallBackFunction() {
						@Override
						public void onCallBack(String data) {

						}
					});
	}

	@Override
	public int getFilter() {
		// TODO Auto-generated method stub
		return Event.DETEIL_EVENT | Event.QUIT_EVENT|Event.JOIN_EVENT;
	}
}
