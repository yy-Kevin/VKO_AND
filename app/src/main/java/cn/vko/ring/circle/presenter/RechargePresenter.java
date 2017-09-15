package cn.vko.ring.circle.presenter;

import java.util.Map;

import com.alibaba.fastjson.JSON;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.listener.IRechargeListener;
import cn.vko.ring.common.listener.IOnClickItemListener;

import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.dialog.UserVbDialog;
import cn.vko.ring.home.model.BtnInfo;
import cn.vko.ring.mine.activity.PlaceOrderActivity;
import cn.vko.ring.mine.model.BaseSocreInfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class RechargePresenter {
	private Context ctx;
	private VolleyUtils<BaseSocreInfo> http;
	// private CommonDialog dialog;
	private UserVbDialog dialog;
	private IRechargeListener listener;
	private String name,memberConfigId;
	private Map<String,String> paramsMap;
	public RechargePresenter(Context ctx, String price,Map<String,String> paramsMap,
			IRechargeListener listener) {
		this.ctx = ctx;
		this.listener = listener;
		this.paramsMap = paramsMap;
		getMemberScrodTask(price);
	}
	public RechargePresenter(Context ctx,String price,String name,String memberConfigId,
			IRechargeListener listener) {
		this.ctx = ctx;
		this.listener = listener;
		this.name = name;
		this.memberConfigId = memberConfigId;
		getMemberScrodTask(price);
	}

	public void getMemberScrodTask(final String price) {
		// TODO Auto-generated method stub
		if (http == null) {
			http = new VolleyUtils<BaseSocreInfo>(ctx,BaseSocreInfo.class);
		}
		Uri.Builder builder = http.getBuilder(ConstantUrl.VK_CIRCLE_SCORE);
		builder.appendQueryParameter("token", VkoContext.getInstance(ctx)
				.getToken());
		http.sendGETRequest(true,builder);
		http.setUiDataListener(new UIDataListener<BaseSocreInfo>() {
			@Override
			public void onDataChanged(BaseSocreInfo response) {
				if (response != null && response.getCode() == 0) {
//							int surplus = Integer.parseInt(response.getData()
//									.getScore()) - Integer.parseInt(price);
					showBuyDialog(Integer.parseInt(response.getData().getScore()), Integer.parseInt(price));
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	private void showBuyDialog(final int havePrice, final int needPrice) {
		if (dialog == null) {
			dialog = new UserVbDialog(ctx);
			dialog.setHeadImage(R.drawable.head_recharge);
			dialog.setOnClickItemListener(new IOnClickItemListener<String>() {

				@Override
				public void onItemClick(int position, String t, View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if (position == 0) {
					} else {
						if (havePrice >= needPrice) {// 购买
							if (listener != null) {
								listener.onRecharge(String.valueOf(needPrice));
							}
						} else {// 充值
							Bundle bundle = new Bundle();
							bundle.putInt("HAVESCORE",havePrice);
							bundle.putDouble("NEEDSCORE", needPrice);
							if(name == null){
								bundle.putString("SUBJECT", "购买精讲视频");
								bundle.putString("EXTENDINFO", JSON.toJSONString(paramsMap,true));
							}else{
								bundle.putString("SUBJECT", name);
								bundle.putString("MEMBERCONFIGID", memberConfigId);
								bundle.putInt("TYPE", 1);
							}
							StartActivityUtil.startActivity(ctx,PlaceOrderActivity.class, bundle,Intent.FLAG_ACTIVITY_SINGLE_TOP);
						}
						
					}
				}
			});
		}
		dialog.setContentText("你需要花费" + needPrice + "V币");
		dialog.setBtnInfo(new BtnInfo("取消", R.color.white,
				R.drawable.shape_gray_btn), new BtnInfo(havePrice >= needPrice ? "购买"
				: "充值", R.color.white,
				R.drawable.selector_light_blue_button));
		dialog.show();
	}
	
//	public void buyCouserTask(){
//		if (listener != null) {
//			listener.onRecharge(price);
//		}
//	}
}
