package cn.vko.ring.circle.activity;

import java.lang.reflect.InvocationTargetException;

import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.ViewUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.event.Event;
import cn.vko.ring.circle.event.EventManager;
import cn.vko.ring.circle.event.IEventListener;
import cn.vko.ring.circle.event.ITopicEventListener;
import cn.vko.ring.circle.event.TopicEvent;
import cn.vko.ring.circle.listener.ICompleteOperationListener;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.circle.presenter.CircleFollowPresenter;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.umeng.EventCountAction;

import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.pop.CircleAddPop;
import cn.vko.ring.common.widget.pop.CircleFollowPop;
import cn.vko.ring.mine.model.BaseResultInfo;
import cn.vko.ring.utils.WebViewUtil;

import com.android.volley.VolleyError;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class CircleHomeActivity extends BaseActivity implements
		ITopicEventListener<Integer> {

	private String FOLLOW_KEY;
	private final static String LONGINURL = "http://m.vko.cn/group/square.html";
	private final static String UNLONGINURL = "http://m.vko.cn/group/found.html";
	@Bind(R.id.imagebtn)
	public ImageView ivAdd;
	@Bind(R.id.tv_title)
	public TextView tvTitle;

	@Bind(R.id.webview)
	public BridgeWebView mWebView;

	@Bind(R.id.layout_follow)
	public View layoutFollow;

	private CircleFollowPop mPOP;
	private CircleAddPop pop;

	private VolleyUtils<BaseResultInfo> http;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_circle_home;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 14) {
			this.getWindow().getDecorView()
					.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}
		tvTitle.setText("圈子");
		ivAdd.setImageResource(R.drawable.icon_add_head);
