package cn.vko.ring.mine.presenter;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;

public class OrderInfoPresenter {
	private VolleyUtils<String> volleyUtils;
	private Context ctx;
	private int openMember;
	private String orderId;
	private int num;
	public OrderInfoPresenter(Context ctx,String orderId,int openMember){
		this.ctx = ctx;
		this.openMember = openMember;
		this.orderId = orderId;
		getOrderInfoTask();
	}

	private void getOrderInfoTask() {
		// TODO Auto-generated method stub
		num++;
		if (volleyUtils == null) {
			volleyUtils = new VolleyUtils<String>(ctx,String.class);
		}
		Uri.Builder builder = volleyUtils.getBuilder(ConstantUrl.VKOIP+"/order/queryOrderByOrderId");
		builder.appendQueryParameter("orderId", orderId);
		volleyUtils.sendGETRequest(true,builder);
		volleyUtils.setUiDataListener(new UIDataListener<String>() {
			@Override
			public void onDataChanged(String response) {
				if(!TextUtils.isEmpty(response)){
					JSONObject jsonObject = JSONObject.parseObject(response);
					int code = jsonObject.getIntValue("code");
					String od = jsonObject.getString("od");
					if(code == 0 && !TextUtils.isEmpty(od)){
						JSONObject json = JSONObject.parseObject(od);
						int status = json.getIntValue("callBackStatus");
						String remark = jsonObject.getString("remark");
						Toast.makeText(ctx, remark, Toast.LENGTH_SHORT);
						if(status == 2){
							if(openMember == 1){
								EventBus.getDefault().post(Constants.OPEN_MEMBER);
							}else{
								EventBus.getDefault().post(Constants.PAYMENT_COMPLETE);
							}
						}
						return;
					}
				}
				if(num <2){
					getOrderInfoTask();

				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {
				if(num <2){
					getOrderInfoTask();

				}
			}
		});
	}
}
