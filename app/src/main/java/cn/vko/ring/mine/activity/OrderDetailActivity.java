package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.greenrobot.eventbus.EventBus;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.home.activity.LoginActivity;
import cn.vko.ring.utils.WebViewUtil;

public class OrderDetailActivity extends BaseActivity {

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
		tvTitle.setText("订单详情");
		initWebView();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		url = getIntent().getExtras().getString("URL");
		mWebView.loadUrl(url);
	}

	private void initWebView() {
		// TODO Auto-generated method stub
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
					StartActivityUtil.startActivity(OrderDetailActivity.this,
							ToPayActivity.class, bundle,
							Intent.FLAG_ACTIVITY_NEW_TASK);
				}
			}
		});

		// 取消订单
		mWebView.registerHandler("canceOrder", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				EventBus.getDefault().post(Constants.ORDER_CANCEL);
			}
		});
		// 登录
				mWebView.registerHandler("toLogin", new BridgeHandler() {

					@Override
					public void handler(String data, CallBackFunction function) {
						// 清除webView cookiet
						WebViewUtil.clearCache(OrderDetailActivity.this);
						StartActivityUtil.startActivity(OrderDetailActivity.this,
								LoginActivity.class);
						overridePendingTransition(R.anim.bottom_in, 0);
					}

				});


	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		mWebView.releaseAllWebViewCallback();
		super.onDestroy();
	}

	@OnClick(R.id.iv_back)
	public void ivFinish() {
		this.finish();
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