//		ivAdd.setVisibility(View.VISIBLE);
		EventManager.register(this);
		EventManager.register(eventListener);
	}

	@Override
	public void initData() {
		FOLLOW_KEY = VkoContext.getInstance(this).getUserId() + "FOLLOW";
		if (!VkoConfigure.getConfigure(this).getBoolean(FOLLOW_KEY, false)
				&& VkoContext.getInstance(this).isLogin()) {
			// 请求接口 判断是否关注过
			http = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
			Uri.Builder builder = http
					.getBuilder(ConstantUrl.VK_CIRCLE_INTEREST_CHECKED);
			builder.appendQueryParameter("userId", VkoContext.getInstance(this).getUserId());
			builder.appendQueryParameter("token", VkoContext.getInstance(this).getToken());
			http.sendGETRequest(true,builder);
			http.setUiDataListener(new UIDataListener<BaseResultInfo>() {
				@Override
				public void onDataChanged(BaseResultInfo response) {
					if (response != null && response.getCode() == 0) {
						if (!response.isData()) {
							// 请求网络数据是否关注过如果还没有则进入推荐关注页
							layoutFollow.setVisibility(View.VISIBLE);
							showFollowPOP();
						} else {
							VkoConfigure.getConfigure(
									CircleHomeActivity.this).put(
									FOLLOW_KEY, true);
							initWebView();
						}
					}
				}

				@Override
				public void onErrorHappened(String errorCode, String errorMessage) {

				}
			});
		} else {
			initWebView();
		}
	}

	private void initWebView() {
		// TODO Auto-generated method stub
		new WebViewUtil(this, mWebView, "CircleHomeActivity");

		if (VkoContext.getInstance(CircleHomeActivity.this).isLogin()) {
			mWebView.loadUrl(LONGINURL);
		} else {
			mWebView.loadUrl(UNLONGINURL);
		}
	}

	private void showFollowPOP() {
		// TODO Auto-generated method stub
		new CircleFollowPresenter(this, layoutFollow, new ICompleteOperationListener() {
			@Override
			public void onComplte(boolean isComplte) {
				if (VkoConfigure.getConfigure(CircleHomeActivity.this).getBoolean(FOLLOW_KEY, false)) {
					initWebView();
					layoutFollow.setVisibility(View.GONE);
				}
			}
		});
//		mPOP = new CircleFollowPop(this);
//		mPOP.update(0, 0, ViewUtils.getScreenWidth(this), ViewUtils.getRealHeight(this)
//						- ViewUtils.getVrtualBtnHeight(this));
//		mPOP.showAtLocation(mWebView, Gravity.CENTER, 0, 0);
//		mPOP.setOnCompleteListener(new ICompleteOperationListener() {
//
//			@Override
//			public void onComplte(boolean isComplte) {
//				// TODO Auto-generated method stub
//				if (VkoConfigure.getConfigure(CircleHomeActivity.this)
//						.getBoolean(FOLLOW_KEY, false)) {
//					initWebView();
//				}
//			}
//		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EventCountAction.onFragRCount(this.getClass());
		if (VkoContext.getInstance(this).isLogin()) {
			WebViewUtil.synCookies(this, "http://vko.cn",
					"vkoweb=" + VkoContext.getInstance(this).getToken()
							+ ";domain=.vko.cn");
			WebViewUtil.synCookies(this, "http://vko.cn",
					"userId=" + VkoContext.getInstance(this).getUserId()
							+ ";domain=.vko.cn");
		}
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		EventCountAction.onFragPCount(this.getClass());
		try {
//			mWebView.getClass().getMethod("onPause")
//					.invoke(mWebView, (Object[]) null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 局部刷新群详情
		mWebView.callHandler("stopVedio", "data from java",
				new CallBackFunction() {
					@Override
					public void onCallBack(String data) {

					}
				});
	}

	@OnClick(R.id.imagebtn)
	void sayAdd(View view) {
		if (pop == null) {
			pop = new CircleAddPop(this);
			pop.update(0, 0, ViewUtils.getScreenWidth(this) * 2 / 5,
					LayoutParams.WRAP_CONTENT);
		}
		// pop.showAsDropDown(view);
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		pop.showAtLocation(
				view,
				Gravity.NO_GRAVITY,
				location[0] - ViewUtils.getScreenWidth(this) * 2 / 5
						+ view.getWidth() - view.getPaddingRight(), location[1]
						+ view.getHeight() - view.getPaddingBottom());
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		// ActivityUtilManager.getInstance().finishActivity(this);
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mWebView.clearCache(this);
		mWebView.releaseAllWebViewCallback();
		EventManager.unRegister(this);
		EventManager.unRegister(eventListener);
		EventBus.getDefault().unregister(this);
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

	private IEventListener<GroupInfo> eventListener = new IEventListener<GroupInfo>() {

		@Override
		public int getFilter() {
			// TODO Auto-generated method stub
			return Event.DETEIL_EVENT | Event.JOIN_EVENT | Event.QUIT_EVENT
					| Event.UPDATE_EVENT | Event.CREATE_EVENT;
		}

		@Override
		public void event(Event<GroupInfo> event) {
			// TODO Auto-generated method stub joinGroups
			if (mWebView == null || event == null)
				return;
			// 局部刷新我加入的群
			mWebView.callHandler("refreshMyGroup", "data from java",
					new CallBackFunction() {
						@Override
						public void onCallBack(String data) {

						}
					});

			// 刷新圈子分类
			mWebView.callHandler("refreshGroupClassify", "data from java",
					new CallBackFunction() {
						@Override
						public void onCallBack(String data) {

						}
					});

			if (event.getEventType() == Event.DETEIL_EVENT) {
				// 局部刷新文章列表
				mWebView.callHandler("refreshArticle", "data from java",
						new CallBackFunction() {
							@Override
							public void onCallBack(String data) {

							}
						});
			}

		}
	};
	@Subscribe
	public void onEventMainThread(String msg) {
		if (!TextUtils.isEmpty(msg)) {
			// 用户登录时刷新数据
			if (msg.equals(Constants.LOGIN_REFRESH)) {
				initData();
			}
		}
	}

}
