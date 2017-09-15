package cn.vko.ring.common.widget.pop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BasePopuWindow;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.umeng.BaseUMShare;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.mine.activity.BindCommunicationActivity;
import cn.vko.ring.mine.activity.ContactsActivity;
import cn.vko.ring.mine.model.StringResultInfo;

public class SharePopupWindow extends BasePopuWindow {

	private TextView tvQQ, tvWeiXin, tvPhone;
	private ShareAction shareAction;
	private Context context;
	public boolean iscomm;
	private String url,orderId;
	private String shareString = "我在微课网下了个单,帮我付款为我的学习加油吧!";
	private VolleyUtils<StringResultInfo> volley ;
	public SharePopupWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setUrl(String url,String orderId) {
		this.url = url;
		this.orderId = orderId;
	}

	@Override
	public void setUpViews(View contentView) {
		// TODO Auto-generated method stub
		tvQQ = (TextView) contentView.findViewById(R.id.tv_qq);
		tvWeiXin = (TextView) contentView.findViewById(R.id.tv_weixin);
		tvPhone = (TextView) contentView.findViewById(R.id.tv_communication);
	}

	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub
		tvQQ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "分享到QQ", Toast.LENGTH_SHORT).show();

				new BaseUMShare((Activity) context, SHARE_MEDIA.QQ).shareByImgId("微课圈",shareString,R.drawable.login_logo,url);
				dismiss();
			}
		});
		tvWeiXin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "分享到微信", Toast.LENGTH_SHORT).show();
				getPayUrl();
			}
		});
		tvPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "手机联系人", Toast.LENGTH_SHORT).show();
				iscomm = VkoConfigure.getConfigure(context).getBoolean(
						"iscomm", false);
				if (iscomm) {
					Intent intent = new Intent(context, ContactsActivity.class);
					intent.putExtra("flag", 1);
					intent.putExtra("url", url);
					context.startActivity(intent);
				} else {
					Intent intent = new Intent(context,
							BindCommunicationActivity.class);
					context.startActivity(intent);
				}
				dismiss();
			}
		});
	}

	protected void getPayUrl() {
		// TODO Auto-generated method stub
		if(volley == null){
			volley = new VolleyUtils<StringResultInfo>(context,StringResultInfo.class);
		}
		Uri.Builder builder = volley.getBuilder(ConstantUrl.VKOIP+"/order/wxauth");
		builder.appendQueryParameter("token", VkoContext.getInstance(context).getToken());
		builder.appendQueryParameter("orderId", orderId);
		volley.sendGETRequest(true,builder);
		volley.setUiDataListener(new UIDataListener<StringResultInfo>() {
			@Override
			public void onDataChanged(StringResultInfo response) {
				if(response != null){
					new BaseUMShare((Activity) context, SHARE_MEDIA.WEIXIN).shareByImgId("微课圈",shareString,R.drawable.login_logo,response.getData());
					dismiss();
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	@Override
	public int getAnimationStyle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResView() {
		// TODO Auto-generated method stub
		return R.layout.pop_share_to_pay;
	}

	@Override
	public boolean updateView(View contentView) {
		// TODO Auto-generated method stub
		return false;
	}



}
