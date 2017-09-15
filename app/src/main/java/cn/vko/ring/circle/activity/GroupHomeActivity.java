package cn.vko.ring.circle.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.event.Event;
import cn.vko.ring.circle.event.EventManager;
import cn.vko.ring.circle.event.IEventListener;
import cn.vko.ring.circle.event.ITopicEventListener;
import cn.vko.ring.circle.event.TopicEvent;
import cn.vko.ring.circle.listener.IRechargeListener;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.circle.presenter.RechargePresenter;
import cn.vko.ring.classgroup.activity.CreateTaskActivty;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.umeng.BaseUMShare;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.dialog.ShapeDialog;
import cn.vko.ring.home.model.BaseResultInfo;
import cn.vko.ring.home.presenter.VbDealPrsenter;
import cn.vko.ring.mine.activity.FormularRemActivity;
import cn.vko.ring.utils.ActivityUtilManager;
import cn.vko.ring.utils.WebViewUtil;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class GroupHomeActivity extends BaseActivity implements 
		ITopicEventListener<Integer> ,UMShareListener {

	@Bind(R.id.webview)
	public BridgeWebView mWebView;
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.iv_edit)
	public ImageView ivEdit;
	@Bind(R.id.imagebtn)
	public ImageView ivSetting;
	@Bind(R.id.image_other)
	public ImageView ivShape;
	@Bind(R.id.layout_html)
	public RelativeLayout layoutHtml;
	@Bind(R.id.tv_tips)
	public TextView tvTips;
	private int topTitleHeight, imageTop;

	private String groupId, url;
	private ShapeDialog shapeDialog;
	private VolleyUtils<BaseResultInfo> volley;
	private RechargePresenter mPresenter;
	private Map<String,String> paramsMap = new HashMap<String,String>();
	private ShareAction shareAction;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.layout_html;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		tvTitle.setText(R.string.circle_group_home);
		ivSetting.setVisibility(View.VISIBLE);
		ivShape.setVisibility(View.VISIBLE);
		ivSetting.setImageResource(R.drawable.icon_group_setting);
		initWebView();
		EventManager.register(this);
		EventManager.register(eventListener);

		int[] locations = new int[2];
		layoutHtml.getLocationInWindow(locations);
		topTitleHeight = locations[1];
	}

	private void initWebView() {
		// TODO Auto-generated method stub
		new WebViewUtil(this, mWebView, "GroupHomeActivity");
		// 出是否显示 新建话题按钮
		mWebView.registerHandler("joinState", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (ivEdit != null
						&& !VkoContext.getInstance(GroupHomeActivity.this)
								.doLoginCheckToSkip(GroupHomeActivity.this)) {
					ivEdit.setVisibility(View.VISIBLE);
				}
			}
		});
		// 分享群 数据
		mWebView.registerHandler("shareGroup", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String groupName = json.getString("groupName");
					String headImg = json.getString("headImg");
					String introduction = json.getString("introduction");
					shareAction = new ShareAction(GroupHomeActivity.this).withTitle(groupName)
							.withTargetUrl(url)
							.withText(introduction.isEmpty() ? "暂无介绍" : introduction).withMedia(new UMImage(GroupHomeActivity.this,headImg))
					.withExtra(new UMImage(GroupHomeActivity.this, R.drawable.ic_logo));
				}
			}
		});

		// 购习精讲视频
		mWebView.registerHandler("toGroupVideosBuy", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					// String groupId = json.getString("groupId");
					String price = json.getString("price");
					paramsMap.put("groupId", groupId);
					paramsMap.put("price", price);
					paramsMap.put("extendType", "2");
					if (mPresenter == null) {
						mPresenter = new RechargePresenter(
								GroupHomeActivity.this, price,paramsMap,
								new IRechargeListener() {

									@Override
									public void onRecharge(String price) {
										// TODO Auto-generated method stub
										buyCourseTask(price);
									}
								});
					} else {
						mPresenter.getMemberScrodTask(price);
					}
				}
			}
		});

	}

	protected void buyCourseTask(String price) {
		// TODO Auto-generated method stub
		if (volley == null) {
			volley = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
		}
		Uri.Builder builder = volley.getBuilder(ConstantUrl.VK_CIRCLE_BUY);
		builder.appendQueryParameter("groupId", groupId);
		builder.appendQueryParameter("price", price);
		builder.appendQueryParameter("token", VkoContext.getInstance(this).getToken());
		volley.sendGETRequest(true,builder);
		volley.setUiDataListener(new UIDataListener<BaseResultInfo>() {
			@Override
			public void onDataChanged(BaseResultInfo response) {
				if (response != null && response.getCode() == 0) {// 购买成功
					refreshData();
					Toast.makeText(GroupHomeActivity.this, "购买成功", Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	protected void refreshData() {
		// TODO Auto-generated method stub
		// 局部刷新群详情
		mWebView.callHandler("refreshGroupBuy", "data from java",
				new CallBackFunction() {
					@Override
					public void onCallBack(String data) {

					}
				});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		groupId = getIntent().getExtras().getString("GROUPID");
		url = getIntent().getExtras().getString("URL");
		if (groupId == null) {
			if (!TextUtils.isEmpty(url) && url.contains("groupId=")) {
				groupId = url.substring(url.indexOf("groupId=") + 8);
			}
		}
		// "http://m.vko.cn/group/crowdHome.html?groupId="+
		mWebView.loadUrl(url);
	}

	@Override
	protected boolean isEnableSwipe() {
		// TODO Auto-generated method stub
		ActivityUtilManager.getInstance().delActivity(this);// 不能及时删除的问题
		return true;
	}

	@OnClick(R.id.iv_edit)
	void toCreatTast(){
		StartActivityUtil.startActivity(GroupHomeActivity.this, CreateTaskActivty.class);
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		ActivityUtilManager.getInstance().delActivity(this);// 不能及时删除的问题
		finish();
		// ActivityUtilManager.getInstance().startMainActivity(this);
	}

	/**
	 * @param lastDownTime
	 *            按下时间
	 * @param thisEventTime
	 * @param longPressTime
	 *            判断长按时间的阀值
	 * @return
	 */
	public static boolean isLongPressed(long lastDownTime, long thisEventTime,
			long longPressTime) {

		long intervalTime = thisEventTime - lastDownTime;
		if (intervalTime >= longPressTime) {
			return true;
		}
		return false;
	}

	/**
	 * 通过layout方法，移动view 优点：对view所在的布局，要求不苛刻，不要是RelativeLayout，而且可以修改view的大小
	 * 
	 * @param view
	 * @param rawX
	 * @param rawY
	 */
	private void moveViewByLayout(View view, int rawX, int rawY) {
		int left = rawX - ivEdit.getWidth() / 2;
		int top = rawY - imageTop - ivEdit.getHeight() / 2;
		if (left < 0) {
			left = 0;
		}
		if (left > layoutHtml.getWidth() - view.getWidth()) {
			left = layoutHtml.getWidth() - view.getWidth();
		}
		if (top < topTitleHeight) {
			top = topTitleHeight;
		}
		if (top > topTitleHeight + layoutHtml.getHeight() - view.getHeight()) {
			top = topTitleHeight + layoutHtml.getHeight() - view.getHeight();
		}

		int width = left + view.getWidth();
		int height = top + view.getHeight();

		view.layout(left, top, width, height);
	}

	@OnClick(R.id.imagebtn)
	void saySetting() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("GROUPID", groupId);
		StartActivityUtil.startActivity(this, GroupDetailActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);

	}

	@OnClick(R.id.image_other)
	void sayShape() {
		if (shapeDialog == null) {
			shapeDialog = new ShapeDialog(this);
			shapeDialog.setCanceledOnTouchOutside(true);
			shapeDialog.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					BaseUMShare umShare = null;
					if (shareAction != null) {
						if (position == 0) {
							umShare = new BaseUMShare(GroupHomeActivity.this, SHARE_MEDIA.WEIXIN);
						} else if (position == 1) {
							umShare= new BaseUMShare(GroupHomeActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE);
						} else if (position == 2) {
							umShare = new BaseUMShare(GroupHomeActivity.this, SHARE_MEDIA.QQ);
						} else if (position == 3) {
							umShare = new BaseUMShare(GroupHomeActivity.this, SHARE_MEDIA.QZONE);
						}
						if(umShare != null){
							umShare.shareAction(shareAction);
//							umShare.setShareListener(GroupHomeActivity.this);
						}
					} else {
						Toast.makeText(GroupHomeActivity.this, "没有分享数据", Toast.LENGTH_LONG).show();
					}
					shapeDialog.dismiss();
				}
			});
		}
		shapeDialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityUtilManager.getInstance().delActivity(this);// 不能及时删除的问题
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// mWebView.clearCache(this);
		mWebView.releaseAllWebViewCallback();
		EventManager.unRegister(this);
		EventManager.unRegister(eventListener);
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	@Subscribe
	public void onEventMainThread(String msg) {
		if (!TextUtils.isEmpty(msg)) {
			// 用户登录时刷新数据
			if (msg.equals(Constants.LOGIN_REFRESH)) {
				initWebView();
				mWebView.loadUrl(url);
			} else if (msg.equals(Constants.PAY_VIDEO)) {
				refreshData();
			}
		}
	}

	@Override
	public void event(TopicEvent<Integer> event) {
		// TODO Auto-generated method stub
		//
		if (mWebView == null || event == null)
			return;
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
			return Event.DETEIL_EVENT | Event.QUIT_EVENT | Event.UPDATE_EVENT
					| Event.JOIN_EVENT;
		}

		@Override
		public void event(Event<GroupInfo> event) {
			// TODO Auto-generated method stub joinGroups
			if (mWebView == null || event == null)
				return;
			if (event.getEventType() == Event.DETEIL_EVENT) {
				finish();
				return;
			}
			// 局部刷新群详情
			mWebView.callHandler("refreshGroupDetail", "data from java",
					new CallBackFunction() {
						@Override
						public void onCallBack(String data) {

						}
					});
		}
	};

	protected void onResume() {
		super.onResume();
		if (VkoContext.getInstance(this).isLogin()) {
			cn.vko.ring.utils.WebViewUtil.synCookies(this, "http://vko.cn",
					"vkoweb=" + VkoContext.getInstance(this).getToken()
							+ ";domain=.vko.cn");
			cn.vko.ring.utils.WebViewUtil.synCookies(this, "http://vko.cn",
					"userId=" + VkoContext.getInstance(this).getUserId()
							+ ";domain=.vko.cn");
		}
	};

	@Override
	public void onResult(SHARE_MEDIA share_media) {
		new VbDealPrsenter(GroupHomeActivity.this)
				.doRequest(VbDealPrsenter.SHARE_ARTICLE);
	}

	@Override
	public void onError(SHARE_MEDIA share_media, Throwable throwable) {

	}

	@Override
	public void onCancel(SHARE_MEDIA share_media) {

	}

}
