package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.home.activity.LoginActivity;
import cn.vko.ring.utils.WebViewUtil;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class MyOrderActivity extends BaseActivity {

	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.webview)
	public BridgeWebView mWebView;
	private String url;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_members_center;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText("我的订单");
		initWebView();
		EventBus.getDefault().register(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
//		url = getIntent().getExtras().getString("URL");
		url = "http://m.vko.cn/order/myOrder.html";
		mWebView.loadUrl(url);
	}

	@OnClick(R.id.iv_back)
	public void ivFinish() {
		this.finish();
	}

	private void initWebView() {
		// TODO Auto-generated method stub
		// 订单详情
		mWebView.registerHandler("orderDetail", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				// System.out.println("data" + data);
				if (data != null) {
					JSONObject jsonObject = JSONObject.parseObject(data);
					String orderUrl = jsonObject.getString("orderInfoUrl");
					Bundle bundle = new Bundle();
					bundle.putString("URL", orderUrl);
					StartActivityUtil.startActivity(MyOrderActivity.this,
							OrderDetailActivity.class, bundle,
							Intent.FLAG_ACTIVITY_NEW_TASK);
				}
			}

		});
		// 会员续费
		mWebView.registerHandler("orderInfo", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				// System.out.println("data" + data);
				if (data != null) {
					JSONObject jsonObject = JSONObject.parseObject(data);
					String orderId = jsonObject.getString("orderId");
					String totalFee = jsonObject.getString("amount");
					String subject = jsonObject.getString("goodsName");
					String discountInfo = jsonObject.getString("discountInfo");
					Bundle bundle = new Bundle();
					bundle.putString("SUBJECT", subject);
					bundle.putString("TOTALFEE", totalFee);
					bundle.putString("ORDERID", orderId);
					bundle.putString("DISCOUNT", discountInfo);

					StartActivityUtil.startActivity(MyOrderActivity.this,
							ToPayActivity.class, bundle,
							Intent.FLAG_ACTIVITY_NEW_TASK);
				}
			}
		});
		// 登录
		mWebView.registerHandler("toLogin", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				// 清除webView cookiet
				WebViewUtil.clearCache(MyOrderActivity.this);
				StartActivityUtil.startActivity(MyOrderActivity.this,
						LoginActivity.class);
				overridePendingTransition(R.anim.bottom_in, 0);
			}

		});

	}
	@Subscribe
	public void onEventMainThread(String event) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(event)) {
			if (event.equals(Constants.PAYMENT_COMPLETE)|| event.equals(Constants.ORDER_CANCEL) ||event.equals(Constants.OPEN_MEMBER)) {
				mWebView.loadUrl(url);
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		mWebView.releaseAllWebViewCallback();
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

}
