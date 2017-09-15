package cn.vko.ring.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.umeng.BaseUMShare;
import cn.vko.ring.common.widget.dialog.ShapeDialog;
import cn.vko.ring.local.activity.LocalCourceDetailActivity;
import cn.vko.ring.mine.model.MessageInfo;
import cn.vko.ring.utils.WebViewUtil;

/**
 * @author shikh 推荐消息详情
 */
public class RecommendMsgDetailActivity extends BaseActivity {
	@Bind(R.id.webview)
	public BridgeWebView mWebView;
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_msg_title)
	public TextView tvContentTitle;
	@Bind(R.id.tv_msg_time)
	public TextView tvContentTime;
	@Bind(R.id.tv_msg)
	public TextView tvContent;
	@Bind(R.id.imagebtn)
	ImageView imgShare;
	private String shapeUrl, msgTitle;
	private ShapeDialog shapeDialog;
	private ShareAction shareAction;
	WebViewUtil webViewUtil ;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		return R.layout.activity_recommend_msg_detail;
	}
	@Override
	public void initView() {
		
		imgShare.setImageResource(R.drawable.icon_shape);
		imgShare.setVisibility(View.VISIBLE);
//		WebSettings webSettings = mWebView.getSettings();
//		webSettings.setJavaScriptEnabled(true);
//		webSettings.setDatabaseEnabled(true);
//		mWebView.requestFocusFromTouch();

		webViewUtil =new WebViewUtil(this, mWebView, "RecommendMsgDetailActivity");

		tvTitle.setText(R.string.message_detail_text);

		//本地课详情
		mWebView.registerHandler("toLocalCourse", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String teacherId = json.getString("teacherId");
					String goodsId = json.getString("goodsId");
					String title = json.getString("title");
					Bundle bundle = new Bundle();
					bundle.putString("TEACHERID", teacherId);
					bundle.putString("GOODSID", goodsId);
					bundle.putString("TITLE", title);
					StartActivityUtil.startActivity(RecommendMsgDetailActivity.this, LocalCourceDetailActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
				}
			}

		});
	}
	@OnClick(R.id.imagebtn)
	public void onShareClick() {
		
		if (shapeDialog == null) {
			shapeDialog = new ShapeDialog(this);
			shapeDialog.setCanceledOnTouchOutside(true);
			shapeDialog.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if (shareAction != null) {

						if (position == 0) {
							new BaseUMShare(RecommendMsgDetailActivity.this, SHARE_MEDIA.WEIXIN).shareAction(shareAction);
						} else if (position == 1) {
							new BaseUMShare(RecommendMsgDetailActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE).shareAction(shareAction);
						} else if (position == 2) {
							new BaseUMShare(RecommendMsgDetailActivity.this, SHARE_MEDIA.QQ).shareAction(shareAction);
						} else if (position == 3) {
							new BaseUMShare(RecommendMsgDetailActivity.this, SHARE_MEDIA.QZONE).shareAction(shareAction);
						}
					} else {
						Toast.makeText(RecommendMsgDetailActivity.this, "没有分享数据", Toast.LENGTH_LONG)
								.show();
					}
					shapeDialog.dismiss();
				}
			});
		}
		shapeDialog.show();
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		MessageInfo msg = (MessageInfo) getIntent().getExtras().get("MESSAGE");
		int type = getIntent().getExtras().getInt("TYPE");
		if (type == 0) {
			// tvShape.setVisibility(View.GONE);
		}
		if (msg != null && !TextUtils.isEmpty(msg.getLinkUrl())) {
			// tvShape.setText("分享");
			msgTitle = msg.getTitle();
			if(!TextUtils.isEmpty(msgTitle)){
				tvTitle.setText(msgTitle);
				if (msgTitle.equals("测评")){
					imgShare.setVisibility(View.GONE);
				}
			}
			shapeUrl = msg.getLinkUrl();
			shareAction = new ShareAction(this).withTitle(msgTitle)
					.withText(msg.getContent()).withTargetUrl(shapeUrl);
			// shapeUrl = msg.getLinkUrl() + "?userid="
			// + VkoContext.getInstance(this).getUserId();
			mWebView.loadUrl(shapeUrl);
		} else if (msg != null) {
			mWebView.setVisibility(View.GONE);
			msgTitle = msg.getTitle();
			if (!TextUtils.isEmpty(msg.getTitle())) {
				tvContentTitle.setVisibility(View.VISIBLE);
				tvContentTitle.setText(msg.getTitle());
			}
			if (!TextUtils.isEmpty(msg.getCtime())) {
				tvContentTime.setVisibility(View.VISIBLE);
				tvContentTime.setText(msg.getCtime());
			}
			if (!TextUtils.isEmpty(msg.getContent())) {
				tvContent.setVisibility(View.VISIBLE);
				tvContent.setText(msg.getContent());
			}
		}else{
			imgShare.setVisibility(View.GONE);
		}
	}
	@OnClick(R.id.iv_back)
	void sayBack() {
		finish();
//		ActivityUtilManager.getInstance().finishActivity(this);
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		webViewUtil.setCookie();
	}
	
}


