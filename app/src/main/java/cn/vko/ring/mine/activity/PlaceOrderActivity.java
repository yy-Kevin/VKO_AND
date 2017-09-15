package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.NoTouchCheckBox;
import cn.vko.ring.common.widget.NoTouchCheckBox.ICheckedChangeListener;
import cn.vko.ring.common.widget.dialog.CommonDialog;
import cn.vko.ring.common.widget.dialog.CommonDialog.Builder.OnButtonClicked;
import cn.vko.ring.home.model.UserInfo;


/**
 * 下单
 * 
 * @author Administrator
 * 
 */
public class PlaceOrderActivity extends BaseActivity implements
		UIDataListener<String> {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_vb)
	public TextView tvVb;
	@Bind(R.id.cb_vb)
	public NoTouchCheckBox cbVb;
	@Bind(R.id.cb_discount)
	public NoTouchCheckBox cbDiscount;
	@Bind(R.id.et_discount)
	public EditText etDiscount;

	@Bind(R.id.tv_discount)
	public TextView tvDiscount;
	@Bind(R.id.radiogroup)
	public RadioGroup mRadioGroup;
	@Bind(R.id.layout_discount)
	public RelativeLayout discountLayout;

	@Bind(R.id.tv_subject)
	public TextView tvOrderName;
	@Bind(R.id.tv_total_fee)
	public TextView tvTotalFee;
	@Bind(R.id.need_pay)
	public TextView tvNeedPay;
	@Bind(R.id.tv_pay)
	public TextView tvPay;
	private VolleyUtils<String> volleyUtils;
	private static final String URL = ConstantUrl.VKOIP + "/order/createOrder";
	private static final String DISCOUNTURL = ConstantUrl.VKOIP+ "/order/queryDiscount";
	private int haveScore,disScore;
	private double totalScore,needsScore;
	private int type;// 1 开通会员 4本地课
	private String memberConfigId, discountId,goodsId;
	private String extendInfo;
	private UserInfo user;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_place_order;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		cbVb.setOnCheckedChangeListener(new ICheckedChangeListener() {

			@Override
			public void onCheckedChange(boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					needsScore = totalScore - haveScore;
				} else if(cbDiscount.isChecked()){
					needsScore = totalScore - disScore;
				}else{
					needsScore = totalScore;
				}
				tvNeedPay.setText((float) (needsScore) / 10 + "元");
			}
		});
		cbDiscount.setOnCheckedChangeListener(new ICheckedChangeListener() {

			@Override
			public void onCheckedChange(boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					needsScore = totalScore - disScore;
					tvNeedPay.setVisibility(View.INVISIBLE);
				} else {
					if(!cbVb.isChecked()){
						needsScore = totalScore;
					}
					tvNeedPay.setVisibility(View.VISIBLE);
				}
			}
		});

		cbVb.setChecked(true);
		cbDiscount.setChecked(false);

	}

	@OnClick(R.id.layout_vb)
	public void clickVb() {
		if (cbDiscount.isChecked()) {
			cbDiscount.setChecked(false);
		}
		cbVb.toggle();

	}

	@OnClick(R.id.layout_discount)
	public void clickDiscount() {
		if (cbVb.isChecked()) {
			cbVb.setChecked(false);
		}
		cbDiscount.toggle();
	}
	@OnClick(R.id.et_discount)
	public void clickEtDiscount() {
		if (cbVb.isChecked()) {
			cbVb.setChecked(false);
		}
		if(!cbDiscount.isChecked()){
			cbDiscount.setChecked(true);
		}
	}

	@Override
	public void initData() {
		user = VkoContext.getInstance(this).getUser();
		// TODO Auto-generated method stub
		tvTitle.setText("下单");
		haveScore = getIntent().getExtras().getInt("HAVESCORE", 0);
		totalScore = getIntent().getExtras().getDouble("NEEDSCORE", 0);
		needsScore = totalScore - haveScore;
		tvOrderName.setText(getIntent().getExtras().getString("SUBJECT"));
		type = getIntent().getExtras().getInt("TYPE", 0);
		if (type == 1) {
			memberConfigId = getIntent().getExtras().getString("MEMBERCONFIGID");
			discountLayout.setVisibility(View.VISIBLE);
			tvNeedPay.setText((float) (needsScore) / 10 + "元");
		} else if(type == 4){
			goodsId = getIntent().getExtras().getString("GOODSID");
			mRadioGroup.setVisibility(View.GONE);
			tvDiscount.setVisibility(View.GONE);
			tvNeedPay.setText((float) (needsScore) / 10 + "元");
		}else{
			extendInfo= getIntent().getExtras().getString("EXTENDINFO");
			tvDiscount.setVisibility(View.GONE);
			tvNeedPay.setText((float) (needsScore) / 10 + "元");
		}
		tvTotalFee.setText((float) totalScore / 10 + "元");
		tvVb.setText("可使用V币：" + haveScore);
	}

	//下单
