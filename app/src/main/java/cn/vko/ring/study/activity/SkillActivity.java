/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: MyMessage.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.mine.views 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-7-22 上午10:30:00 
 * @version: V1.0   
 */
package cn.vko.ring.study.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;

/**
 * 拍照技巧
 * @ClassName: MyMessage
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-7-22 上午10:30:00
 */
public class SkillActivity extends BaseActivity {
	@Bind(R.id.tv_title)
	TextView tvTitle;
	@Bind(R.id.iv_back)
	ImageView ivGoBack;
	@Bind(R.id.wv_message)
	WebView wv;
	Intent mIntent;
	private String msgUrl;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_my_message;
	}
	/*
	 * @Description: TODO
	 */
	@Override
	public void initView() {
		tvTitle.setText("拍照技巧");
		initWebview();
	}
	
	/*
	 * @Description: TODO
	 */
	@Override
	public void initData() {
		getIntentData();
	}
	public void getIntentData() {
		loadMessage();
	}
	/**
	 * @Title: loadMessage
	 * @Description: TODO
	 * @return: void
	 */
	private void loadMessage() {
		// TODO Auto-generated method stub
		wv.loadUrl("http://m.vko.cn/intro/photo_skill.html");
	}
	private void initWebview() {
		wv.getSettings().setJavaScriptEnabled(true);
		wv.requestFocusFromTouch();
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
			wv.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@OnClick(R.id.iv_back)
	void sayBack(){
		finish();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		EventCountAction.onActivityResumCount(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		EventCountAction.onActivityPauseCount(this);
	}
}
