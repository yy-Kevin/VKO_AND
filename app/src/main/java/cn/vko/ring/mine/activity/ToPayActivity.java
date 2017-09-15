package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.alipay.PayResult;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.dialog.CommonDialog;
import cn.vko.ring.common.widget.dialog.CommonDialog.Builder.OnButtonClicked;
import cn.vko.ring.common.widget.pop.SharePopupWindow;
import cn.vko.ring.wxapi.Constants;

//import cn.vko.ring.Constants;

public class ToPayActivity extends BaseActivity implements
		UIDataListener<String> {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_discount)
	public TextView tvDiscount;
	@Bind(R.id.zhifubao)
	public RelativeLayout tvZhiFuBao;
	@Bind(R.id.weixin)
	public RelativeLayout tvWeixin;
	@Bind(R.id.family)
	public RelativeLayout tvFamily;
	@Bind(R.id.bg_zhifubao)
	public ImageView bgZhifubao;
	@Bind(R.id.bg_weixin)
	public ImageView bgWeixin;
	@Bind(R.id.bg_family)
	public ImageView bgFamily;
	@Bind(R.id.tv_subject)
	public TextView tvSubject;
	@Bind(R.id.tv_total_fee)
	public TextView tvTotalFee;
	@Bind(R.id.count_down)
	public TextView tvCountDown;
	@Bind(R.id.tv_pay)
	public TextView tvPay;
	private SharePopupWindow sharePopupWindow;
	private int payWay = 0;
	private static final int SDK_PAY_FLAG = 1;
	private static final int ZHIFUBAO = 3;
	private static final int WEIXIN = 16;
	private static final int FINALLY = 5;
	private VolleyUtils<String> volleyUtils;
	private static final String URL = ConstantUrl.VKOIP + "/pay/appPay";
	private String orderId;
	private String amount;
	private String payUrl = "http://pay.vko.cn/pay/order/payment/";
	private IWXAPI api;
	private int openMember;
	public static String transaction;
	
	// 接收支付结果
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();
				Bundle bunldle = new Bundle();
				bunldle.putString("resultStatus", resultStatus);
				bunldle.putString("ORDERID", orderId);
				bunldle.putInt("OPEN_MEMBER", openMember);
				StartActivityUtil.startActivity(ToPayActivity.this, ALIPayEntryActivity.class, bunldle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
				if(resultStatus != null &&"9000".equals(resultStatus)){
					finish();
				}
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_to_pay;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		sharePopupWindow = new SharePopupWindow(ToPayActivity.this);
	}
	

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tvTitle.setText("选择支付方式");
		amount = getIntent().getExtras().getString("TOTALFEE");
		orderId = getIntent().getExtras().getString("ORDERID");
		tvSubject.setText(getIntent().getExtras().getString("SUBJECT"));
		tvDiscount.setText(getIntent().getExtras().getString("DISCOUNT"));
		openMember = getIntent().getExtras().getInt("open_member");
		tvTotalFee.setText(amount +"元");
		payUrl = payUrl + orderId + ".html";
		api = WXAPIFactory.createWXAPI(this, "wxebbcb968c1531360");
		transaction = orderId+"#"+openMember;
	}

	private void initVolley() {
		if(volleyUtils == null){
			volleyUtils = new VolleyUtils<String>(ToPayActivity.this,String.class);
		}
		Builder builder = volleyUtils.getBuilder(URL);
		Map<String,String> params = new HashMap<>();
		params.put("payType", String.valueOf(payWay));
		params.put("token", VkoContext.getInstance(this)
				.getUser().getToken());
		params.put("orderId", orderId);
		volleyUtils.setUiDataListener(this);
		volleyUtils.sendPostRequest(true,URL,params);
//		volleyUtils.requestHttpPost(builder, this, String.class);
	}

	@OnClick(R.id.iv_back)
	public void sayBack() {
		showHintMsg();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		showHintMsg();
	}
	private void showHintMsg() {
		// TODO Auto-generated method stub
		new CommonDialog.Builder(ToPayActivity.this)
		.setContentText("下单后24小时将自动取消订单,请尽快完成支付").setSureText("决定离开")
		.setCancleText("继续支付")
		.setOnButtonClicked(new OnButtonClicked() {

			@Override
			public void onSureButtonClick(CommonDialog commonDialog) {
				// TODO Auto-generated method stub
				commonDialog.dismiss();
				finish();
			}

			@Override
			public void onCancleButtonClick(
					CommonDialog commonDialog) {
				// TODO Auto-generated method stub
				commonDialog.dismiss();
			}
		}).build().show();
	}

	@OnClick(R.id.zhifubao)
	public void zhifubaoPay() {
		payWay = ZHIFUBAO;
		switchDrawable(payWay);
	}

	@OnClick(R.id.weixin)
	public void weixinPay() {
		payWay = WEIXIN;
		switchDrawable(payWay);
	}

	@OnClick(R.id.family)
	public void familyPay() {
		payWay = FINALLY;
		switchDrawable(payWay);

	}

	@OnClick(R.id.tv_pay)
	public void toPay() {
		switch (payWay) {
		case 0:
			Toast.makeText(ToPayActivity.this, "请选择支付方式", Toast.LENGTH_SHORT)
					.show();
			break;
		case ZHIFUBAO:
			initVolley();
			break;
		case WEIXIN:
			if(isWXAppInstalledAndSupported()){
				initVolley();
			}else{
				Toast.makeText(ToPayActivity.this, "未安装微信",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case FINALLY:
			sharePopupWindow.setUrl(payUrl, orderId);
			sharePopupWindow.showAtLocation(tvTitle, Gravity.BOTTOM, 0, 0);
		default:
			break;
		}

	}

	// 标识选中的支付方式
	public void switchDrawable(int payWay) {
		bgZhifubao.setVisibility(View.INVISIBLE);
		bgWeixin.setVisibility(View.INVISIBLE);
		bgFamily.setVisibility(View.INVISIBLE);
		switch (payWay) {
		case ZHIFUBAO:
			bgZhifubao.setVisibility(View.VISIBLE);
			break;
		case WEIXIN:
			bgWeixin.setVisibility(View.VISIBLE);
			break;
		case FINALLY:
			bgFamily.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * 调用支付宝支付
	 * 
	 */
	public void aliPay(final String content) {

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(ToPayActivity.this);
				// 调用支付接口，获取支付结果
				Log.i("myPayString", content);
				Log.i("=============", "调起支付宝");
				String result = alipay.pay(content);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 调用微信支付
	 * 
	 */
	public void wxPay(String content) {
		tvPay.setEnabled(false);
		Toast.makeText(ToPayActivity.this, "获取订单中...", Toast.LENGTH_SHORT)
				.show();
		try {
			if (content != null) {
				JSONObject json = new JSONObject(content);
				if (null != json && !json.has("retcode")) {
					PayReq req = new PayReq();
					req.appId = json.getString("appid");
					req.partnerId = json.getString("partnerid");
					req.prepayId = json.getString("prepayid");
					req.nonceStr = json.getString("noncestr");
					req.timeStamp = json.getString("timestamp");
					req.packageValue = json.getString("package");
					req.sign = json.getString("sign");
					req.extData = "app data"; // optional
//					req.transaction = orderId+"#"+openMember;
					Toast.makeText(ToPayActivity.this, "正常调起支付",
							Toast.LENGTH_SHORT).show();
					// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
					api.sendReq(req);
				} else {
					Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
					// Toast.makeText(ToPayActivity.this,
					// "返回错误" + json.getString("retmsg"),
					// Toast.LENGTH_SHORT).show();
				}
			} else {
				Log.d("PAY_GET", "服务器请求错误");
				Toast.makeText(ToPayActivity.this, "服务器请求错误",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.e("PAY_GET", "异常：" + e.getMessage());
			// Toast.makeText(ToPayActivity.this, "异常：" + e.getMessage(),
			// Toast.LENGTH_SHORT).show();
		}
		tvPay.setEnabled(true);
	}

	private boolean isWXAppInstalledAndSupported() {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
        msgApi.registerApp(Constants.APP_ID);

        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
                && msgApi.isWXAppSupportAPI();

        return sIsWXAppInstalledAndSupported;
    }
	@Subscribe
	public void onEventMainThread(String event) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(event) && event.equals(cn.vko.ring.Constants.WX_PAYMENT_COMPLETE)) {
//			if(openMember == 1){
//				EventBus.getDefault().post(cn.vko.ring.Constants.OPEN_MEMBER);
//			}else{
//				EventBus.getDefault().post(cn.vko.ring.Constants.PAYMENT_COMPLETE);
//			}
//			new OrderInfoPresenter(this, orderId, openMember);
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onDataChanged(String response) {
		JSONObject json;
		try {
			if(response == null) return;
			json = new JSONObject(response);
			int code = json.getInt("code");
			if (code == 0) {
				switch (payWay) {
					case ZHIFUBAO:
						aliPay(json.getString("data"));
						break;
					case WEIXIN:
						wxPay(json.getString("data"));
						break;
					case FINALLY:
						break;
					default:
						break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