//	private void initVolley() {
//		if (volleyUtils == null) {
//			volleyUtils = new VolleyUtils<String>(PlaceOrderActivity.this,String.class);
//		}
//		Builder builder = volleyUtils.getBuilder(URL);
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("token", VkoContext.getInstance(this)
//				.getUser().getToken());
//		Log.e("===sourceId",user.getSourceCityId()+"==");
//		if (user.getSourceCityId()!=null){
//			params.put("sourceId",user.getSourceCityId() );
//			params.put("sourceType","4" );
//		}
//		if (type == 1) {// 开通会员
//			params.put("memberConfigId", memberConfigId);
//			params.put("orderType", String.valueOf(8));
//			if (!TextUtils.isEmpty(discountId) && cbDiscount.isChecked()) {
//				params.put("discountId", discountId);
//			} else if(cbVb.isChecked()){
//				params.put("vcoin", String.valueOf(haveScore));
//			}
//		} else if(type == 4){
//			params.put("orderType", String.valueOf(10));
//			params.put("goodsId",goodsId);
//		}else{
//			params.put("extendInfo",extendInfo);
//			params.put("orderType", String.valueOf(5));
//			params.put("amount",String.valueOf((float) (needsScore) / 10));
//		}
//		// builder.appendQueryParameter("orderId", orderId);
//		volleyUtils.setUiDataListener(this);
//		volleyUtils.sendPostRequest(true,URL,params);
////		volleyUtils.requestHttpPost(builder, this, String.class);
//	}

	private void initVolley() {
		if (volleyUtils == null) {
			volleyUtils = new VolleyUtils<String>(PlaceOrderActivity.this,String.class);
		}
		Builder builder = volleyUtils.getBuilder(URL);
		builder.appendQueryParameter("token", VkoContext.getInstance(this)
				.getUser().getToken());
		if (user.getSourceCityId()!=null){
			builder.appendQueryParameter("sourceId", user.getSourceCityId());
			builder.appendQueryParameter("sourceType","4");
		}
		if (type == 1) {// 开通会员
			builder.appendQueryParameter("memberConfigId", memberConfigId);
			builder.appendQueryParameter("orderType", String.valueOf(8));
			if (!TextUtils.isEmpty(discountId) && cbDiscount.isChecked()) {
				builder.appendQueryParameter("discountId", discountId);
			} else if(cbVb.isChecked()){
				builder.appendQueryParameter("vcoin", String.valueOf(haveScore));
			}
		} else if(type == 4){
			builder.appendQueryParameter("orderType", String.valueOf(10));
			builder.appendQueryParameter("goodsId",goodsId);
		}else{
			builder.appendQueryParameter("extendInfo",extendInfo);
			builder.appendQueryParameter("orderType", String.valueOf(5));
			builder.appendQueryParameter("amount",String.valueOf((float) (needsScore) / 10));
		}

		volleyUtils.setUiDataListener(this);
		volleyUtils.sendGETRequest(true,builder);
//		volleyUtils.requestHttpPost(builder, this, String.class);
	}


	@OnClick(R.id.iv_back)
	public void sayBack() {
		finish();
	}

	@OnClick(R.id.tv_pay)
	public void toPay() {
		if (type == 1 && cbDiscount.isChecked()) {// 开通会员用优惠码 先检码优惠码
			checkDiscountTask();
			return;
		}
		initVolley();
	}

	// 检查优惠码
	private void checkDiscountTask() {
		// TODO Auto-generated method stub
		if (volleyUtils == null) {
			volleyUtils = new VolleyUtils<String>(PlaceOrderActivity.this,String.class);
		}
		discountId = etDiscount.getText().toString();
		if (TextUtils.isEmpty(discountId)) {
			Toast.makeText(this, "请输入优惠码", Toast.LENGTH_LONG).show();
			return;
		}
		Builder builder = volleyUtils.getBuilder(DISCOUNTURL);
		builder.appendQueryParameter("discountId", discountId);
		builder.appendQueryParameter("token", VkoContext.getInstance(this)
				.getUser().getToken());
		volleyUtils.sendGETRequest(true,builder);
		volleyUtils.setUiDataListener(new UIDataListener<String>() {
			@Override
			public void onDataChanged(String response) {
				if (response != null) {
					JSONObject json;
					try {
						json = new JSONObject(response);
						int code = json.getInt("code");
						if (code == 0) {
							JSONObject data = new JSONObject(json
									.getString("data"));
							boolean flag = data.getBoolean("flag");
							int discountPrice = data
									.getInt("discountPrice");
							showDiscount(discountPrice, flag);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {
				Toast.makeText(PlaceOrderActivity.this, "检查优惠码异常", Toast.LENGTH_LONG).show();
			}
		});

	}

	protected void showDiscount(int discountPrice, boolean flag) {
		// TODO Auto-generated method stub
		if (flag) {
			disScore = discountPrice*10;
			new CommonDialog.Builder(PlaceOrderActivity.this)
					.setContentText(
							"优惠码可优惠"+discountPrice+"元，你还需支付"
									+ ((float) totalScore / 10 - discountPrice)
									+ "元").setSureText("确定")
					.setCancleText("取消")
					.setOnButtonClicked(new OnButtonClicked() {

						@Override
						public void onSureButtonClick(CommonDialog commonDialog) {
							// TODO Auto-generated method stub
							initVolley();
							commonDialog.dismiss();
						}

						@Override
						public void onCancleButtonClick(
								CommonDialog commonDialog) {
							// TODO Auto-generated method stub
							commonDialog.dismiss();
						}
					}).build().show();

		} else {
			disScore = 0;
			new CommonDialog.Builder(PlaceOrderActivity.this)
					.setContentText("优惠码不可用，请重新输入").setSureText("确定")
					.setOnButtonClicked(new OnButtonClicked() {

						@Override
						public void onSureButtonClick(CommonDialog commonDialog) {
							// TODO Auto-generated method stub
							commonDialog.dismiss();
						}

						@Override
						public void onCancleButtonClick(
								CommonDialog commonDialog) {
							// TODO Auto-generated method stub
							commonDialog.dismiss();
						}
					}).build().show();
		}
	}

	@Override
	public void onDataChanged(String response) {
		JSONObject json;
		try {
			if (response == null)
				return;
			json = new JSONObject(response);
			int code = json.getInt("code");
			String orderId = json.getString("data");
			String  msg  = json.getString("msg");

			if (code == 0 && !TextUtils.isEmpty(orderId)) {
				Bundle bundle = new Bundle();
				String discountInfo="";
				if (!TextUtils.isEmpty(discountId) && cbDiscount.isChecked()) {
					discountInfo = "优惠码 ("+discountId+")";
					needsScore = totalScore - disScore;
				} else if(cbVb.isChecked()){
					discountInfo = "V币 ("+haveScore+")";
				}
				bundle.putString("TOTALFEE", String.valueOf((float) (needsScore) / 10));
				bundle.putString("SUBJECT", tvOrderName.getText().toString());

				bundle.putString("ORDERID", orderId);
				bundle.putString("DISCOUNT", discountInfo);
				bundle.putInt("open_member", type);
				StartActivityUtil.startActivity(PlaceOrderActivity.this,ToPayActivity.class, bundle,Intent.FLAG_ACTIVITY_SINGLE_TOP);
				finish();
			}else{
				if(!TextUtils.isEmpty(msg)){
					Toast.makeText(PlaceOrderActivity.this, msg, Toast.LENGTH_LONG).show();
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
