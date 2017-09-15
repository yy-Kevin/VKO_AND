package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.listener.IRechargeListener;
import cn.vko.ring.circle.model.BaseRechargeInfo;
import cn.vko.ring.circle.presenter.RechargePresenter;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.activity.LoginActivity;

import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.utils.WebViewUtil;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * @ClassName: MembersCenterActivity
 * @Description: 会员中兴
 * @author: JiaRH
 * @date: 2016-1-10 下午2:09:00
 */
public class MembersCenterActivity extends BaseActivity {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.webview)
	public BridgeWebView mWebView;
	private final static String URL = "http://m.vko.cn/member/member.html";
	private VolleyUtils<BaseRechargeInfo> volley;

	private UserInfo user;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_members_center;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText("会员中心");
		EventBus.getDefault().register(this);
		initWebView();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@OnClick(R.id.iv_back)
	public void ivFinish() {
		this.finish();
	}

	private void initWebView() {
		// TODO Auto-generated method stub

		user = VkoContext.getInstance(this).getUser();

		String newUrl=URL+"?"+"gradeId="+user.getGradeId();

		Log.e("URL",newUrl);
		mWebView.loadUrl(newUrl);

		// 会员续费
		mWebView.registerHandler("renewMember", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				// System.out.println("data" + data);
				if (data != null) {
					try {
						JSONObject jsonObject = JSONObject.parseObject(data);
						final String memberConfigId = jsonObject
								.getString("memberConfigId");
						String vCoinPrice = jsonObject.getString("vCoinPrice");
						String name = jsonObject.getString("memberConfigName");
//						Log.e("=======memberConfigId",memberConfigId);
//						Log.e("=======vCoinPrice",vCoinPrice);
//						Log.e("=======memberConfigName",name);
						if (TextUtils.isEmpty(name)) {
							name = "开通会员";
						}
						new RechargePresenter(MembersCenterActivity.this,
								vCoinPrice, name, memberConfigId,
								new IRechargeListener() {

									@Override
									public void onRecharge(String price) {
										// TODO Auto-generated method stub
										buyCourseTask(memberConfigId);
									}
								});

					} catch (Exception e) {

					}
				}
			}

		});
		// 我的订单
		mWebView.registerHandler("myOrder", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				// System.out.println("data" + data);
				if (data != null) {
					try{
						JSONObject jsonObject = JSONObject.parseObject(data);
						String orderUrl = jsonObject.getString("orderUrl");
						Log.e("====>",orderUrl);
						Bundle bundle = new Bundle();
						bundle.putString("URL", orderUrl);
						StartActivityUtil.startActivity(MembersCenterActivity.this,
								MyOrderActivity.class, bundle,
								Intent.FLAG_ACTIVITY_NEW_TASK);
					}catch(Exception e){
						
					}
				}
			}

		});
		// 登录
		mWebView.registerHandler("toLogin", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				// 清除webView cookiet
				WebViewUtil.clearCache(MembersCenterActivity.this);
				StartActivityUtil.startActivity(MembersCenterActivity.this,
						LoginActivity.class);
				overridePendingTransition(R.anim.bottom_in, 0);
			}

		});

	}

	protected void buyCourseTask(String memberConfigId) {
		// TODO Auto-generated method stub
		user = VkoContext.getInstance(this).getUser();
		if (volley == null) {
			volley = new VolleyUtils<BaseRechargeInfo>(this,BaseRechargeInfo.class);
		}
		Uri.Builder builder = volley.getBuilder(ConstantUrl.VKOIP
				+ "/user/renewMember");
		builder.appendQueryParameter("memberConfigId", memberConfigId);
		builder.appendQueryParameter("token", VkoContext.getInstance(this)
				.getToken());
//		builder.appendQueryParameter("city",user.getCity() );
		volley.sendGETRequest(true,builder);
		volley.setUiDataListener(new UIDataListener<BaseRechargeInfo>() {
			@Override
			public void onDataChanged(BaseRechargeInfo response) {
				if (response != null && response.getCode() == 0
						&& response.getData() != null) {// 开通会员成功
					if (response.getData().isFlag()) {
						EventBus.getDefault().post(Constants.OPEN_MEMBER);
						refreshMemberInfo();
						Toast.makeText(MembersCenterActivity.this,"开通会员成功", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(
								MembersCenterActivity.this,
								"你还需要"+ response.getData().getNeedScore() + "个V币", Toast.LENGTH_LONG).show();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}
	@Subscribe
	public void onEventMainThread(String event) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(event) && event.equals(Constants.OPEN_MEMBER)) {
			refreshMemberInfo();
		}
	}

	public void refreshMemberInfo() {
		// 刷新会员信息
		mWebView.callHandler("refreshMemberInfo", "data from java",
				new CallBackFunction() {
					@Override
					public void onCallBack(String data) {

					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mWebView.releaseAllWebViewCallback();
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (VkoContext.getInstance(this).isLogin()) {
			WebViewUtil.synCookies(this, "http://vko.cn",
					"vkoweb=" + VkoContext.getInstance(this).getToken()
							+ ";domain=.vko.cn");
			WebViewUtil.synCookies(this, "http://vko.cn",
					"userId=" + VkoContext.getInstance(this).getUserId()
							+ ";domain=.vko.cn");
		}
	}

	/*@Override
	public void onRequestPermissionsResult(int permission, boolean granted) {
		super.onRequestPermissionsResult(permission, granted);
		Toast.makeText(this, "设置权限", Toast.LENGTH_SHORT).show();
	}*/

}
