package cn.vko.ring.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.base.BaseResponseInfo;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.AD;
import cn.vko.ring.home.model.AD.ADactivity.ADitem;
import cn.vko.ring.message.activity.RecommendMsgDetailActivity;
import cn.vko.ring.message.model.MsgItemModel;
import cn.vko.ring.mine.model.MessageInfo;

import com.android.volley.toolbox.ImageLoader;

import org.greenrobot.eventbus.EventBus;

/**
 * 广告
 * 
 * @author JiaRH
 * 
 */
public class AdActivity extends BaseActivity {

	@Bind(R.id.iv_ad)
	ImageView ivAd;
	@Bind(R.id.iv_delete)
	ImageView ivDelete;

	private String adPicUrl = "http://img1.imgtn.bdimg.com/it/u=1910299670,1940591177&fm=21&gp=0.jpg";
	private String acUrl="http://www.vko.cn";
	private String adName="";
	private String adNote="";
	private VolleyUtils<BaseResponseInfo> mVolleyUtils;
	ADitem adDitem;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_ad;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
//		Intent intent = getIntent();
//		Bundle b = intent.getExtras();
//		if (b != null) {
//			adDitem = (ADitem) b.getSerializable("AD");
////			adPicUrl = adDitem.getActUrl();
//		}
		adPicUrl = VkoConfigure.getConfigure(this).getString(AD.AD_PIC_URL);
		acUrl= VkoConfigure.getConfigure(this).getString(AD.AD_AC_URL);
		adName = VkoConfigure.getConfigure(this).getString(AD.AD_NAME);
		adNote= VkoConfigure.getConfigure(this).getString(AD.AD_NOTE);
	
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mVolleyUtils = new VolleyUtils<BaseResponseInfo>(this,BaseResponseInfo.class);
		if (!TextUtils.isEmpty(adPicUrl)) {
			ImageLoader.ImageListener imageListener = ImageLoader
					.getImageListener(ivAd, R.drawable.ad_default, R.drawable.ad_default);
			mVolleyUtils.getImageLoader().get(adPicUrl, imageListener);
		}
	}

	@Override
	public boolean isEnableSwipe() {
		return false;
	}

	@OnClick(R.id.iv_ad)
	public void onAdClick() {
		MsgItemModel item = new MsgItemModel();
		item.setLinkUrl(acUrl);
		if(TextUtils.isEmpty(adName)){
			adName="微课圈";
		}
		if(TextUtils.isEmpty(adNote)){
			adNote="微课圈";
		}
		item.setTitle(adName);
		item.setContent(adNote);
		goToLinkMsgDetail(item);
		
		VkoConfigure.getConfigure(this).put(AD.AD_HAS_LOOK, true);
		EventBus.getDefault().post(Constants.ACTION_DEAL_AD);
		ivAd.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				finish();
			}
		}, 200);
		
	}

	private void goToLinkMsgDetail(MsgItemModel item) {
		MessageInfo info = new MessageInfo();
		
		if (!TextUtils.isEmpty(item.getLinkUrl())) {
			info.setLinkUrl(item.getLinkUrl());
		}

		if (!TextUtils.isEmpty(item.getTitle())) {
			info.setTitle(item.getTitle());
		}

		if (!TextUtils.isEmpty(item.getContent())) {
			info.setContent(item.getContent());
		}
		Bundle b = new Bundle();
		b.putSerializable("MESSAGE", info);
		b.putInt("TYPE", 1);
		StartActivityUtil.startActivity(this, RecommendMsgDetailActivity.class, b,
				Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	@OnClick(R.id.iv_delete)
	public void onDeleteClick() {
		
		finish();
	}

}
